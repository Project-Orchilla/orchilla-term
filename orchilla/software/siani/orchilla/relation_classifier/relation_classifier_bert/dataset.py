import torch
from torch.utils.data import Dataset


class RelationDataset(Dataset):
    def __init__(self, texts, labels, tokenizer, max_length=128):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.encodings = tokenizer(texts, truncation=True, padding='max_length', max_length=max_length)
        self.labels = labels

    def __getitem__(self, idx):
        item = {k: torch.tensor(v[idx]).to(self.device) for k, v in self.encodings.items()}
        item['labels'] = torch.tensor(self.labels[idx]).to(self.device)
        return item

    def __len__(self):
        return len(self.labels)