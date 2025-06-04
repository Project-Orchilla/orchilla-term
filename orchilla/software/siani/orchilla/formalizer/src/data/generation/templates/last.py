import random

from orchilla.software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class LastDateEntryTemplate(DateEntryTemplate):
    ScaleTemplates = ["last {}", "the last {}", "past {}", "the past {}", "of last {}", "of the last {}", "of the past {}", "from the last {}", "from the past {}", "from last {}", "from past {}", "the previous {}", "from the previous", "previous {}"]
    SingleWeekdayTemplates = ["last {}", "past {}", "this past {}", "the {} before", "previous {}", "{} prior"]
    MultipleWeekdayTemplates = ["{} {}s ago", "{} {}s back", "{} {}s prior", "{} {}s earlier", "{} {}s before"]
    DayMonthTemplates = ["last {} {}", "last {} of {}", "past {} {}", "past {} of {}", "this past {} {}", "this past {} of {}", "the last {} {}", "the last {} of {}", "the past {} {}", "the past {} of {}", "on last {} {}", "on last {} of {}", "on the last {} {}", "on the last {} of {}", "on the past {} {}", "on the past {} of {}", "at last {} {}", "at last {} of {}", "at the last {} {}", "at the last {} of {}", "previous {} of {}", "previous {} {}", "the previous {} of {}", "the previous {} {}", "on the previous {} of {}", "on the previous {} {}", "the {} of {} that passed", "the {} {} that passed", "{} of {} that passed", "{} {} that passed"]
    MonthDayTemplates = ["last {} {}", "past {} {}", "this past {} {}", "the last {} {}", "the past {} {}", "on last {} {}", "on the last {} {}", "on the past {} {}", "at last {} {}", "at the last {} {}", "previous {} {}", "the previous {} {}", "on the previous {} {}", "the {} {} that passed", "{} {} that passed"]

    def __call__(self):
        probability = random.random()
        if probability < 1/3: return self.__generate_scale()
        if probability < 2/3: return self.__generate_weekday()
        return self.__generate_default()

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return "last 1{}\t{}".format(scale[0], random.choice(self.ScaleTemplates).format(scale))

    def __generate_weekday(self):
        n = self.number()
        weekday = random.choice(self.Weekdays)
        template = random.choice(self.MultipleWeekdayTemplates)
        if n == 1: template = random.choice(self.SingleWeekdayTemplates)
        return "last {}{}\t{}".format(n, weekday.lower(), template.format(n, weekday))

    def __generate_default(self):
        day = self.number(size=32, zero=False)
        month = random.choice(self.Months)
        entry = self.__generate_day_month(day, month) if random.random() < .5 else self.__generate_month_day(month, day)
        return "last dm{:02d}M{}\t{}".format(day, month.lower(), entry)

    def __generate_day_month(self, day: int, month: int):
        return random.choice(self.DayMonthTemplates).format(self.format_day(day), month)

    def __generate_month_day(self, month: int, day: int):
        return random.choice(self.MonthDayTemplates).format(month, self.format_day(day))
