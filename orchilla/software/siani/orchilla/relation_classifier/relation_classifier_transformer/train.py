import torch
import torch.nn.functional as F

import torch
import torch.nn.functional as F


def train_step(
		batch,
		ds_train,
		encoder,
		cross_encoder,
		classifier,
		optimizer,
		device,
		criterion=None,
		threshold=0.5
):

	CLS_ID = ds_train.vocab.token_to_id["<cls>"]
	CTX_ID = ds_train.vocab.token_to_id["<ctx>"]
	P1_ID = ds_train.vocab.token_to_id["<p1>"]
	P2_ID = ds_train.vocab.token_to_id["<p2>"]
	PAD_ID = ds_train.vocab.pad_id

	T1_ID = ds_train.vocab.token_to_id.get("<t1>", P1_ID)
	T2_ID = ds_train.vocab.token_to_id.get("<t2>", P2_ID)

	p1_ids = batch["p1_ids"].to(device)
	p2_ids = batch["p2_ids"].to(device)
	t1_ids = batch["term_1"].to(device)
	t2_ids = batch["term_2"].to(device)
	labels = batch["label"].to(device)

	def lengths_from_padded(x, pad_id):
		return (x != pad_id).long().sum(dim=1)

	len_p1 = lengths_from_padded(p1_ids, PAD_ID)
	len_p2 = lengths_from_padded(p2_ids, PAD_ID)
	len_t1 = lengths_from_padded(t1_ids, PAD_ID)
	len_t2 = lengths_from_padded(t2_ids, PAD_ID)

	B, dtype = p1_ids.size(0), p1_ids.dtype

	def cat_rows(rows, pad_id, device, dtype):
		rows = [torch.cat(parts, dim=0) for parts in rows]
		T = max(r.size(0) for r in rows) if rows else 0
		out = torch.full((len(rows), T), fill_value=pad_id, dtype=dtype, device=device)
		for i, r in enumerate(rows):
			out[i, : r.size(0)] = r
		return out

	enc_rows = [
		[
			torch.tensor([CLS_ID, CTX_ID, P1_ID], device=device, dtype=dtype),
			p1_ids[i, : len_p1[i]],
			torch.tensor([T1_ID], device=device, dtype=dtype),
			t1_ids[i, : len_t1[i]],
			torch.tensor([P2_ID], device=device, dtype=dtype),
			p2_ids[i, : len_p2[i]],
			torch.tensor([T2_ID], device=device, dtype=dtype),
			t2_ids[i, : len_t2[i]],
		]
		for i in range(B)
	]
	encoder_input = cat_rows(enc_rows, PAD_ID, device, dtype)
	enc_kpm = (encoder_input == PAD_ID)

	dec_rows = [
		[
			torch.tensor([CLS_ID, P2_ID], device=device, dtype=dtype),
			p2_ids[i, : len_p2[i]],
		]
		for i in range(B)
	]
	decoder_input = cat_rows(dec_rows, PAD_ID, device, dtype)
	dec_kpm = (decoder_input == PAD_ID)

	optimizer.zero_grad()

	try:
		enc_states = encoder.hidden_states(encoder_input, self_key_padding_mask=enc_kpm)
	except TypeError:
		enc_states = encoder.hidden_states(encoder_input)

	dec_states = cross_encoder.hidden_states(
		decoder_input,
		enc_states,
		self_key_padding_mask=dec_kpm,
		cross_key_padding_mask=enc_kpm,
	)

	h_last = dec_states[-1]
	logits = classifier(h_last)

	if not torch.isfinite(logits).all():
		raise RuntimeError("Logits no finitos (NaN/Inf)")


	is_multilabel = (labels.dim() == 2)
	if criterion is None:
		if is_multilabel:
			loss = F.binary_cross_entropy_with_logits(logits, labels.float())
		else:
			loss = F.cross_entropy(logits, labels.long())
	else:

		if isinstance(criterion, torch.nn.BCEWithLogitsLoss):
			loss = criterion(logits, labels.float())
			is_multilabel = True
		else:
			loss = criterion(logits, labels.long())
			is_multilabel = False

	loss.backward()
	torch.nn.utils.clip_grad_norm_(
		list(encoder.parameters())
		+ list(cross_encoder.parameters())
		+ list(classifier.parameters()),
		max_norm=1.0,
	)
	optimizer.step()

	with torch.no_grad():
		metrics = {}
		if is_multilabel:
			probs = torch.sigmoid(logits)
			preds = (probs > threshold).int()
			y = labels.int()

			subset_acc = (preds == y).all(dim=1).float().mean().item()

			tp = (preds & y).sum().item()
			fp = (preds & (1 - y)).sum().item()
			fn = ((1 - preds) & y).sum().item()
			micro_f1 = (2 * tp) / (2 * tp + fp + fn + 1e-8) if (tp + fp + fn) > 0 else 0.0

			metrics.update({"subset_acc": subset_acc, "micro_f1": micro_f1})
		else:
			preds = logits.argmax(dim=-1)
			acc = (preds == labels).float().mean().item()
			metrics.update({"acc": acc})

	return loss.item(), metrics


