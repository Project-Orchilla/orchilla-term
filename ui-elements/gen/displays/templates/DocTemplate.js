import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiHtmlViewer from "alexandria-ui-elements/src/displays/components/HtmlViewer";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import DocTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import DocTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DocTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new DocTemplateNotifier(this);
		this.requester = new DocTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock layout="vertical" style={{...this.props.style,...display}}>
				<UiHtmlViewer context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_801921090">
				</UiHtmlViewer>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(DocTemplate));
DisplayFactory.register("DocTemplate", withStyles(styles, { withTheme: true })(withSnackbar(DocTemplate)));