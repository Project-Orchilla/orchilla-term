from software.siani.orchilla.decomposer.src.decomposer import Decomposer
from software.siani.orchilla.formalizer.src.formalizer import Formalizer
from software.siani.orchilla.recognizer.src.recognizer import Recognizer

recognizer = Recognizer(path="C:/Users/juanc/IdeaProjects/orchilla-term/orchilla/software/siani/orchilla/recognizer/model")
decomposer = Decomposer(path="C:/Users/juanc/PycharmProjects/date-transcriber/splitter/model")
formalizer = Formalizer(path="../model")

with open("test.tsv", "r", encoding="utf-8") as f:
    for line in f:
        text, command = line.split("\t")
        print(text)
        for i, temporal_expression in enumerate(recognizer.recognize(text)):
            print(temporal_expression)
            for unit in decomposer.decompose(temporal_expression):
                print("Texto: " + unit + " ------> Comando: " + formalizer.formalize(unit))
        print("-" * 50)

