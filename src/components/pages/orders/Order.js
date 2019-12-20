import React, { Component } from "react";
import { Typography, withStyles, Grid } from "@material-ui/core";
import moment from "moment";

import notifications from "../../../stores/notifications";
import Bigneon from "../../../helpers/bigneon";
import PageHeading from "../../elements/PageHeading";
import layout from "../../../stores/layout";
import Card from "../../elements/Card";
import { fontFamilyDemiBold, fontFamily } from "../../../config/theme";
import AppPromoCard from "../../elements/AppPromoCard";
import Divider from "../../common/Divider";
import Loader from "../../elements/loaders/Loader";
import servedImage from "../../../helpers/imagePathHelper";

const styles = theme => ({
	root: {},
	content: {
		padding: theme.spacing.unit * 8,
		[theme.breakpoints.down("xs")]: {
			padding: theme.spacing.unit * 2
		}
	},
	cardHeader: {
		display: "flex",
		justifyContent: "space-between"
	},
	logo: {
		width: 80,
		height: 80,
		[theme.breakpoints.down("xs")]: {
			width: "100%",
			height: "100%",
			maxWidth: 40
		}
	},
	orderNumber: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.7,
		[theme.breakpoints.down("xs")]: {
			fontSize: theme.typography.fontSize * 1.1
		}
	},
	purchasedOn: {
		fontFamily: fontFamilyDemiBold,
		textTransform: "uppercase",
		fontSize: theme.typography.fontSize * 0.9,
		marginBottom: theme.spacing.unit * 6,
		[theme.breakpoints.down("xs")]: {
			marginBottom: theme.spacing.unit * 3
		}
	},
	date: {
		textTransform: "none",
		fontFamily: fontFamily
	},
	eventName: {
		maxWidth: 600,
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 2,
		marginBottom: theme.spacing.unit * 6,
		[theme.breakpoints.down("xs")]: {
			fontSize: theme.typography.fontSize * 1.2,
			marginBottom: theme.spacing.unit * 3
		}
	},
	itemHeading: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 0.9
	},
	item: {
		fontSize: theme.typography.fontSize * 0.9
	}
});

const LineEntry = ({ col1, col2, col3, col4 }) => (
	<Grid alignItems="center" container>
		<Grid item xs={3} sm={2} md={2} lg={2} style={{ textAlign: "center" }}>
			{col1}
		</Grid>

		<Grid item xs={3} sm={6} md={6} lg={6}>
			{col2}
		</Grid>

		<Grid item xs={3} sm={2} md={2} lg={2} style={{ textAlign: "right" }}>
			{col3}
		</Grid>

		<Grid item xs={3} sm={2} md={2} lg={2} style={{ textAlign: "right" }}>
			{col4}
		</Grid>
	</Grid>
);

class Order extends Component {
	constructor(props) {
		super(props);

		this.state = {
			order: null
		};
	}

	componentDidMount() {
		if (
			this.props.match &&
			this.props.match.params &&
			this.props.match.params.id
		) {
			const { id } = this.props.match.params;
			Bigneon()
				.orders.read({ id })
				.then(response => {
					const { data } = response;
					this.setState({ order: data });
				})
				.catch(error => {
					console.error(error);
					notifications.showFromErrorResponse({
						defaultMessage: "Loading order details failed.",
						error
					});
				});
		} else {
			//TODO return 404
		}
	}

	renderStatus() {
		const { classes } = this.props;
		const { order } = this.state;
		const { paid_at, status } = order;

		if (!order) {
			return null;
		}

		if (paid_at) {
			return (
				<Typography className={classes.purchasedOn}>
					Purchased on:{" "}
					<span className={classes.date}>
						{moment
							.utc(paid_at, moment.HTML5_FMT.DATETIME_LOCAL_MS)
							.format("ddd MM/DD/YY, h:mm A z")}
					</span>
				</Typography>
			);
		}

		//Options are: Paid, Draft, PendingPayment, Cancelled
		return (
			<Typography className={classes.purchasedOn}>
				Status:{" "}
				<span className={classes.date}>
					{status === "PendingPayment" ? "Pending payment" : status}
				</span>
			</Typography>
		);
	}

