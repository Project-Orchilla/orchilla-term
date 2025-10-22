import json
import pprint
from collections import defaultdict

from nervaluate import Evaluator

from software.siani.orchilla.recognizer.src.recognizer import Recognizer

recognizer = Recognizer("../src/model/en")

y_true = []
y_preds = []


def parse(ents: list):
    return [{"label": label, "start": start, "end": end} for start, end, label in ents]


def flip_nested_dict(dd):
    result = defaultdict(dict)
    for k1, d in dd.items():
        for k2, v in d.items():
            result[k2][k1] = v
    return dict(result)


def show(txt: str, ents: list):
    result = []
    for start, end, label in ents:
        result.append(txt[start:end])
    return result


with open("test.tsv", "r", encoding="utf-8") as f:
    bad = 0
    blank = 0
    total = 0
    for line in f:
        total += 1
        text, entities = line.split("\t")
        y_true.append(parse(json.loads(entities)['entities']))
        y_preds.append(recognizer.recognize_positions(text))
        real = show(text, json.loads(entities)['entities'])
        predicted = recognizer.recognize(text)
        if len(real) == 0 and len(predicted) == 0:
            blank += 1
        if sorted(real) != sorted(predicted):
            bad += 1
            print(text)
            print("Predicted: [" + ', '.join(predicted) + "]-------> Real: [" + ', '.join(real) + "]")
            print("-"*50)


evaluator = Evaluator(y_true, y_preds, tags=['TEMP_EXP'], loader="dict")
results, results_by_tag, result_indices, result_indices_by_tag = evaluator.evaluate()
pprint.pprint(results)
