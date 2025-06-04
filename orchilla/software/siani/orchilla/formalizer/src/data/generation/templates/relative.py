import random

from orchilla.software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class RelativeEntryTemplate(DateEntryTemplate):
    EarlyTemplates = ["start", "beginning", "the start", "the beginning"]
    MidTemplates = ["in the middle", "halfway through", "mid"]
    LateTemplates = ["end"]
    AroundTemplates = ["around"]


    def __call__(self):
        probability = random.random()
        if probability < 1 / 4: return self.__generate_early()
        if probability < 2 / 4: return self.__generate_mid()
        if probability < 3 / 4: return self.__generate_late()
        return self.__generate_around()

    def __generate_early(self):
        return "early\t{}".format(random.choice(self.EarlyTemplates))

    def __generate_mid(self):
        return "mid\t{}".format(random.choice(self.MidTemplates))

    def __generate_late(self):
        return "late\t{}".format(random.choice(self.LateTemplates))

    def __generate_around(self):
        return "around\t{}".format(random.choice(self.AroundTemplates))