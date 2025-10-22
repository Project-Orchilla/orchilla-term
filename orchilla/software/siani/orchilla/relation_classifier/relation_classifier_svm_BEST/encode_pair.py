from transformers import AutoTokenizer, AutoModel
import numpy as np
import torch

MODEL_NAME = "FacebookAI/roberta-base"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModel.from_pretrained(MODEL_NAME)
for p in model.parameters(): p.requires_grad = False
model.eval()

special_tokens = {"additional_special_tokens": ["<E1>", "</E1>", "<E2>", "</E2>"]}
tokenizer.add_special_tokens(special_tokens)
model.resize_token_embeddings(len(tokenizer))

@torch.no_grad()
def encode_pair(s1: str, s2: str, e1: str, e2: str, device="cpu"):
	def mark_span(sent, span, op, cl):
		i = sent.lower().find(span.lower())
		return sent if i < 0 else sent[:i] + f" {op} " + sent[i:i + len(span)] + f" {cl} " + sent[i + len(span):]

	s1m = mark_span(s1, e1, "<E1>", "</E1>")
	s2m = mark_span(s2, e2, "<E2>", "</E2>")

	text = s1m + " </s> " + s2m
	enc = tokenizer(text, return_tensors="pt", truncation=True, max_length=256)
	input_ids = enc["input_ids"].to(device)
	attn = enc["attention_mask"].to(device)

	out = model(input_ids=input_ids, attention_mask=attn)
	H = out.last_hidden_state.squeeze(0)  # [T, d]
	ids = input_ids.squeeze(0).tolist()

	def mean_between(ids, H, start_tok, end_tok):
		try:
			i = ids.index(tokenizer.convert_tokens_to_ids(start_tok))
			j = ids.index(tokenizer.convert_tokens_to_ids(end_tok))
			if j > i + 1:
				return H[i + 1:j].mean(0)
		except ValueError:
			pass
		return None

	he1 = mean_between(ids, H, "<E1>", "</E1>")
	he2 = mean_between(ids, H, "<E2>", "</E2>")
	hcls = H[0]

	if he1 is None: he1 = hcls
	if he2 is None: he2 = hcls

	feat = torch.cat([hcls, he1, he2, he1 * he2, torch.abs(he1 - he2)], dim=-1)  # [5d]
	return feat.cpu().numpy()


def build_features(relations):
	X, y = [], []
	for r in relations:
		X.append(encode_pair(r.sentence_1, r.sentence_2, r.term_1, r.term_2))
		y.append(r.label)
	return np.vstack(X), np.array(y)
