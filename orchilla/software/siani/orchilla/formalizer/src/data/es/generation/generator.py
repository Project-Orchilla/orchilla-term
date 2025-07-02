import random

from software.siani.orchilla.formalizer.src.data.es.generation.templates.ordinal import OrdinalDateEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.set import SetTimeEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.template import DateEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.add import AddDateEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.last import LastDateEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.next import NextDateEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.fuzzy import FuzzyEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.set import SetDateEntryTemplate
from software.siani.orchilla.formalizer.src.data.es.generation.templates.sub import SubDateEntryTemplate


class DateDatasetGenerator:
    def __init__(self, templates: list[DateEntryTemplate], num_samples: int = 100):
        self.templates = templates
        self.num_samples = num_samples

    def generate(self, file: str):
        with open(file, 'w', encoding='utf-8') as f:
            f.write("operator\ttext\n")
            for i in range(self.num_samples):
                f.write(random.choice(self.templates)())
                if self.__is_last_line(i): continue
                f.write("\n")

    def __is_last_line(self, i):
        return i == self.num_samples - 1


if __name__ == '__main__':
    generator = DateDatasetGenerator([SubDateEntryTemplate(),
                                      AddDateEntryTemplate(),
                                      NextDateEntryTemplate(),
                                      LastDateEntryTemplate(),
                                      SetDateEntryTemplate(),
                                      SetTimeEntryTemplate(),
                                      OrdinalDateEntryTemplate(),
                                      FuzzyEntryTemplate()
                                      ], num_samples=800000)
    generator.generate("../dataset.tsv")
