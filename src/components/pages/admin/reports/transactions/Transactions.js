import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import moment from "moment";
import PropTypes from "prop-types";

import notifications from "../../../../../stores/notifications";
import Bigneon from "../../../../../helpers/bigneon";
import TransactionRow from "./TransactionRow";
import TransactionDialog from "./TransactionDialog";
import Divider from "../../../../common/Divider";
import Button from "../../../../elements/Button";
import downloadCSV from "../../../../../helpers/downloadCSV";
import Loader from "../../../../elements/loaders/Loader";

const styles = theme => ({
	root: {},
	header: {
		display: "flex",
		minHeight: 60,
		alignItems: "center"
		//borderStyle: "solid"
	}
});

const dollars = cents => `$${(cents / 100).toFixed(2)}`;

class Transactions extends Component {
	constructor(props) {
		super(props);

		this.state = { items: null };
	}

	componentDidMount() {
		this.refreshData();
	}

	exportCSV() {
		const { items } = this.state;

		if (!items) {
			return notifications.show({
				message: "No rows to export.",
				variant: "warning"
			});
		}

		const { eventName, eventId } = this.props;

		const csvRows = [];

		let title = "Transaction details report";
		if (eventName) {
			title = `${title} - ${eventName}`;
		}

		csvRows.push([title]);
		csvRows.push([""]);

		csvRows.push([
			"Event",
			"Ticket type",
			"Order type",
			"Payment method",
			"Transaction date",
			"Redemption code",
			"Order ID",
			"Order type",
			"Payment method",

			"Quantity Sold",
			"Quantity Refunded",
			"Actual Quantity",
			"Unit price",
			"Face value",
			"Service fees",
			"Gross",
			"User Name",
			"Email"
		]);

		items.forEach(item => {
			const {
				client_fee_in_cents,
				company_fee_in_cents,
				event_fee_gross_in_cents,
				event_fee_company_in_cents,
				event_fee_client_in_cents,
				event_id,
				event_name,
				gross,
				order_id,
				order_type,
				payment_method,
				quantity,
				refunded_quantity,
				redemption_code,
				ticket_name,
				transaction_date,
				unit_price_in_cents,
				user_id,
				gross_fee_in_cents_total,
				event_fee_gross_in_cents_total,
				user_name,
				email
			} = item;

			csvRows.push([
				event_name,
				ticket_name,
				order_type,
				payment_method,
				transaction_date,
				redemption_code,
				order_id.slice(-8),
				order_type,
				payment_method,

				quantity,
				refunded_quantity,
				quantity - refunded_quantity,
				dollars(unit_price_in_cents),
				dollars((quantity - refunded_quantity) * unit_price_in_cents), //Face value
				dollars(event_fee_gross_in_cents_total + gross_fee_in_cents_total),
				dollars(gross),
				user_name,
				email
			]);
		});

		downloadCSV(csvRows, "transaction-report");
	}

	refreshData() {
		//TODO date filter
		//start_utc
		//end_utc

		const { eventId, organizationId } = this.props;

		let queryParams = { organization_id: organizationId };

		if (eventId) {
			queryParams = { ...queryParams, event_id: eventId };
		}

		Bigneon()
			.reports.transactionDetails(queryParams)
			.then(response => {
				const items = [];
				const eventFees = {};
				response.data.forEach(item => {
					const transaction_date = moment
						.utc(item.transaction_date)
						.local()
						.format("MM/DD/YYYY h:mm:A");
					items.push({ ...item, transaction_date });
				});

				this.setState({ items });
			})
			.catch(error => {
				console.error(error);
				this.setState({ items: false });

				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Loading event transaction report failed."
				});
			});
	}

	renderDialog() {
		const { activeIndex, items } = this.state;

		let userId = null;
		let activeItem = null;

		if (!isNaN(activeIndex) && items) {
			const item = items[activeIndex];
			if (item) {
				activeItem = item;
				const { user_id } = item;
				if (user_id) {
					userId = user_id;
				}
			}
		}

		return (
			<TransactionDialog
				open={!!activeItem}
				userId={userId}
				item={activeItem}
				onClose={() => this.setState({ activeIndex: null })}
			/>
		);
	}

	renderList() {
		const { items, hoverIndex } = this.state;

		if (items === false) {
			//Query failed
			return null;
		}

		if (items === null) {
			return <Loader/>;
		}

		if (items.length === 0) {
			return <Typography>No transactions found.</Typography>;
		}

		//If we're showing this on an org level then we need to show event names
		const includeEventName = !this.props.eventId;

		const ths = [
			"Ticket name",
			"Order type",
			"Time",
			"Quantity",
			"Unit price",
			"Total face value",
			"Service fees",
			"Gross"
		];

		if (includeEventName) {
			ths.splice(0, 0, "Event");
		}

		return (
			<div>
				<TransactionRow heading>{ths}</TransactionRow>

				{items.map((item, index) => {
					const {
						event_name,
						gross,
						order_type,
						quantity,
						ticket_name,
						transaction_date,
						unit_price_in_cents,
						gross_fee_in_cents_total,
						event_fee_gross_in_cents_total,
						user_name,
						email,
						refunded_quantity
					} = item;

					const tds = [
						ticket_name,
						order_type,
						transaction_date,
						quantity - refunded_quantity,
						dollars(unit_price_in_cents),
						dollars((quantity - refunded_quantity) * unit_price_in_cents),
						dollars(event_fee_gross_in_cents_total + gross_fee_in_cents_total),
						dollars(gross)
					];

					if (includeEventName) {
						tds.splice(0, 0, event_name);
					}

					return (
						<TransactionRow
							key={index}
							onClick={() => this.setState({ activeIndex: index })}
							onMouseEnter={() => this.setState({ hoverIndex: index })}
							onMouseLeave={() => this.setState({ hoverIndex: null })}
							active={false}
							gray={!(index % 2)}
							active={hoverIndex === index}
						>
							{tds}
						</TransactionRow>
					);
				})}
			</div>
		);
	}

	render() {
		const { eventId, classes } = this.props;

		return (
			<div>
				<div className={classes.header}>
					<Typography variant="title">
						{eventId ? "Event" : "Organization"} transaction report
					</Typography>
					<span style={{ flex: 1 }}/>
					<Button
						iconUrl="/icons/csv-active.svg"
						variant="text"
						onClick={this.exportCSV.bind(this)}
					>
						Export CSV
					</Button>
				</div>
				<Divider style={{ marginBottom: 40 }}/>

				{this.renderDialog()}
				{this.renderList()}
			</div>
		);
	}
}

Transactions.propTypes = {
	classes: PropTypes.object.isRequired,
	organizationId: PropTypes.string.isRequired,
	eventId: PropTypes.string,
	eventName: PropTypes.string
};

export default withStyles(styles)(Transactions);
