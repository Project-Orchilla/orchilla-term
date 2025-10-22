import joblib
from transformers import AutoTokenizer, AutoModel
from software.siani.orchilla.relation_classifier.relation_classifier_svm_BEST.encode_pair import encode_pair

MODEL_NAME = "FacebookAI/roberta-base"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModel.from_pretrained(MODEL_NAME)
for p in model.parameters(): p.requires_grad = False
model.eval()

clf = joblib.load("svm_relation_classifier/model.joblib")

feat = encode_pair(
	"Two weeks later the last meeting",
	"The meeting occurred one day after Dans death",
	"Two weeks later the last meeting",
	"The meeting occurred one day after Dans death")
pred = clf.predict([feat])
print(pred)
