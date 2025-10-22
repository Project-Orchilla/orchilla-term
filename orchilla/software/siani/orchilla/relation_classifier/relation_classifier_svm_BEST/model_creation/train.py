import os
import joblib
import numpy as np
from sklearn.svm import LinearSVC
from matplotlib import pyplot as plt
from transformers import AutoTokenizer, AutoModel
from sklearn.metrics import accuracy_score, f1_score, classification_report, confusion_matrix
from software.siani.orchilla.relation_classifier.relation_classifier_svm_BEST.encode_pair import build_features
from software.siani.orchilla.relation_classifier.relation_classifier_llm.dataset import DatasetReader, stratified_split

MODEL_NAME = "FacebookAI/roberta-base"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModel.from_pretrained(MODEL_NAME)
for p in model.parameters(): p.requires_grad = False
model.eval()

special_tokens = {"additional_special_tokens": ["<E1>", "</E1>", "<E2>", "</E2>"]}
tokenizer.add_special_tokens(special_tokens)
model.resize_token_embeddings(len(tokenizer))

if __name__ == "__main__":
	include_not_related_label = False
	relations, _ = DatasetReader().read("data/EN-TB-Dense/train.txt", include_not_related=include_not_related_label)

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
	plt.savefig(f"confusion_matrices/confusion_matrix.png", dpi=150)
	plt.show()

	os.makedirs("svm_relation_classifier", exist_ok=True)

	joblib.dump(clf, "svm_relation_classifier/model.joblib")

	tokenizer.save_pretrained("svm_relation_classifier/tokenizer")
