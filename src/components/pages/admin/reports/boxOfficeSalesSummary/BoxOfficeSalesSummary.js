import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import PropTypes from "prop-types";
import moment from "moment-timezone";

import notifications from "../../../../../stores/notifications";
import Button from "../../../../elements/Button";
import downloadCSV from "../../../../../helpers/downloadCSV";
import { fontFamilyDemiBold } from "../../../../../config/theme";
import Loader from "../../../../elements/loaders/Loader";
import Bigneon from "../../../../../helpers/bigneon";
import GrandTotalTable from "./GrandTotalTable";
import OperatorTable from "./OperatorTable";
import ReportsDate from "../ReportDate";
import { dollars } from "../../../../../helpers/money";

const styles = theme => ({
	root: {},
	pageTitle: {
		fontSize: 30,
		fontFamily: fontFamilyDemiBold
	},
	header: {
		display: "flex",
		minHeight: 60,
		alignItems: "center"
	},
	subheading: {
		marginBottom: theme.spacing.unit
	},
	boldText: {
		fontFamily: fontFamilyDemiBold
	},
	tableHeading: {
		marginBottom: theme.spacing.unit,
		fontFamily: fontFamilyDemiBold,
		fontSize: 20
	}
});

class BoxOfficeSalesSummary extends Component {
	constructor(props) {
		super(props);

		this.state = {
			payments: null,
			operators: null,
			startDateDisplay: null,
			endDateDisplay: null
		};
	}

	componentDidMount() {
		if (this.props.printVersion) {
			this.refreshData();
		}
	}

	exportCSV() {
		const {
			startDateDisplay,
			endDateDisplay,
			payments,
			operators
		} = this.state;

		const reportRunDisplay = moment(event.event_date).format(
			"MMM DD, YYY at h:mm A"
		);

		const csvRows = [];

		csvRows.push(["Box office sales summary report"]);
		csvRows.push([`Report run ${reportRunDisplay}`]);
		csvRows.push([
			`Transactions from ${startDateDisplay} to ${endDateDisplay}`
		]);

		csvRows.push([""]);
		csvRows.push(["Grand total"]);

		let totalInCents = 0;
		payments.forEach(payment => {
			const { payment_type, total_sales_in_cents } = payment;
			totalInCents = totalInCents + total_sales_in_cents;
			csvRows.push([payment_type, dollars(total_sales_in_cents)]);
		});

		csvRows.push(["Grand total box office sales", dollars(totalInCents)]);
		csvRows.push([""]);

		operators.forEach(operator => {
			const { operator_name, events, payments } = operator;
			csvRows.push([`Operator name: ${operator_name}`]);

			csvRows.push([
				"Event name",
				"Date",
				"Face value",
				"Rev share",
				"Box office sold",
				"Total value"
			]);

			let totalTicketSales = 0;
			let totalInCents = 0;

			events.forEach(event => {
				const {
					event_name,
					event_date,
					face_value_in_cents,
					number_of_tickets,
					revenue_share_value_in_cents,
					total_sales_in_cents
				} = event;

				totalTicketSales = totalTicketSales + number_of_tickets;
				totalInCents = totalInCents + total_sales_in_cents;

				csvRows.push([
					event_name,
					event_date,
					dollars(face_value_in_cents),
					dollars(revenue_share_value_in_cents),
					number_of_tickets,
					dollars(total_sales_in_cents)
				]);
			});

			csvRows.push([""]);

			payments.forEach(payment => {
				const { payment_type, quantity, total_sales_in_cents } = payment;

				csvRows.push([
					payment_type,
					"",
					"",
					"",
					quantity,
					dollars(total_sales_in_cents)
				]);
			});

			csvRows.push([
				"Operator total",
				"",
				"",
				"",
				totalTicketSales,
				dollars(totalInCents)
			]);
		});

		downloadCSV(csvRows, "box-office-sales-summary");
	}

