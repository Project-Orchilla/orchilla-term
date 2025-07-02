import random

from software.siani.orchilla.formalizer.src.data.en.generation.template import DateEntryTemplate


class AddDateEntryTemplate(DateEntryTemplate):
    TodayTemplates = ["today", "from today", "now", "from now", "at present"]
    ScaleTemplates = ["this {}", "of this {}", "from this {}", "the current {}", "of the current {}", "current {}"]
    TimeTemplates = ["in {}", "after {}"]
    NextWeekendTemplates = ["next {}", "the next {}", "following {}", "the following {}", "of next {}", "of the next {}", "of the following {}", "from next {}", "from the next {}", "from the following {}"]
    MultipleWeekendTemplates = ["within {} weekends", "in {} weekends", "after {} weekends", "by {} weekends", "in the next {} weekends"]
    DefaultTemplates = ["within {} {}s", "in {} {}s", "after {} {}s", "by {} {}s", "in the next {} {}s"]

    def __call__(self):
        probability = random.random()
        if probability < 1/6: return self.__generate_today()
        if probability < 2/6: return self.__generate_tomorrow()
        if probability < 3/6: return self.__generate_scale()
        if probability < 4/6: return self.__generate_time()
        if probability < 5/6: return self.__generate_weekend()
        return self.__generate_default()

    def __generate_today(self):
        return "add 0d\t{}".format(random.choice(self.TodayTemplates))

    def __generate_tomorrow(self):
        return "add 1d\tTomorrow" if random.random() >= 0.5 else "add 2d\tThe day after tomorrow"

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return "add 0{}\t{}".format(self.code_of(scale), random.choice(self.ScaleTemplates).format(scale.replace("-", " ")))

    def __generate_time(self):
        if random.random() >= 0.5:
            return "add 15m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["a quarter of an hour", "quarter of an hour", "a quarter hour"])))
        if random.random() >= 0.5:
            return "add 45m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["three quarters of an hour", "three-quartes of an hour"])))
        return "add 30m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["half an hour", "a half hour"])))

    def __generate_weekend(self):
        n = self.number()
        if random.random() <= 0.2:
            return "add 0we\t{}".format(random.choice(self.ScaleTemplates).format("weekend"))
        if n == 1:
            return "add 1we\t{}".format(random.choice(self.NextWeekendTemplates).format("weekend"))
        return "add {}we\t{}".format(n, random.choice(self.MultipleWeekendTemplates).format(n))

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        if random.random() <= 0.3:
            percentage = random.choice(self.Percentages)
            template += " " + self.textify(percentage)
            return "add {}{}\t{}".format(day + percentage, self.code_of(scale), template.format(day, scale.replace("-", " "))).replace("Centurys", "Centuries")
        return "add {}{}\t{}".format(day, self.code_of(scale), template.format(day, scale.replace("-", " "))).replace("Centurys", "Centuries")
