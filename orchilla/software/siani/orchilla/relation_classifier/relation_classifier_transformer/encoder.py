import math
from typing import Optional

import torch
from torch import nn


class EncoderLayer(nn.Module):
	def __init__(self, embedding_size, num_heads, dim_feedforward, dropout_prob):
		super().__init__()
		self.layer_normalization_0 = nn.LayerNorm(embedding_size)
		self.attention = nn.MultiheadAttention(embedding_size, num_heads, batch_first=True)
		self.layer_normalization_1 = nn.LayerNorm(embedding_size)
		self.ffn = nn.Sequential(
			nn.Linear(embedding_size, dim_feedforward),
			nn.GELU(),
			nn.Dropout(dropout_prob),
			nn.Linear(dim_feedforward, embedding_size),
			nn.Dropout(dropout_prob)
		)

	def forward(self, x: torch.Tensor, key_padding_mask: Optional[torch.Tensor] = None,
				attention_mask: Optional[torch.Tensor] = None) -> torch.Tensor:
		_x = self.layer_normalization_0(x)
		_x, _ = self.attention(_x, _x, _x, attn_mask=attention_mask, key_padding_mask=key_padding_mask)
		_x = self.layer_normalization_1(_x + x)
		_x = self.ffn(_x)
		return _x


class Encoder(nn.Module):
	def __init__(self, num_layers, vocab_size, embedding_size, pad_id, max_len=256, num_heads=8, dim_feedforward=1024,
				 dropout_prob=0.1):
		super().__init__()
		self.embedding_size = embedding_size
		self.embedding = nn.Embedding(vocab_size, embedding_size, padding_idx=pad_id)
		self.pos_encoder = nn.Parameter(torch.zeros(1, max_len, embedding_size))
		nn.init.normal_(self.pos_encoder, mean=0.0, std=0.02)
		self.num_of_layers = num_layers
		self.layers = nn.ModuleList(
			[
				EncoderLayer(embedding_size, num_heads, dim_feedforward, dropout_prob) for _ in range(num_layers)
			]
		)
		self.fc_out = nn.Linear(embedding_size, vocab_size)

	def forward(self, x, self_key_padding_mask: Optional[torch.Tensor] = None,
				attention_mask: Optional[torch.Tensor] = None) -> torch.Tensor:
		seq_len = x.size(1)
		_x = self.embedding(x) * math.sqrt(self.embedding_size)
		_x = _x + self.pos_encoder[:, :seq_len, :]
		for layer in self.layers: _x = layer(_x, attention_mask=attention_mask, key_padding_mask=self_key_padding_mask)
		return self.fc_out(_x)

	def hidden_states(self, x: torch.Tensor, self_key_padding_mask: Optional[torch.Tensor] = None,
					  attention_mask: Optional[torch.Tensor] = None) -> list[torch.Tensor]:
		seq_len = x.size(1)
		_x = self.embedding(x) * math.sqrt(self.embedding_size)
		_x = _x + self.pos_encoder[:, :seq_len, :]
		states = []
		for layer in self.layers:
			_x = layer(_x, attention_mask=attention_mask, key_padding_mask=self_key_padding_mask)
			states.append(_x)
		return states


class CrossEncoderLayer(nn.Module):
	def __init__(self, embedding_size, num_heads, dim_feedforward, dropout_prob):
		super().__init__()
		self.layer_norm_self = nn.LayerNorm(embedding_size)
		self.self_attn = nn.MultiheadAttention(embedding_size, num_heads, batch_first=True)
		self.layer_norm_cross = nn.LayerNorm(embedding_size)
		self.cross_attn = nn.MultiheadAttention(embedding_size, num_heads, batch_first=True)
		self.layer_norm_ffn = nn.LayerNorm(embedding_size)
		self.ffn = nn.Sequential(
			nn.Linear(embedding_size, dim_feedforward),
			nn.GELU(),
			nn.Dropout(dropout_prob),
			nn.Linear(dim_feedforward, embedding_size),
			nn.Dropout(dropout_prob)
		)

	def forward(self, x: torch.Tensor, context: torch.Tensor, self_key_padding_mask: Optional[torch.Tensor] = None,
				cross_key_padding_mask: Optional[torch.Tensor] = None,
				self_attention_mask: Optional[torch.Tensor] = None,
				cross_attention_mask: Optional[torch.Tensor] = None) -> torch.Tensor:
		_x = self.layer_norm_self(x)
		_x, _ = self.self_attn(_x, _x, _x, attn_mask=self_attention_mask, key_padding_mask=self_key_padding_mask)
		x = x + _x
		_x = self.layer_norm_cross(x)
		_x, _ = self.cross_attn(_x, context, context, attn_mask=cross_attention_mask,
								key_padding_mask=cross_key_padding_mask)
		x = x + _x
		_x = self.layer_norm_ffn(x)
		_x = self.ffn(_x)
		x = x + _x
		return x


