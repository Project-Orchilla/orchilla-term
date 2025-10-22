from flask import Flask, request, jsonify
from decomposer import Decomposer

app = Flask(__name__)

MODEL_PATH = "/app/model"

decomposer = Decomposer(MODEL_PATH)


@app.route("/decompose", methods=["POST"])
def decompose():
    data = request.get_json(force=True)
    if "text" not in data:
        return jsonify({"error": "Missing required field: 'text'"}), 400

    try:
        text = data["text"]
        entities = decomposer.decompose(text)
        return jsonify({"entities": entities})
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
