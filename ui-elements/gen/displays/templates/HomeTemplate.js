import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiTemplateStamp from "alexandria-ui-elements/src/displays/components/TemplateStamp";
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiBlockConditional from "alexandria-ui-elements/src/displays/components/BlockConditional";
import DisplaysHeaderTemplate from "ui-elements/gen/displays/templates/HeaderTemplate";
import DisplaysLandingTemplate from "ui-elements/gen/displays/templates/LandingTemplate";
import DisplaysDocTemplate from "ui-elements/gen/displays/templates/DocTemplate";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import HomeTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import HomeTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class HomeTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new HomeTemplateNotifier(this);
		this.requester = new HomeTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock layout="vertical" width="100.0%" height={this.fixHeight != null ? this.fixHeight("100.0%") : "100.0%"} style={{...this.props.style,...display}}>
				<DisplaysHeaderTemplate context={this._context.bind(this)} owner={this._owner.bind(this)} id="a2083916884">
				</DisplaysHeaderTemplate>
				<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1883088444" layout="vertical flexible">
					<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1883088444.a835950859" format="centered" layout="vertical" width="100.0%" height="100.0%">
						<UiBlockConditional context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1883088444.a835950859.a_1186359385" layout="vertical flexible">
							<DisplaysLandingTemplate context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1883088444.a835950859.a_1186359385.a_1471608444">
							</DisplaysLandingTemplate>
						</UiBlockConditional>
						<UiBlockConditional context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1883088444.a835950859.a2036908744" layout="vertical flexible">
							<DisplaysDocTemplate context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1883088444.a835950859.a2036908744.a1402462466">
							</DisplaysDocTemplate>
						</UiBlockConditional>
					</UiBlock>
				</UiBlock>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(HomeTemplate));
DisplayFactory.register("HomeTemplate", withStyles(styles, { withTheme: true })(withSnackbar(HomeTemplate)));