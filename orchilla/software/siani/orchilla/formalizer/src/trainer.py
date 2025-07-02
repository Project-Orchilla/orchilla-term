import json
from pathlib import Path

import numpy as np
import torch
from datasets import load_dataset, DatasetDict
from evaluate import load
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM, Seq2SeqTrainingArguments, DataCollatorForSeq2Seq, \
    Seq2SeqTrainer, TrainerCallback


class FormalizerTrainer:
    MaxInputLength = 1024
    MaxTargetLength = 512
    Models = {"en": "google-t5/t5-small", "es": "google/mt5-small"}

    def __init__(self, language: str, batch_size: int = 32):
        self.language = language
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.tokenizer = AutoTokenizer.from_pretrained(self.Models[language])
        self.model = AutoModelForSeq2SeqLM.from_pretrained(self.Models[language]).to(self.device)
        self.metric = load("rouge")
        self.batch_size = batch_size

    def train(self, dataset: str, path: str):
        data = self.__prepare_dataset(path=dataset).map(self.__preprocess, batched=True)
        trainer = self.__build_trainer(data)
        trainer.train()
        self.__save_model(path, trainer)

    def __prepare_dataset(self, path: str):
        data = load_dataset("csv", data_files=path, delimiter="\t")["train"]
        train_test_split = data.train_test_split(test_size=0.2, seed=42)
        train_dataset = train_test_split["train"]
        val_test_split = train_test_split["test"].train_test_split(test_size=0.5, seed=42)
        val_dataset = val_test_split["train"]
        test_dataset = val_test_split["test"]
        return DatasetDict({
            "train": train_dataset,
            "validation": val_dataset,
            "test": test_dataset,
        })

    def __preprocess(self, examples):
        inputs = ["translate" + text for text in examples["text"]]
        model_inputs = self.tokenizer(inputs, max_length=self.MaxInputLength, truncation=True).to(self.device)
        labels = self.tokenizer(text_target=examples["operator"], max_length=self.MaxTargetLength, truncation=True).to(self.device)
        model_inputs["labels"] = labels["input_ids"]
        return model_inputs

    def __build_trainer(self, dataset: DatasetDict):
        args = Seq2SeqTrainingArguments(
            output_dir=f"./operator-formalizer",
            eval_strategy="epoch",
            learning_rate=2e-5,
            per_device_train_batch_size=self.batch_size,
            per_device_eval_batch_size=self.batch_size,
            weight_decay=0.01,
            save_total_limit=3,
            num_train_epochs=3,
            logging_steps=1000,
            predict_with_generate=True,
            save_strategy="no",
            fp16=True
        )
        collator = DataCollatorForSeq2Seq(self.tokenizer, model=self.model)
        return Seq2SeqTrainer(
            model=self.model,
            args=args,
            train_dataset=dataset["train"],
            eval_dataset=dataset["validation"],
            data_collator=collator,
            processing_class=self.tokenizer,
            compute_metrics=self.__compute_metrics,
            callbacks=[Logger(".")]
        )

    def __compute_metrics(self, eval_pred):
        predictions, labels = eval_pred
        decoded_preds = self.tokenizer.batch_decode(predictions, skip_special_tokens=True)
        labels = np.where(labels != -100, labels, self.tokenizer.pad_token_id)
        decoded_labels = self.tokenizer.batch_decode(labels, skip_special_tokens=True)
        result = self.metric.compute(predictions=decoded_preds, references=decoded_labels, use_stemmer=True, use_aggregator=True)
        result = {key: value * 100 for key, value in result.items()}
        prediction_lens = [np.count_nonzero(pred != self.tokenizer.pad_token_id) for pred in predictions]
        result["gen_len"] = np.mean(prediction_lens)
        return {k: round(v, 4) for k, v in result.items()}

    def __save_model(self, path: str, trainer):
        Path(f"{path}/{self.language}").mkdir(parents=True, exist_ok=True)
        trainer.save_model(f"{path}/{self.language}")


class Logger(TrainerCallback):
    def __init__(self, path: str):
        Path(path).mkdir(parents=True, exist_ok=True)
        self.logging_file = open(path + "/formalizer.log", "w", encoding='utf-8')

    def on_log(self, args, state, control, logs=None, **kwargs):
        self.logging_file.write(json.dumps(logs))
        self.logging_file.write("\n")
        self.logging_file.flush()
