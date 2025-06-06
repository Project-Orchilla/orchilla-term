import re


class OperatorSorter:
    Granularities = {"s": 13, "m": 12, "h": 11, "d": 10, "bd": 10, "wd": 10, "we": 9, "w": 8, "M": 7, "Q": 6, "SM": 6, "S": 6, "Y": 5, "L": 4, "D": 3, "C": 2, "ML": 1}

    def sort(self, operators: list) -> list:
        result = {}
        for operator in operators:
            tokens = re.findall(r"\D+|\d+", self.__remove_operator(operator))
            for token in tokens:
                if token in self.Granularities.keys():
                    if operator not in result or result[operator] < self.Granularities.get(token):
                        result[operator] = self.Granularities.get(token)
            if operator not in result: result[operator] = 14
        return list(reversed(sorted(result, key=lambda op: result[op])))

    def __remove_operator(self, operator):
        start = operator.find(" ")
        if start == -1: return operator
        return operator[start:].strip()
