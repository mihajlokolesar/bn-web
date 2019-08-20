import React from "react";
import { Typography, withStyles } from "@material-ui/core";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

import Card from "../../../../../../elements/Card";
import { dollars } from "../../../../../../../helpers/money";
import CheckBox from "../../../../../../elements/form/CheckBox";
import {
	fontFamilyDemiBold,
	secondaryHex
} from "../../../../../../../config/theme";
import ColorTag from "../../../../../../elements/ColorTag";
import ellipsis from "../../../../../../../helpers/ellipsis";

const styles = theme => ({
	root: { marginTop: 16 },
	detailsSection: {
		marginTop: 15,
		paddingTop: 20,
		paddingLeft: 20,
		paddingRight: 20
	},
	row1: {
		display: "flex",
		justifyContent: "flex-start",
		marginBottom: 16
	},
	row2: {
		display: "flex",
		marginBottom: 16
	},
	row3: {
		display: "flex",
		marginBottom: 16
	},
	rowOrderFee: {
		backgroundColor: "#f7f8fa",
		height: 46,
		display: "flex",
		alignItems: "center",
		paddingLeft: 20,
		paddingRight: 20
	},
	orderFeeRow: {
		height: 46,
		display: "flex",
		alignItems: "center",
		paddingLeft: 20,
		paddingRight: 20
	},
	orderNumberText: {
		fontSize: 18,
		paddingTop: 0,
		fontFamily: fontFamilyDemiBold,
		color: "#2c3136"
	},
	orderFeeLabelText: {
		fontSize: 14,
		paddingTop: 0,
		color: "#8b94a7"
	},
	heading: {
		textTransform: "uppercase",
		fontSize: 12,
		color: "#2c3136",
		opacity: 0.3
	},
	nameText: {
		color: secondaryHex,
		fontSize: 15,
		fontFamily: fontFamilyDemiBold
	},
	emailText: {
		fontSize: 14,
		color: "#8b94a7"
	},
	valueText: {
		fontSize: 15,
		fontFamily: fontFamilyDemiBold
	},
	subText: {
		fontSize: 13,
		color: "#8b94a7"
	},
	perTicketFeeLabel: {
		fontSize: 14,
		color: "#8b94a7"
	},
	discountValueText: {
		color: "#8b94a7"
	}
});

const MobileTicketCard = ({
	classes,
	refundable,
	total_price_in_cents,
	status,
	fees_price_in_cents,
	ticket_instance_id,
	ticket_price_in_cents,
	isChecked,
	onCheck,
	attendee_id,
	attendee_email,
	attendee_first_name,
	attendee_last_name,
	code,
	code_type,
	ticket_type_name,
	discount_price_in_cents,
	...rest
}) => {
	const checkbox = (
		<CheckBox
			size={"small"}
			variant={"white"}
			active={isChecked}
			onClick={onCheck}
			disabled={!refundable}
			labelClass={
				ticket_instance_id ? classes.orderNumberText : classes.orderFeeLabelText
			}
		>
			{ticket_instance_id ? `#${ticket_instance_id.slice(-8)}` : "Order fee"}
		</CheckBox>
	);

	if (ticket_instance_id) {
		return (
			<Card className={classes.root}>
				<div className={classes.detailsSection}>
					<div className={classes.row1}>
						{checkbox}
						<ColorTag variant={"green"}>
							{ellipsis(ticket_type_name, 12)}
						</ColorTag>
					</div>
					<div className={classes.row2}>
						<div style={{ flex: 11 }}>
							<Typography className={classes.heading}>Attendee</Typography>
							<Typography className={classes.nameText}>
								{attendee_first_name} {attendee_last_name}
							</Typography>
							<Typography className={classes.emailText}>
								{attendee_email}
							</Typography>
						</div>

						<div style={{ flex: 4 }}>
							<Typography className={classes.heading}>Status</Typography>
							<Typography className={classes.valueText}>{status}</Typography>
						</div>
					</div>
					<div className={classes.row3}>
						<div style={{ flex: 8 }}>
							<Typography className={classes.heading}>Code</Typography>
							<Typography className={classes.valueText}>
								{code || "-"}
								{discount_price_in_cents ? (
									<span className={classes.discountValueText}>
										&nbsp;/&nbsp;{dollars(discount_price_in_cents * -1, true)}
									</span>
								) : null}
							</Typography>
							<Typography className={classes.subText}>
								{code_type || ""}
							</Typography>
						</div>
						<div style={{ flex: 3 }}>
							<Typography className={classes.heading}>QTY</Typography>
							<Typography className={classes.valueText}>1</Typography>
						</div>
						<div style={{ flex: 4 }}>
							<Typography className={classes.heading}>Total</Typography>
							<Typography className={classes.valueText}>
								{dollars(total_price_in_cents - fees_price_in_cents)}
							</Typography>
						</div>
					</div>
				</div>
				<div className={classes.rowOrderFee}>
					<Typography
						style={{ flex: 11 }}
						className={classes.perTicketFeeLabel}
					>
						Per ticket fee
					</Typography>
					<Typography style={{ flex: 4 }} className={classes.valueText}>
						{dollars(fees_price_in_cents)}
					</Typography>
				</div>
			</Card>
		);
	}

	return (
		<Card className={classes.root}>
			<div className={classes.orderFeeRow}>
				<div style={{ flex: 11 }}>{checkbox}</div>
				<Typography className={classes.valueText} style={{ flex: 4 }}>
					{dollars(total_price_in_cents)}
				</Typography>
			</div>
		</Card>
	);
};

MobileTicketCard.propTypes = {
	classes: PropTypes.object.isRequired,
	onCheck: PropTypes.func.isRequired,
	isChecked: PropTypes.bool.isRequired
};

export default withStyles(styles)(MobileTicketCard);
