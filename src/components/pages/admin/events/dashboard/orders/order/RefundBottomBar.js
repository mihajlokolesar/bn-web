import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { Hidden, Typography } from "@material-ui/core";

import Button from "../../../../../../elements/Button";
import { dollars } from "../../../../../../../helpers/money";
import {
	callToActionBackground,
	fontFamilyDemiBold,
	secondaryHex
} from "../../../../../../../config/theme";

const styles = theme => {
	return {
		bar: {
			width: "100%",
			position: "fixed",
			bottom: 0,
			left: 80,
			zIndex: "100000; position:relative",
			justifyContent: "space-between",
			display: "flex",
			alignItems: "center",
			flex: 1,
			borderTop: "1px solid #DEE2E8",
			[theme.breakpoints.down("sm")]: {
				left: 0
			}
		},
		desktopContainer: {
			display: "flex",
			justifyContent: "flex-end",
			flex: 1,
			padding: theme.spacing.unit,
			paddingRight: 80 + theme.spacing.unit * 2, //Catering for width being 100% and end components going off page
			backgroundColor: "#FFFFFF"
		},
		//Mobile styles
		mobileContainer: {
			flex: 1,
			display: "flex",
			height: 60,
			background: callToActionBackground,
			justifyContent: "center",
			alignItems: "center"
		},
		mobileText: {
			textAlign: "center",
			alignContent: "center",
			color: "#FFFFFF",
			fontFamily: fontFamilyDemiBold,
			fontSize: 17
		}
	};
};

const RefundBottomBar = ({ classes, onClick, amountInCents }) => {
	if (amountInCents === null) {
		return null;
	}

	return (
		<div className={classes.bar}>
			{/*DESKTOP*/}
			<Hidden smDown>
				<div className={classes.desktopContainer}>
					<Button variant="callToAction" onClick={onClick}>
						Refund {dollars(amountInCents)}
					</Button>
				</div>
			</Hidden>

			{/*/!*MOBILE*!/*/}
			<Hidden mdUp>
				<div className={classes.mobileContainer} onClick={onClick}>
					<Typography className={classes.mobileText}>
						Refund {dollars(amountInCents)}
					</Typography>
				</div>
			</Hidden>
		</div>
	);
};

RefundBottomBar.propTypes = {
	classes: PropTypes.object.isRequired,
	onClick: PropTypes.func.isRequired,
	amountInCents: PropTypes.number
};

export default withStyles(styles)(RefundBottomBar);
