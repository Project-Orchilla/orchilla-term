import ast
import json
from collections import defaultdict

import pandas as pd
from nervaluate import Evaluator

y_true = []
y_preds = []


def parse(ents: list):
    return [{"label": label, "start": start, "end": end} for start, end, label in ents]


def parse_pred(ents: list):
    return [{"label": "TEMP_EXP", "start": start, "end": end} for start, end, _, _ in ents]


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


def show_ents(ents: list):
    result = []
    for start, end, extrc, label in ents:
        result.append(extrc)
    return result


with open("test.tsv", "r", encoding="utf-8") as f1, open("C:/Users/juanc/PycharmProjects/date-transcriber/sutime.tsv", "r", encoding="utf-8") as f2:
    bad = 0
    blank = 0
    total = 0
    for line1, line2 in zip(f1, f2):
        total += 1
        text, entities = line1.split("\t")
        y_true.append(parse(json.loads(entities)['entities']))
        _, predict = line2.split("\t")
        y_preds.append(parse_pred(json.loads(predict)['entities']))
        real = show(text, json.loads(entities)['entities'])
        predicted = show_ents(json.loads(predict)['entities'])
        if len(real) == 0 and len(predicted) == 0:
            blank += 1
        if sorted(real) != sorted(predicted):
            bad += 1
            print(text)
            print("Predicted: [" + ', '.join(predicted) + "]-------> Real: [" + ', '.join(real) + "]")
            print("-"*50)

print("Has fallado {} de {}".format(bad, total))
print("blanos", blank)
evaluator = Evaluator(y_true, y_preds, tags=['TEMP_EXP'], loader="default")

results, results_by_tag, result_indices, result_indices_by_tag = evaluator.evaluate()
#print(pd.DataFrame(flip_nested_dict(results)))
import pprint
pprint.pprint(results)
