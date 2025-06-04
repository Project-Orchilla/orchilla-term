import random

from orchilla.software.siani.orchilla.formalizer.src.data.generation.template import DateEntryTemplate


class NextDateEntryTemplate(DateEntryTemplate):
    ScaleTemplates = ["next {}", "the next {}", "following {}", "the following {}", "of next {}", "of the next {}", "of the following {}", "from next {}", "from the next {}", "from the following {}"]
    SingleWeekdayTemplates = ["next {}", "this {}", "the coming {}", "the upcoming {}", "the following {}", "coming {}", "the {} ahead", "the {} to come"]
    MultipleWeekdayTemplates = ["within {} {}s", "in {} {}s", "after {} {}s", "by {} {}s", "in the next {} {}s"]
    DayMonthTemplates = ["next {} {}", "next {} of {}", "the next {} {}", "the next {} of {}", "on next {} {}", "on next {} of {}", "on the next {} {}", "on the next {} of {}", "at next {} {}", "at next {} of {}", "at the next {} {}", "at the next {} of {}", "the coming {} of {}", "the coming {} {}", "at the coming {} of {}", "at the coming {} {}", "the upcoming {} of {}", "the upcoming {} {}", "the following {} of {}", "the following {} {}", "at the following {} {}", "at the following {} of {}", "on the following {} of {}", "on the following {} {}", "the {} {} ahead", "the {} of {} ahead", "the {} of {} to come", "the {} {} to come"]
    MonthDayTemplates = ["next {} {}", "the next {} {}", "on next {} {}", "on the next {} {}", "at next {} {}", "at the next {} {}", "the coming {} {}", "on the coming {} {}", "at the coming {} {}", "the upcoming {} {}", "on the upcoming {} {}", "at the upcoming {} {}", "the following {} {}", "at the following {} {}", "on the following {} {}", "the {} {} ahead", "the {} {} to come"]

    def __call__(self):
        probability = random.random()
        if probability < 1/3: return self.__generate_scale()
        if probability < 2/3: return self.__generate_weekday()
        return self.__generate_default()

    def __generate_scale(self):
        scale = random.choice(self.Scales)
        return "next 1{}\t{}".format(scale[0], random.choice(self.ScaleTemplates).format(scale.lower()))

    def __generate_weekday(self):
        n = self.number()
        weekday = random.choice(self.Weekdays)
        template = random.choice(self.MultipleWeekdayTemplates)
        if n == 1: template = random.choice(self.SingleWeekdayTemplates)
        return "next {}{}\t{}".format(n, weekday.lower(), template.format(n, weekday))

    def __generate_default(self):
        day = self.number(size=32, zero=False)
        month = random.choice(self.Months)
        entry = self.__generate_day_month(day, month) if random.random() < .5 else self.__generate_month_day(month, day)
        return "next dm{:02d}M{}\t{}".format(day, month.lower(), entry)

    def __generate_day_month(self, day: int, month: int):
        return random.choice(self.DayMonthTemplates).format(self.format_day(day), month)

    def __generate_month_day(self, month: int, day: int):
        return random.choice(self.MonthDayTemplates).format(month, self.format_day(day))
