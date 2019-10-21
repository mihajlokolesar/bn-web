import React, { Component } from "react";
import { withStyles, Typography, Hidden } from "@material-ui/core";
import LandingAppBar from "../../../elements/header/LandingAppBar";
import user from "../../../../stores/user";
import { fontFamilyBold, secondaryHex } from "../../../../config/theme";

const styles = theme => ({
	root: {
		backgroundColor: "#221D27",
		display: "flex",
		flexDirection: "column",
		minHeight: 267,
		justifyContent: "center",
		[theme.breakpoints.down("sm")]: {
			flexDirection: "column",
			minHeight: 350
		}
	},
	heading: {
		fontSize: 72,
		fontFamily: fontFamilyBold,
		color: "#fff",
		lineHeight: "72px",
		// marginTop: theme.spacing.unit * 4,
		[theme.breakpoints.down("sm")]: {
			fontSize: theme.typography.fontSize * 2.9,
			paddingLeft: theme.spacing.unit * 3,
			paddingRight: theme.spacing.unit * 3
		}
	},
	viewMapLinkText: {
		fontSize: 16,
		marginLeft: theme.spacing.unit * 1.5,
		color: "#fff",
		cursor: "pointer"
	},
	headingContainer: {
		width: 1400,
		margin: "0 auto"
	},
	subHeading: {
		color: "#9DA3B4",
		fontSize: 21,
		lineSpace: 1,
		[theme.breakpoints.down("sm")]: {
			fontSize: theme.typography.fontSize * 1.4
		}
	}
});

const VenueLandingHero = props => {
	return (
		<div className={props.classes.root}>
			<Hidden smDown>
				<LandingAppBar
					isAuthenticated={user.isAuthenticated}
					history={props.history}
				/>
			</Hidden>

			<div className={props.classes.headingContainer}>
				<Typography variant={"display1"} className={props.classes.heading}>
					{props.pageTitle}
				</Typography>
				<Typography className={props.classes.subHeading}>
					{props.pageSubTitle}
					{props.mapLink ? (
						<a target="_blank" href={props.mapLink}>
							<span className={props.classes.viewMapLinkText}>View on map</span>
						</a>
					) : null}
				</Typography>
			</div>
		</div>
	);
};

export default withStyles(styles)(VenueLandingHero);
