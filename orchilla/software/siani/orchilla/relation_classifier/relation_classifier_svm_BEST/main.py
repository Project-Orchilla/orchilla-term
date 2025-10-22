from flask import Flask, request, jsonify
from model import RelationClassifier

app = Flask(__name__)
clf = RelationClassifier()


@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json(force=True)

    required = ["sentence_1", "sentence_2", "entity_1", "entity_2"]
    if not all(k in data for k in required):
        return jsonify({"error": f"Missing one or more required fields: {required}"}), 400

    try:
        result = clf.predict(data["sentence_1"], data["sentence_2"], data["entity_1"], data["entity_2"])
        return jsonify({"prediction": result})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
