import json
import os
import pickle
import shutil
from pathlib import Path

import spacy
from spacy.cli import download
from spacy.cli.train import train
from spacy.tokens import DocBin


class DecomposerTrainer:
    Models = {"en": "en_core_web_lg", "es": "es_core_news_lg"}

    def train(self, dataset: list, path: str, language: str = "en"):
        self.__prepare_dataset(language=language, data=dataset)
        train(f"../res/decomposer.cfg", "./output", overrides={"paths.train": "./train.spacy", "paths.dev": "./train.spacy"})
        self.__save_model(path, language)

    def __prepare_dataset(self, language: str, data: list):
        nlp = self.__load(language)
        dataset = DocBin()
        for source, target in data:
            doc = nlp.make_doc(text=source)
            ents = []
            for start, end, label in target:
                if (span := doc.char_span(int(start), int(end), label=label, alignment_mode="contract")) is None: continue
                ents.append(span)
            doc.ents = ents
            dataset.add(doc)
        dataset.to_disk("./train.spacy")

    def __save_model(self, path: str, language: str):
        Path(f"{path}/{language}").mkdir(parents=True, exist_ok=True)
        with open(f"{path}/{language}/decomposer.mdl", mode='wb') as f:
            pickle.dump(spacy.load("./output/model-best"), f)
        shutil.rmtree("./output")
        os.remove("./train.spacy")

    def __load(self, language: str):
        try:
            return spacy.load(self.Models[language])
        except OSError:
            download(self.Models[language])
            return spacy.load(self.Models[language])


class DecomposerDatasetReader:
    @staticmethod
    def read(path: str) -> list:
        dataset = []
        with open(path, mode='r', encoding="utf-8") as file:
            while line := file.readline():
                source, target = line.rstrip().split("\t")
                dataset.append((source, json.loads(target)['entities']))
        return dataset


if __name__ == "__main__":
    DecomposerTrainer().train(DecomposerDatasetReader.read("data/en/dataset.tsv"), "model", "en")