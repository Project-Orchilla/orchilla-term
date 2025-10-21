from matplotlib import pyplot as plt
from transformers import AutoTokenizer, AutoModel
import torch
from sklearn.svm import LinearSVC
from sklearn.metrics import accuracy_score, f1_score, classification_report, confusion_matrix
import numpy as np
from software.siani.orchilla.relation_classifier.relation_classifier_llm.dataset import DatasetReader, stratified_split

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


if __name__ == "__main__":
	relations, _ = DatasetReader().read("../tlink_probing-main/data/EN-TB-Dense/train.txt", include_not_related=False)

	train, val = stratified_split(relations, val_ratio=0.2, seed=42)

	Xtr, ytr = build_features(train)
	Xva, yva = build_features(val)

	clf = LinearSVC()
	clf.fit(Xtr, ytr)
	pred = clf.predict(Xva)

	print("Val acc:", accuracy_score(yva, pred), "micro-F1:", f1_score(yva, pred, average="micro"))

	labels_order = list(sorted(set(list(yva) + list(pred))))

	cm = confusion_matrix(yva, pred, labels=labels_order)
	print("\nConfusion matrix (counts):\n", cm)

	cm_norm = cm.astype(float) / (cm.sum(axis=1, keepdims=True) + 1e-9)
	print("\nConfusion matrix (row-normalized):\n", np.round(cm_norm, 3))

	print("\nClassification report:\n", classification_report(yva, pred, labels=labels_order, digits=3))

	fig, ax = plt.subplots(figsize=(6, 5))
	im = ax.imshow(cm, interpolation='nearest')
	ax.set_title("Confusion Matrix")
	ax.set_xlabel("Predicted")
	ax.set_ylabel("True")
	ax.set_xticks(range(len(labels_order)))
	ax.set_xticklabels(labels_order, rotation=45, ha="right")
	ax.set_yticks(range(len(labels_order)))
	ax.set_yticklabels(labels_order)

	for i in range(len(labels_order)):
		for j in range(len(labels_order)):
			ax.text(j, i, int(cm[i, j]), ha="center", va="center")

	plt.tight_layout()
	plt.savefig("confusion_matrix_without_not_related.png", dpi=150)
	plt.show()
