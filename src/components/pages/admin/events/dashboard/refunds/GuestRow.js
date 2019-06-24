import React from "react";
import PropTypes from "prop-types";
import { withStyles, Typography, Collapse } from "@material-ui/core";

import Card from "../../../../../elements/Card";
import {
	fontFamilyDemiBold,
	secondaryHex
} from "../../../../../../config/theme";
import GuestTicketRow from "./GuestTicketRow";
import CheckBox from "../../../../../elements/form/CheckBox";
import servedImage from "../../../../../../helpers/imagePathHelper";
import StyledLink from "../../../../../elements/StyledLink";

const styles = theme => ({
	root: {
		marginBottom: theme.spacing.unit / 2
	},
	inner: {
		paddingLeft: theme.spacing.unit * 2,
		paddingRight: theme.spacing.unit * 2,
		paddingTop: theme.spacing.unit * 1.5,
		paddingBottom: theme.spacing.unit,
		display: "flex",
		alignItems: "center",
		justifyContent: "space-between",
		cursor: "pointer"
	},
	indexNumber: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.2,
		color: secondaryHex
	},
	name: {
		marginLeft: theme.spacing.unit / 2,
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.2
	},
	ticketInfo: {
		color: "#9DA3B4"
	},
	leftContent: {},
	rightContent: {},
	topRow: {
		display: "flex"
	},
	bottomRow: {
		display: "flex"
	},
	expandContainer: {
		cursor: "pointer",
		height: "100%"
	},
	expandIcon: {
		height: 10
	},
	ticketContainer: {
		padding: theme.spacing.unit * 2
	}
});

const GuestRow = props => {
	const {
		index,
		userId,
		email,
		first_name,
		last_name,
		phone,
		tickets,
		onExpandChange,
		expanded,
		onTicketSelect,
		selectedTickets,
		onCancelTransfer,
		classes
	} = props;

	let displayName = `Guest (No Details Provided)`;
	if (first_name && last_name) {
		displayName = `${last_name}, ${first_name}`;
	} else if (first_name) {
		displayName = first_name;
	}

	return (
		<div className={classes.root}>
			<Card>
				<div
					onClick={() => onExpandChange(expanded ? null : userId)}
					className={classes.inner}
				>
					<div className={classes.leftContent}>
						<div className={classes.topRow}>
							<Typography className={classes.indexNumber}>
								{index + 1}.
							</Typography>
							<Typography className={classes.name}>{displayName}</Typography>
						</div>
						<div className={classes.bottomRow}>
							<Typography className={classes.ticketInfo}>
								{tickets.map(({ id }) => {
									return <span key={id}>#{id.slice(-8)}. </span>;
								})}
							</Typography>
						</div>
					</div>
					<div className={classes.rightContent}>
						<div className={classes.expandContainer}>
							<img
								alt="Expand icon"
								className={classes.expandIcon}
								src={servedImage(
									expanded ? "/icons/up-active.svg" : "/icons/down-active.svg"
								)}
							/>
						</div>
					</div>
				</div>

				<Collapse in={expanded}>
					<div className={classes.ticketContainer}>
						<GuestTicketRow heading>
							<span>&nbsp;</span>
							<span>Ticket #</span>
							<span>Order #</span>
							<span>Ticket type</span>
							<span>Price</span>
							<span>Status</span>
							<span>Transfer</span>
						</GuestTicketRow>

						{tickets.map(ticket => {
							const {
								id,
								ticket_type,
								price_in_cents,
								order_id,
								status,
								transfer_key
							} = ticket;
							const isPurchased = status === "Purchased";

							return (
								<GuestTicketRow key={id}>
									<span>
										{isPurchased ? (
											<CheckBox
												active={!!selectedTickets[id]}
												onClick={() => onTicketSelect(ticket)}
											/>
										) : null}
									</span>
									<Typography>{id.slice(-8)}</Typography>
									<Typography>{order_id ? order_id.slice(-8) : "-"}</Typography>
									<Typography>{ticket_type}</Typography>
									<Typography>$ {(price_in_cents / 100).toFixed(2)}</Typography>
									<Typography>{status}</Typography>
									{transfer_key ? (
										<StyledLink
											underlined
											onClick={() => onCancelTransfer(transfer_key)}
										>
											cancel transfer
										</StyledLink>
									) : (
										<span>-</span>
									)}
								</GuestTicketRow>
							);
						})}
					</div>
				</Collapse>
			</Card>
		</div>
	);
};

GuestRow.propTypes = {
	classes: PropTypes.object.isRequired,
	userId: PropTypes.string.isRequired,
	first_name: PropTypes.string,
	last_name: PropTypes.string,
	tickets: PropTypes.array.isRequired,
	onExpandChange: PropTypes.func.isRequired,
	expanded: PropTypes.bool.isRequired,
	onTicketSelect: PropTypes.func.isRequired,
	selectedTickets: PropTypes.object.isRequired,
	onCancelTransfer: PropTypes.func.isRequired
};

export default withStyles(styles)(GuestRow);
