import React, { Component } from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Dialog from "./Dialog";
import SMSLinkForm from "./SMSLinkForm";

const styles = {
	content: {
		minWidth: 400,
		alignContent: "center",
		textAlign: "center"
	}
};

class SMSAppLinkDialog extends Component {
	render() {
		const { classes, onClose, open, ...other } = this.props;

		const iconUrl = "/icons/phone-white.svg";
		return (
			<Dialog
				open={open}
				onClose={onClose}
				iconUrl={iconUrl}
				title={"Download the Big Neon App."}
				{...other}
			>
				<div className={classes.content}>
					<SMSLinkForm autoFocus={!!open} onSuccess={onClose}/>
				</div>
			</Dialog>
		);
	}
}

SMSAppLinkDialog.propTypes = {
	classes: PropTypes.object.isRequired,
	onClose: PropTypes.func.isRequired
};

export default withStyles(styles)(SMSAppLinkDialog);
