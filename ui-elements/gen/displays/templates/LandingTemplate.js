import React from "react";
import { withStyles } from '@material-ui/core/styles';
import UiDisplayStamp from "alexandria-ui-elements/src/displays/components/DisplayStamp";
import UiText from "alexandria-ui-elements/src/displays/components/Text";
import UiBlock from "alexandria-ui-elements/src/displays/components/Block";
import UiDateEditable from "alexandria-ui-elements/src/displays/components/DateEditable";
import UiTextEditable from "alexandria-ui-elements/src/displays/components/TextEditable";
import UiFileEditable from "alexandria-ui-elements/src/displays/components/FileEditable";
import UiAction from "alexandria-ui-elements/src/displays/components/Action";
import DisplaysTimelineDisplay from "ui-elements/src/displays/TimelineDisplay";
import UiTemplate from "alexandria-ui-elements/src/displays/components/Template";
import LandingTemplateNotifier from "alexandria-ui-elements/gen/displays/notifiers/TemplateNotifier";
import LandingTemplateRequester from "alexandria-ui-elements/gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class LandingTemplate extends UiTemplate {

	constructor(props) {
		super(props);
		this.notifier = new LandingTemplateNotifier(this);
		this.requester = new LandingTemplateRequester(this);
	};

	render() {
		const display = !this.state.visible ? {display:'none'} : undefined;
		const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
		return(
			<UiBlock layout="vertical flexible center" width="100.0%" height={this.fixHeight != null ? this.fixHeight("100.0%") : "100.0%"} style={{...this.props.style,...display}}>
				<UiText context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1321408913" format="airBottom airTop bold h2 whiteColor" mode="normal" translate={true}>
				</UiText>
				<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865" layout="vertical" width="1000px" height="500px">
					<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073" layout="vertical flexible centercenter">
						<UiDateEditable context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a945456514" label="Context" format="middleWidth" pattern="YYYY/MM/DD HH:mm:ss" timePicker={true}>
						</UiDateEditable>
						<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a_1199674312" layout="vertical flexible centercenter" width="50.0%" height="100.0%">
							<UiTextEditable context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a_1199674312.a_869904231" format="airBottom airTop h6 whiteColor" mode="normal" editionMode="Raw" rows={8} translate={true}>
							</UiTextEditable>
						</UiBlock>
						<UiFileEditable context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a2106483622" format="airBottom" width="200px" height="190px" dropZone={true} dropZoneLimit={1}>
						</UiFileEditable>
						<UiAction context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a_1593735372" title="Evaluate" target="self" mode="Button" size="Medium">
						</UiAction>
						<UiBlock context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a669366937" layout="vertical flexible centercenter" width="100.0%" height="100.0%">
							<UiText context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a669366937.a_776504594" format="airBottom airTop bold h6 whiteColor" mode="normal" translate={true}>
							</UiText>
						</UiBlock>
						<UiDisplayStamp context={this._context.bind(this)} owner={this._owner.bind(this)} id="a_1233192865.a_1862327073.a1090063943" format="fullSize">
						</UiDisplayStamp>
					</UiBlock>
				</UiBlock>
			</UiBlock>
		);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(LandingTemplate));
DisplayFactory.register("LandingTemplate", withStyles(styles, { withTheme: true })(withSnackbar(LandingTemplate)));