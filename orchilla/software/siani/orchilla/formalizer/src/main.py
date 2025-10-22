from flask import Flask, request, jsonify
from formalizer import Formalizer

app = Flask(__name__)

MODEL_PATH = "/app/model/en"
formalizer = Formalizer(MODEL_PATH)


@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json(force=True)
    if "units" not in data:
        return jsonify({"error": "Missing required field: 'units' (must be a list of strings)"}), 400
    try:
        units = data["units"]
        if not isinstance(units, list) or not all(isinstance(u, str) for u in units):
            return jsonify({"error": "'units' must be a list of strings"}), 400
        prediction = formalizer.formalize(units)
        return jsonify({"prediction": f"({prediction})"})
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
