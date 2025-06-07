import requests
from software.siani.orchilla.decomposer.src.decomposer import Decomposer
from software.siani.orchilla.formalizer.src.formalizer import Formalizer

decomposer = Decomposer(path="../../decomposer/model")
formalizer = Formalizer(path="../model")

url = "http://127.0.0.1:8080/process"
headers = {
    "Content-Type": "application/json",
    "Accept": "application/json"
}

def transcribe(operations: str) -> dict:
    payload = {
        "context": "2025",
        "operations": operations
    }
    r = requests.post(url, json=payload, headers=headers)
    return r.json()

correct = 0
mid = 0
with open("test.tsv", "r", encoding="utf-8") as f:
    lines = f.readlines()
    for line in lines:
        text, command = line.strip().split("\t")

        pred = formalizer.formalize(decomposer.decompose(text))
        pred_response = transcribe(pred)
        real_response = transcribe(command)
        print("Command: " + command)
        print("Pred Command: " + pred)
        print(f"Source:    {text}")
        print(f"Real:      {real_response}")
        print(f"Predicted: {pred_response}")
        print("-" * 50)
        if (real_response["head"] if "head" in real_response else "") == (pred_response["head"] if "head" in pred_response else ""):
            correct += 1
            continue
        mid += (real_response["head"].split("T")[0] if "head" in real_response else "") == (pred_response["head"].split("T")[0] if "head" in pred_response else "")
        mid += (real_response["head"].split("T")[1] if "head" in real_response else "") == (pred_response["head"].split("T")[1] if "head" in pred_response else "")

print(str(correct) + "/" + str(len(lines)))
print(str(mid) + "/" + str(len(lines)))
