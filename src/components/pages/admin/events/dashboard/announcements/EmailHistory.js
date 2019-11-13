import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import SentEmailCard from "./SentEmailCard";
import { fontFamilyDemiBold } from "../../../../../../config/theme";

const styles = theme => ({
	root: {
		marginTop: 10
	},
	noEmailText: {
		color: "#868F9B",
		fontSize: 14
	},
	headerRow: {
		display: "flex",
		paddingLeft: 25,
		paddingRight: 25
	},
	headerText: {
		fontFamily: fontFamilyDemiBold
	}
});

class EmailHistory extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			expandedEmailId: null
		};

		this.onSelectToggle = this.onSelectToggle.bind(this);
	}

	onSelectToggle(id) {
		let { expandedEmailId } = this.state;
		expandedEmailId = expandedEmailId == id ? null : id;
		this.setState({ expandedEmailId });
	}

	render() {
		const { expandedEmailId } = this.state;
		const { emails, classes } = this.props;

		const colStyles = [
			{ flex: 1 },
			{ flex: 4 },
			{ flex: 1 },
			{ flex: 1, textAlign: "right" }
		];

		return (
			<div className={classes.root}>
				{emails.length > 0 ? (
					<div>
						<div className={classes.headerRow}>
							<Typography style={colStyles[0]} className={classes.headerText}>
								Date sent
							</Typography>
							<Typography style={colStyles[1]} className={classes.headerText}>
								Subject line
							</Typography>
							<Typography style={colStyles[2]} className={classes.headerText}>
								Recipients
							</Typography>
							<span style={colStyles[3]}/>
						</div>
						{emails.map(email => (
							<SentEmailCard
								key={email.id}
								{...email}
								onSelectToggle={this.onSelectToggle}
								isExpanded={expandedEmailId === email.id}
								colStyles={colStyles}
							/>
						))}
					</div>
				) : (
					<Typography className={classes.noEmailText}>
						No emails sent yet.
					</Typography>
				)}
			</div>
		);
	}
}

EmailHistory.propTypes = {
	classes: PropTypes.object.isRequired,
	emails: PropTypes.array.isRequired
};

export default withStyles(styles)(EmailHistory);
