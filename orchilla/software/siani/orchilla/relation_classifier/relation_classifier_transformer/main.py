import torch
import random
from torch.utils.data import DataLoader
from software.siani.orchilla.relation_classifier.relation_classifier_llm.dataset import DatasetReader, \
	EventRelationDataset, make_collate_fn, stratified_split
from software.siani.orchilla.relation_classifier.relation_classifier_llm.encoder import Encoder, CrossEncoder, \
	Classifier
from software.siani.orchilla.relation_classifier.relation_classifier_llm.train import train
from software.siani.orchilla.relation_classifier.relation_classifier_llm.visualization import predict_all, \
	confusion_matrix, print_confusion_matrix


def make_oversampled_train_set(relations, seed=42):
	rnd = random.Random(seed)
	pos = [r for r in relations if r.dependency]
	neg = [r for r in relations if not r.dependency]
	if len(pos) == 0 or len(neg) == 0:
		return relations
	k = len(neg) // max(1, len(pos))
	extra = []
	for _ in range(k - 1):
		extra.extend(pos)
	remainder = len(neg) - (len(pos) * k)
	if remainder > 0:
		extra.extend(rnd.sample(pos, min(remainder, len(pos))))
	balanced = neg + pos + extra
	rnd.shuffle(balanced)
	return balanced


if __name__ == "__main__":
	device = "mps"
	relations = DatasetReader().read("tlink_probing-main/data/EN-TB-Dense/train.txt")

	train_rel, val_rel = stratified_split(relations[0], val_ratio=0.2, seed=42)

	ds_train = EventRelationDataset(
		train_rel,
		max_len_phrase=256,
		min_freq=1,
		max_vocab_size=None,
	)

	ds_val = EventRelationDataset(
		val_rel,
		tokenizer=ds_train.tok,
		vocab=ds_train.vocab,
		build_vocab_from_texts=False,
		max_len_phrase=512,
	)

	collate_fn = make_collate_fn(ds_train.vocab)
	dl_train = DataLoader(ds_train, batch_size=4, shuffle=True, collate_fn=collate_fn)
	dl_val = DataLoader(ds_val, batch_size=8, shuffle=False, collate_fn=collate_fn)

	SPECIAL_TOKENS = ["<cls>", "<sep>", "<ctx>", "<p1>", "<p2>", "<t1>", "<t2>"]
	for s in SPECIAL_TOKENS:
		ds_train.vocab.add_token(s)

	vocab_size = len(ds_train.vocab)
	pad_id = ds_train.vocab.pad_id
	hidden_size = 512
	num_classes = len(relations[1])
	num_layers = 8

	encoder = Encoder(num_layers=num_layers, vocab_size=vocab_size, embedding_size=hidden_size, pad_id=pad_id).to(device)
	cross_encoder = CrossEncoder(num_layers=num_layers, vocab_size=vocab_size, embedding_size=hidden_size,
								 pad_id=pad_id).to(device)
	cross_encoder.embedding = encoder.embedding
	classifier = Classifier(embedding_size=hidden_size, num_classes=num_classes, pooling="cls").to(device)

	shared_params = list(encoder.embedding.parameters())
	encoder_params = [p for n, p in encoder.named_parameters() if "embedding" not in n]
	cross_params = [p for n, p in cross_encoder.named_parameters() if "embedding" not in n]

	criterion = torch.nn.CrossEntropyLoss()
	optimizer = torch.optim.AdamW([
		{"params": shared_params, "lr": 1e-6},
		{"params": encoder_params, "lr": 1e-6},
		{"params": cross_params, "lr": 1e-6},
		{"params": classifier.parameters(), "lr": 1e-6},
	], weight_decay=1e-4)

	train(encoder, cross_encoder, classifier, criterion, optimizer, device, dl_train, dl_val, ds_train)

	criterion = torch.nn.CrossEntropyLoss()

	y_true, y_pred = predict_all(dl_val, encoder, cross_encoder, classifier, device)
	cm = confusion_matrix(y_true, y_pred, num_classes=num_classes)
	print("\nMatriz de confusión (val):")
	print_confusion_matrix(cm, labels=["no-dep (0)", "dep (1)"])
