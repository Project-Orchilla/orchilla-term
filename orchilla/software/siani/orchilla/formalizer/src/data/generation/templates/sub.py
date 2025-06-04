import random

from orchilla.software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class SubDateEntryTemplate(DateEntryTemplate):
    TimeTemplates = ["{} ago", "{} back", "{} prior", "{} earlier", "{} before"]
    DefaultTemplates = ["{} {}s ago", "{} {}s back", "{} {}s prior", "{} {}s earlier", "{} {}s before"]

    def __call__(self):
        probability = random.random()
        if probability < 2/10: return self.__generate_yesterday()
        if probability < 6/10: return self.__generate_time()
        return self.__generate_default()

    def __generate_yesterday(self):
        return "sub 1d\tYesterday" if random.random() >= 0.5 else "sub 2d\tThe day before yesterday"

    def __generate_time(self):
        if random.random() >= 0.5:
            return "sub 15m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["a quarter of an hour", "quarter of an hour", "a quarter hour"])))
        if random.random() >= 0.5:
            return "sub 45m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["three quarters of an hour", "three-quartes of an hour"])))
        return "sub 30m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["half an hour", "a half hour"])))

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        return "sub {}{}\t{}".format(day, scale[0], template.format(day, scale)).replace("Centurys", "Centuries")
