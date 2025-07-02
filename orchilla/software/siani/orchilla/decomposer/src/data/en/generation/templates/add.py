import random

from software.siani.orchilla.decomposer.src.data.en.generation.template import DateEntryTemplate


class AddDateEntryTemplate(DateEntryTemplate):
    TodayTemplates = ["today", "from today", "now", "from now", "at present"]
    ScaleTemplates = ["this {}", "of this {}", "from this {}", "the current {}", "of the current {}", "current {}"]
    TimeTemplates = ["in {}", "after {}"]
    NextWeekendTemplates = ["next {}", "the next {}", "following {}", "the following {}", "of next {}", "of the next {}", "of the following {}", "from next {}", "from the next {}", "from the following {}"]
    MultipleWeekendTemplates = ["within {} weekends", "in {} weekends", "after {} weekends", "by {} weekends", "in the next {} weekends"]
    DefaultTemplates = ["within {} {}s", "in {} {}s", "after {} {}s", "by {} {}s", "in the next {} {}s"]

    def __call__(self):
        probability = random.random()
        if probability < 1/6: return random.choice(self.TodayTemplates)
        if probability < 2/6: return self.__generate_tomorrow()
        if probability < 3/6: return self.__generate_scale()
        if probability < 4/6: return self.__generate_time()
        if probability < 5/6: return self.__generate_weekend()
        return self.__generate_default()

    def __generate_tomorrow(self):
        return "Tomorrow" if random.random() >= 0.5 else "The day after tomorrow"

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return random.choice(self.ScaleTemplates).format(scale.replace("-", " "))

    def __generate_time(self):
        if random.random() >= 0.5:
            return random.choice(self.TimeTemplates).format(random.choice(["a quarter of an hour", "quarter of an hour", "a quarter hour"]))
        if random.random() >= 0.5:
            return random.choice(self.TimeTemplates).format(random.choice(["three quarters of an hour", "three-quartes of an hour"]))
        return random.choice(self.TimeTemplates).format(random.choice(["half an hour", "a half hour"]))

    def __generate_weekend(self):
        n = self.number()
        if random.random() <= 0.2:
            return random.choice(self.ScaleTemplates).format("weekend")
        if n == 1:
            return random.choice(self.NextWeekendTemplates).format("weekend")
        return random.choice(self.MultipleWeekendTemplates).format(n)

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        if random.random() <= 0.3:
            percentage = random.choice(self.Percentages)
            template += " " + self.textify(percentage)
            return template.format(day, scale.replace("-", " "))
        return template.format(day, scale.replace("-", " "))
