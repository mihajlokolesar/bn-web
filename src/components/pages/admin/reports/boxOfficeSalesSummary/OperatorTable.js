import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core";

import TableRow from "./TableRow";
import { dollars } from "../../../../../helpers/money";

const styles = theme => {
	return {
		root: {
			marginBottom: 40
		}
	};
};

const OperatorTable = props => {
	const { classes, payments, events, printVersion } = props;
	const columnStyles = [
		{ flex: 4 },
		{ flex: 3 },
		{ flex: 2 },
		{ flex: 2 },
		{ flex: 2 },
		{ flex: 2 }
	];

	let totalInCents = 0;
	let totalTicketSales = 0;

	return (
		<div className={classes.root}>
			<TableRow columnStyles={columnStyles} heading>
				{[
					"Event name",
					"Date",
					"Face value",
					"Rev share",
					"Box office sold",
					"Total value"
				]}
			</TableRow>

			{events.map((event, index) => {
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

				return (
					<div key={index}>
						<TableRow
							columnStyles={columnStyles}
							gray={!!(index % 2)}
							printVersion={printVersion}
						>
							{[
								event_name,
								event_date,
								dollars(face_value_in_cents),
								dollars(revenue_share_value_in_cents),
								number_of_tickets,
								dollars(total_sales_in_cents)
							]}
						</TableRow>
					</div>
				);
			})}

			{payments.map((payment, index) => {
				const { payment_type, quantity, total_sales_in_cents } = payment;

				return (
					<div key={index}>
						<TableRow
							columnStyles={columnStyles}
							subTotal1={payment_type === "Cash"}
							subTotal2={payment_type === "CreditCard"}
							gray={payment_type !== "Cash" && payment_type !== "CreditCard"}
							printVersion={printVersion}
						>
							{[
								payment_type,
								"",
								"",
								"",
								quantity,
								dollars(total_sales_in_cents)
							]}
						</TableRow>
					</div>
				);
			})}

			<TableRow columnStyles={columnStyles} total printVersion={printVersion}>
				{[
					"Operator total",
					"",
					"",
					"",
					totalTicketSales,
					dollars(totalInCents)
				]}
			</TableRow>
		</div>
	);
};

OperatorTable.propTypes = {
	classes: PropTypes.object.isRequired,
	operator_name: PropTypes.string.isRequired,
	payments: PropTypes.array.isRequired,
	events: PropTypes.array.isRequired,
	printVersion: PropTypes.bool
};

export default withStyles(styles)(OperatorTable);
