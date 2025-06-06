from text_to_num import alpha2digit
import re

class NumberTranscriber:
    Ordinals = {"first": 1, "second": 2, "third": 3, "fourth": 4, "fifth": 5, "sixth": 6, "seventh": 7, "eighth": 8,
                "ninth": 9, "tenth": 10, "eleventh": 11, "twelfth": 12, "thirteenth": 13, "fourteenth": 14,
                "fifteenth": 15, "sixteenth": 16, "seventeenth": 17, "eighteenth": 18, "nineteenth": 19,
                "twentieth": 20, "twenty-first": 21, "twenty-second": 22, "twenty-third": 23, "twenty-fourth": 24,
                "twenty-fifth": 25, "twenty-sixth": 26, "twenty-seventh": 27, "twenty-eighth": 28, "twenty-ninth": 29,
                "thirtieth": 30, "thirty-first": 31}
    Decades = {20: "twenties", 30: "thirties", 40: "forties", 50: "fifties", 60: "sixties", 70: "seventies", 80: "eighties", 90: "nineties"}

    def transcribe(self, text: str):
        if self.__only_contains_dots_or_commas(text): return text
        string = self.__replace_ordinal_words(self.__clean_numbers(alpha2digit(text.replace("and ", ""), lang="en").replace("one", "1")))
        if string.isdigit() or self.__is_float(string): return int(string) if not string.__contains__(".") else float(string)
        return self.__replace_decades(string)

    def __only_contains_dots_or_commas(self, text):
        if len(text.split(" ")) != 1: return False
        return text.count(",") + text.count(".") > 1 or (len(text) == 1 and (text == "," or text == "."))

    def __replace_ordinal_words(self, text):
        return " ".join([str(self.Ordinals[word.replace(",", "").lower()]) if word.replace(",", "").lower() in self.Ordinals else word for word in text.split(" ")])

    def __clean_numbers(self, text):
        if re.compile("[0-9]*[.][0-9]*").match(text): return text.replace(" ", "")
        if re.compile("[0-9]*[,][0-9]*").match(text): return text.replace(" ", "").replace(",", ".")
        return " ".join([token[0:-2] if self.__is_ordinal_number(token) else token for token in text.split(" ")])

    def __is_float(self, value):
        try:
            float(value)
            return '.' in value or ',' in value
        except ValueError:
            return False

    def __replace_decades(self, text: str):
        for decade, name in self.Decades.items():
            text = text.lower().replace(name, str(decade) + "s")
        return text

    def __is_ordinal_number(self, token):
        return token[0:-2].isdigit() and (token[-2:] == "st" or token[-2:] == "nd" or token[-2:] == "rd" or token[-2:] == "th" or token.__contains__("."))
