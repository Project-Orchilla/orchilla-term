import random

from orchilla.software.siani.orchilla.decomposer.src.data.generation.template import DateEntryTemplate


class RelativeEntryTemplate(DateEntryTemplate):
    StartTemplates = ["start", "beginning", "the start", "the beginning", "at present"]
    MidTemplates = ["in the middle", "halfway through", "mid", "the beginning", "at present"]
    EndTemplates = ["end"]

    def __call__(self):
        probability = random.random()
        if probability < 1 / 3: return self.__generate_start()
        if probability < 2 / 3: return self.__generate_mid()
        return self.__generate_end()

    def __generate_start(self):
        return random.choice(self.StartTemplates)

    def __generate_mid(self):
        return random.choice(self.MidTemplates)

    def __generate_end(self):
        return random.choice(self.EndTemplates)