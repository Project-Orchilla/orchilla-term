import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiImage from "alexandria-ui-elements/src/displays/components/Image";
import UiAction from "alexandria-ui-elements/src/displays/components/Action";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import HeaderTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import HeaderTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class HeaderTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new HeaderTemplateNotifier(this);
		this.requester = new HeaderTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock format="headerStyle" layout="horizontal center" style={{...this.props.style,...display}}>
				<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_2055520906" format="middleAirBottom middleAirLeft middleAirTop" layout="horizontal center flexible">
					<UiImage context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_2055520906.a1642143976" format="airLeft" width="50px" height="50px" mobileReduceFactor={75}>
					</UiImage>
					<UiAction context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_2055520906.a_314297614" title="" target="self" mode="Link" size="Medium" format="logoLink">
					</UiAction>
					<UiAction context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_2055520906.a1688858194" title="Docs" target="self" mode="Link" size="Medium" format="airLeft">
					</UiAction>
				</UiBlock>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(HeaderTemplate));
DisplayFactory.register("HeaderTemplate", withStyles(styles, { withTheme: true })(withSnackbar(HeaderTemplate)));