import spacy


class Decomposer:
	def __init__(self, path: str):
		self.model = self.__load_model(path)

	def decompose(self, event: str):
		return [ent.text for ent in self.model(event).ents]

	def decompose_positions(self, text: str):
		ents = []
		for ent in self.model(text).ents:
			ents.append({"label": ent.label_, "start": ent.start_char, "end": ent.end_char})
		return ents

	def __load_model(self, path: str):
		return spacy.load(path)
