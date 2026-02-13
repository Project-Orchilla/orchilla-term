import AlexandriaUiI18n from "alexandria-ui-elements/gen/I18n";

const I18n = (function () {
	var cache = {};

	var translators = {

	};

	function merge(dictionary, lang) {
		if (dictionary == null) dictionary = {};
		addWords(dictionary, AlexandriaUiI18n.load(lang));
		return dictionary;
	};

	function addWords(dictionary, words) {
		if (words == null) return;
		for (var index in words) dictionary[index] = words[index];
		return dictionary;
	};

	var loader = {
		load: (lang) => {
			if (cache[lang] == null) cache[lang] = merge(translators[lang], lang);
			return cache[lang];
		}
	};

	return loader;
})();

export default I18n;