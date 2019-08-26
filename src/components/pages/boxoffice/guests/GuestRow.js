import React from "react";
import PropTypes from "prop-types";
import { withStyles, Typography, Collapse, Hidden } from "@material-ui/core";

import Card from "../../../elements/Card";
import { fontFamilyDemiBold, secondaryHex } from "../../../../config/theme";
import GuestTicketRow from "./GuestTicketRow";
import CheckBox from "../../../elements/form/CheckBox";
import { dollars } from "../../../../helpers/money";
import StyledLink from "../../../elements/StyledLink";
import servedImage from "../../../../helpers/imagePathHelper";

const styles = theme => ({
	root: {
		marginBottom: theme.spacing.unit / 2
	},
	header: {
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
		color: "#9DA3B4",
		maxWidth: "90vw"
	},
	leftContent: {},
	rightContent: {
		display: "flex",
		alignItems: "center"
	},
	paymentRequiredIcon: {
		width: "auto",
		height: 30,
		marginRight: theme.spacing.unit * 2
	},
	topRow: {
		display: "flex"
	},
	bottomRow: {
		display: "flex"
	},
	expandIcon: {
		height: 10
	},
	ticketContainerOuter: {
		display: "flex"
	},
	ticketContainerInner: {
		padding: theme.spacing.unit * 2,
		flex: 1,

		[theme.breakpoints.down("sm")]: {
			padding: 0,
			paddingLeft: theme.spacing.unit,
			width: 320 - theme.spacing.unit * 4,
			overflowX: "scroll",
			overflowY: "hidden",
			whiteSpace: "nowrap"
		}
	}
});

const DiscountIcon = ({ classes }) => (
	<img
		className={classes.paymentRequiredIcon}
		alt={"Payment required"}
		src={servedImage("/icons/sales-active.svg")}
	/>
);

const ticketIdsString = (tickets, isMobile) => {
	const subheadingMobileCharLimit = 38;

	let subHeading = tickets
		.map(({ id }) => {
			return `#${id.slice(-8)}`;
		})
		.join(", ");

	if (isMobile && subHeading.length > subheadingMobileCharLimit) {
		subHeading = `${subHeading.substring(0, subheadingMobileCharLimit)}...`;
	}

	return subHeading;
};

const GuestRow = props => {
	const {
		index,
		type,
		rowKey,
		email,
		name,
		phone,
		hold_type,
		tickets,
		available,
		onExpandChange,
		expanded,
		onSelect,
		selectedTickets,
		selectedHolds,
		ticketTypeName,
		discountedPriceInCents,
		classes
	} = props;

	let ticketHeadings = [];
	let ticketRows = [];

	if (type === "guest") {
		ticketHeadings = [
			" ",
			"Ticket #",
			"Order #",
			"Ticket type",
			"Price",
			"Status"
		];

		ticketRows = tickets.map(ticket => {
			const {
				id,
				ticket_type,
				price_in_cents,
				order_id,
				status,
				event_id
			} = ticket;
			const isPurchased = status === "Purchased";

			return (
				<GuestTicketRow key={id}>
					<span>
						{isPurchased ? (
							<CheckBox
								active={!!selectedTickets[id]}
								onClick={() => onSelect(ticket)}
							/>
						) : null}
					</span>
					<Typography>{id.slice(-8)}</Typography>
					<Typography>
						{order_id ? (
							<StyledLink
								underlined
								href={`/admin/events/${event_id}/dashboard/orders/manage/${order_id}`}
								target="_blank"
							>
								{order_id.slice(-8)}
							</StyledLink>
						) : (
							"-"
						)}
					</Typography>
					<Typography>{ticket_type}</Typography>
					<Typography>$ {(price_in_cents / 100).toFixed(2)}</Typography>
					<Typography>{status}</Typography>
				</GuestTicketRow>
			);
		});
	}

	if (type === "hold") {
		ticketHeadings = [" ", "Ticket #", "Hold", "Ticket type", "Price", ""];

		for (let index = 0; index < available; index++) {
			let price = "";
			if (hold_type === "Comp") {
				price = "Free";
			} else if (hold_type === "Discount" && discountedPriceInCents) {
				price = dollars(discountedPriceInCents);
			}

			ticketRows.push(
				<GuestTicketRow key={index}>
					<span>
						<CheckBox
							active={!!selectedHolds[index]}
							onClick={() => onSelect(index)}
						/>
					</span>
					<Typography>{rowKey.slice(-8)}</Typography>
					<Typography>{name}</Typography>
					<Typography>{ticketTypeName}</Typography>
					<Typography>{price}</Typography>
					{hold_type === "Discount" ? <DiscountIcon classes={classes}/> : null}
				</GuestTicketRow>
			);
		}
	}

	return (
		<div className={classes.root}>
			<Card>
				<div
					className={classes.header}
					onClick={() => onExpandChange(expanded ? null : rowKey)}
				>
					<div className={classes.leftContent}>
						<div className={classes.topRow}>
							<Typography className={classes.indexNumber}>
								{index + 1}.
							</Typography>
							<Typography className={classes.name}>{name}</Typography>
						</div>
						<div className={classes.bottomRow}>
							{type === "guest" ? (
								<span>
									<Hidden smDown>
										<Typography noWrap className={classes.ticketInfo}>
											{ticketIdsString(tickets, false)}
										</Typography>
									</Hidden>
									<Hidden mdUp>
										<Typography noWrap className={classes.ticketInfo}>
											{ticketIdsString(tickets, true)}
										</Typography>
									</Hidden>
								</span>
							) : (
								<Typography noWrap className={classes.ticketInfo}>
									{hold_type}
								</Typography>
							)}
						</div>
					</div>
					<div className={classes.rightContent}>
						{hold_type === "Discount" ? (
							<DiscountIcon classes={classes}/>
						) : null}
						<img
							alt="Expand icon"
							className={classes.expandIcon}
							src={servedImage(
								expanded ? "/icons/up-active.svg" : "/icons/down-active.svg"
							)}
						/>
					</div>
				</div>

				<Collapse in={expanded}>
					<div className={classes.ticketContainerOuter}>
						<div className={classes.ticketContainerInner}>
							<GuestTicketRow heading>
								{ticketHeadings.map((heading, index) => (
									<span key={index}>{heading}</span>
								))}
							</GuestTicketRow>
							{ticketRows}
						</div>
					</div>
				</Collapse>
			</Card>
		</div>
	);
};

