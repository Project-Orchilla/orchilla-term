import joblib
from transformers import AutoTokenizer, AutoModel

from software.siani.orchilla.relation_classifier.relation_classifier_svm_BEST.main import encode_pair

MODEL_NAME = "FacebookAI/roberta-base"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModel.from_pretrained(MODEL_NAME)
for p in model.parameters(): p.requires_grad = False
model.eval()

clf = joblib.load("svm_relation_classifier/model.joblib")

feat = encode_pair(
	"People have predicted his demise so many times , and the US has tried to hasten it on several occasions.",
	"People have predicted his demise so many times , and the US has tried to hasten it on several occasions.",
	"predicted",
	"demise")
pred = clf.predict([feat])
print(pred)
