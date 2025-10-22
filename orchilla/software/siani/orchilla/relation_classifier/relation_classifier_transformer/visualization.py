import torch


@torch.no_grad()
def predict_all(dataloader, encoder, cross_encoder, classifier, device):
	encoder.eval();
	cross_encoder.eval();
	classifier.eval()
	y_true, y_pred = [], []
	for batch in dataloader:
		enc_inp = batch["p1_ids"].to(device)
		dec_inp = batch["p2_ids"].to(device)
		labels = batch["label"].to(device)

		enc_states = encoder.hidden_states(enc_inp)
		dec_states = cross_encoder.hidden_states(dec_inp, enc_states)
		h_last = dec_states[-1]
		logits = classifier(h_last)
		probs = torch.sigmoid(logits)
		preds = (probs > 0.5).long().squeeze(1)

		y_true.append(labels.cpu())
		y_pred.append(preds.cpu())
	y_true = torch.cat(y_true, dim=0)
	y_pred = torch.cat(y_pred, dim=0)
	return y_true, y_pred


def confusion_matrix(y_true: torch.Tensor, y_pred: torch.Tensor, num_classes: int):
	cm = torch.zeros((num_classes, num_classes), dtype=torch.long)
	for t, p in zip(y_true, y_pred):
		cm[t.long(), p.long()] += 1
	return cm


def print_confusion_matrix(cm: torch.Tensor, labels=None):
	if labels is None:
		labels = [str(i) for i in range(cm.size(0))]
	header = [" "] + [f"pred={l}" for l in labels]
	row_fmt = "{:>10}" * len(header)
	print(row_fmt.format(*header))
	for i, l in enumerate(labels):
		row = [f"true={l}"] + [str(cm[i, j].item()) for j in range(cm.size(1))]
		print(row_fmt.format(*row))
	cm_float = cm.float()
	row_sums = cm_float.sum(dim=1, keepdim=True).clamp_min(1)
	cm_norm = cm_float / row_sums
	print("\nMatriz de confusión (normalizada por filas):")
	print(row_fmt.format(*header))
	for i, l in enumerate(labels):
		row = [f"true={l}"] + [f"{cm_norm[i, j].item():.3f}" for j in range(cm_norm.size(1))]
		print(row_fmt.format(*row))
