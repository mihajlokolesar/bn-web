import React, { Component } from "react";
import { Typography, withStyles, CardMedia } from "@material-ui/core";

import notifications from "../../../../stores/notifications";
import user from "../../../../stores/user";
import Bigneon from "../../../../helpers/bigneon";
import { observer } from "mobx-react";
import getUrlParam from "../../../../helpers/getUrlParam";
import CancelTransferDialog from "./CancelTransferDialog";
import Loader from "../../../elements/loaders/Loader";

const styles = theme => ({});

@observer
class CancelTransfer extends Component {
	constructor(props) {
		super(props);

		this.state = {};
	}

	componentDidMount() {
		//If we just landed on this page, make sure the user is logged in first
		user.refreshUser(
			() => this.setTransferKey(),
			() => this.attemptShowCancelDialog()
		);
	}

	attemptShowCancelDialog() {
		if (!user.isAuthenticated) {
			//Show dialog for the user to signup/login, try again when they're authenticated
			user.showAuthRequiredDialog(this.setTransferKey.bind(this));
			return;
		}

		this.setTransferKey();
	}

	setTransferKey() {
		const transferKey = getUrlParam("transfer_key");
		this.setState({ transferKey });
	}

	onCancel() {
		this.props.history.push("/my-events");
	}

	render() {
		if (!user.isAuthenticated) {
			return null;
		}

		const { transferKey } = this.state;

		return (
			<div>
				{transferKey ? (
					<CancelTransferDialog
						onClose={this.onCancel.bind(this)}
						transferKey={transferKey}
					/>
				) : (
					<Loader/>
				)}
			</div>
		);
	}
}

export default withStyles(styles)(CancelTransfer);
