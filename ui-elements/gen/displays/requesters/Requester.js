import history from "alexandria-ui-elements/src/util/History";

export default class Requester {
	constructor(element) {
		this.element = element;
		this.pushService = Application.services.pushService;
		this.fileService = Application.services.fileService;
	};

	available = (unit) => {
		return this.pushService.isConnectionRegistered(unit);
	};

	connect = (unit, successCallback, errorCallback) => {
		if (this.available(unit)) {
			if (successCallback != null) successCallback();
			return;
		}
		const pushConnections = Application.configuration.pushConnections;
		for (let i=0; i<pushConnections.length; i++) {
			const connection = pushConnections[i].split("_##_");
			if (connection[0].toLowerCase() !== unit.toLowerCase()) continue;
			this.pushService.openConnection(connection[0], connection[1], successCallback, errorCallback);
		}
	};

	addToHistory = (param) => {
		const address = this.element.historyAddress();
		const id = (param instanceof Array) ? (param.length > 0 ? param[0] : "") : param;
		if (address == null) return false;
		history.push(address.replace(/\/:[^/]+/g, "/" + id).replace(/([?&][^=]+=):[^&#]*/, "$1" + id), {});
		return true;
	};
}