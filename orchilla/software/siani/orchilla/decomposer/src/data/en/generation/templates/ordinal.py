import random

from software.siani.orchilla.decomposer.src.data.en.generation.template import DateEntryTemplate


class OrdinalDateEntryTemplate(DateEntryTemplate):
    Ordinals = [
        "first", "second", "third", "fourth"
    ]
    HourTemplates = ["{} hour", "the {} hour", "on the {} hour"]
    DayTemplates = ["{} day", "the {} day", "on the {} day"]
    WeekdayTemplates = ["{} {}", "the {} {}", "on the {} {}"]
    WeekTemplates = ["{} week", "the {} week", "on the {} week"]
    WeekendTemplates = ["{} weekend", "the {} weekend", "on the {} weekend"]
    MonthTemplates = ["{} month", "the {} month", "on the {} month"]
    SemesterTemplates = ["{} Semester", "the {} Semester", "on the {} Semester"]
    QuarterTemplates = ["{} Quarter", "the {} Quarter", "on the {} Quarter"]

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
