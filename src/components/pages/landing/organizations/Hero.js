import React, { Component } from "react";
import { withStyles, Typography, Hidden } from "@material-ui/core";
import LandingAppBar from "../../../elements/header/LandingAppBar";
import user from "../../../../stores/user";
import { fontFamilyBold } from "../../../../config/theme";

const styles = theme => ({
	root: {
		backgroundColor: "#221D27",
		display: "flex",
		flexDirection: "column",
		justifyContent: "center",

		minHeight: 267,
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
		[theme.breakpoints.down("sm")]: {
			fontSize: theme.typography.fontSize * 2.9,
			paddingLeft: theme.spacing.unit * 3,
			paddingRight: theme.spacing.unit * 3
		}
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

const OrgLandingHero = props => {
	return (
		<div className={props.classes.root}>
			<Hidden smDown>
				<LandingAppBar
					isAuthenticated={user.isAuthenticated}
					history={history}
				/>
			</Hidden>

			<div className={props.classes.headingContainer}>
				<Typography className={props.classes.heading}>{props.pageTitle}</Typography>
			</div>
		</div>
	);
};

export default withStyles(styles)(OrgLandingHero);
