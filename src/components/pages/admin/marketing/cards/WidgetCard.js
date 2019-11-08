import React, { Component } from "react";
import PropTypes from "prop-types";
import { Grid, Typography, withStyles } from "@material-ui/core";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Card from "@material-ui/core/Card";
import user from "../../../../../stores/user";
import InputGroup from "../../../../common/form/InputGroup";
import Button from "../../../../elements/Button";
import notifications from "../../../../../stores/notifications";
import Bigneon from "../../../../../helpers/bigneon";

const styles = theme => ({
	paper: {
		padding: theme.spacing.unit,
		marginBottom: theme.spacing.unit
	}
});

class InviteUserCard extends Component {
	constructor(props) {
		super(props);

		this.state = {
			email: "",
			orgMembers: [],
			errors: {},
			isSubmitting: false,
			styleString: "",
			utmSource: "",
			utmMedium: ""
		};
	}

	componentDidMount() {
		fetch("/site/css/widget.css")
			.then(r => r.text())
			.then(styleString => {
				this.setState({ styleString });
			});
	}

	validateFields() {
		//Don't validate every field if the user has not tried to submit at least once
		if (!this.submitAttempted) {
			return true;
		}

		const { email } = this.state;

		const errors = {};

		this.setState({ errors });

		if (Object.keys(errors).length > 0) {
			return false;
		}

		return true;
	}

	render() {
		const { classes } = this.props;
		const { styleString, utmSource, utmMedium } = this.state;
		const widgetSrcUrl = `${window.location.origin}/widgets/events.min.js`;
		const organizationId = user.currentOrganizationId;
		const baseUrl = window.location.origin;
		let apiUrl = Bigneon().client.params.baseURL;
		if (apiUrl[0] === "/") {
			apiUrl = `${baseUrl}${apiUrl}`;
		}
		const outputStyleString = styleString
			.replace(/[\t|\n]*/g, "")
			.replace(/\s{2,}/g, " ");
		const output = `<div id="bn-event-list"></div><style>${outputStyleString}</style>\n<script src="${widgetSrcUrl}" data-target="#bn-event-list" data-organization-id="${organizationId}" data-base-url="${baseUrl}/" data-api-url="${apiUrl}/" data-utm-source="${utmSource}" data-utm-medium="${utmMedium}"></script>`;

		return (
			<Card className={classes.paper}>
				<CardContent>
					<p>
						Copy and paste the following code where you would like
						the widget to
						appear:
					</p>
					<Grid container spacing={32}>
						<Grid item xs={12} sm={12} md={6} lg={6}>
							<InputGroup
								value={utmSource}
								name="utmSource"
								label="UTM Source"
								placeholder="UTM Source"
								type="text"
								onChange={e => this.setState({ utmSource: e.target.value })}
							/>
						</Grid>
						<Grid item xs={12} sm={12} md={6} lg={6}>
							<InputGroup
								value={utmMedium}
								name="utmMedium"
								label="UTM Medium"
								placeholder="UTM Medium"
								type="text"
								onChange={e => this.setState({ utmMedium: e.target.value })}
							/>
						</Grid>
					</Grid>
					<InputGroup
						label={"Customize Styling"}
						value={output}
						name={"output"}
						onFocus={e => {
							e.target.select();
						}}
						onChange={e => {
						}}
					/>
					<textarea
						value={styleString}
						style={{ width: "100%", height: "300px" }}
						onChange={e => {
							this.setState({ styleString: e.target.value });
						}}
					/>
				</CardContent>
			</Card>
		);
	}
}

InviteUserCard.propTypes = {
	organizationId: PropTypes.string.isRequired
};

export default withStyles(styles)(InviteUserCard);
