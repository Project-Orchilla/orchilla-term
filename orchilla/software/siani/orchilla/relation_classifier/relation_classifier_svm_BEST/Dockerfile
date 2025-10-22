FROM pytorch/pytorch:2.3.0-cuda12.1-cudnn8-runtime

WORKDIR /app

RUN apt-get update && apt-get install -y git wget curl && rm -rf /var/lib/apt/lists/*

COPY encode_pair.py /app/encode_pair.py
COPY main.py /app/main.py
COPY model.py /app/model.py

COPY svm_relation_classifier /app/svm_relation_classifier

RUN pip install --no-cache-dir \
    flask==3.0.3 \
    transformers==4.43.3 \
    torch==2.3.0 \
    scikit-learn==1.5.1 \
    joblib==1.4.2 \
    datasets==2.20.0 \
    evaluate==0.4.2

RUN python -c "from transformers import AutoTokenizer, AutoModel; \
    AutoTokenizer.from_pretrained('FacebookAI/roberta-base'); \
    AutoModel.from_pretrained('FacebookAI/roberta-base')"

EXPOSE 8000

CMD ["python", "main.py"]
