import Notifier from "./Notifier";

export default class TimelineDisplayNotifier extends Notifier {
	constructor(element) {
		super(element);
		this.setup();
	};

	setup() {
		if (this.element == null || this.pushLinked) return;
		super.setup();
		this.when("refresh").toSelf().execute((parameters) => this.element.refresh(parameters.v));
		this.pushLinked = true;
	};
}