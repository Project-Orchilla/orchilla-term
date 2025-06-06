import torch
from torch import nn
from torch.nn import ModuleList, ReLU, Linear
from torch_geometric.graphgym import GATConv


class TemporalGNN(torch.nn.Module):
    def __init__(self, in_channels, hidden_channels):
        super().__init__()
        self.convs = ModuleList([
            GATConv(in_channels, hidden_channels, heads=1, concat=True),
            GATConv(hidden_channels, hidden_channels, heads=1, concat=True)
        ])
        self.relu = ReLU()
        self.classifier = nn.Sequential(
            Linear(hidden_channels * 2 + in_channels, 128),
            ReLU(),
            Linear(128, 1))

    def forward(self, x, edge_index, edge_pairs, context):
        for conv in self.convs:
            x = self.relu(conv(x, edge_index))
        row, col = edge_pairs
        edge_feats = torch.cat([x[row], x[col], context.expand(row.size(0), -1)], dim=1)
        return self.classifier(edge_feats).squeeze(1)
