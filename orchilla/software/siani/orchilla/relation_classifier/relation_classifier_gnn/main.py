import json
import torch
from pathlib import Path
from transformers import AutoTokenizer, AutoModel
from torch_geometric.data import Data

import torch.nn as nn
from torch_geometric.nn import GCNConv
from torch.nn import ReLU, Linear

class RelationalClassifier(torch.nn.Module):
    def __init__(self, in_channels: int, hidden_channels: int):
        super().__init__()
        self.conv1 = GCNConv(in_channels, hidden_channels)
        self.conv2 = GCNConv(hidden_channels, hidden_channels)
        self.relu = ReLU()
        self.classifier = nn.Sequential(
            Linear(hidden_channels * 2 + in_channels, 128),
            ReLU(),
            Linear(128, 1)
        )

    def forward(self, x, edge_index, edge_pairs, context):
        x = self.relu(self.conv1(x, edge_index))
        x = self.relu(self.conv2(x, edge_index))
        row, col = edge_pairs
        edge_feats = torch.cat([
            x[row],
            x[col],
            context.unsqueeze(0).expand(row.size(0), -1)
        ], dim=1)
        return self.classifier(edge_feats).squeeze(1)


class RelationClassifierPredictor:
    def __init__(self,
                 model_dir: str,
                 in_channels: int = 768,
                 hidden_channels: int = 256,
                 max_event_length: int = 64,
                 device: torch.device = None):
        self.device = device or (torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu"))
        self.tokenizer = AutoTokenizer.from_pretrained("bert-base-cased")
        self.encoder = AutoModel.from_pretrained("bert-base-cased").to(self.device)
        ckpt_enc = Path(model_dir) / "best_encoder.pt"
        if ckpt_enc.exists():
            self.encoder.load_state_dict(torch.load(str(ckpt_enc), map_location=self.device))
        self.encoder.eval()
        self.model = RelationalClassifier(in_channels=in_channels, hidden_channels=hidden_channels).to(self.device)
        ckpt_gnn = Path(model_dir) / "best_model.pt"
        if not ckpt_gnn.exists():
            raise FileNotFoundError(f"No se encontró el checkpoint GNN en {ckpt_gnn}")
        state_dict = torch.load(str(ckpt_gnn), map_location=self.device)
        self.model.load_state_dict(state_dict)
        self.model.eval()
        self.max_event_length = max_event_length

    def encode_text(self, text: str) -> torch.Tensor:
        tokens = self.tokenizer(text,
                                return_tensors="pt",
                                truncation=True,
                                max_length=512).to(self.device)
        with torch.no_grad():
            last_hidden = self.encoder(**tokens).last_hidden_state  # [1, seq_len, in_channels]
        cls_emb = last_hidden[:, 0, :].squeeze(0).cpu()             # [in_channels]
        return cls_emb

    def encode_event(self, event_text: str) -> torch.Tensor:
        tokens = self.tokenizer(event_text,
                                return_tensors="pt",
                                truncation=True,
                                max_length=self.max_event_length).to(self.device)
        with torch.no_grad():
            last_hidden = self.encoder(**tokens).last_hidden_state
        cls_emb = last_hidden[:, 0, :].squeeze(0).cpu()
        return cls_emb

    def process_entry(self, entry: dict) -> Data:
        node_feats = []
        id_to_idx = {}
        for idx, ev in enumerate(entry["events"]):
            node_feats.append(self.encode_event(ev["text"]))
            id_to_idx[ev["id"]] = idx
        pos_edge_index = []
        for tgt in entry["events"]:
            deps = tgt.get("depends_on", None)
            if deps is None:
                continue
            if isinstance(deps, str):
                deps = [deps]
            for src_id in deps:
                pos_edge_index.append([ id_to_idx[src_id], id_to_idx[tgt["id"]] ])
        num_nodes = len(entry["events"])
        all_pairs = [(i,j) for i in range(num_nodes) for j in range(num_nodes) if i!=j]
        pos_set = { (u,v) for u, v in pos_edge_index }
        neg_candidates = [pair for pair in all_pairs if pair not in pos_set]
        num_pos = len(pos_edge_index)
        sampled_neg = neg_candidates[:num_pos]

        full_edge_index = []
        for u,v in pos_edge_index:
            full_edge_index.append([u, v])
        for u,v in sampled_neg:
            full_edge_index.append([u, v])
        context_emb = self.encode_text(entry["text"])
        x = torch.stack(node_feats, dim=0)
        edge_index_tensor = torch.tensor(full_edge_index,
                                         dtype=torch.long).t().contiguous()
        edge_label_tensor = torch.zeros(edge_index_tensor.size(1), dtype=torch.float)
        return Data(
            x=x,
            edge_index=edge_index_tensor,
            edge_label=edge_label_tensor,
            context=context_emb
        )

    def predict(self, entry_json: str):
        if isinstance(entry_json, str):
            entry = json.loads(entry_json)
        else:
            entry = entry_json
        data = self.process_entry(entry)
        data = data.to(self.device)
        with torch.no_grad():
            x = data.x.to(self.device)
            edge_index = data.edge_index.to(self.device)
            edge_pairs = data.edge_index.to(self.device)
            context = data.context.to(self.device)

            logits = self.model(x, edge_index, edge_pairs, context)
            probs = torch.sigmoid(logits).cpu().tolist()
            preds = [1 if p > 0.5 else 0 for p in probs]

        idx_to_id = { idx: ev["id"] for idx, ev in enumerate(entry["events"]) }

        edge_list = edge_index.t().cpu().tolist()
        results = []
        for i, (u, v) in enumerate(edge_list):
            src_id = idx_to_id[u]
            tgt_id = idx_to_id[v]
            results.append((src_id, tgt_id, probs[i], preds[i]))

        return results

if __name__ == "__main__":
    predictor = RelationClassifierPredictor(model_dir="model")

    with open("dataset.jsonl", "r", encoding="utf-8") as f:
        line = f.readlines()[20].strip()
    print(line)
    preds = predictor.predict(line)
    print("src_id → tgt_id   |  Probabilidad  | Predicción  ")
    print("-------------------------------------------------")
    for src, tgt, prob, lab in preds:
        print(f"{src} → {tgt}             |   {prob:.4f}       |     {lab}")
