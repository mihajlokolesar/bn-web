import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import PropTypes from "prop-types";
import Divider from "../../../../common/Divider";
import Button from "../../../../elements/Button";
import downloadCSV from "../../../../../helpers/downloadCSV";
import EventSummaryRow from "./EventSummaryRow";
import { fontFamilyDemiBold } from "../../../../../config/theme";
import { EventSalesTable } from "./EventSalesTable";
import Loader from "../../../../elements/loaders/Loader";
import summaryReport from "../../../../../stores/reports/summaryReport";
import { observer } from "mobx-react";
import { dollars } from "../../../../../helpers/money";
import Card from "../../../../elements/Card";

const styles = theme => ({
	root: {},
	subHeading: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.3
	}
});

export const REVENUE_SHARE_HEADINGS = [
	"Ticket",
	"Price",
	"Online rev share",
	"Online",
	" ", //"Box office rev share",
	" ", //"Box office",
	"Total value"
];

@observer
class EventSummary extends Component {
	constructor(props) {
		super(props);

		this.state = {};
	}

	componentDidMount() {
		this.refreshData();
	}

	refreshData() {
		const { eventId, organizationId, onLoad } = this.props;

		const queryParams = { organization_id: organizationId, event_id: eventId };
		//TODO date filter
		//start_utc
		//end_utc

		summaryReport.fetchCountAndSalesData(queryParams, onLoad);
	}

	exportCSV() {
		const { eventId } = this.props;
		downloadCSV(
			summaryReport.csv(
				eventId,
				summaryReport.feesData[eventId]
			),
			"event-summary-report"
		);
	}

	renderEventSales() {
		const { eventId } = this.props;
		const eventSales = summaryReport.dataByPrice[eventId];

		if (eventSales === false) {
			//Query failed
			return null;
		}

		if (eventSales === null || eventSales === undefined) {
			return <Loader/>;
		}

		if (Object.keys(eventSales.tickets).length === 0) {
			return <Typography>No event summary available.</Typography>;
		}

		const { classes } = this.props;

		return (
			<div>
				<Typography className={classes.subHeading}>All event sales</Typography>
				<EventSalesTable
					eventSales={eventSales.tickets}
					salesTotals={eventSales.totals}
				/>
			</div>
		);
	}

	renderRevenueShare() {
		const { eventId, classes } = this.props;
		const eventSales = summaryReport.dataByPriceAndFee[eventId];
		const eventFees = summaryReport.feesData[eventId];
		if (eventSales === false) {
			//Query failed
			return null;
		}

		if (eventSales === null || eventSales === undefined) {
			return <Loader/>;
		}

		if (Object.keys(eventSales.tickets).length === 0) {
			return <Typography>No event summary available.</Typography>;
		}
		const { totalOnlineClientFeesInCents } = eventSales.totals;

		let totalOnlineClientFeesInCentsWithOrderFees = 0;

		return (
			<div>
				<Typography className={classes.subHeading}>Revenue share</Typography>

				<EventSummaryRow heading>{REVENUE_SHARE_HEADINGS}</EventSummaryRow>

				{Object.keys(eventSales.tickets).map((ticketId, index) => {
					const ticketSale = eventSales.tickets[ticketId];
					const { totals, sales, name } = ticketSale;

					const { totalSoldOnlineCount, totalOnlineClientFeesInCents } = totals;

					totalOnlineClientFeesInCentsWithOrderFees += totalOnlineClientFeesInCents;

					return (
						<div key={ticketId}>
							<EventSummaryRow ticketTypeRow gray>
								{[
									name,
									" ",
									" ",
									totalSoldOnlineCount,
									" ",
									" ",
									dollars(totalOnlineClientFeesInCents)
								]}
							</EventSummaryRow>

							{sales.filter(function(pricePoint) {
    							return pricePoint.online_fee_count > 0;
  						}).map((pricePoint, priceIndex) => {
								const {
									ticket_pricing_price_in_cents,
									promo_code_discounted_ticket_price,
									client_online_fees_in_cents,
									online_fee_count,
									ticket_pricing_name,
									hold_name,
									promo_redemption_code
								} = pricePoint;

								let rowName = ticket_pricing_name;
								if (hold_name) {
									rowName =
										(promo_redemption_code ? "Promo - " : "Hold - ") +
										hold_name;
								}

								const priceInCents =
									ticket_pricing_price_in_cents +
									promo_code_discounted_ticket_price;
								const clientFeesPerSale =
									client_online_fees_in_cents / online_fee_count;

								return (
									<EventSummaryRow key={priceIndex}>
										{[
											rowName,
											dollars(priceInCents),
											dollars(clientFeesPerSale),
											online_fee_count,
											" ",
											" ",
											dollars(client_online_fees_in_cents)
										]}
									</EventSummaryRow>
								);
							})}
						</div>
					);
				})}

				{eventFees.map((fee, feeIndex) => {
					const rowName = "Order Fees";
					totalOnlineClientFeesInCentsWithOrderFees =
						totalOnlineClientFeesInCents + fee;

					return (
						<EventSummaryRow key={feeIndex} ticketTypeRow>
							{[rowName, "-", dollars(fee), "-", " ", " ", dollars(fee)]}
						</EventSummaryRow>
					);
				})}

				<EventSummaryRow ticketTypeRow total>
					{[
						"Total revenue share",
						" ",
						" ",
						" ",
						" ",
						" ",
						dollars(totalOnlineClientFeesInCentsWithOrderFees)
					]}
				</EventSummaryRow>
			</div>
		);
	}

	render() {
		const { printVersion, classes } = this.props;

		if (printVersion) {
			return (
				<div>
					{this.renderEventSales()}
					<br/>
					<br/>
					{this.renderRevenueShare()}
				</div>
			);
		}

		return (
			<div className={classes.root}>
				<div
					style={{
						display: "flex",
						minHeight: 60,
						alignItems: "center"
					}}
				>
					<Typography variant="title">Event summary report</Typography>
					<span style={{ flex: 1 }}/>
					<Button
						iconUrl="/icons/csv-active.svg"
						variant="text"
						onClick={this.exportCSV.bind(this)}
					>
						Export CSV
					</Button>
					<Button
						href={`/exports/reports/?type=summary&event_id=${
							this.props.eventId
						}`}
						target={"_blank"}
						iconUrl="/icons/pdf-active.svg"
						variant="text"
					>
						Export PDF
					</Button>
				</div>
				<Divider style={{ marginBottom: 40 }}/>

				{this.renderEventSales()}

				<br/>
				<br/>

				{this.renderRevenueShare()}
			</div>
		);
	}
}

EventSummary.propTypes = {
	classes: PropTypes.object.isRequired,
	organizationId: PropTypes.string.isRequired,
	eventId: PropTypes.string.isRequired,
	eventName: PropTypes.string,
	printVersion: PropTypes.bool,
	onLoad: PropTypes.func
};

export const EventSummaryReport = withStyles(styles)(EventSummary);
