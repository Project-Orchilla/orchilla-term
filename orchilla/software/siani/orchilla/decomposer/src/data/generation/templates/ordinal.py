import random

from software.siani.orchilla.decomposer.src.data.generation.template import DateEntryTemplate


class OrdinalDateEntryTemplate(DateEntryTemplate):
    Ordinals = [
        "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth",
        "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth", "sixteenth", "seventeenth", "eighteenth", "nineteenth", "twentieth",
        "twenty-first", "twenty-second", "twenty-third", "twenty-fourth", "twenty-fifth", "twenty-sixth", "twenty-seventh", "twenty-eighth", "twenty-ninth", "thirtieth",
        "thirty-first", "thirty-second", "thirty-third", "thirty-fourth", "thirty-fifth", "thirty-sixth", "thirty-seventh", "thirty-eighth", "thirty-ninth", "fortieth",
        "forty-first", "forty-second", "forty-third", "forty-fourth", "forty-fifth", "forty-sixth", "forty-seventh", "forty-eighth", "forty-ninth", "fiftieth",
        "fifty-first", "fifty-second", "fifty-third"
    ]
    HourTemplates = ["{} hour", "the {} hour"]
    DayTemplates = ["{} day", "the {} day"]
    WeekdayTemplates = ["{} {}", "the {} {}"]
    WeekTemplates = ["{} week", "the {} week"]
    WeekendTemplates = ["{} weekend", "the {} weekend"]
    MonthTemplates = ["{} month", "the {} month"]
    SemesterTemplates = ["{} Semester", "the {} Semester"]
    QuarterTemplates = ["{} Quarter", "the {} Quarter"]

    def __call__(self):
        probability = random.random()
        if probability < 1/8: return self.__generate_hour()
        if probability < 2/8: return self.__generate_day()
        if probability < 3/8: return self.__generate_weekday()
        if probability < 4/8: return self.__generate_weekend()
        if probability < 5/8: return self.__generate_week()
        if probability < 6/8: return self.__generate_month()
        if probability < 7/8: return self.__generate_quarter()
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
