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
    WeekdayTemplates = ["{} {}", "the {} {}"]
    WeekTemplates = ["{} week", "the {} week"]
    WeekendTemplates = ["{} weekend", "the {} weekend"]

    def __call__(self):
        probability = random.random()
        if probability < 1/3: return self.__generate_weekday()
        if probability < 2/3: return self.__generate_week()
        return self.__generate_weekend()

    def __generate_weekday(self):
        ordinal = random.choice(self.Ordinals)
        weekday = random.choice(self.Weekdays)
        return random.choice(self.WeekdayTemplates).format(ordinal, weekday)

    def __generate_week(self):
        ordinal = random.choice(self.Ordinals)
        return random.choice(self.WeekTemplates).format(ordinal)

    def __generate_weekend(self):
        ordinal = random.choice(self.Ordinals)
        return random.choice(self.WeekendTemplates).format(ordinal)
