from pathlib import Path

import numpy as np
from datasets import load_dataset, DatasetDict
from evaluate import load
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM, Seq2SeqTrainingArguments, DataCollatorForSeq2Seq, Seq2SeqTrainer


class FormalizerTrainer:
    MaxInputLength = 1024
    MaxTargetLength = 128

    def __init__(self, batch_size: int = 32):
        self.tokenizer = AutoTokenizer.from_pretrained("google-t5/t5-small")
        self.model = AutoModelForSeq2SeqLM.from_pretrained("google-t5/t5-small")
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
        model_inputs = self.tokenizer(inputs, max_length=self.MaxInputLength, truncation=True)
        labels = self.tokenizer(text_target=examples["command"], max_length=self.MaxTargetLength, truncation=True)
        model_inputs["labels"] = labels["input_ids"]
        return model_inputs

    def __build_trainer(self, dataset: DatasetDict):
        args = Seq2SeqTrainingArguments(
            output_dir=f"./command-transcriber",
            eval_strategy="epoch",
            learning_rate=2e-5,
            per_device_train_batch_size=self.batch_size,
            per_device_eval_batch_size=self.batch_size,
            weight_decay=0.01,
            save_total_limit=3,
            num_train_epochs=2,
            logging_steps=8000,
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
            compute_metrics=self.__compute_metrics
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
        Path(path).mkdir(parents=True, exist_ok=True)
        trainer.save_model(path)


if __name__ == "__main__":
    FormalizerTrainer().train(dataset="data/dataset.tsv", path="../model")