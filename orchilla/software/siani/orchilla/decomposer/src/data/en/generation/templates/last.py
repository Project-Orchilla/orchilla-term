import random

from software.siani.orchilla.decomposer.src.data.en.generation.template import DateEntryTemplate


class LastDateEntryTemplate(DateEntryTemplate):
    ScaleTemplates = ["last {}", "the last {}", "past {}", "the past {}", "of last {}", "of the last {}", "of the past {}", "from the last {}", "from the past {}", "from last {}", "from past {}", "the previous {}", "from the previous", "previous {}"]
    DecadeTemplates = ["the past {}s decade", "the last {}s decade", "prior {}s decade"]
    SingleTemplates = ["last {}", "past {}", "this past {}", "the {} before", "previous {}", "{} prior"]
    MultipleTemplates = ["{} {}s ago", "{} {}s back", "{} {}s prior", "{} {}s earlier", "{} {}s before"]
    DayOfMonthTemplates = ["the past {} day", "the previous {} day", "the prior {} day"]
    DayMonthTemplates = ["last {} {}", "last {} of {}", "past {} {}", "past {} of {}", "this past {} {}", "this past {} of {}", "the last {} {}", "the last {} of {}", "the past {} {}", "the past {} of {}", "on last {} {}", "on last {} of {}", "on the last {} {}", "on the last {} of {}", "on the past {} {}", "on the past {} of {}", "at last {} {}", "at last {} of {}", "at the last {} {}", "at the last {} of {}", "previous {} of {}", "previous {} {}", "the previous {} of {}", "the previous {} {}", "on the previous {} of {}", "on the previous {} {}", "the {} of {} that passed", "the {} {} that passed", "{} of {} that passed", "{} {} that passed"]
    MonthDayTemplates = ["last {} {}", "past {} {}", "this past {} {}", "the last {} {}", "the past {} {}", "on last {} {}", "on the last {} {}", "on the past {} {}", "at last {} {}", "at the last {} {}", "previous {} {}", "the previous {} {}", "on the previous {} {}", "the {} {} that passed", "{} {} that passed"]

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