class CrossEncoder(nn.Module):
	def __init__(self, num_layers, vocab_size, embedding_size, pad_id, max_len=256, num_heads=8, dim_feedforward=1024,
				 dropout_prob=0.1):
		super().__init__()
		self.embedding_size = embedding_size
		self.embedding = nn.Embedding(vocab_size, embedding_size, padding_idx=pad_id)
		self.pos_encoder = nn.Parameter(torch.zeros(1, max_len, embedding_size))
		nn.init.normal_(self.pos_encoder, mean=0.0, std=0.02)
		self.layers = nn.ModuleList(
			[
				CrossEncoderLayer(embedding_size, num_heads, dim_feedforward, dropout_prob)
				for _ in range(num_layers)
			]
		)

	def forward(self, x: torch.Tensor, contexts: list[torch.Tensor],
				self_key_padding_mask: Optional[torch.Tensor] = None,
				cross_key_padding_mask: Optional[torch.Tensor] = None,
				self_attention_mask: Optional[torch.Tensor] = None,
				cross_attention_masks: Optional[list[torch.Tensor]] = None) -> torch.Tensor:
		seq_len = x.size(1)
		_x = self.embedding(x) * math.sqrt(self.embedding_size)
		_x = _x + self.pos_encoder[:, :seq_len, :]
		for i, layer in enumerate(self.layers):
			cross_mask = None
			if cross_attention_masks is not None:
				cross_mask = cross_attention_masks[i]
			_x = layer(_x, contexts[i], self_attention_mask=self_attention_mask, cross_attention_mask=cross_mask,
					   self_key_padding_mask=self_key_padding_mask, cross_key_padding_mask=cross_key_padding_mask)
		return _x

	def hidden_states(self, x: torch.Tensor, contexts: list[torch.Tensor],
					  self_key_padding_mask: Optional[torch.Tensor] = None,
					  cross_key_padding_mask: Optional[torch.Tensor] = None,
					  self_attention_mask: Optional[torch.Tensor] = None,
					  cross_attention_masks: Optional[list[torch.Tensor]] = None) -> list[torch.Tensor]:
		seq_len = x.size(1)
		_x = self.embedding(x) * math.sqrt(self.embedding_size)
		_x = _x + self.pos_encoder[:, :seq_len, :]
		states = []
		for i, layer in enumerate(self.layers):
			cross_mask = None
			if cross_attention_masks is not None:
				cross_mask = cross_attention_masks[i]
			_x = layer(_x, contexts[i], self_attention_mask=self_attention_mask, cross_attention_mask=cross_mask,
					   self_key_padding_mask=self_key_padding_mask, cross_key_padding_mask=cross_key_padding_mask)
			states.append(_x)
		return states


class Classifier(nn.Module):
	def __init__(self, embedding_size, num_classes=1, dropout=0.05, pooling: str = "cls"):
		super().__init__()
		self.norm = nn.LayerNorm(embedding_size)
		self.mlp = nn.Sequential(
			nn.Linear(embedding_size, embedding_size),
			nn.GELU(),
			nn.Dropout(dropout),
			nn.Linear(embedding_size, num_classes),
		)
		if pooling == "mean":
			self.pool = lambda x: x.mean(dim=1)
		elif pooling == "cls":
			self.pool = lambda x: x[:, 0, :]
		else:
			raise ValueError("pooling must be 'mean' or 'cls'")

	def forward(self, x):
		h = self.pool(x)
		h = self.norm(h)
		return self.mlp(h)
