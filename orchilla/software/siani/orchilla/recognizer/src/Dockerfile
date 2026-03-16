FROM --platform=linux/amd64 python:3.10-slim

WORKDIR /app

COPY main.py /app/main.py
COPY recognizer.py /app/recognizer.py
COPY model /app/model

RUN pip install --no-cache-dir flask==3.0.3 spacy==3.7.5

EXPOSE 8000

CMD ["python", "main.py"]
