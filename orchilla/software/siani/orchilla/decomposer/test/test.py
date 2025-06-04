import ast
import json
from collections import defaultdict

import pandas as pd
from nervaluate import Evaluator

from orchilla.software.siani.orchilla.decomposer.src.decomposer import Decomposer

decomposer = Decomposer("../model")

y_true = []
y_preds = []


def parse(txt: str, ents: list):
    position = 0
    entities = []
    for ent in ents:
        start = txt[position:].find("ent")
        end = start + position + len(ent)
        entities.append({"label": "DATE", "start": start, "end": end})
        position = end
    return entities


def flip_nested_dict(dd):
    result = defaultdict(dict)
    for k1, d in dd.items():
        for k2, v in d.items():
            result[k2][k1] = v
    return dict(result)


with open("test.tsv", "r", encoding="utf-8") as f:
    for line in f:
        text, real = line.split("\t")
        y_true.append(parse(text, real))
        y_preds.append(decomposer.decompose_positions(text))
        real = ast.literal_eval(real)
        predicted = decomposer.decompose(text)
        if sorted(real) != sorted(predicted):
            print(text)
            print("Predicted: [" + ', '.join(predicted) + "]-------> Real: [" + ', '.join(real) + "]")
            print("-"*50)

evaluator = Evaluator(y_true, y_preds, tags=['UNIT'], loader="default")

results, results_by_tag, result_indices, result_indices_by_tag = evaluator.evaluate()
#print(pd.DataFrame(flip_nested_dict(results)))
import pprint
pprint.pprint(results)
