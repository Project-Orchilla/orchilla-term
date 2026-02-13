import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiHtmlViewer from "alexandria-ui-elements/src/displays/components/HtmlViewer";
import UiSetDarkMode from "alexandria-ui-elements/src/displays/components/SetDarkMode";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import AboutTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import AboutTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class AboutTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new AboutTemplateNotifier(this);
		this.requester = new AboutTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock layout="vertical" width="100.0%" height={this.fixHeight != null ? this.fixHeight("100.0%") : "100.0%"} style={{...this.props.style,...display}}>
				<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a659505588" format="autoHeight doubleBottomSpaced whiteBackground" layout="vertical flexible" width="100.0%" height="100.0%" autoSize={true}>
					<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a659505588.a1137446975" layout="vertical flexible center" width="80.0%" height="100.0%">
						<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a659505588.a1137446975.a394940061" format="whiteBackground" layout="vertical flexible" width="50.0%" height="100.0%">
							<UiHtmlViewer context={this._context.bind(this)} owner={this._owner.bind(this)} id="a659505588.a1137446975.a394940061.a_1129771944">
							</UiHtmlViewer>
						</UiBlock>
					</UiBlock>
				</UiBlock>
				<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a240328251" layout="horizontal endjustified">
					<UiSetDarkMode context={this._context.bind(this)} owner={this._owner.bind(this)} id="a240328251.a_520961886" title="Set dark mode" target="self" mode="MaterialIconButton" icon="Brightness4" size="Small" format="airRight">
					</UiSetDarkMode>
				</UiBlock>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(AboutTemplate));
DisplayFactory.register("AboutTemplate", withStyles(styles, { withTheme: true })(withSnackbar(AboutTemplate)));