import torch

from transformers import AutoModelForSeq2SeqLM, AutoTokenizer


class Formalizer:
    def __init__(self, path: str):
        self.device = torch.device("cpu")
        self.model = AutoModelForSeq2SeqLM.from_pretrained(path, local_files_only=True).to(self.device)
        self.tokenizer = AutoTokenizer.from_pretrained(path, local_files_only=True)

    def formalize(self, text: str) -> str:
        input_text = f"translate: {text}"
        input_ids = self.tokenizer(input_text, return_tensors="pt").to(self.device)
        output_ids = self.model.generate(**input_ids, max_length=10)
        return self.tokenizer.decode(output_ids[0], skip_special_tokens=True)
