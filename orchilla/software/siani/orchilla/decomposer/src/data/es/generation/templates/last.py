import random

from software.siani.orchilla.decomposer.src.data.es.generation.template import DateEntryTemplate


class LastDateEntryTemplate(DateEntryTemplate):
    ScaleTemplates = ["último {}", "el último {}", "{} pasado", "el {} pasado", "del último {}", "del pasado {}", "de la última {}", "de los últimos {}", "de los pasados {}", "del pasado {}", "de la última {}", "el {} anterior", "del anterior", "{} anterior"]
    DecadeTemplates = ["la década de los {}s pasados", "la década de los últimos {}s", "la década anterior de los {}s"]
    SingleTemplates = ["el último {}", "el {} pasado", "este {} pasado", "el {} anterior", "{} anterior", "{} previo"]
    MultipleTemplates = ["hace {} {}s", "{} {}s atrás", "{} {}s antes", "{} {}s previos", "{} {}s anteriores"]
    DayOfMonthTemplates = ["el pasado día {}", "el día anterior {}", "el día previo {}"]
    DayMonthTemplates = ["último {} {}", "último {} de {}", "{} {} pasado", "{} de {} pasado", "este último {} {}", "este último {} de {}", "el último {} {}", "el último {} de {}", "el {} {} pasado", "el {} de {} pasado", "el {} {} anterior", "el {} de {} anterior", "el pasado {} {}", "el pasado {} de {}", "en el último {} {}", "en el último {} de {}", "en el pasado {} {}", "en el pasado {} de {}", "en el {} {} pasado", "en el {} de {} pasado", "en el {} {} anterior", "en el {} de {} anterior", "el {} de {} que pasó", "el {} {} que pasó", "{} de {} que pasó", "{} {} que pasó"]
    MonthDayTemplates = ["último {} {}", "{} {} pasado", "este {} {} pasado", "el último {} {}", "el {} {} pasado", "en el último {} {}", "en el último {} {}", "en el {} {} pasado", "en el {} {} anterior", "el {} {} anterior", "el anterior {} {}", "en el {} {} anterior", "el {} {} que pasó", "{} {} que pasó"]

    def __call__(self):
        probability = random.random()
        if probability < 1/5: return self.__generate_scale()
        if probability < 2/5: return self.__generate_weekday()
        if probability < 3/5: return self.__generate_decade()
        if probability < 4/5: return self.__generate_day()
        return self.__generate_default()

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return random.choice(self.ScaleTemplates).format(scale.replace("-", " "))

    def __generate_weekday(self):
        n = self.number()
        weekday = random.choice(self.Weekdays)
        if n == 1:
            template = random.choice(self.SingleTemplates)
            return template.format(weekday)
        template = random.choice(self.MultipleTemplates)
        return template.format(n, weekday)

    def __generate_season(self):
        n = self.number()
        season = random.choice(self.Seasons)
        if n == 1:
            return random.choice(self.SingleTemplates).format(season)
        return random.choice(self.MultipleTemplates).format(n, season)

    def __generate_decade(self):
        decade = random.choice([10, 20, 30, 40, 50, 60, 70, 80, 90])
        return random.choice(self.DecadeTemplates).format(decade)

    def __generate_day(self):
        n = self.number()
        return random.choice(self.DayOfMonthTemplates).format(self.format_day(day=n))

    def __generate_default(self):
        day = self.number(size=32, zero=False)
        month = random.choice(self.Months)
        return self.__generate_day_month(day, month) if random.random() < .5 else self.__generate_month_day(month, day)

    def __generate_day_month(self, day: int, month: int):
        return random.choice(self.DayMonthTemplates).format(self.format_day(day), month)

    def __generate_month_day(self, month: int, day: int):
        return random.choice(self.MonthDayTemplates).format(month, self.format_day(day))
