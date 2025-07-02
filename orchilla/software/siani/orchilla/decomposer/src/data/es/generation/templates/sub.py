import random

from software.siani.orchilla.decomposer.src.data.es.generation.template import DateEntryTemplate


class SubDateEntryTemplate(DateEntryTemplate):
    TimeTemplates = ["hace {}", "{} atrás", "{} antes", "{} previamente", "{} anteriormente"]
    PastWeekendTemplates = ["el pasado {}", "el {} pasado", "este pasado {}", "el {} anterior", "el {} previo", "{} antes"]
    MultipleWeekendTemplates = ["hace {} fines de semana", "{} fines de semana atrás", "{} fines de semana antes", "los {} fines de semana anteriores", "los últimos {} fines de semana"]
    DefaultTemplates = ["hace {} {}s", "{} {}s atrás", "{} {}s antes", "{} {}s previamente", "{} {}s anteriormente"]

    def __call__(self):
        probability = random.random()
        if probability < 1/4: return self.__generate_yesterday()
        if probability < 2/4: return self.__generate_time()
        if probability < 3/4: return self.__generate_weekend()
        return self.__generate_default()

    def __generate_yesterday(self):
        return "Ayer" if random.random() >= 0.5 else "Anteayer"

    def __generate_time(self):
        if random.random() >= 0.5:
            return random.choice(self.TimeTemplates).format(random.choice(["un cuarto de hora", "cuarto de hora"]))
        if random.random() >= 0.5:
            return random.choice(self.TimeTemplates).format(random.choice(["tres cuartos de hora"]))
        return random.choice(self.TimeTemplates).format(random.choice(["media hora"]))

    def __generate_weekend(self):
        n = self.number()
        if n == 1:
            return random.choice(self.PastWeekendTemplates).format("fin de semana")
        return random.choice(self.MultipleWeekendTemplates).format(n)

    def __generate_default(self):
        day = self.number(zero=True)
        scale = random.choice(self.Scales)
        template = random.choice(self.DefaultTemplates)
        if day == 1: template.removesuffix("s")
        if random.random() <= 0.3:
            percentage = random.choice(self.Percentages)
            template += " " + self.textify(percentage)
            return template.format(day, scale.replace("-", " ")).replace("Centurys", "Centuries")
        return template.format(day, scale.replace("-", " ")).replace("Centurys", "Centuries")
