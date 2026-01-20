from transformers import BertTokenizer, BertForSequenceClassification
import torch
import torch.nn.functional as F

tokenizer = BertTokenizer.from_pretrained("bert-large-uncased")
model = BertForSequenceClassification.from_pretrained("bert-large-uncased", num_labels=4)

labels = ["A before B", "A after B", "no temporal relation"]

A = "Juan se levantó y fue a desayunar"
B = "después fue al trabajo."
C = "Juan se levantó y fue a desayunar, después fue al trabajo."

input_text = f"This is the text: {C}, Event A: {A} Event B: {B} Which happened first?"
inputs = tokenizer(input_text, return_tensors="pt", truncation=True, padding=True)

with torch.no_grad():
    outputs = model(**inputs)
    probs = F.softmax(outputs.logits, dim=1)
    pred = torch.argmax(probs, dim=1).item()

print(f"Probabilidades: {probs.tolist()[0]}")
print(f"Predicción: {labels[pred]}")
