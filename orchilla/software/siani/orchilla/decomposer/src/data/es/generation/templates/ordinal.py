import random

from software.siani.orchilla.decomposer.src.data.es.generation.template import DateEntryTemplate


class OrdinalDateEntryTemplate(DateEntryTemplate):
    Ordinals = ["primer", "segundo", "tercer", "cuarto"]
    HourTemplates = ["{} horas", "las {} horas", "a las {} horas"]
    DayTemplates = ["{} día", "el {} día", "en el {} día"]
    WeekdayTemplates = ["{} {}", "el {} {}", "en el {} {}"]
    WeekTemplates = ["{} semana", "la {} semana", "en la {} semana"]
    WeekendTemplates = ["{} fin de semana", "el {} fin de semana", "en el {} fin de semana"]
    MonthTemplates = ["{} mes", "el {} mes", "en el {} mes"]
    SemesterTemplates = ["{} semestre", "en {} semestre", "en el {} semestre"]
    QuarterTemplates = ["{} cuarto", "el {} cuarto", "en el {} cuarto"]

    def __call__(self):
        probability = random.random()
        if probability < 1/8: return self.__generate_hour()
        if probability < 2/8: return self.__generate_day()
        if probability < 5/10: return self.__generate_weekday()
        if probability < 6/8: return self.__generate_weekend()
        if probability < 7/8: return self.__generate_week()
        if probability < 8/8: return self.__generate_month()
        if probability < 9/8: return self.__generate_quarter()
        return self.__generate_semester()

    def __generate_hour(self):
        ordinal = random.choice(self.Ordinals[0:24])
        return random.choice(self.HourTemplates).format(ordinal)

    def __generate_day(self):
        ordinal = random.choice(self.Ordinals)
        return random.choice(self.DayTemplates).format(ordinal)

    def __generate_weekday(self):
        ordinal = random.choice(self.Ordinals)
        weekday = random.choice(self.Weekdays)
        return random.choice(self.WeekdayTemplates).format(ordinal, weekday)

    def __generate_weekend(self):
        ordinal = random.choice(self.Ordinals)
        return random.choice(self.WeekendTemplates).format(ordinal)

    def __generate_week(self):
        ordinal = random.choice(self.Ordinals)
        return random.choice(self.WeekTemplates).format(ordinal)

    def __generate_month(self):
        ordinal = random.choice(self.Ordinals[0:12])
        return random.choice(self.MonthTemplates).format(ordinal)

    def __generate_quarter(self):
        ordinal = random.choice(self.Ordinals[0:3])
        return random.choice(self.QuarterTemplates).format(ordinal)

    def __generate_semester(self):
        ordinal = random.choice(self.Ordinals[0:2])
        return random.choice(self.SemesterTemplates).format(ordinal)
