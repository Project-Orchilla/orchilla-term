import random

from software.siani.orchilla.formalizer.src.data.en.generation.template import DateEntryTemplate


class SubDateEntryTemplate(DateEntryTemplate):
    TimeTemplates = ["{} ago", "{} back", "{} prior", "{} earlier", "{} before"]
    PastWeekendTemplates = ["last {}", "past {}", "this past {}", "the {} before", "previous {}", "{} prior"]
    MultipleWeekendTemplates = ["{} weekends ago", "{} weekends back", "{} weekends before", "the prior {} weekends", "last {} weekends"]
    DefaultTemplates = ["{} {}s ago", "{} {}s back", "{} {}s prior", "{} {}s earlier", "{} {}s before"]

    def __call__(self):
        probability = random.random()
        if probability < 1/4: return self.__generate_yesterday()
        if probability < 2/4: return self.__generate_time()
        if probability < 3/4: return self.__generate_weekend()
        return self.__generate_default()

    def __generate_yesterday(self):
        return "sub 1d\tYesterday" if random.random() >= 0.5 else "sub 2d\tThe day before yesterday"

    def __generate_time(self):
        if random.random() >= 0.5:
            return "sub 15m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["a quarter of an hour", "quarter of an hour", "a quarter hour"])))
        if random.random() >= 0.5:
            return "sub 45m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["three quarters of an hour", "three-quartes of an hour"])))
        return "sub 30m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["half an hour", "a half hour"])))

    def __generate_weekend(self):
        n = self.number()
        if n == 1:
            return "sub 1we\t{}".format(random.choice(self.PastWeekendTemplates).format("weekend"))
        return "sub {}we\t{}".format(n, random.choice(self.MultipleWeekendTemplates).format(n))

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        if random.random() <= 0.3:
            percentage = random.choice(self.Percentages)
            template += " " + self.textify(percentage)
            return "sub {}{}\t{}".format(day + percentage, self.code_of(scale), template.format(day, scale.replace("-", " "))).replace("Centurys", "Centuries")
        return "sub {}{}\t{}".format(day, self.code_of(scale), template.format(day, scale.replace("-", " "))).replace("Centurys", "Centuries")
