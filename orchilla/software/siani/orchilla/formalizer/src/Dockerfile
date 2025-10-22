FROM pytorch/pytorch:2.3.0-cuda12.1-cudnn8-runtime

WORKDIR /app

COPY main.py main.py
COPY model model
COPY formalizer.py formalizer.py
COPY sorter.py sorter.py

RUN pip install --no-cache-dir \
    flask==3.0.3 \
    torch==2.3.0 \
    transformers==4.43.3

EXPOSE 8000

CMD ["python", "/app/main.py"]
