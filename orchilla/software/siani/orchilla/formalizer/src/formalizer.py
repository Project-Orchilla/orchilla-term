import torch
from transformers import AutoModelForSeq2SeqLM, AutoTokenizer

from sorter import OperatorSorter


class Formalizer:
    Weekdays = {"monday": 1, "tuesday": 2, "wednesday": 3, "thursday": 4, "friday": 5, "saturday": 6, "sunday": 7}
    Months = {"january": 1, "february": 2, "march": 3, "april": 4, "may": 5, "june": 6, "july": 7, "august": 8, "september": 9, "october": 10, "november": 11, "december": 12}
    Seasons = {"spring": 1, "summer": 2, "autumn": 3, "winter": 4}

    def __init__(self, path: str):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model = AutoModelForSeq2SeqLM.from_pretrained(path, local_files_only=True).to(self.device)
        self.tokenizer = AutoTokenizer.from_pretrained(path, local_files_only=True)
        self.sorter = OperatorSorter()

    def formalize(self, units: list) -> str:
        return ">>".join(self.sorter.sort([self.__replace_symbols(self.__to_operator(unit)) for unit in units]))

    def __to_operator(self, unit):
        input_text = f"translate: {unit}"
        input_ids = self.tokenizer(input_text, return_tensors="pt").to(self.device)
        output_ids = self.model.generate(**input_ids, max_length=512)
        return self.tokenizer.decode(output_ids[0], skip_special_tokens=True)

    def __replace_symbols(self, text: str) -> str:
        for weekday in self.Weekdays.keys():
            if weekday in text.lower():
                start = text.lower().find(weekday)
                end = start + len(weekday)
                text = f"{text[:start]}{self.Weekdays.get(weekday):02d}{text[end:]}"
        for month in self.Months.keys():
            if month in text.lower():
                start = text.lower().find(month)
                end = start + len(month)
                text = f"{text[:start]}{self.Months.get(month):02d}{text[end:]}"
        for season in self.Seasons.keys():
            if season in text.lower():
                start = text.lower().find(season)
                end = start + len(season)
                text = f"{text[:start]}{self.Seasons.get(season):02d}{text[end:]}"
        return text
