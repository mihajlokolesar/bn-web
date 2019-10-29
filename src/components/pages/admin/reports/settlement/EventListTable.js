import React from "react";
import PropTypes from "prop-types";
import TotalsRow from "./TotalsRow";
import { Typography, withStyles } from "@material-ui/core";
import { fontFamilyDemiBold } from "../../../../../config/theme";

const styles = theme => ({
	root: {},
	heading: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.3,
		marginBottom: 10
	}
});

const EventListTable = props => {
	const { classes, eventList, onlyFinishedEvents, displayDateRangeNoTimezone } = props;

	const columnStyles = [
		{ flex: 1, textAlign: "left" },
		{ flex: 1, textAlign: "left" },
		{ flex: 1, textAlign: "left" },
		{ flex: 3, textAlign: "left" }
	];

	return (
		<div>
			<Typography className={classes.heading}>
				{onlyFinishedEvents ? "Events" : "Sales occurring"} from{" "}
				{displayDateRangeNoTimezone}
			</Typography>

			<TotalsRow columnStyles={columnStyles} heading>
				{[
					"Event start Date/Time",
					"Venue",
					"Event Name"
				]}
			</TotalsRow>

			{eventList
				? eventList.map((event, index) => {
					const { id, displayStartTime, venue, name } = event;
					const even = index % 2 === 0;

					return (
						<TotalsRow
							key={id}
							gray={even}
							darkGray={!even}
							columnStyles={columnStyles}
						>
							{[displayStartTime, venue.name, name]}
						</TotalsRow>
					);
				  })
				: null}
		</div>
	);
};

EventListTable.propTypes = {
	classes: PropTypes.object.isRequired,
	eventList: PropTypes.array.isRequired,
	onlyFinishedEvents: PropTypes.bool.isRequired,
	displayDateRangeNoTimezone: PropTypes.string.isRequired
};

export default withStyles(styles)(EventListTable);