import torch
import torch.nn.functional as F


@torch.no_grad()
def evaluate(dataloader, ds_train, encoder, cross_encoder, classifier, criterion, device, threshold: float = 0.5):
	encoder.eval()
	cross_encoder.eval()
	classifier.eval()

	CLS_ID = ds_train.vocab.token_to_id["<cls>"]
	CTX_ID = ds_train.vocab.token_to_id["<ctx>"]
	P1_ID = ds_train.vocab.token_to_id["<p1>"]
	P2_ID = ds_train.vocab.token_to_id["<p2>"]
	PAD_ID = ds_train.vocab.pad_id
	T1_ID = ds_train.vocab.token_to_id.get("<t1>", P1_ID)
	T2_ID = ds_train.vocab.token_to_id.get("<t2>", P2_ID)

	total_loss = 0.0
	steps = 0


	sum_acc = 0.0
	sum_subset = 0.0
	sum_micro_f1 = 0.0
	saw_multiclass = False
	saw_multilabel = False

	def lengths_from_padded(x, pad_id):
		return (x != pad_id).long().sum(dim=1)

	def cat_rows(rows, pad_id, device, dtype):
		rows = [torch.cat(parts, dim=0) for parts in rows]
		T = max(r.size(0) for r in rows) if rows else 0
		out = torch.full((len(rows), T), fill_value=pad_id, dtype=dtype, device=device)
		for i, r in enumerate(rows):
			out[i, : r.size(0)] = r
		return out

	for batch in dataloader:
		p1_ids = batch["p1_ids"].to(device)
		p2_ids = batch["p2_ids"].to(device)
		t1_ids = batch["term_1"].to(device)
		t2_ids = batch["term_2"].to(device)
		labels = batch["label"].to(device)

		len_p1 = lengths_from_padded(p1_ids, PAD_ID)
		len_p2 = lengths_from_padded(p2_ids, PAD_ID)
		len_t1 = lengths_from_padded(t1_ids, PAD_ID)
		len_t2 = lengths_from_padded(t2_ids, PAD_ID)

		B, dtype = p1_ids.size(0), p1_ids.dtype

		enc_rows = [
			[
				torch.tensor([CLS_ID, CTX_ID, P1_ID], device=device, dtype=dtype),
				p1_ids[i, : len_p1[i]],
				torch.tensor([T1_ID], device=device, dtype=dtype),
				t1_ids[i, : len_t1[i]],
				torch.tensor([P2_ID], device=device, dtype=dtype),
				p2_ids[i, : len_p2[i]],
				torch.tensor([T2_ID], device=device, dtype=dtype),
				t2_ids[i, : len_t2[i]],
			]
			for i in range(B)
		]
		encoder_input = cat_rows(enc_rows, PAD_ID, device, dtype)
		enc_kpm = (encoder_input == PAD_ID)

		dec_rows = [
			[
				torch.tensor([CLS_ID, P2_ID], device=device, dtype=dtype),
				p2_ids[i, : len_p2[i]],
			]
			for i in range(B)
		]
		decoder_input = cat_rows(dec_rows, PAD_ID, device, dtype)
		dec_kpm = (decoder_input == PAD_ID)

		try:
			enc_states = encoder.hidden_states(encoder_input, self_key_padding_mask=enc_kpm)
		except TypeError:
			enc_states = encoder.hidden_states(encoder_input)

		dec_states = cross_encoder.hidden_states(
			decoder_input,
			enc_states,
			self_key_padding_mask=dec_kpm,
			cross_key_padding_mask=enc_kpm,
		)
		h_last = dec_states[-1]
		logits = classifier(h_last)

		if not torch.isfinite(logits).all():
			raise RuntimeError("Logits no finitos (NaN/Inf)")


		is_multilabel = (labels.dim() == 2)
		if criterion is None:
			loss = (F.binary_cross_entropy_with_logits(logits, labels.float())
					if is_multilabel else
					F.cross_entropy(logits, labels.long()))
		else:
			if isinstance(criterion, torch.nn.BCEWithLogitsLoss):
				loss = criterion(logits, labels.float());
				is_multilabel = True
			else:
				loss = criterion(logits, labels.long());
				is_multilabel = False

		total_loss += float(loss)
		steps += 1

		if is_multilabel:
			probs = torch.sigmoid(logits)
			preds = (probs > threshold).int()
			y = labels.int()
			subset_acc = (preds == y).all(dim=1).float().mean().item()
			tp = (preds & y).sum().item()
			fp = (preds & (1 - y)).sum().item()
			fn = ((1 - preds) & y).sum().item()
			micro_f1 = (2 * tp) / (2 * tp + fp + fn + 1e-8) if (tp + fp + fn) > 0 else 0.0
			sum_subset += subset_acc
			sum_micro_f1 += micro_f1
			saw_multilabel = True
		else:
			preds = logits.argmax(dim=-1)
			acc = (preds == labels).float().mean().item()
			sum_acc += acc
			saw_multiclass = True

	avg_loss = total_loss / max(steps, 1)
	out = {"loss": avg_loss}
	if saw_multiclass:
		out["acc"] = sum_acc / max(steps, 1)
	if saw_multilabel:
		out["subset_acc"] = sum_subset / max(steps, 1)
		out["micro_f1"] = sum_micro_f1 / max(steps, 1)
	return out