	refreshData(
		dataParams = {
			start_utc: null,
			end_utc: null,
			startDate: null,
			endDate: null
		}
	) {
		const { startDate, endDate, start_utc, end_utc } = dataParams;

		const { organizationId, onLoad, organizationTimezone } = this.props;

		const queryParams = { organization_id: organizationId, start_utc, end_utc };

		Bigneon()
			.reports.boxOfficeSalesSummary(queryParams)
			.then(response => {
				const { payments, operators } = response.data;

				//Set event dates to org timezone and format
				operators.forEach(({ events }) => {
					events.forEach(event => {
						event.event_date = moment
							.utc(event.event_date)
							.tz(organizationTimezone)
							.format("MM/DD/YYYY h:mm A, z");
					});
				});

				const displayFormat = "MMM DD, YYYY";
				const startDateDisplay = moment
					.utc(startDate)
					.tz(organizationTimezone)
					.format(displayFormat);
				const endDateDisplay = moment
					.utc(endDate)
					.tz(organizationTimezone)
					.format(displayFormat);

				this.setState(
					{ payments, operators, startDateDisplay, endDateDisplay },
					() => {
						onLoad ? onLoad() : null;
					}
				);
			})
			.catch(error => {
				console.error(error);
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Failed to load report."
				});
			});
	}

	renderGrandTotal() {
		const { classes } = this.props;
		const { payments } = this.state;

		if (!payments) {
			return <Loader/>;
		}

		return (
			<React.Fragment>
				<Typography className={classes.tableHeading}>Grand total</Typography>
				<GrandTotalTable payments={payments}/>
			</React.Fragment>
		);
	}

	renderOperators() {
		const { classes, printVersion } = this.props;
		const { operators } = this.state;

		if (!operators) {
			return null;
		}

		return (
			<React.Fragment>
				{operators.map(operator => (
					<React.Fragment key={operator.operator_id}>
						<Typography className={classes.tableHeading}>
							Operator: {operator.operator_name}
						</Typography>
						<OperatorTable {...operator} printVersion={printVersion}/>
					</React.Fragment>
				))}
			</React.Fragment>
		);
	}

	render() {
		const { classes, printVersion, organizationTimezone } = this.props;
		const {
			startDateDisplay,
			endDateDisplay,
			operators,
			payments
		} = this.state;
		const isLoaded = operators && payments;

		if (printVersion) {
			return (
				<React.Fragment>
					{this.renderGrandTotal()}
					{this.renderOperators()}
				</React.Fragment>
			);
		}

		return (
			<div className={classes.root}>
				<div className={classes.header}>
					<Typography className={classes.pageTitle}>
						Box office sales summary report
					</Typography>
					<span style={{ flex: 1 }}/>
					<div>
						{isLoaded ? (
							<Button
								iconUrl="/icons/csv-active.svg"
								variant="text"
								onClick={() => this.exportCSV()}
							>
								Export CSV
							</Button>
						) : null}
						<Button
							href={`/exports/reports/?type=box_office_sales_summary`} //TODO add date range here
							target={"_blank"}
							iconUrl="/icons/pdf-active.svg"
							variant="text"
						>
							Export PDF
						</Button>
					</div>
				</div>
				{startDateDisplay && endDateDisplay ? (
					<Typography className={classes.subheading}>
						Transactions from{" "}
						<span className={classes.boldText}>{startDateDisplay}</span> to{" "}
						<span className={classes.boldText}>{endDateDisplay}</span>
					</Typography>
				) : null}
				<ReportsDate
					timezone={organizationTimezone}
					onChange={this.refreshData.bind(this)}
				/>
				{this.renderGrandTotal()}
				{this.renderOperators()}
			</div>
		);
	}
}

BoxOfficeSalesSummary.propTypes = {
	classes: PropTypes.object.isRequired,
	organizationId: PropTypes.string.isRequired,
	organizationTimezone: PropTypes.string.isRequired,
	printVersion: PropTypes.bool,
	onLoad: PropTypes.func
};

export default withStyles(styles)(BoxOfficeSalesSummary);
