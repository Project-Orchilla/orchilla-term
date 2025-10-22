from collections import Counter
from typing import Iterable, Optional

from software.siani.orchilla.relation_classifier.relation_classifier_llm.tokenizer import WordTokenizer


class Vocabulary:
	PAD = "<pad>"
	UNK = "<unk>"

	def __init__(self, token_to_id: Optional[dict[str, int]] = None):
		if token_to_id is None:
			token_to_id = {self.PAD: 0, self.UNK: 1}
		self.token_to_id: dict[str, int] = dict(token_to_id)
		self.id_to_token: list[str] = [None] * len(self.token_to_id)
		for tok, idx in self.token_to_id.items():
			self.id_to_token[idx] = tok

	@property
	def pad_id(self) -> int:
		return self.token_to_id[self.PAD]

	@property
	def unk_id(self) -> int:
		return self.token_to_id[self.UNK]

	def __len__(self) -> int:
		return len(self.token_to_id)

	def add_token(self, token: str) -> int:
		if token not in self.token_to_id:
			idx = len(self.token_to_id)
			self.token_to_id[token] = idx
			self.id_to_token.append(token)
		return self.token_to_id[token]

	def encode(self, tokens: Iterable[str]) -> list[int]:
		return [self.token_to_id.get(t, self.unk_id) for t in tokens]

	def decode(self, ids: Iterable[int]) -> list[str]:
		return [self.id_to_token[i] if 0 <= i < len(self.id_to_token) else self.UNK for i in ids]

	@classmethod
	def build(
		cls,
		texts: Iterable[str],
		tokenizer: WordTokenizer,
		min_freq: int = 1,
		max_size: Optional[int] = None,
		specials: Optional[list[str]] = None,
	) -> "Vocabulary":
		counter = Counter()
		for t in texts:
			counter.update(tokenizer(t))
		items = sorted(counter.items(), key=lambda kv: (-kv[1], kv[0]))

		token_to_id = {cls.PAD: 0, cls.UNK: 1}
		if specials:
			for s in specials:
				if s not in token_to_id:
					token_to_id[s] = len(token_to_id)

		for tok, freq in items:
			if freq < min_freq:
				continue
			if tok in token_to_id:
				continue
			if max_size is not None and len(token_to_id) >= max_size:
				break
			token_to_id[tok] = len(token_to_id)

		return cls(token_to_id)
