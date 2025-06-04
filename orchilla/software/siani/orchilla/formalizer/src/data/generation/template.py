import random
from abc import ABC, abstractmethod


class DateEntryTemplate(ABC):
    Scales = ["second", "minute", "hour", "day", "Week", "Month", "Year", "Lustrum", "Decade", "Century", "Millennium"]
    Weekdays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    Months = ["January", "February", "March", "April", "May", "June",  "July", "August", "September", "October", "November", "December"]
    NumberSuffixes = {1: "st", 2: "nd", 3: "rd"}
    Festivities = ["Christmas", "Halloween", "Spring", "Summer", "Winter", "Thanksgiving", "Independence day", "New Year's Eve"]

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
        return identifier[0:3]

    def __to_ordinal(self, day: int):
        if self.__is_teen(day): return f"{day}th"
        return f"{day}{self.NumberSuffixes.get(day % 10, 'th')}"

    def __is_teen(self, day):
        return 10 <= day % 100 <= 20
