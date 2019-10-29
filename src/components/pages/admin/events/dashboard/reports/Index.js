import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import { observer } from "mobx-react";
import PropTypes from "prop-types";
import Bigneon from "../../../../../../helpers/bigneon";

import user from "../../../../../../stores/user";
import Container from "../Container";
import TransactionsList from "../../../reports/transactions/Transactions";
import { EventSummaryReport } from "../../../reports/eventSummary/EventSummary";
import TicketCounts from "../../../reports/counts/TicketCounts";
import EventAudit from "../../../reports/eventAudit/Audit";
import EventSummaryAudit from "../../../reports/eventSummaryAudit/SummaryAudit";
import EventPromoCodesReport from "../../../reports/eventPromoCode/EventPromoCode";
import Loader from "../../../../../elements/loaders/Loader";

const styles = theme => ({
	root: {}
});

@observer
class Report extends Component {
	constructor(props) {
		super(props);

		this.state = {
			eventName: null,
			salesStartStringUtc: null,
			venueTimeZone: null
		};
	}

	componentDidMount() {
		const eventId = this.props.match.params.id;

		Bigneon()
			.events.read({ id: eventId })
			.then(response => {
				const { name, publish_date, venue } = response.data;
				this.setState({
					eventName: name,
					salesStartStringUtc: publish_date,
					venueTimeZone: venue.timezone
				});
			})
			.catch(error => {
				console.error(error);
			});
	}

	render() {
		const eventId = this.props.match.params.id;
		const type = this.props.match.params.type;
		const organizationId = user.currentOrganizationId;

		if (!organizationId) {
			return <Loader/>;
		}

		const { eventName, salesStartStringUtc, venueTimeZone } = this.state;

		let content;

		switch (type) {
			case "transactions":
				if (!venueTimeZone) {
					return <Loader>Loading event details...</Loader>;
				}
				content = (
					<TransactionsList
						eventName={eventName}
						eventId={eventId}
						salesStartStringUtc={salesStartStringUtc}
						organizationId={organizationId}
						venueTimeZone={venueTimeZone}
					/>
				);
				break;

			case "summary":
				content = (
					<EventSummaryReport
						eventName={eventName}
						organizationId={organizationId}
						eventId={eventId}
					/>
				);
				break;

			case "ticket-counts":
				content = (
					<TicketCounts
						eventName={eventName}
						organizationId={organizationId}
						eventId={eventId}
					/>
				);
				break;

			case "audit":
				content = (
					<EventAudit
						eventName={eventName}
						organizationId={organizationId}
						eventId={eventId}
					/>
				);
				break;
			case "summary-audit":
				content = (
					<EventSummaryAudit
						eventName={eventName}
						organizationId={organizationId}
						eventId={eventId}
					/>
				);
				break;

			case "promo-codes":
				content = (
					<EventPromoCodesReport
						eventName={eventName}
						organizationId={organizationId}
						eventId={eventId}
					/>
				);
				break;

			default:
				content = <Typography>Report unavailable.</Typography>;
				break;
		}

		return (
			<Container
				eventId={eventId}
				subheading={"reports"}
				layout={"childrenInsideCard"}
			>
				{content}
			</Container>
		);
	}
}

Report.propTypes = {
	classes: PropTypes.object.isRequired,
	match: PropTypes.object.isRequired
};

export default withStyles(styles)(Report);
