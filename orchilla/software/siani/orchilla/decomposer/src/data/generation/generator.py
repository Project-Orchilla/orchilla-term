import json
import random

from software.siani.orchilla.decomposer.src.data.generation.template import DateEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.fuzzy import FuzzyEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.last import LastDateEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.next import NextDateEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.ordinal import OrdinalDateEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.set import SetDateEntryTemplate, SetTimeEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.sub import SubDateEntryTemplate
from software.siani.orchilla.decomposer.src.data.generation.templates.add import AddDateEntryTemplate


class DateDatasetGenerator:
    def __init__(self, templates: list[DateEntryTemplate], num_samples: int = 100):
        self.templates = templates
        self.num_samples = num_samples

    def generate(self, file: str):
        with open(file, 'w') as f:
            for i in range(self.num_samples):
                entities = []
                prev = 0
                for _ in range(random.randint(1, 3)):
                    template = random.choice(self.templates)
                    entry = template()
                    f.write(f"{entry} ")
                    entities.append([prev, prev + len(entry), "UNIT"])
                    prev += len(entry) + 1
                f.write(f"\t{json.dumps({"entities": entities})}")
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
                                      ], num_samples=10000)
    generator.generate("../../data/dataset.tsv")
