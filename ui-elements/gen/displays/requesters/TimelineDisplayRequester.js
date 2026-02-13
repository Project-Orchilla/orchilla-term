import Requester from "./Requester";

export default class TimelineDisplayRequester extends Requester {
	constructor(element) {
		super(element);
	};
	updateTimeline = (value) => {
		if (this.addToHistory(value)) return;
		this.pushService.send({ app: this.element.context, op: "updateTimeline", s: "timelinedisplay", d: this.element.shortId(), o: this.element.props.owner(), c: this.element.props.context(), v: encodeURIComponent(JSON.stringify(value))}, this.element.ownerUnit());
	};
	didMount = () => {
		this.pushService.send({ op: "didMount", s: "timelinedisplay", d: this.element.shortId(), o: this.element.props.owner(), c: this.element.props.context()}, this.element.ownerUnit());
	};
}