from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
import torch

model_path = "model/en"
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

tokenizer = AutoTokenizer.from_pretrained(model_path)
model = AutoModelForSeq2SeqLM.from_pretrained(model_path).to(device)

input_text = "3 weeks ago"

inputs = tokenizer(input_text, return_tensors="pt", truncation=True, max_length=1024).to(device)

with torch.no_grad():
    outputs = model.generate(**inputs, max_length=512)

predicted_text = tokenizer.decode(outputs[0], skip_special_tokens=True)

print(predicted_text)