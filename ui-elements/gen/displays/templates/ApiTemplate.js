import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import ApiTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import ApiTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ApiTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new ApiTemplateNotifier(this);
		this.requester = new ApiTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock layout="vertical" style={{...this.props.style,...display}}>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(ApiTemplate));
DisplayFactory.register("ApiTemplate", withStyles(styles, { withTheme: true })(withSnackbar(ApiTemplate)));