import random

from software.siani.orchilla.formalizer.src.data.es.generation.template import DateEntryTemplate


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
        return "sub 1{}>>{}\t{}".format(self.code_of(scale), scale, random.choice(self.ScaleTemplates).format(scale.replace("-", " ")))

    def __generate_weekday(self):
        n = self.number()
        weekday = random.choice(self.Weekdays)
        if n == 1:
            template = random.choice(self.SingleTemplates)
            return "last n1wd{}\t{}".format(weekday, template.format(weekday))
        template = random.choice(self.MultipleTemplates)
        return "last n{}wd{}\t{}".format(n, weekday, template.format(n, weekday))

    def __generate_season(self):
        n = self.number()
        season = random.choice(self.Seasons)
        if n == 1:
            template = random.choice(self.SingleTemplates)
            return "last n1S{}\t{}".format(season, template.format(season))
        template = random.choice(self.MultipleTemplates)
        return "last n{}S{}\t{}".format(n, season, template.format(n, season))

    def __generate_decade(self):
        decade = random.choice([10, 20, 30, 40, 50, 60, 70, 80, 90])
        return "last D{}\t{}".format(decade, random.choice(self.DecadeTemplates).format(decade))

    def __generate_day(self):
        n = self.number()
        return "last d{}\t{}".format(n, random.choice(self.DayOfMonthTemplates).format(self.format_day(day=n)))

    def __generate_default(self):
        day = self.number(size=32, zero=False)
        month = random.choice(self.Months)
        entry = self.__generate_day_month(day, month) if random.random() < .5 else self.__generate_month_day(month, day)
        return "last M{}>>set d{:02d}\t{}".format(month, day, entry)

    def __generate_day_month(self, day: int, month: int):
        return random.choice(self.DayMonthTemplates).format(self.format_day(day), month)

    def __generate_month_day(self, month: int, day: int):
        return random.choice(self.MonthDayTemplates).format(month, self.format_day(day))
