from encode_pair import encode_pair
from transformers import AutoTokenizer, AutoModel
import joblib
import torch


class RelationClassifier:
    def __init__(self, model_name="FacebookAI/roberta-base", svm_path="/app/svm_relation_classifier/model.joblib"):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.tokenizer = AutoTokenizer.from_pretrained(model_name)
        self.model = AutoModel.from_pretrained(model_name).to(self.device)
        for p in self.model.parameters():
            p.requires_grad = False
        self.model.eval()

        self.clf = joblib.load(svm_path)

    def predict(self, text_a1: str, text_a2: str, text_b1: str, text_b2: str):
        feat = encode_pair(text_a1, text_a2, text_b1, text_b2)
        pred = self.clf.predict([feat])
        return str(pred[0])
