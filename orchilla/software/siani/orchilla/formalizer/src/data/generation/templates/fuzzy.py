import random

from software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class FuzzyEntryTemplate(DateEntryTemplate):
    EarlyTemplates = ["start", "beginning", "the start", "the beginning"]
    MidTemplates = ["in the middle", "halfway through", "mid"]
    LateTemplates = ["end", "late"]
    AroundTemplates = ["around", "approximately"]
    MorningTemplates = ["in the morning", "morning"]
    AfternoonTemplates = ["afternoon", "in the afternoon"]
    EveningTemplates = ["evening", "in the evening"]
    NightTemplates = ["night", "in the night"]
    BeforeTemplates = ["before"]
    AfterTemplates = ["after"]

    def __call__(self):
        probability = random.random()
        if probability < 1/10: return self.__generate_early()
        if probability < 2/10: return self.__generate_mid()
        if probability < 3/10: return self.__generate_late()
        if probability < 4/10: return self.__generate_morning()
        if probability < 5/10: return self.__generate_afternoon()
        if probability < 6/10: return self.__generate_evening()
        if probability < 7/10: return self.__generate_night()
        if probability < 8/10: return self.__generate_before()
        if probability < 9/10: return self.__generate_after()
        return self.__generate_around()

    def __generate_early(self):
        return "early\t{}".format(random.choice(self.EarlyTemplates))

    def __generate_mid(self):
        return "mid\t{}".format(random.choice(self.MidTemplates))

    def __generate_late(self):
        return "late\t{}".format(random.choice(self.LateTemplates))

    def __generate_before(self):
        return "before\t{}".format(random.choice(self.BeforeTemplates))

    def __generate_after(self):
        return "after\t{}".format(random.choice(self.AfterTemplates))

    def __generate_morning(self):
        return "morning\t{}".format(random.choice(self.MorningTemplates))

    def __generate_afternoon(self):
        return "afternoon\t{}".format(random.choice(self.AfternoonTemplates))

    def __generate_evening(self):
        return "evening\t{}".format(random.choice(self.EveningTemplates))

    def __generate_night(self):
        return "night\t{}".format(random.choice(self.NightTemplates))

    def __generate_around(self):
        return "around\t{}".format(random.choice(self.AroundTemplates))
