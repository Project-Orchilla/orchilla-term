import torch
import random
from typing import Optional, Tuple
from torch.utils.data import Dataset

from software.siani.orchilla.relation_classifier.relation_classifier_llm.tokenizer import WordTokenizer
from software.siani.orchilla.relation_classifier.relation_classifier_llm.vocabulary import Vocabulary


class EventsRelation:
	def __init__(self, sample_id, line):
		self.id = sample_id
		self.sentence_1 = line[0]
		self.sentence_2 = line[1]
		self.source_event_idx = int(line[2][3:])
		self.target_event_idx = int(line[4][3:])
		self.term_1 = line[3]
		self.term_2 = line[5]
		self.label = line[6]
		self.representation = dict()


class DatasetReader:
	def read(self, path: str, include_not_related=True) -> list[EventsRelation]:
		samples = []
		i = 0
		for line in open(path, 'r'):
			line = line.strip().split('\t')
			if len(line) < 7: continue
			if not include_not_related and line[-1] == "NOT_RELATED": continue
			sample = EventsRelation(i, line)
			samples.append(sample)
			i += 1
		label_list = set(sample.label for sample in samples)
		return samples, list(label_list)


class EventRelationDataset(Dataset):
	def __init__(
			self,
			events_relation_list: list[EventsRelation],
			tokenizer: Optional[WordTokenizer] = None,
			vocab: Optional[Vocabulary] = None,
			build_vocab_from_texts: bool = True,
			min_freq: int = 1,
			max_vocab_size: Optional[int] = None,
			max_len_phrase: Optional[int] = None,
	):
		self.data = events_relation_list
		self.tok = tokenizer or WordTokenizer()
		self.max_len_phrase = max_len_phrase
		self.labels = list(set([e.label for e in self.data]))
		self.build_vocab(build_vocab_from_texts, events_relation_list, max_vocab_size, min_freq, vocab)

	def build_vocab(self, build_vocab_from_texts, events_relation_list, max_vocab_size, min_freq, vocab):
		if vocab is None and build_vocab_from_texts:
			text = []
			for event_relation in events_relation_list:
				text.append(event_relation.sentence_1)
				text.append(event_relation.sentence_2)
				text.append(event_relation.term_1)
				text.append(event_relation.term_2)
			self.vocab = Vocabulary.build(
				texts=text, tokenizer=self.tok, min_freq=min_freq, max_size=max_vocab_size
			)
		elif vocab is not None:
			self.vocab = vocab
		else:
			self.vocab = Vocabulary.build(
				texts=(er.text for er in events_relation_list), tokenizer=self.tok
			)

	def __len__(self) -> int:
		return len(self.data)

	def _truncate(self, ids: list[int], max_len: Optional[int]) -> list[int]:
		if max_len is None or len(ids) <= max_len:
			return ids
		return ids[:max_len]

	def __getitem__(self, idx: int) -> dict[str, torch.Tensor]:
		event_relation = self.data[idx]

		p1_ids = self.vocab.encode(self.tok(event_relation.sentence_1))
		p2_ids = self.vocab.encode(self.tok(event_relation.sentence_2))
		term_1 = self.vocab.encode(self.tok(event_relation.term_1))
		term_2 = self.vocab.encode(self.tok(event_relation.term_2))
		p1_ids = self._truncate(p1_ids, self.max_len_phrase)
		p2_ids = self._truncate(p2_ids, self.max_len_phrase)

		label = self.labels.index(event_relation.label)

		return {
			"p1_ids": torch.tensor(p1_ids, dtype=torch.long),
			"p2_ids": torch.tensor(p2_ids, dtype=torch.long),
			"label": torch.tensor(label, dtype=torch.long),
			"term_1": torch.tensor(term_1, dtype=torch.long),
			"term_2": torch.tensor(term_2, dtype=torch.long)
		}


def pad_sequence_1d(seqs: list[torch.Tensor], pad_id: int) -> torch.Tensor:
	max_len = max((len(s) for s in seqs), default=0)
	if max_len == 0:
		return torch.empty((len(seqs), 0), dtype=torch.long)
	out = torch.full((len(seqs), max_len), pad_id, dtype=torch.long)
	for i, s in enumerate(seqs):
		out[i, : len(s)] = s
	return out


def make_collate_fn(vocab: Vocabulary):
	def collate(batch: list[dict[str, torch.Tensor]]) -> dict[str, torch.Tensor]:
		return {
			"p1_ids": pad_sequence_1d([b["p1_ids"] for b in batch], vocab.pad_id),
			"p2_ids": pad_sequence_1d([b["p2_ids"] for b in batch], vocab.pad_id),
			"term_1": pad_sequence_1d([b["term_1"] for b in batch], vocab.pad_id),
			"term_2": pad_sequence_1d([b["term_2"] for b in batch], vocab.pad_id),
			"label": torch.stack([b["label"] for b in batch], dim=0),
		}
	return collate


def stratified_split(relations, val_ratio: float = 0.2, seed: int = 42) -> Tuple[list, list]:
	rnd = random.Random(seed)
	rnd.shuffle(relations)
	pos = int(len(relations) * val_ratio)
	val = relations[:pos]
	train = relations[pos:]
	rnd.shuffle(train)
	rnd.shuffle(val)
	return train, val


if __name__ == "__main__":
	DatasetReader().read("tlink_probing-main/data/EN-TB-Dense/train.txt")
