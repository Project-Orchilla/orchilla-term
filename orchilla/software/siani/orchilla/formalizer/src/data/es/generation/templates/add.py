import random

from software.siani.orchilla.formalizer.src.data.es.generation.template import DateEntryTemplate


class AddDateEntryTemplate(DateEntryTemplate):
    TodayTemplates = ["hoy", "a partir de hoy", "ahora", "a partir de ahora", "en el presente"]
    ScaleTemplates = ["este {}", "de este {}", "desde este {}", "el presente {}", "del presente {}", "presente {}"]
    TimeTemplates = ["en {}", "después de {}"]
    NextWeekendTemplates = ["próximo {}", "el próximo {}", "siguiente {}", "el siguiente {}", "del siguiente {}", "del próximo {}"]
    MultipleWeekendTemplates = ["en {} fines de semana", "en {} fines de semana", "después de {} fines de semana", "tras {} fines de semana", "en los próximos {} fines de semana"]
    DefaultTemplates = ["en {} {}s", "tras {} {}s", "después {} {}s", "en los próximos {} {}s"]

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
        return "add 1d\tMañana" if random.random() >= 0.5 else "add 2d\tPasado mañana"

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return "add 0{}\t{}".format(self.code_of(scale), random.choice(self.ScaleTemplates).format(scale.replace("-", " ")))

    def __generate_time(self):
        if random.random() >= 0.5:
            return "add 15m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["un cuarto de hora", "cuarto de hora"])))
        if random.random() >= 0.5:
            return "add 45m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["tres cuartos de hora", "unos tres cuartos de hora"])))
        return "add 30m\t{}".format(random.choice(self.TimeTemplates).format(random.choice(["media hora", "una media hora"])))

    def __generate_weekend(self):
        n = self.number()
        if random.random() <= 0.2:
            return "add 0we\t{}".format(random.choice(self.ScaleTemplates).format("fin de semana"))
        if n == 1:
            return "add 1we\t{}".format(random.choice(self.NextWeekendTemplates).format("fin de semana"))
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
