import React from "react";
import { Typography, withStyles } from "@material-ui/core";
import { fontFamilyDemiBold, secondaryHex } from "../../../config/theme";

const styles = theme => ({
	text: {
		fontSize: theme.typography.fontSize * 0.88,
		color: "#abadaf",
		textAlign: "center"
	},
	linkText: {
		color: secondaryHex,
		fontFamily: fontFamilyDemiBold,
		textDecoration: "capitalize"
	}
});

const TermsAndConditionsLinks = ({ classes }) => {
	return (
		<Typography className={classes.text}>
			By continuing you agree to our
			<br/>
			<a className={classes.linkText} href="/terms.html" target="_blank">
				terms of service
			</a>{" "}
			&{" "}
			<a className={classes.linkText} href="/privacy.html" target="_blank">
				privacy policy
			</a>
		</Typography>
	);
};

export default withStyles(styles)(TermsAndConditionsLinks);