def train(encoder, cross_encoder, classifier, criterion, optimizer, device, dl_train, dl_val, ds_train,
		  epochs: int = 30, log_every: int = 100, threshold: float = 0.5):
	def pick_main_metric(metrics: dict):
		if "acc" in metrics: return "acc", metrics["acc"]
		if "micro_f1" in metrics: return "micro_f1", metrics["micro_f1"]
		if "subset_acc" in metrics: return "subset_acc", metrics["subset_acc"]
		return "metric", float("nan")

	for epoch in range(1, epochs + 1):
		encoder.train()
		cross_encoder.train()
		classifier.train()

		running_loss = 0.0
		running_metric = 0.0
		metric_name = "metric"
		steps = 0

		for i, batch in enumerate(dl_train, start=1):
			loss_val, metrics = train_step(
				batch, ds_train, encoder, cross_encoder, classifier,
				optimizer, device, criterion=criterion, threshold=threshold
			)
			mname, mval = pick_main_metric(metrics)
			metric_name = mname
			running_loss += loss_val
			running_metric += mval
			steps += 1

			if i % log_every == 0:
				print(
					f"[Epoch {epoch} | Batch {i}] loss={running_loss / steps:.4f} {metric_name}={running_metric / steps:.4f}")

		train_loss = running_loss / max(steps, 1)
		train_metric = running_metric / max(steps, 1)

		val_metrics = evaluate(dl_val, ds_train, encoder, cross_encoder, classifier, criterion, device, threshold)
		val_loss = val_metrics["loss"]
		val_mname, val_mval = pick_main_metric(val_metrics)

		print(f"Epoch {epoch}: train_loss={train_loss:.4f} {metric_name}={train_metric:.4f} | "
			  f"val_loss={val_loss:.4f} {val_mname}={val_mval:.4f}")
