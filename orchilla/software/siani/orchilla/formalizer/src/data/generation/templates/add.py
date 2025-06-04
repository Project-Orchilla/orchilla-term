import random

from orchilla.software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class AddDateEntryTemplate(DateEntryTemplate):
    TodayTemplates = ["today", "from today", "now", "from now", "at present", "in the blink of an eye", "in a flash", "in no time"]
    ScaleTemplates = ["this {}", "of this {}", "from this {}", "the current {}", "of the current {}", "current {}"]
    TimeTemplates = ["in {}", "after {}"]
    DefaultTemplates = ["within {} {}s", "in {} {}s", "after {} {}s", "by {} {}s", "in the next {} {}s"]

    def __call__(self):
        probability = random.random()
        if probability < 1/5: return self.__generate_today()
        if probability < 2/5: return self.__generate_tomorrow()
        if probability < 3/5: return self.__generate_scale()
        if probability < 4/5: return self.__generate_time()
        return self.__generate_default()

    def __generate_today(self):
        return "add 0d\t{}".format(random.choice(self.TodayTemplates))

    def __generate_tomorrow(self):
        return "add 1d\tTomorrow" if random.random() >= 0.5 else "add 2d\tThe day after tomorrow"

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return "add 0{}\t{}".format(scale[0], random.choice(self.ScaleTemplates).format(scale.lower()))

    def __generate_time(self):
        if random.random() >= 0.5:
            return "add 15m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["a quarter of an hour", "quarter of an hour", "a quarter hour"])))
        if random.random() >= 0.5:
            return "add 45m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["three quarters of an hour", "three-quartes of an hour"])))
        return "add 30m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["half an hour", "a half hour"])))

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        return "add {}{}\t{}".format(day, scale[0], template.format(day, scale)).replace("Centurys", "Centuries")
