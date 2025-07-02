import random

from software.siani.orchilla.decomposer.src.data.es.generation.template import DateEntryTemplate


class SetDateEntryTemplate(DateEntryTemplate):
    WeekdayTemplates = ["{}", "en {}", "el {}", "durante {}"]
    LastWeekdayTemplates = ["pasado {}", "el pasado {}", "en el pasado {}"]
    PenultimateWeekdayTemplates = ["penúltimo {}", "el penúltimo {}", "en el penúltimo {}"]
    DayTemplates = ["en el {}", "{}", "en el {} día", "por el {}", "por el {} día", "para el {}", "para el {} día", "este {} día", "este {}", "el {}", "el {} día", "el día {}"]
    MonthTemplates = ["de {}", "{}", "por {}", "este {}", "en {}"]
    YearTemplates = ["en el año {}", "año {}", "en {}"]
    SeasonTemplates = ["este {}", "en {}", "en el {}"]
    DayMonthTemplates = ["{} {}", "a las {} {}", "en el {} {}", "{}/{}", "{}-{}"]
    MonthDayTemplates = ["{} {}", "en {} {}"]
    MonthYearTemplates = ["{} {}", "el {} {}", "en {} {}", ]
    DayMonthYearTemplates = ["{} {} {}", "{} de {} {}", "en {} {} {}", "en {} de {} {}", "en el {} {} {}", "en el {} de {} {}", "{} {}, {}", "{} de {}, {}", "en el {} {}, {}", "en {} {}, {}", "en el {} de {}, {}"]
    MonthDayYearTemplates = ["{} {}, {}", "{} {} {}", "en {} {}, {}", "en {} {} {}", "el {} {}, {}", "el {} {} {}", "en {} {}, {}", "en {} {} {}"]
    YearMonthDayTemplates = ["{}-{}-{}", "{}/{}/{}"]

    def __call__(self):
        probability = random.random()
        if probability < 1/9: return self.__generate_weekday()
        if probability < 2/9: return self.__generate_day()
        if probability < 3/9: return self.__generate_month()
        if probability < 4/9: return self.__generate_year()
        if probability < 5/9: return self.__generate_day_month()
        if probability < 6/9: return self.__generate_month_year()
        if probability < 7/9: return self.__generate_event()
        if probability < 8/9: return self.__generate_season()
        return self.__generate_default()

    def __generate_weekday(self):
        weekday = random.choice(self.Weekdays)
        if random.random() <= 0.5:
            if random.random() <= 0.5:
                return random.choice(self.LastWeekdayTemplates).format(weekday)
            return random.choice(self.PenultimateWeekdayTemplates).format(weekday)
        return random.choice(self.WeekdayTemplates).format(weekday)

    def __generate_day(self):
        day = self.number(32)
        return random.choice(self.DayTemplates).format(day)

    def __generate_month(self):
        month = random.choice(self.Months)
        return random.choice(self.MonthTemplates).format(month)

    def __generate_year(self):
        year = self.__randomize_year()
        return random.choice(self.YearTemplates).format(year)

    def __generate_day_month(self):
        day = self.number(32)
        month = random.choice(self.Months)
        return self.__generate_day_and_month(day, month) if random.random() < .5 else self.__generate_month_and_day(month, day)

    def __generate_month_year(self):
        month = random.choice(self.Months)
        year = self.__randomize_year()
        return random.choice(self.MonthYearTemplates).format(month, year)

    def __generate_event(self):
        return random.choice(self.Events)

    def __generate_season(self):
        season = random.choice(self.Seasons)
        return random.choice(self.SeasonTemplates).format(season)

    def __generate_default(self):
        day = self.number(32)
        month = random.choice(self.Months)
        year = self.__randomize_year()
        return random.choice([self.__generate_day_month_year(day, month, year), self.__generate_month_day_year(day, month, year), self.__generate_year_month_day(day, month, year)])

    def __randomize_year(self):
        return random.choices(range(1500, 3000), weights=([1] * 10 + [20] * (1500 - 10)))[0]

    def __generate_day_and_month(self, day: int, month: int):
        return random.choice(self.DayMonthTemplates).format(self.format_day(day), month)

    def __generate_month_and_day(self, month: int, day: int):
        return random.choice(self.MonthDayTemplates).format(month, self.format_day(day))

    def __generate_day_month_year(self, day: int, month: int, year: int):
        return random.choice(self.DayMonthYearTemplates).format(self.format_day(day), month, year)

    def __generate_month_day_year(self, day: int, month: int, year: int):
        return random.choice(self.MonthDayYearTemplates).format(month, self.format_day(day), year)

    def __generate_year_month_day(self, day: int, month: int, year: int):
        month = self.Months.index(month) + 1 if random.random() else month
        return random.choice(self.YearMonthDayTemplates).format(year, month, self.format_day(day))


class SetTimeEntryTemplate(DateEntryTemplate):
    HourTemplates = ["a las {}"]
    OneQuarterTemplates = ["a las {} y cuarto", "{} y cuarto", "15 minutos pasados las {}", "a las {} y 15"]
    ThreeQuarterTemplates = ["a las {} y tres cuartos", "a las {} 45", "a las {}:45"]
    ThreeQuarterPlusTemplates = ["{} menos cuarto", "cuarto para las {}"]
    HalvesTemplates = ["a las {} y media", "{} y media", "{} y 30", "a las {}:30"]
    AmTemplates = ["a las {:02d}:{:02d} AM", "a las {:02d}:{:02d}AM", "a las {:02d}:{:02d} de la mañana",]
    PmTemplates = ["a las {:02d}:{:02d} PM", "a las {:02d}:{:02d}PM", "a las {:02d}:{:02d} de la tarde"]
    MiddayTemplates = ["mediodia"]
    MidnightTemplates = ["a medianoche", "medianoche"]
    TimeTemplates = ["a las {:02d}:{:02d}"]

    def __call__(self):
        probability = random.random()
        if probability < 1/6: return self.__generate_hour()
        if probability < 2/6: return self.__generate_quarters()
        if probability < 3/6: return self.__generate_halves()
        if probability < 4/6: return self.__generate_labeled()
        if probability < 5/6: return self.__generate_situational()
        return self.__generate_default()

    def __generate_hour(self):
        hour = self.number(24)
        return random.choice(self.HourTemplates).format(hour)

    def __generate_quarters(self):
        hour = self.number(24)
        if random.random() < 0.5:
            return random.choice(self.OneQuarterTemplates).format(hour)
        return random.choice(self.ThreeQuarterTemplates).format(hour) if random.random() < 0.5 else random.choice(self.ThreeQuarterPlusTemplates).format(hour + 1)

    def __generate_halves(self):
        hour = self.number(24)
        return random.choice(self.HalvesTemplates).format(hour)

    def __generate_labeled(self):
        hour = self.number(12)
        minute = self.number(60)
        if random.random() < 0.5:
            return random.choice(self.AmTemplates).format(hour, minute)
        return random.choice(self.PmTemplates).format(hour, minute)

    def __generate_situational(self):
        return random.choice(self.MiddayTemplates) if random.random() <= 0.5 else random.choice(self.MidnightTemplates)

    def __generate_default(self):
        hour = self.number(24)
        minute = self.number(60)
        return random.choice(self.TimeTemplates).format(hour, minute)
