import pickle


class Decomposer:
    def __init__(self, path: str):
        self.model = self.__load_model(path)

    def decompose(self, event: str):
        return [ent.text for ent in self.model(text=event).ents]

    def decompose_positions(self, text: str):
        ents = []
        for ent in self.model(text).ents:
            ents.append({"label": "UNIT", "start": ent.start_char, "end": ent.end_char})
        return ents

    def __load_model(self, path: str):
        with open(f"{path}/decomposer.mdl", 'rb') as f:
            return pickle.load(f)


if __name__ == "__main__":
    print(Decomposer("C:/Users/juanc/IdeaProjects/orchilla-term/orchilla/software/siani/orchilla/decomposer/model/es").decompose("el primer lunes de este mes"))