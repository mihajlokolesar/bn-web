import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import moment from "moment-timezone";
import { observer } from "mobx-react";

import getUrlParam from "../../../../helpers/getUrlParam";
import Loader from "../../../elements/loaders/Loader";
import Bigneon from "../../../../helpers/bigneon";
import NotFound from "../../../common/NotFound";
import notification from "../../../../stores/notifications";
import { fontFamilyDemiBold } from "../../../../config/theme";
import ExportMetaTags from "./ExportMetaTags";
import { EventSummaryReport } from "./eventSummary/EventSummary";
import Transactions from "./transactions/Transactions";
import TicketCounts from "./counts/TicketCounts";
import Audit from "./eventAudit/Audit";
import SummaryAudit from "./eventSummaryAudit/SummaryAudit";
import EventPromoCodesReport from "./eventPromoCode/EventPromoCode.js";
import user from "../../../../stores/user";
import servedImage from "../../../../helpers/imagePathHelper";
import BoxOfficeSalesSummary from "./boxOfficeSalesSummary/BoxOfficeSalesSummary";
import SettlementReport from "./settlement/SettlementReport";
import EventScanCount from "./eventScanCount/EventScanCount";

const styles = theme => ({
	root: {
		padding: theme.spacing.unit * 2,
		backgroundColor: "#FFFFFF"
	},
	header: {
		display: "flex",
		justifyContent: "space-between",
		paddingBottom: theme.spacing.unit * 2
	},
	logo: {
		height: 60,
		width: "auto"
	},
	title: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 2
	},
	detail: {}
});

const reportTypes = {
	summary: {
		label: "Event summary report",
		ReportComponent: EventSummaryReport
	},
	transactions: {
		label: "Event transaction report",
		ReportComponent: Transactions
	},
	ticket_counts: {
		label: "Event ticket counts report",
		ReportComponent: TicketCounts
	},
	audit: {
		label: "Event audit report",
		ReportComponent: Audit
	},
	summary_audit: {
		label: "Event summary audit report",
		ReportComponent: SummaryAudit
	},
	promo_codes: {
		label: "Event promo codes report",
		ReportComponent: EventPromoCodesReport
	},
	box_office_sales_summary: {
		label: "Box office sales summary",
		ReportComponent: BoxOfficeSalesSummary
	},
	settlement: {
		label: "Settlement report",
		ReportComponent: SettlementReport
	},
	scan_count: {
		label: "Scan count report",
		ReportComponent: EventScanCount
	}
};

@observer
class ExportPDF extends Component {
	constructor(props) {
		super(props);

		this.state = {
			event: null,
			venue: null,
			displayEventStartDate: null
		};
	}

	componentDidMount() {
		const id = getUrlParam("event_id");

		if (id) {
			Bigneon()
				.events.read({ id })
				.then(response => {
					const { artists, venue, ...event } = response.data;
					const { event_start } = event;

					const venueTimeZone = venue.timezone;
					const eventStartDateMoment = moment.utc(event_start);
					const displayLocalVenueTime = eventStartDateMoment
						.tz(venueTimeZone)
						.format("dddd, MMMM Do YYYY hh:mm:A");

					this.setState({
						event,
						venue,
						displayLocalVenueTime,
						venueTimeZone
					});
				})
				.catch(error => {
					this.setState({ event: false });

					notification.showFromErrorResponse({
						defaultMessage: "Failed to load event.",
						error
					});
				});
		}
	}

	onReportLoad() {
		setTimeout(() => window.print(), 500);
	}

	render() {
		const organizationId = user.currentOrganizationId;
		const organizationTimezone = user.currentOrgTimezone;

		if (!organizationId || !organizationTimezone) {
			return <Loader/>;
		}

		const { classes } = this.props;
		const { event, venue, displayLocalVenueTime, venueTimeZone } = this.state;

		const event_id = getUrlParam("event_id");
		const settlement_id = getUrlParam("settlement_id");
		const type = getUrlParam("type");
		const start_utc = getUrlParam("start_utc");
		const end_utc = getUrlParam("end_utc");
		const reportType = reportTypes[type];

		if (!reportType) {
			return <NotFound>Unknown report type</NotFound>;
		}

		if (event_id && event === null) {
			return <Loader>Loading event...</Loader>;
		}

		if (event_id && event === false) {
			return <NotFound>Event not found</NotFound>;
		}

		const { ReportComponent, label } = reportType;

		return (
			<div className={classes.root}>
				<ExportMetaTags
					eventName={event ? event.name : null}
					reportLabel={label}
				/>
				<div className={classes.header}>
					<img
						alt="Header logo"
						className={classes.logo}
						src={servedImage("/images/bn-logo-text-web.svg")}
					/>

					<div>
						{event ? (
							<Typography className={classes.title}>{event.name}</Typography>
						) : null}
						<Typography className={classes.detail}>Report: {label}</Typography>
						{venue ? (
							<Typography className={classes.detail}>
								Venue: {venue.name}
							</Typography>
						) : null}
						{displayLocalVenueTime ? (
							<Typography className={classes.detail}>
								Event time: {displayLocalVenueTime}
							</Typography>
						) : null}
					</div>
				</div>

				<ReportComponent
					organizationId={organizationId}
					organizationTimezone={organizationTimezone}
					eventId={event_id}
					printVersion
					startUtc={start_utc}
					endUtc={end_utc}
					onLoad={this.onReportLoad}
					venueTimeZone={venueTimeZone}
					settlementId={settlement_id}
					isPDFExport={true}
				/>
			</div>
		);
	}
}

export default withStyles(styles)(ExportPDF);
