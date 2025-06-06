import random
from abc import ABC, abstractmethod


class DateEntryTemplate(ABC):
    Scales = ["second", "minute", "hour", "day", "business-day", "week", "Month", "Year", "Lustrum", "Decade", "Century", "Millennium"]
    Weekdays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    Months = ["January", "February", "March", "April", "May", "June",  "July", "August", "September", "October", "November", "December"]
    Seasons = ["Spring", "Summer", "Autumn", "Winter"]
    NumberSuffixes = {1: "st", 2: "nd", 3: "rd"}
    Percentages = [.25, .5, .45]
    Events = ["Christmas", "Halloween", "Thanksgiving", "Independence Day", "New Year's Eve", "Second World War", "my birthday", "Football tournament", "Valentine's Day", "Easter", "April Fool's Day", "Mother's Day", "Father's Day", "the day i got promoted", "Black Friday", "Cyber Monday", "Wedding Anniversary", "Graduation Day", "Prom Night", "Football World Cup", "Oktoberfest", "St. Patrick's Day"]

    @abstractmethod
    def __call__(self):
        pass

    def number(self, size: int = 100, zero: bool = False):
        if zero:
            return random.choices(range(size), weights=([20] * 10 + [1] * (size - 10)))[0]
        return random.choices(range(1, size), weights=([20] * 9 + [1] * (size - 10)))[0]

    def format_day(self, day: int):
        return day if random.random() < .5 else self.__to_ordinal(day)

    def code_of(self, identifier: str):
        if identifier == "business-day": return "bd"
        if identifier == "Milenium": return "ML"
        return identifier[0]

    def textify(self, percentage: float):
        if percentage == 0.25: return "and a quarter"
        if percentage == 0.5: return "and a half"
        return "and three quarters"

    def __to_ordinal(self, day: int):
        if self.__is_teen(day): return f"{day}th"
        return f"{day}{self.NumberSuffixes.get(day % 10, 'th')}"

    def __is_teen(self, day):
        return 10 <= day % 100 <= 20