	render() {
		const { classes } = this.props;
		const { order } = this.state;

		if (!order) {
			return <Loader/>;
		}

		const { id, paid_at, status, total_in_cents, items } = order;

		//We can only get the event name from fields in the items array. Append a list if there's more than one.
		let eventName = "";
		let fee_total_in_cents = 0;

		const listItems = [];

		if (items) {
			items.forEach(item => {
				const { description, item_type, unit_price_in_cents, quantity } = item;

				if (
					item_type === "Fees" ||
					item_type === "EventFees" ||
					item_type === "PerUnitFees" ||
					item_type === "CreditCardFees"
				) {
					fee_total_in_cents =
						fee_total_in_cents + unit_price_in_cents * quantity;
				} else {
					listItems.push(item);

					if (item_type === "Tickets") {
						if (eventName === "") {
							eventName = description;
						} else {
							eventName = `${eventName}, ${description}`;
						}
					}
				}
			});
		}

		const orderNumber = id.slice(-8); //TODO eventually this will also come in the API

		return (
			<div>
				<PageHeading iconUrl="/icons/chart-multi.svg">
					Order details
				</PageHeading>

				<Card variant="form" topBorderHighlight>
					<div className={classes.content}>
						<div className={classes.cardHeader}>
							<div>
								<Typography className={classes.orderNumber}>
									Order #{orderNumber}
								</Typography>
								{this.renderStatus()}
							</div>
							<img
								src={servedImage("/images/bn-logo.png")}
								className={classes.logo}
							/>
						</div>

						<Typography className={classes.eventName}>{eventName}</Typography>

						<LineEntry
							col1={(
								<Typography className={classes.itemHeading}>
									Quantity
								</Typography>
							)}
							col2={(
								<Typography className={classes.itemHeading}>
									Ticket type
								</Typography>
							)}
							col3={
								<Typography className={classes.itemHeading}>Price</Typography>
							}
							col4={(
								<Typography className={classes.itemHeading}>
									Subtotal
								</Typography>
							)}
						/>
						<Divider style={{ marginBottom: 15 }}/>

						{listItems.map(item => {
							const { id, quantity, unit_price_in_cents, description } = item;

							return (
								<div key={id}>
									<LineEntry
										col1={(
											<Typography className={classes.item}>
												{quantity}
											</Typography>
										)}
										col2={(
											<Typography className={classes.item}>
												{description}
											</Typography>
										)}
										col3={(
											<Typography className={classes.item}>
												{(unit_price_in_cents / 100).toFixed(2)}
											</Typography>
										)}
										col4={(
											<Typography className={classes.item}>
												{((unit_price_in_cents / 100) * quantity).toFixed(2)}
											</Typography>
										)}
									/>
									<Divider style={{ marginBottom: 15 }}/>
								</div>
							);
						})}

						<LineEntry
							col1={null}
							col2={null}
							col3={(
								<Typography className={classes.itemHeading}>
									Service fees
								</Typography>
							)}
							col4={(
								<Typography className={classes.itemHeading}>
									$ {(fee_total_in_cents / 100).toFixed(2)}
								</Typography>
							)}
						/>

						<div style={{ marginTop: 20 }}/>

						<LineEntry
							col1={null}
							col2={null}
							col3={(
								<Typography className={classes.itemHeading}>
									Order total
								</Typography>
							)}
							col4={(
								<Typography className={classes.itemHeading}>
									$ {(total_in_cents / 100).toFixed(2)}
								</Typography>
							)}
						/>

						<AppPromoCard style={{ marginTop: 80 }}/>
					</div>
				</Card>
			</div>
		);
	}
}

export default withStyles(styles)(Order);
