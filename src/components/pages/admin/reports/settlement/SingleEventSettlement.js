import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core";
import Typography from "@material-ui/core/Typography";

import { fontFamilyDemiBold } from "../../../../../config/theme";
import EventSettlementRow from "./EventSettlementRow";
import { dollars } from "../../../../../helpers/money";
import splitByCamelCase from "../../../../../helpers/splitByCamelCase";

const styles = theme => {
	return {
		root: {
			marginBottom: theme.spacing.unit * 8
		},
		heading: {
			fontSize: theme.typography.fontSize * 1.6,
			fontFamily: fontFamilyDemiBold
		},
		boldSpan: {
			fontFamily: fontFamilyDemiBold
		}
	};
};

const SingleEventSettlement = props => {
	const { classes, eventDetails, entries } = props;

	const { displayStartTime, venue, name } = eventDetails;

	let totalOnlineSoldQuantity = 0;
	let totalFaceInCents = 0;
	let totalRevShareInCents = 0;
	let totalSalesInCents = 0;

	return (
		<div className={classes.root}>
			<Typography className={classes.heading}>{name}</Typography>
			<Typography>
				<span className={classes.boldSpan}>{displayStartTime}</span>
			</Typography>
			<Typography>
				Venue: <span className={classes.boldSpan}>{venue.name}</span>
			</Typography>

			<EventSettlementRow heading>
				{[
					" ",
					"Face",
					"Rev share",
					"Online sold",
					"Total face",
					"Total rev share",
					"Total"
				]}
			</EventSettlementRow>

			{entries.map((entry, index) => {
				const {
					settlement_entry_type,
					ticket_type_name,
					face_value_in_cents,
					fee_sold_quantity,
					online_sold_quantity,
					revenue_share_value_in_cents,
					total_sales_in_cents
				} = entry;

				totalOnlineSoldQuantity =
					totalOnlineSoldQuantity + online_sold_quantity;
				totalFaceInCents =
					totalFaceInCents + face_value_in_cents * online_sold_quantity;
				totalRevShareInCents =
					totalRevShareInCents +
					revenue_share_value_in_cents * fee_sold_quantity;
				totalSalesInCents = totalSalesInCents + total_sales_in_cents;
				
				let description = ticket_type_name;
				if (
					!description &&
					settlement_entry_type &&
					settlement_entry_type !== "TicketType"
				) {
					description = splitByCamelCase(settlement_entry_type);
				}

				return (
					<EventSettlementRow
						subHeading={!!ticket_type_name}
						totalNoRadius={settlement_entry_type === "EventFees"}
						key={index}
					>
						{[
							description,
							dollars(face_value_in_cents),
							dollars(revenue_share_value_in_cents),
							online_sold_quantity,
							dollars(face_value_in_cents * online_sold_quantity),
							dollars(revenue_share_value_in_cents * fee_sold_quantity),
							dollars(total_sales_in_cents)
						]}
					</EventSettlementRow>
				);
			})}

			{/*<EventSettlementRow totalNoRadius>*/}
			{/*	{["Per order revenue share", "", "TODO", "", "", "TODO", "TODO"]}*/}
			{/*</EventSettlementRow>*/}
			<EventSettlementRow total>
				{[
					"Total",
					"",
					"",
					totalOnlineSoldQuantity,
					dollars(totalFaceInCents),
					dollars(totalRevShareInCents),
					dollars(totalSalesInCents)
				]}
			</EventSettlementRow>
		</div>
	);
};

SingleEventSettlement.propTypes = {
	classes: PropTypes.object.isRequired,
	eventDetails: PropTypes.object.isRequired,
	entries: PropTypes.array.isRequired
};

export default withStyles(styles)(SingleEventSettlement);
