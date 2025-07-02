import random

from software.siani.orchilla.formalizer.src.data.es.generation.template import DateEntryTemplate


class NextDateEntryTemplate(DateEntryTemplate):
    ScaleTemplates = ["próximo {}", "el próximo {}", "{} siguiente", "el {} siguiente", "del próximo {}", "del siguiente {}", "del {} siguiente", "desde el próximo {}", "desde el siguiente {}", "desde el {} siguiente"]
    DecadeTemplates = ["la década de los próximos {}s"]
    MultipleWeekendTemplates = [ "dentro de {} fines de semana", "en {} fines de semana", "después de {} fines de semana", "para {} fines de semana", "en los próximos {} fines de semana"]
    SingleTemplates = ["próximo {}", "el {} que viene", "el próximo {}", "el siguiente {}", "{} que viene", "el {} por venir", "el {} que está por venir"]
    MultipleTemplates = ["dentro de {} {}s", "en {} {}s", "después de {} {}s", "para {} {}s", "en los próximos {} {}s"]
    DayOfMonthTemplates = ["el próximo día {}", "el día {} que viene"]
    DayMonthTemplates = ["próximo {} {}", "próximo {} de {}", "el próximo {} {}", "el próximo {} de {}", "el {} {} siguiente", "el {} de {} siguiente", "en próximo {} {}", "en próximo {} de {}", "en el próximo {} {}", "en el próximo {} de {}", "en el siguiente {} {}", "en el siguiente {} de {}", "en el {} {} que viene", "en el {} de {} que viene", "en el {} {} por venir", "en el {} de {} por venir", "el {} de {} que viene", "el {} {} que viene", "el {} de {} siguiente", "el {} {} siguiente", "en el {} de {} siguiente", "en el {} {} siguiente", "el {} {} por venir", "el {} de {} por venir"]
    MonthDayTemplates = ["próximo {} {}", "el próximo {} {}", "en próximo {} {}", "en el próximo {} {}", "en {} {} que viene", "en el {} {} que viene", "el {} {} que viene", "en el {} {} por venir", "el {} {} por venir", "el {} {} siguiente", "en el {} {} siguiente", "el {} {} que está por venir", "el {} {} que vendrá"]

    def __call__(self):
        probability = random.random()
        if probability < 1/6: return self.__generate_scale()
        if probability < 2/6: return self.__generate_weekday()
        if probability < 3/6: return self.__generate_decade()
        if probability < 4/6: return self.__generate_day()
        if probability < 5/6: return self.__generate_season()
        return self.__generate_default()

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return "add 1{}>>{}\t{}".format(self.code_of(scale), scale, random.choice(self.ScaleTemplates).format(scale.replace("-", " ")))

    def __generate_weekday(self):
        n = self.number()
        weekday = random.choice(self.Weekdays)
        if n == 1:
            template = random.choice(self.SingleTemplates)
            return "next n1wd{}\t{}".format(weekday, template.format(weekday))
        template = random.choice(self.MultipleTemplates)
        return "next n{}wd{}\t{}".format(n, weekday, template.format(n, weekday))

    def __generate_season(self):
        n = self.number()
        season = random.choice(self.Seasons)
        if n == 1:
            template = random.choice(self.SingleTemplates)
            return "next n1S{}\t{}".format(season, template.format(season))
        template = random.choice(self.MultipleTemplates)
        return "next n{}S{}\t{}".format(n, season, template.format(n, season))

    def __generate_decade(self):
        decade = random.choice([10, 20, 30, 40, 50, 60, 70, 80, 90])
        return "next D{}\t{}".format(decade, random.choice(self.DecadeTemplates).format(decade))

    def __generate_day(self):
        n = self.number(size=32)
        return "next d{}\t{}".format(n, random.choice(self.DayOfMonthTemplates).format(self.format_day(day=n)))

    def __generate_default(self):
        day = self.number(size=32, zero=False)
        month = random.choice(self.Months)
        entry = self.__generate_day_month(day, month) if random.random() < .5 else self.__generate_month_day(month, day)
        return "next M{}>>set d{:02d}\t{}".format(month, day, entry)

    def __generate_day_month(self, day: int, month: int):
        return random.choice(self.DayMonthTemplates).format(self.format_day(day), month)

    def __generate_month_day(self, month: int, day: int):
        return random.choice(self.MonthDayTemplates).format(month, self.format_day(day))
