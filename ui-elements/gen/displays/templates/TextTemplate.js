import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiText from "alexandria-ui-elements/src/displays/components/Text";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import TextTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import TextTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class TextTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new TextTemplateNotifier(this);
		this.requester = new TextTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock layout="vertical" width="100.0%" height={this.fixHeight != null ? this.fixHeight("100.0%") : "100.0%"} style={{...this.props.style,...display}}>
				<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1129638478" format="autoHeight doubleBottomSpaced whiteBackground" layout="vertical flexible" width="100.0%" height="100.0%" autoSize={true}>
					<UiText context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1129638478.a_727926400" format="airBottom bold h6 whiteColor" mode="normal" translate={true} value="Hello World!">
					</UiText>
				</UiBlock>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(TextTemplate));
DisplayFactory.register("TextTemplate", withStyles(styles, { withTheme: true })(withSnackbar(TextTemplate)));