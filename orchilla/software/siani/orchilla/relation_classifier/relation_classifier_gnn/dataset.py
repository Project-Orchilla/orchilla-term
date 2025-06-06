import json

import torch
from torch_geometric.data import InMemoryDataset, Data


class TemporalDependencyDataset(InMemoryDataset):
    def __init__(self, path, tokenizer, model, max_length=64):
        super().__init__()
        self.device = torch.device("cuda" if torch.cuda.is_available() else "mps" if torch.backends.mps.is_available() else "cpu")
        self.tokenizer = tokenizer
        self.encoder = model.to(self.device)
        self.max_length = max_length
        self.data_list = self.load_graphs(path)
        self.data, self.slices = self.collate(self.data_list)

    def load_graphs(self, path):
        data_list = []
        with open(path, "r", encoding="utf-8") as f:
            for line in f:
                data_list.append(self.process_entry(json.loads(line)))
        return data_list

    def encode_text(self, text):
        tokens = self.tokenizer(text, return_tensors="pt", truncation=True, max_length=512).to(self.device)
        with torch.no_grad():
            output = self.encoder(**tokens).last_hidden_state
        return output[:, 0, :].squeeze(0).cpu()

    def encode_event(self, event_text):
        tokens = self.tokenizer(event_text, return_tensors="pt", truncation=True, max_length=self.max_length).to(self.device)
        with torch.no_grad():
            output = self.encoder(**tokens).last_hidden_state
        return output[:, 0, :].squeeze(0).cpu()


    def process_entry(self, entry):
        node_feats, id_to_idx = self.get_node_features(entry["events"])
        pos_edge_index, pos_edge_label = self.get_positive_edges(entry["events"], id_to_idx)

        full_edge_index, full_edge_label = self.add_negative_edges(
            pos_edge_index,
            num_nodes=len(entry["events"])
        )

        context_emb = self.encode_text(entry["text"])
        return Data(
            x=torch.stack(node_feats),
            edge_index=torch.tensor(full_edge_index).t().contiguous(),
            edge_label=torch.tensor(full_edge_label, dtype=torch.float),
            context=context_emb
        )

    def get_node_features(self, events):
        node_features = []
        id_to_idx = {}
        for idx, event in enumerate(events):
            node_features.append(self.encode_event(event["text"]))
            id_to_idx[event["id"]] = idx
        return node_features, id_to_idx

    def get_positive_edges(self, events, id_to_idx):
        edge_index = []
        edge_label = []
        for target in events:
            deps = target["depends_on"]
            if deps is None:
                continue
            if isinstance(deps, str):
                deps = [deps]
            for src_id in deps:
                edge_index.append([id_to_idx[src_id], id_to_idx[target["id"]]])
                edge_label.append(1)
        return edge_index, edge_label

    def add_negative_edges(self, pos_edge_index, num_nodes):
        neg_candidates = list(set((i, j) for i in range(num_nodes) for j in range(num_nodes) if i != j) - set(tuple(e) for e in pos_edge_index))
        num_pos = len(pos_edge_index)
        sampled_neg = neg_candidates[:num_pos]

        full_edge_index = []
        full_edge_label = []

        for pair in pos_edge_index:
            full_edge_index.append(pair)
            full_edge_label.append(1)

        for pair in sampled_neg:
            full_edge_index.append([pair[0], pair[1]])
            full_edge_label.append(0)

        return full_edge_index, full_edge_label