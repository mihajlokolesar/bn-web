import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

import Dialog from "../../../../../elements/Dialog";
import Button from "../../../../../elements/Button";
import { Typography } from "@material-ui/core";
import { fontFamilyDemiBold } from "../../../../../../config/theme";

const styles = theme => ({
	root: {},
	content: {
		marginTop: 20,
		maxWidth: 450
	},
	actionButtonContainer: {
		marginTop: 20,
		display: "flex",
		justifyContent: "center"
	},
	text: {
		textAlign: "center"
	},
	boldText: {
		fontFamily: fontFamilyDemiBold
	}
});

const ConfirmSendDialog = ({
	onClose,
	open,
	classes,
	isSent,
	isSending,
	onSend,
	numberOfRecipients,
	...rest
}) => {
	return (
		<Dialog
			onClose={onClose}
			iconUrl={"/icons/tickets-white.svg"}
			title={isSent ? "Email sent!" : "Send Email?"}
			open={open}
			{...rest}
		>
			{isSent ? (
				<React.Fragment>
					<div className={classes.actionButtonContainer}>
						<Button variant={"plainWhite"} onClick={onClose}>
							Close
						</Button>
					</div>
				</React.Fragment>
			) : (
				<div className={classes.content}>
					<Typography className={classes.text}>
						Are you sure you want to send this announcement email to the
						<span className={classes.boldText}> {numberOfRecipients} </span>
						current ticket holders?
					</Typography>

					<div className={classes.actionButtonContainer}>
						<Button
							variant={"plainWhite"}
							style={{ flex: 1, marginRight: 5 }}
							disabled={!!isSending}
							onClick={onClose}
						>
							Cancel
						</Button>
						<Button
							variant={"secondary"}
							style={{ flex: 1, marginLeft: 5 }}
							onClick={onSend}
							disabled={!!isSending}
						>
							{isSending ? "Sending..." : "Send Email"}
						</Button>
					</div>
				</div>
			)}
		</Dialog>
	);
};

ConfirmSendDialog.propTypes = {
	classes: PropTypes.object.isRequired,
	onClose: PropTypes.func.isRequired,
	onSend: PropTypes.func.isRequired,
	numberOfRecipients: PropTypes.number,
	isSent: PropTypes.bool.isRequired,
	isSending: PropTypes.bool.isRequired
};

export default withStyles(styles)(ConfirmSendDialog);
