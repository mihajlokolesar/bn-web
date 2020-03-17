import React, { Component } from "react";
import { Grid, Typography } from "@material-ui/core";
import servedImage from "../../../../../../../helpers/imagePathHelper";
import NotificationProgress from "./NotificationProgress";
class ScheduleSelector extends Component {
	render() {
		const {
			classes,
			isEventEnded,
			renderTimes,
			eventStart,
			eventEnd
		} = this.props;
		return (
			<Grid container alignItems="center" spacing={8}>
				<Grid item xs={1}>
					<img
						style={{ height: 28, width: 28 }}
						className={classes.icon}
						src={servedImage("/icons/drinks-pink.svg")}
					/>
				</Grid>
				<Grid item xs={9} md={4}>
					<Typography className={classes.descriptionHeading}>
						{isEventEnded ? "Event has ended" : "Schedule the Last Call"}
					</Typography>
				</Grid>
				<Grid item xs={12} md={4}>
					{isEventEnded ? null : renderTimes}
				</Grid>
				<Grid item xs={12}>
					<Typography className={classes.greyText}>
						Event Period:{" "}
						<span className={classes.blackText}>
							{eventStart ? eventStart : ""}
							{" - "}
							{eventEnd ? eventEnd : ""}
						</span>
					</Typography>
				</Grid>
			</Grid>
		);
	}

}
export default ScheduleSelector;
