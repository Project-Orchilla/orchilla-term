import random
from abc import ABC, abstractmethod


class DateEntryTemplate(ABC):
    Scales = ["segundo", "minuto", "hora", "día", "dia laboral", "semana", "mes", "cuarto", "semestre", "año", "década", "lustro", "siglo", "milenio"]
    Weekdays = ["lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"]
    Months = ["enero", "febrero", "marzo", "abril", "mayo", "junio",  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"]
    Seasons = ["primavera", "verano", "otoño", "invierno"]
    NumberSuffixes = {1: "ero", 2: "ndo", 3: "ero"}
    Percentages = [.25, .5, .45]
    Events = ["los años 20", "los años 30", "los años 40", "los años 50", "los años 60", "los años 70", "los años 80", "los años 90", "Navidad", "Halloween", "Acción de Gracias", "Día de la Independencia", "Nochevieja", "Segunda Guerra Mundial", "mi cumpleaños", "torneo de fútbol", "Día de San Valentín", "Pascua", "Día de los Inocentes", "Día de la Madre", "Día del Padre", "el día que me ascendieron", "Viernes Negro", "Ciberlunes", "Aniversario de bodas", "Día de Graduación", "Noche de Promoción", "Copa Mundial de Fútbol", "Oktoberfest", "Día de San Patricio"]

    @abstractmethod
    def __call__(self):
        pass

    def number(self, size: int = 100, zero: bool = False):
        if zero:
            return random.choices(range(size), weights=([20] * 10 + [1] * (size - 10)))[0]
        return random.choices(range(1, size), weights=([20] * 9 + [1] * (size - 10)))[0]

    def format_day(self, day: int):
        return day if random.random() < .5 else self.__to_ordinal(day)

    def textify(self, percentage: float):
        if percentage == 0.25: return "y cuarto"
        if percentage == 0.5: return "y medio"
        return "y tres cuartos"

    def __to_ordinal(self, day: int):
        return f"{day}º"

    def __is_teen(self, day):
        return 10 <= day % 100 <= 20