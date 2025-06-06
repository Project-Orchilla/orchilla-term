import torch
from torch import nn
from torch.nn import ReLU, Linear
from torch_geometric.nn import GCNConv


class RelationalClassifier(torch.nn.Module):
    def __init__(self, in_channels, hidden_channels):
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
            context.expand(row.size(0), -1)
        ], dim=1)
        return self.classifier(edge_feats).squeeze(1)