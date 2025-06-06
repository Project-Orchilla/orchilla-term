import random

from software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class NextDateEntryTemplate(DateEntryTemplate):
    ScaleTemplates = ["next {}", "the next {}", "following {}", "the following {}", "of next {}", "of the next {}", "of the following {}", "from next {}", "from the next {}", "from the following {}"]
    DecadeTemplates = ["the next {}s decade"]
    MultipleWeekendTemplates = ["within {} weekends", "in {} weekends", "after {} weekends", "by {} weekends", "in the next {} weekends"]
    SingleTemplates = ["next {}", "the coming {}", "the upcoming {}", "the following {}", "coming {}", "the {} ahead", "the {} to come"]
    MultipleTemplates = ["within {} {}s", "in {} {}s", "after {} {}s", "by {} {}s", "in the next {} {}s"]
    DayOfMonthTemplates = ["the next {} day", "the coming {} day"]
    DayMonthTemplates = ["next {} {}", "next {} of {}", "the next {} {}", "the next {} of {}", "on next {} {}", "on next {} of {}", "on the next {} {}", "on the next {} of {}", "at next {} {}", "at next {} of {}", "at the next {} {}", "at the next {} of {}", "the coming {} of {}", "the coming {} {}", "at the coming {} of {}", "at the coming {} {}", "the upcoming {} of {}", "the upcoming {} {}", "the following {} of {}", "the following {} {}", "at the following {} {}", "at the following {} of {}", "on the following {} of {}", "on the following {} {}", "the {} {} ahead", "the {} of {} ahead", "the {} of {} to come", "the {} {} to come"]
    MonthDayTemplates = ["next {} {}", "the next {} {}", "on next {} {}", "on the next {} {}", "at next {} {}", "at the next {} {}", "the coming {} {}", "on the coming {} {}", "at the coming {} {}", "the upcoming {} {}", "on the upcoming {} {}", "at the upcoming {} {}", "the following {} {}", "at the following {} {}", "on the following {} {}", "the {} {} ahead", "the {} {} to come"]

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
