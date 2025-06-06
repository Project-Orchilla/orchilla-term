from software.siani.orchilla.decomposer.src.decomposer import Decomposer
from software.siani.orchilla.formalizer.src.formalizer import Formalizer

decomposer = Decomposer(path="C:/Users/juanc/PycharmProjects/date-transcriber/splitter/model")
formalizer = Formalizer(path="../model")

with open("test.tsv", "r", encoding="utf-8") as f:
    for line in f:
        text, command = line.strip().split("\t")
        print(f"Source: {text}")
        pred = formalizer.formalize(decomposer.decompose(text))
        print(f"Predicted: {pred} ---------------------- Real: {command}")
        print("-"*50)

