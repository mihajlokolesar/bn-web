import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

import Dialog from "../../../../../elements/Dialog";
import InputGroup from "../../../../../common/form/InputGroup";
import Button from "../../../../../elements/Button";
import user from "../../../../../../stores/user";
import { validEmail } from "../../../../../../validators";

const styles = theme => ({
	root: {},
	actionButtonContainer: {
		marginTop: 20,
		display: "flex",
		justifyContent: "center"
	}
});

class PreviewSendDialog extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			email: "",
			errors: {}
		};
	}

	componentDidUpdate(prevProps, prevState, snapshot) {
		if (!prevProps.open && this.props.open && !this.state.email) {
			//Did open without an email set yet

			this.setState({ email: user.email });
		}
	}

	validateFields() {
		//Don't validate every field if the user has not tried to submit at least once
		if (!this.submitAttempted) {
			return true;
		}

		const errors = {};

		const { email } = this.state;

		if (!validEmail(email)) {
			errors.email = "Invalid email address";
		}

		this.setState({ errors });

		if (Object.keys(errors).length > 0) {
			return false;
		}

		return true;
	}

	onSubmit() {
		this.submitAttempted = true;

		if (!this.validateFields()) {
			return false;
		}

		const { email } = this.state;
		const { onSend } = this.props;

		onSend(email);
	}

	render() {
		const { email, errors } = this.state;
		const { onClose, open, classes, isSent, isSending, ...rest } = this.props;

		return (
			<Dialog
				onClose={onClose}
				iconUrl={"/icons/tickets-white.svg"}
				title={isSent ? "Test Sent" : "Send Test"}
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
					<React.Fragment>
						<InputGroup
							error={errors.email}
							value={email}
							name="email"
							label="Email Address *"
							type="email"
							onChange={e => this.setState({ email: e.target.value })}
							placeholder={"email@domain.com"}
							multiline
						/>

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
								onClick={this.onSubmit.bind(this)}
								disabled={!!isSending}
							>
								{isSending ? "Sending" : "Send preview email"}
							</Button>
						</div>
					</React.Fragment>
				)}
			</Dialog>
		);
	}
}

PreviewSendDialog.propTypes = {
	classes: PropTypes.object.isRequired,
	onClose: PropTypes.func.isRequired,
	onSend: PropTypes.func.isRequired,
	isSent: PropTypes.bool,
	isSending: PropTypes.bool
};

export default withStyles(styles)(PreviewSendDialog);
