import random

from software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class SetDateEntryTemplate(DateEntryTemplate):
    WeekdayTemplates = ["{}", "on {}", "at {}", "during {}"]
    LastWeekdayTemplates = ["last {}", "the last {}", "on the last {}", "in the last {}"]
    PenultimateWeekdayTemplates = ["penultimate {}", "the penultimate {}", "on the penultimate {}", "in the penultimate {}"]
    DayTemplates = ["on the {}", "{}", "on the {} day", "by the {}", "by the {} day", "for the {}", "for the {} day", "this {} day", "this {}", "the {}", "the {} day", "the day {}"]
    MonthTemplates = ["of {}", "{}", "by {}", "this {}", "on {}"]
    YearTemplates = ["in the year {}", "year {}", "in {}"]
    SeasonTemplates = ["this {}", "in {}", "in the {}"]
    DayMonthTemplates = ["{} {}", "at {} {}", "at {} of {}", "in the {} of {}", "in the {} {}", "on {} {}", "on the {} {}", "on {} of {}", "{} {}", "{} of {}", "{}/{}", "{}-{}"]
    MonthDayTemplates = ["{} {}", "at {} {}", "in {} {}", "on {} {}", "on the {} {}"]
    MonthYearTemplates = ["{} {}", "at {} {}", "in {} {}", "on {} {}"]
    DayMonthYearTemplates = ["{} {} {}", "{} of {} {}", "on {} {} {}", "on {} of {} {}", "on the {} {} {}", "on the {} of {} {}", "{} {}, {}", "{} of {}, {}", "on the {} {}, {}", "on {} {}, {}", "on the {} of {}, {}"]
    MonthDayYearTemplates = ["{} {}, {}", "{} {} {}", "on {} {}, {}", "on {} {} {}", "at {} {}, {}", "at {} {} {}", "in {} {}, {}", "in {} {} {}"]
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
                return "set n-1wd{}\t{}".format(weekday, random.choice(self.LastWeekdayTemplates).format(weekday))
            return "set n-2wd{}\t{}".format(weekday, random.choice(self.PenultimateWeekdayTemplates).format(weekday))
        return "set wd{}\t{}".format(weekday, random.choice(self.WeekdayTemplates).format(weekday))

    def __generate_day(self):
        day = self.number(365)
        return "set d{:03d}\t{}".format(day, random.choice(self.DayTemplates).format(day))

    def __generate_month(self):
        month = random.choice(self.Months)
        return "set M{}\t{}".format(month, random.choice(self.MonthTemplates).format(month))

    def __generate_year(self):
        year = self.__randomize_year()
        return "set Y{:04d}\t{}".format(year, random.choice(self.YearTemplates).format(year))

    def __generate_day_month(self):
        day = self.number(32)
        month = random.choice(self.Months)
        entry = self.__generate_day_and_month(day, month) if random.random() < .5 else self.__generate_month_and_day(month, day)
        return "set M{}d{:02d}\t{}".format(month, day, entry)

    def __generate_month_year(self):
        month = random.choice(self.Months)
        year = self.__randomize_year()
        return "set Y{:04d}M{}\t{}".format(year, month, random.choice(self.MonthYearTemplates).format(month, year))

    def __generate_event(self):
        event = random.choice(self.Events)
        return "set \"{}\"\t{}".format(event, event)

    def __generate_season(self):
        season = random.choice(self.Seasons)
        return "set S{}\t{}".format(season, random.choice(self.SeasonTemplates).format(season))

    def __generate_default(self):
        day = self.number(32)
        month = random.choice(self.Months)
        year = self.__randomize_year()
        entry = random.choice([self.__generate_day_month_year(day, month, year), self.__generate_month_day_year(day, month, year), self.__generate_year_month_day(day, month, year)])
        return "set Y{:04d}M{}d{:02d}\t{}".format(year, month, day, entry)

    def __randomize_year(self):
        return random.choices(range(3000), weights=([1] * 10 + [20] * (3000 - 10)))[0]

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
    HourTemplates = ["at {}"]
    OneQuarterTemplates = ["at quarter past {}", "quarter past {}", "at 15 minutes past {}", "at {} 15"]
    ThreeQuarterTemplates = ["at 45 minutes past {}", "at {} 45", "at {}:45"]
    ThreeQuarterPlusTemplates = ["15 to {}", "at quarter to {}", "quarter to {}", "at {}:15"]
    HalvesTemplates = ["at half past {}", "at 30 minutes past {}", "half past {}", "at {}:30"]
    AmTemplates = ["at {:02d}:{:02d} AM", "at {:02d}:{:02d}AM", "at {:02d}:{:02d} in the morning",]
    PmTemplates = ["at {:02d}:{:02d} PM", "at {:02d}:{:02d}PM", "at {:02d}:{:02d} in the afternoon", "at {:02d}:{:02d} in the evening"]
    MiddayTemplates = ["at noon", "noon", "at midday", "midday"]
    MidnightTemplates = ["at midnight", "midnight"]
    TimeTemplates = ["at {:02d}:{:02d}"]

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
        return "set h{:02d}m00\t{}".format(hour, random.choice(self.HourTemplates).format(hour))

    def __generate_quarters(self):
        hour = self.number(24)
        if random.random() < 0.5:
            return "set h{:02d}m15\t{}".format(hour, random.choice(self.OneQuarterTemplates).format(hour))
        template = random.choice(self.ThreeQuarterTemplates).format(hour) if random.random() < 0.5 else random.choice(self.ThreeQuarterPlusTemplates).format(hour + 1)
        return "set h{:02d}m45\t{}".format(hour, template)

    def __generate_halves(self):
        hour = self.number(24)
        return "set h{:02d}m30\t{}".format(hour, random.choice(self.HalvesTemplates).format(hour))

    def __generate_labeled(self):
        hour = self.number(12)
        minute = self.number(60)
        if random.random() < 0.5:
            return "set h{:02d}m{:02d}\t{}".format(hour, minute, random.choice(self.AmTemplates).format(hour, minute))
        return "set h{:02d}m{:02d}\t{}".format(hour + 12, minute, random.choice(self.PmTemplates).format(hour, minute))

    def __generate_situational(self):
        return "set h12m00\t{}".format(random.choice(self.MiddayTemplates)) if random.random() <= 0.5 else "set h00m00\t{}".format(random.choice(self.MidnightTemplates))

    def __generate_default(self):
        hour = self.number(24)
        minute = self.number(60)
        return "set h{:02d}m{:02d}\t{}".format(hour, minute, random.choice(self.TimeTemplates).format(hour, minute))
