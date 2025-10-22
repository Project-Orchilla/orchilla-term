from flask import Flask, request, jsonify
from recognizer import Recognizer

app = Flask(__name__)

recognizer = Recognizer("/app/model/en")


@app.route("/recognize", methods=["POST"])
def recognize_text():
    data = request.get_json(force=True)
    if "text" not in data:
        return jsonify({"error": "Missing required field: 'text'"}), 400

    try:
        entities = recognizer.recognize(data["text"])
        return jsonify({"entities": entities})
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
