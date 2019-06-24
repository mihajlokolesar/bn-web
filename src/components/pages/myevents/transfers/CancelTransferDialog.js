import React, { Component } from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

import Button from "../../../elements/Button";
import notification from "../../../../stores/notifications";
import Bigneon from "../../../../helpers/bigneon";
import Dialog from "../../../elements/Dialog";
import { Typography } from "@material-ui/core";
import { fontFamilyDemiBold, secondaryHex } from "../../../../config/theme";

const styles = theme => ({
	content: {
		minWidth: 400,
		alignContent: "center",
		textAlign: "center"
	},
	actionButtons: {
		display: "flex",
		justifyContent: "center"
	},
	infoText: {
		fontSize: theme.typography.fontSize * 1.125,
		textAlign: "center"
	},
	emailText: {
		color: secondaryHex,
		fontFamily: fontFamilyDemiBold
	}
});

class CancelTransferDialog extends Component {
	constructor(props) {
		super(props);

		this.state = {
			isCancelling: false,
			recipientEmail: null,
			transferId: null,
			cancelSuccess: false
		};

		this.onClose = this.onClose.bind(this);
	}

	componentDidMount() {
		this.loadTransferDetails();
	}

	componentDidUpdate(prevProps, prevState, snapshot) {
		if (
			this.props.transferKey &&
			prevProps.transferKey !== this.props.transferKey
		) {
			this.loadTransferDetails();
		}
	}

	loadTransferDetails() {
		const { transferKey } = this.props;

		if (!transferKey) {
			return null;
		}

		Bigneon()
			.transfers.read({
				id: transferKey
			})
			.then(response => {
				const { id, transfer_address } = response.data;
				this.setState({ recipientEmail: transfer_address, transferId: id });
			})
			.catch(error => {
				this.setState({ isCancelling: false });
				console.error(error);
				notification.showFromErrorResponse({
					error,
					defaultMessage: "Could not find transfer details."
				});
			});
	}

	onClose() {
		const { isCancelling } = this.state;
		if (isCancelling) {
			return;
		}

		this.setState({ recipientEmail: null, cancelSuccess: false }, () => {
			const { onClose } = this.props;
			onClose();
		});
	}

	onCancel() {
		this.setState({ isCancelling: true });
		const { transferId } = this.state;

		Bigneon()
			.transfers.cancel({
				id: transferId
			})
			.then(response => {
				this.setState({ isCancelling: false, cancelSuccess: true }, () => {
					if (this.props.onSuccess) {
						this.props.onSuccess();
					}
				});
			})
			.catch(error => {
				this.setState({ isCancelling: false });
				console.error(error);
				notification.showFromErrorResponse({
					error,
					defaultMessage: "Ticket transfer cancel failed.",
					variant: "error"
				});
			});
	}

	renderCancelContent() {
		const { classes } = this.props;

		const { recipientEmail, isCancelling, transferId } = this.state;

		let explainerText = null;
		if (recipientEmail) {
			explainerText = (
				<Typography className={classes.infoText}>
					Please confirm that you want to cancel this transfer. We’ll send{" "}
					<span className={classes.emailText}>{recipientEmail}</span> a message
					to let them know.
				</Typography>
			);
		}

		return (
			<React.Fragment>
				<br/>
				{explainerText}
				<br/>
				<div className={classes.actionButtons}>
					<Button
						size="large"
						style={{ flex: 1, marginRight: 5 }}
						disabled={isCancelling}
						onClick={this.onClose}
					>
						Go back
					</Button>
					<Button
						size="large"
						style={{ flex: 1, marginLeft: 5 }}
						onClick={this.onCancel.bind(this)}
						variant="secondary"
						disabled={isCancelling || !transferId}
					>
						{isCancelling ? "Cancelling..." : "Cancel transfer"}
					</Button>
				</div>
			</React.Fragment>
		);
	}

	renderSuccessContent() {
		const { classes } = this.props;

		const { recipientEmail } = this.state;

		let explainerText = null;
		if (recipientEmail) {
			explainerText = (
				<Typography className={classes.infoText}>
					The message to{" "}
					<span className={classes.emailText}>{recipientEmail}</span> has been
					sent and the tickets have been assigned back to you.
				</Typography>
			);
		}

		return (
			<React.Fragment>
				<br/>
				{explainerText}
				<br/>
				<div className={classes.actionButtons}>
					<Button
						size="large"
						style={{ flex: 1, maxWidth: 300 }}
						onClick={this.onClose}
						variant="secondary"
					>
						Got it!
					</Button>
				</div>
			</React.Fragment>
		);
	}

	render() {
		const { transferKey, classes, onSuccess, ...rest } = this.props;
		const { cancelSuccess } = this.state;

		const iconUrl = "/icons/tickets-white.svg";
		return (
			<Dialog
				onClose={this.onClose}
				iconUrl={iconUrl}
				title={
					cancelSuccess
						? "We've cancelled the transfer!"
						: "Want to cancel this transfer?"
				}
				open={!!transferKey}
				{...rest}
			>
				{cancelSuccess
					? this.renderSuccessContent()
					: this.renderCancelContent()}
			</Dialog>
		);
	}
}

CancelTransferDialog.propTypes = {
	classes: PropTypes.object.isRequired,
	onClose: PropTypes.func.isRequired,
	transferKey: PropTypes.string,
	onSuccess: PropTypes.func
};

export default withStyles(styles)(CancelTransferDialog);
