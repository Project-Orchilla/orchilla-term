import ast
import json
from collections import defaultdict

import pandas as pd
from nervaluate import Evaluator

from orchilla.software.siani.orchilla.recognizer.src.recognizer import Recognizer

recognizer = Recognizer("../model")

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


with open("test.tsv", "r", encoding="utf-8") as f:
    for line in f:
        text, entities, real = line.split("\t")
        y_true.append(parse(json.loads(entities)['entities']))
        y_preds.append(recognizer.recognize_positions(text))
        real = ast.literal_eval(real)
        predicted = recognizer.recognize(text)
        if sorted(real) != sorted(predicted):
            print(text)
            print("Predicted: [" + ', '.join(predicted) + "]-------> Real: [" + ', '.join(real) + "]")
            print("-"*50)

evaluator = Evaluator(y_true, y_preds, tags=['TEMP_EXP'], loader="default")

results, results_by_tag, result_indices, result_indices_by_tag  = evaluator.evaluate()
#print(pd.DataFrame(flip_nested_dict(results)))
import pprint
pprint.pprint(results)
