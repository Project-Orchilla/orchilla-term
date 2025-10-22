FROM python:3.10-slim

WORKDIR /app

COPY decomposer.py decomposer.py
COPY main.py main.py
COPY output/model-best model

RUN pip install --no-cache-dir spacy==3.7.5 \
    flask==3.0.3

CMD ["python", "main.py"]