GuestRow.propTypes = {
	classes: PropTypes.object.isRequired,
	type: PropTypes.oneOf(["guest", "hold"]).isRequired,
	rowKey: PropTypes.string.isRequired,
	onExpandChange: PropTypes.func.isRequired,
	expanded: PropTypes.bool.isRequired,
	onSelect: PropTypes.func.isRequired,
	name: PropTypes.string.isRequired,

	//Used for type=guest
	tickets: (props, propName, componentName) => {
		if (props.type === "guest" && !props[propName]) {
			return new Error(
				`Invalid prop '${propName}' supplied to '${componentName}'. If type='guest' then '${propName}' is required`
			);
		}
	},
	selectedTickets: (props, propName, componentName) => {
		if (props.type === "guest" && !props[propName]) {
			return new Error(
				`Invalid prop '${propName}' supplied to '${componentName}'. If type='guest' then '${propName}' is required`
			);
		}
	},

	//Used for type=hold
	available: (props, propName, componentName) => {
		if (props.type === "hold" && isNaN(props[propName])) {
			return new Error(
				`Invalid prop '${propName}' supplied to '${componentName}'. If type='hold' then '${propName}' number is required`
			);
		}
	},
	hold_type: (props, propName, componentName) => {
		if (
			props.type === "hold" &&
			props[propName] !== "Discount" &&
			props[propName] !== "Comp"
		) {
			return new Error(
				`Invalid prop '${propName}' supplied to '${componentName}'. If type='hold' then '${propName}' must be either 'Discount' or 'Comp'`
			);
		}
	},
	ticketTypeName: (props, propName, componentName) => {
		if (props.type === "hold" && !props[propName]) {
			return new Error(
				`Missing prop '${propName}' for '${componentName}'. If type='hold' then '${propName}' is required`
			);
		}
	},
	discountedPriceInCents: (props, propName, componentName) => {
		if (
			props.type === "hold" &&
			props.hold_type === "Discount" &&
			isNaN(props[propName])
		) {
			return new Error(
				`Invalid prop '${propName}' supplied to '${componentName}'. Requires a number when hold_type===Discount`
			);
		}
	},
	selectedHolds: (props, propName, componentName) => {
		if (props.type === "hold" && !props[propName]) {
			return new Error(
				`Invalid prop '${propName}' supplied to '${componentName}'. If type='guest' then '${propName}' is required`
			);
		}
	}
};

export default withStyles(styles)(GuestRow);
