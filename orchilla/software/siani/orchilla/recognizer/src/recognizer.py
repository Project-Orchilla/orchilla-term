import pickle


class Recognizer:
    def __init__(self, path: str):
        self.model = self.__load_model(path)

    def recognize(self, text: str):
        return [ent.text for ent in self.model(text).ents]

    def recognize_positions(self, text: str):
        return [{"label": "TEMP_EXP", "start": ent.start_char, "end": ent.end_char} for ent in self.model(text).ents]

    def __load_model(self, path: str):
        with open(f"{path}/recognizer.mdl", 'rb') as f:
            return pickle.load(f)
