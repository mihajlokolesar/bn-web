import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

import Dialog from "../../Dialog";
import Button from "../../Button";
import InputGroup from "../../../common/form/InputGroup";
import { validUrl } from "../../../../validators";

const styles = {
	content: {
		minWidth: 200,
		alignContent: "center",
		textAlign: "center"
	},
	actionButtonContainer: {
		display: "flex",
		marginTop: 10
	}
};

class LinkFieldDialog extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			linkText: "",
			error: ""
		};

		this.onClose = this.onClose.bind(this);
		this.onSetUrl = this.onSetUrl.bind(this);
	}

	componentDidUpdate(prevProps, prevState, snapshot) {
		if (
			!this.state.linkText &&
			prevProps.existingUrl !== this.props.existingUrl
		) {
			this.setState({ linkText: this.props.existingUrl });
		}
	}

	onClose() {
		this.setState({ linkText: "", error: "" });
		this.props.onClose();
	}

	onSetUrl() {
		const { linkText } = this.state;

		//TODO validate the url
		if (!validUrl(linkText)) {
			return this.setState({ error: "Not a valid URL" });
		}

		this.props.onSetUrl(linkText);
		this.onClose();
	}

	render() {
		const { classes, ...other } = this.props;
		const { linkText, error } = this.state;

		const iconUrl = "/icons/link-white.svg";

		return (
			<Dialog
				onClose={this.onClose}
				iconUrl={iconUrl}
				title={"Set link"}
				{...other}
			>
				<div className={classes.content}>
					<InputGroup
						placeholder={"http://google.com"}
						value={linkText}
						onChange={e => this.setState({ linkText: e.target.value })}
						error={error}
					/>
				</div>

				<div className={classes.actionButtonContainer}>
					<Button style={{ flex: 1, marginRight: 5 }} onClick={this.onClose}>
						Cancel
					</Button>
					<Button
						variant={"secondary"}
						style={{ flex: 1, marginLeft: 5 }}
						onClick={this.onSetUrl}
					>
						Set
					</Button>
				</div>
			</Dialog>
		);
	}
}

LinkFieldDialog.propTypes = {
	classes: PropTypes.object.isRequired,
	open: PropTypes.bool.isRequired,
	onClose: PropTypes.func.isRequired,
	onSetUrl: PropTypes.func.isRequired,
	existingUrl: PropTypes.string
};

export default withStyles(styles)(LinkFieldDialog);
