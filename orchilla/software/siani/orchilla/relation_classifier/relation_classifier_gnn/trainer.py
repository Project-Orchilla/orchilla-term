from pathlib import Path

import torch
from torch_geometric.data import DataLoader
from transformers import AutoModel, AutoTokenizer

from software.siani.orchilla.relation_classifier.relation_classifier_gnn.GNNModel import TemporalGNN
from software.siani.orchilla.relation_classifier.relation_classifier_gnn.dataset import TemporalDependencyDataset


class RelationClassifierTrainer:
    def __init__(self, epochs: int = 2_000, lr: float = 1e-4):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "mps" if torch.backends.mps.is_available() else "cpu")
        self.tokenizer = AutoTokenizer.from_pretrained("bert-base-cased")
        self.encoder = AutoModel.from_pretrained("bert-base-cased").to(self.device)
        self.encoder.eval()
        self.epochs = epochs
        self.lr = lr

    def train(self, dataset_path: str, path: str):
        dataset = TemporalDependencyDataset(Path(dataset_path), self.tokenizer, self.encoder)
        training_dataloader, validation_dataloader = self.__split(dataset)
        model = TemporalGNN(in_channels=768, hidden_channels=256).to(self.device)
        optimizer = torch.optim.Adam(list(model.parameters()) + list(self.encoder.parameters()), lr=self.lr)
        criterion = torch.nn.BCEWithLogitsLoss()
        best_f1 = 0
        for epoch in range(self.epochs):
            train_loss = self.__train(model, training_dataloader, optimizer, criterion)
            print(f"Epoch {epoch+1}, Loss: {train_loss:.4f}")
            p, r, f1, acc = self.__evaluate(model, validation_dataloader)
            print(f"Validation — Precision: {p:.3f}, Recall: {r:.3f}, F1: {f1:.3f}, Accuracy: {acc:.3f}")
            if f1 > best_f1:
                best_f1 = f1
                torch.save(model.state_dict(), path + "/best_model.pt")
                print("Checkpoint saved!")

    def __split(self, dataset: TemporalDependencyDataset):
        return DataLoader(dataset[:160], batch_size=1, shuffle=True), DataLoader(dataset[160:], batch_size=1)

    def __train(self, model: TemporalGNN, dataloader: DataLoader, optimizer: torch.optim.Optimizer, criterion: torch.nn.BCEWithLogitsLoss):
        model.train()
        total_loss = 0
        for batch in dataloader:
            batch = batch.to(self.device)
            edge_pairs = batch.edge_index
            context = batch.context.to(self.device)
            optimizer.zero_grad()
            out = model(batch.x, batch.edge_index, edge_pairs, context)
            loss = criterion(out, batch.edge_label)
            loss.backward()
            optimizer.step()
            total_loss += loss.item()
        return total_loss

    def __evaluate(self, model: TemporalGNN, dataloader: DataLoader):
        model.eval()
        y_true, y_pred = [], []
        with torch.no_grad():
            for batch in dataloader:
                batch = batch.to(self.device)
                edge_pairs = batch.edge_index
                context = batch.context.to(self.device)
                out = model(batch.x, batch.edge_index, edge_pairs, context)
                pred = torch.sigmoid(out) > 0.5
                y_true.extend(batch.edge_label.cpu().tolist())
                y_pred.extend(pred.cpu().tolist())
        return self.__compute_metrics(y_true, y_pred)

    def __compute_metrics(self, y_true, y_pred):
        y_true = torch.tensor(y_true)
        y_pred = torch.tensor(y_pred)
        tp = ((y_true == 1) & (y_pred == 1)).sum().item()
        fp = ((y_true == 0) & (y_pred == 1)).sum().item()
        fn = ((y_true == 1) & (y_pred == 0)).sum().item()
        tn = ((y_true == 0) & (y_pred == 0)).sum().item()
        precision = tp / (tp + fp + 1e-8)
        recall = tp / (tp + fn + 1e-8)
        f1 = 2 * precision * recall / (precision + recall + 1e-8)
        accuracy = (tp + tn) / (tp + tn + fp + fn + 1e-8)
        return precision, recall, f1, accuracy