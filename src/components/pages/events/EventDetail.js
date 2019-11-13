import servedImage from "../../../helpers/imagePathHelper";
import React from "react";
import { fontFamilyBold, secondaryHex } from "../../../config/theme";
const styles = theme => {
	return {
		eventDetailsRow: {
			display: "flex"
		},
		iconContainer: {
			flex: 1
		},
		icon: {
			width: 22,
			height: "auto"
		},
		eventDetailContainer: {
			paddingTop: 4,
			flex: 6
		},
		eventDetailText: {
			color: "#3C383F"
		},
		eventDetailBoldText: {
			font: "inherit",
			fontFamily: fontFamilyBold
		},
		eventDetailLinkText: {
			font: "inherit",
			color: secondaryHex,
			cursor: "pointer"
		},
		divider: {
			marginTop: theme.spacing.unit,
			marginBottom: theme.spacing.unit * 4
		}
	};
};
const EventDetail = ({ classes, children, iconUrl }) => (
	<div className={classes.eventDetailsRow}>
		<div className={classes.iconContainer}>
			<img
				alt="Event Details Icon"
				className={classes.icon}
				src={servedImage(iconUrl)}
			/>
		</div>

		<div className={classes.eventDetailContainer}>{children}</div>
	</div>
);

export default EventDetail;
