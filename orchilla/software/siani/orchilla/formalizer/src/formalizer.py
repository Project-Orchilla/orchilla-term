import torch

from transformers import AutoModelForSeq2SeqLM, AutoTokenizer

from software.siani.orchilla.decomposer.src.decomposer import Decomposer
from software.siani.orchilla.formalizer.src.sorter import OperatorSorter
from software.siani.orchilla.formalizer.src.transcriber import NumberTranscriber


class Formalizer:
    def __init__(self, path: str):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model = AutoModelForSeq2SeqLM.from_pretrained(path, local_files_only=True).to(self.device)
        self.tokenizer = AutoTokenizer.from_pretrained(path, local_files_only=True)

    def formalize(self, text: str) -> str:
        input_text = f"translate: {text}"
        input_ids = self.tokenizer(input_text, return_tensors="pt").to(self.device)
        output_ids = self.model.generate(**input_ids, max_length=512)
        return self.tokenizer.decode(output_ids[0], skip_special_tokens=True)


if __name__ == "__main__":
    text = "Thursday following the third Monday in June"
    formalizer = Formalizer("../model")
    decomposer = Decomposer("C:/Users/juanc/IdeaProjects/orchilla-term/orchilla/software/siani/orchilla/decomposer/src/model")
    transcriber = NumberTranscriber()
    sorter = OperatorSorter()
    units = list(reversed(decomposer.decompose(text)))
    print(units)
    print(">>".join(sorter.sort([formalizer.formalize(transcriber.transcribe(unit)) for unit in units])))