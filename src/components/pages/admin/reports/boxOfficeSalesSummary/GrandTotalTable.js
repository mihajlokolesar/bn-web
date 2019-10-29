import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core";

import TableRow from "./TableRow";
import { dollars } from "../../../../../helpers/money";

const styles = theme => {
	return {
		root: {
			marginBottom: 60
		}
	};
};

const GrandTotalTable = props => {
	const { classes, payments } = props;
	const columnStyles = [{ flex: 3 }, { flex: 1 }];

	let totalInCents = 0;

	return (
		<div className={classes.root}>
			<TableRow columnStyles={columnStyles} heading>
				{["Payment type", "Total value"]}
			</TableRow>

			{payments.map((payment, index) => {
				const { payment_type, quantity, total_sales_in_cents } = payment;

				totalInCents = totalInCents + total_sales_in_cents;

				return (
					<div key={index}>
						<TableRow columnStyles={columnStyles} gray={!!(index % 2)}>
							{[payment_type, dollars(total_sales_in_cents)]}
						</TableRow>
					</div>
				);
			})}

			<TableRow columnStyles={columnStyles} total>
				{["Grand total box office sales", dollars(totalInCents)]}
			</TableRow>
		</div>
	);
};

GrandTotalTable.propTypes = {
	classes: PropTypes.object.isRequired,
	payments: PropTypes.array.isRequired
};

export default withStyles(styles)(GrandTotalTable);
