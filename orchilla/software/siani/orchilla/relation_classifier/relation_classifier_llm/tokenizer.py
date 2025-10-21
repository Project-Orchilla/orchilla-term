import re


class WordTokenizer:
    _splitter = re.compile(r"[^\w]+", flags=re.UNICODE)  # split on non-word chars

    def __call__(self, text: str) -> list[str]:
        if text is None: return []
        parts = self._splitter.split(text.lower().strip())
        return [p for p in parts if p]