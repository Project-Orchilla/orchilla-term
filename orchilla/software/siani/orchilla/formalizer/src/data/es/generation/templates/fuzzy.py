import random

from software.siani.orchilla.formalizer.src.data.es.generation.template import DateEntryTemplate


class FuzzyEntryTemplate(DateEntryTemplate):
    EarlyTemplates = ["inicios", "comienzos", "el inicio", "el comienzo"]
    MidTemplates = ["mediados", "a mitad", "medio"]
    LateTemplates = ["finales"]
    AroundTemplates = ["alrededor", "aproximadamente"]
    MorningTemplates = ["en la mañana", "por la mañana"]
    AfternoonTemplates = ["por la tarde", "en la tarde"]
    EveningTemplates = ["por la tarde"]
    NightTemplates = ["por la noche", "en la noche"]
    BeforeTemplates = ["antes"]
    AfterTemplates = ["después"]

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
