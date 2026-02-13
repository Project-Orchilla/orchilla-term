import React from "react";
import {withStyles} from '@material-ui/core/styles';
import AbstractTimelineDisplay from "../../gen/displays/AbstractTimelineDisplay";
import TimelineDisplayNotifier from "../../gen/displays/notifiers/TimelineDisplayNotifier";
import TimelineDisplayRequester from "../../gen/displays/requesters/TimelineDisplayRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from 'notistack';

import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import Timeline from 'highcharts/modules/timeline';

Timeline(Highcharts);

const styles = theme => ({});

class TimelineDisplay extends AbstractTimelineDisplay {

    constructor(props) {
        super(props);
        this.notifier = new TimelineDisplayNotifier(this);
        this.requester = new TimelineDisplayRequester(this);
        this.state = {data: []};
    }

    refresh = (timelineDTOs) => {
        if (!timelineDTOs || timelineDTOs.length === 0) return;

        this.setState(
            {
                data: timelineDTOs.map(item => ({
                    name: item.name,
                    label: item.label,
                    description: item.description,
                    x: Date.parse(item.x) || Date.now()
                }))
            }
        );
    };

    render() {
        return (
            <div style={{ padding: 16 }}>
                <HighchartsReact
                    highcharts={Highcharts}
                    options={this.options()}
                />
            </div>
        );
    }

    options = () => {
        return {
            chart: {
                type: 'timeline',
                backgroundColor: '#303030',
            },
            title: {
                text: '',
                style: { color: '#FFFFFF' }
            },
            xAxis: {
                type: 'datetime',
                visible: false
            },
            yAxis: {
                visible: false
            },
            tooltip: {
                style: { width: 500, color: '#FFFFFF' },
                backgroundColor: '#1a1a1a'
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        backgroundColor: '#424242',
                        borderColor: '#666',
                        borderRadius: 6,
                        borderWidth: 1,
                        color: '#FFFFFF',
                        style: {
                            color: '#FFFFFF',
                            fontWeight: 'normal',
                            textOutline: 'none',
                            fontSize: '13px'
                        }
                    }
                }
            },
            credits :{
                enabled: false
            },
            series: [
                {
                    data: this.state.data,
                    color: '#00bcd4'
                }
            ]
        };
    }
}

export default withStyles(styles, { withTheme: true })(withSnackbar(TimelineDisplay));
DisplayFactory.register("TimelineDisplay", withStyles(styles, { withTheme: true })(withSnackbar(TimelineDisplay)));
