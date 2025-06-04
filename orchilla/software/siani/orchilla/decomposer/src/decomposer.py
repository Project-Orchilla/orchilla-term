import pickle


class Decomposer:
    def __init__(self, path: str):
        self.model = self.__load_model(path)

    def decompose(self, event: str):
        return [ent.text for ent in self.model(text=event).ents]

    def decompose_positions(self, text: str):
        return [{"label": "UNIT", "start": ent.start_char, "end": ent.end_char} for ent in self.model(text).ents]

    def __load_model(self, path: str):
        with open(f"{path}/decomposer.mdl", 'rb') as f:
            return pickle.load(f)
