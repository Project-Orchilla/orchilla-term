import random

from orchilla.software.siani.orchilla.decomposer.src.data.generation.template import DateEntryTemplate


class AddDateEntryTemplate(DateEntryTemplate):
    TodayTemplates = ["today", "from today", "now", "from now", "at present", "in the blink of an eye", "in a flash", "in no time"]
    ScaleTemplates = ["this {}", "of this {}", "from this {}", "the current {}", "of the current {}", "current {}"]
    TimeTemplates = ["in {}", "after {}"]
    DefaultTemplates = ["within {} {}s", "in {} {}s", "after {} {}s", "by {} {}s", "in the next {} {}s"]

    def __call__(self):
        probability = random.random()
        if probability < 1/5: return random.choice(self.TodayTemplates)
        if probability < 2/5: return self.__generate_tomorrow()
        if probability < 3/5: return self.__generate_scale()
        if probability < 4/5: return self.__generate_time()
        return self.__generate_default()

    def __generate_tomorrow(self):
        return "Tomorrow" if random.random() >= 0.5 else "The day after tomorrow"

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return random.choice(self.ScaleTemplates).format(scale.lower())

    def __generate_time(self):
        if random.random() >= 0.5:
            return random.choice(self.TimeTemplates).format(random.choice(["a quarter of an hour", "quarter of an hour", "a quarter hour"]))
        if random.random() >= 0.5:
            return random.choice(self.TimeTemplates).format(random.choice(["three quarters of an hour", "three-quartes of an hour"]))
        return random.choice(self.TimeTemplates).format(random.choice(["half an hour", "a half hour"]))

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        return template.format(day, scale)
