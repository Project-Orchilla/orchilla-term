from pathlib import Path

import numpy as np
import pandas as pd
import torch
from torch.utils.data import random_split
from transformers import BertTokenizer, BertForSequenceClassification, TrainingArguments, Trainer

from software.siani.orchilla.relation_classifier.relation_classifier_bert.dataset import RelationDataset


class RelationClassifierTrainer:
    SpecialTokens = ['[BOTEXT]', '[EOTEXT]', '[BOPHRASE]', '[EOPHRASE]']

    def __init__(self):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "mps" if torch.backends.mps.is_available() else "cpu")
        self.tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
        self.tokenizer.add_special_tokens({'additional_special_tokens': self.SpecialTokens})
        self.model = BertForSequenceClassification.from_pretrained('bert-base-uncased', num_labels=2).to(self.device)
        self.model.resize_token_embeddings(len(self.tokenizer))

    def train(self, dataset_path: str, path: str):
        dataset = RelationDataset(*self.__read(dataset_path), self.tokenizer)
        training_dataset, validation_dataset = self.__split(dataset)
        trainer = self.__build_trainer(training_dataset, validation_dataset)
        trainer.train()
        self.__evaluate(trainer, validation_dataset)
        self.__save_model(path, trainer)

    def __read(self, dataset: str):
        self.df = pd.read_csv(dataset, sep='\t', names=['text', 'phrase1', 'phrase2', 'label'])
        self.df['labels'] = self.df['label'].map({'not-related': 0, 'related': 1})
        self.df['input_text'] = self.df.apply(lambda row: self.__format_input(row['text'], row['phrase1'], row['phrase2']), axis=1)
        inputs = self.df['input_text'].tolist()
        labels = self.df['labels'].tolist()
        return inputs, labels

    def __build_trainer(self, train_dataset: RelationDataset, validation_dataset: RelationDataset):
        training_args = TrainingArguments(
            output_dir='./results',
            eval_strategy='epoch',
            num_train_epochs=5,
            per_device_train_batch_size=8,
            logging_dir='./logs'
        )
        return Trainer(model=self.model, args=training_args, train_dataset=train_dataset, eval_dataset=validation_dataset)

    def __evaluate(self, trainer: Trainer, test_dataset: RelationDataset):
        preds_output = trainer.predict(test_dataset)
        correct = (np.argmax(preds_output.predictions, axis=1) == preds_output.label_ids).sum()
        total = len(preds_output.label_ids)
        accuracy = correct / total
        print(f"Correct predictions: {correct} / {total}")
        print(f"Accuracy: {accuracy:.4f}")
        val_indices = test_dataset.indices if hasattr(test_dataset, 'indices') else test_dataset.indices
        for i, idx in enumerate(val_indices):
            if np.argmax(preds_output.predictions, axis=1)[i] != preds_output.label_ids[i]:
                text = self.df.loc[idx, 'text']
                phrase1 = self.df.loc[idx, 'phrase1']
                phrase2 = self.df.loc[idx, 'phrase2']
                true_label = self.df.loc[idx, 'label']
                pred_label = {0: 'not-related', 1: 'related'}[int(np.argmax(preds_output.predictions, axis=1)[i])]
                print(f"Text: {text}")
                print(f"  Phrase1: {phrase1}")
                print(f"  Phrase2: {phrase2}")
                print(f"  True: {true_label}, Predicted: {pred_label}")

    def __save_model(self, path: str, trainer):
        Path(path).mkdir(parents=True, exist_ok=True)
        trainer.save_model(path)

    def __split(self, dataset: RelationDataset):
        total_size = len(dataset)
        validation_size = int(0.1 * total_size)
        train_size = total_size - validation_size
        return random_split(dataset, [train_size, validation_size])

    def __format_input(self, text, phrase1, phrase2):
        marked = text.replace(phrase1, f"[BOPHRASE] {phrase1} [EOPHRASE]", 1)
        marked = marked.replace(phrase2, f"[BOPHRASE] {phrase2} [EOPHRASE]", 1)
        return f"[BOTEXT] {marked} [EOTEXT]"