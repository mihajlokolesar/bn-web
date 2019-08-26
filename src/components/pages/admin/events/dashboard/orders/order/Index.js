import React, { Component } from "react";
import { Hidden, Typography, withStyles, Grid } from "@material-ui/core";
import { observer } from "mobx-react";
import PropTypes from "prop-types";

import user from "../../../../../../../stores/user";
import Loader from "../../../../../../elements/loaders/Loader";
import Card from "../../../../../../elements/Card";
import Bigneon from "../../../../../../../helpers/bigneon";
import moment from "moment-timezone";
import notifications from "../../../../../../../stores/notifications";
import Divider from "../../../../../../common/Divider";
import OrderItems from "./OrderItems";
import Header from "./Header";
import BackLink from "../../../../../../elements/BackLink";
import CreateNote from "./CreateNote";
import { fontFamilyDemiBold } from "../../../../../../../config/theme";
import RelatedOrders from "./RelatedOrders";
import OrderHistory from "./OrderHistory";

const styles = theme => ({
	root: {
		padding: 31,
		paddingLeft: 65,
		paddingRight: 65
	},
	heading: {
		fontFamily: fontFamilyDemiBold,
		fontSize: 20,
		textDecoration: "capitalize",
		marginTop: 20,
		marginBottom: 10
	}
});

class SingleOrder extends Component {
	constructor(props) {
		super(props);

		this.eventId = this.props.match.params.eventId;
		this.orderId = this.props.match.params.orderId;

		this.state = {
			eventDetails: null,
			order: null,
			orderItems: null,
			orderHistory: null,
			showMobileTicketsView: false
		};

		this.refreshOrder = this.loadAll.bind(this);
		this.loadOrderHistory = this.loadOrderHistory.bind(this);
	}

	componentDidMount() {
		this.loadAll();
	}

	loadAll() {
		this.loadEventDetails();
		this.loadOrder();
		this.loadOrderItems();
		this.loadOrderHistory();
	}

	loadEventDetails() {
		Bigneon()
			.events.read({ id: this.eventId })
			.then(response => {
				const { data } = response;

				const { event_start, venue } = data;

				const displayDate = moment(event_start)
					.tz(venue.timezone)
					.format("ddd, MMM DD, YYYY");

				this.setState({
					eventDetails: { ...data, displayDate }
				});
			})
			.catch(error => {
				console.error(error);

				notifications.showFromErrorResponse({
					defaultMessage: "Loading event details failed.",
					error
				});
			});
	}

	loadOrder() {
		Bigneon()
			.orders.read({ id: this.orderId })
			.then(response => {
				const { data } = response;
				const { date, is_box_office, items, user_id } = data;

				const { timezone } = this.props;
				const displayDate = moment(date)
					.tz(timezone)
					.format("MM/DD/YYYY h:mm A z");

				const platform = is_box_office ? "Box office" : data.platform || "";

				let fees_in_cents = 0;
				items.forEach(({ item_type, unit_price_in_cents, quantity }) => {
					//Only include fee type items
					if (
						["CreditCardFees", "PerUnitFees", "CreditCardFees"].indexOf(
							item_type
						) > -1
					) {
						fees_in_cents += unit_price_in_cents * quantity;
					}
				});

				this.setState({
					order: { ...data, displayDate, platform, fees_in_cents }
				});
			})
			.catch(error => {
				console.error(error);

				notifications.showFromErrorResponse({
					defaultMessage: "Loading order failed.",
					error
				});
			});
	}

	loadOrderItems() {
		Bigneon()
			.orders.details({ id: this.orderId })
			.then(response => {
				const { data } = response;

				this.setState({ orderItems: data.items });
			})
			.catch(error => {
				console.error(error);

				notifications.showFromErrorResponse({
					defaultMessage: "Loading order details failed.",
					error
				});
			});
	}

	loadOrderHistory() {
		const { timezone } = this.props;

		Bigneon()
			.orders.activity({
				id: this.orderId
			})
			.then(response => {
				const { data } = response.data;

				const orderHistory = data.map(item => {
					const { occurred_at } = item;
					const occurredAt = occurred_at
						? moment
							.utc(occurred_at)
							.tz(timezone)
							.format("llll")
						: "-";

					return { ...item, occurredAt };
				});

				this.setState({
					orderHistory
				});
			})
			.catch(error => {
				console.error(error);
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Failed to load order history."
				});
			});
	}

	toggleMobileTicketsView() {
		this.setState(({ showMobileTicketsView }) => {
			return { showMobileTicketsView: !showMobileTicketsView };
		});
	}

	render() {
		const {
			order,
			orderItems,
			eventDetails,
			orderHistory,
			showMobileTicketsView
		} = this.state;
		const { classes, timezone, organizationId } = this.props;

		const isReady = order && eventDetails && orderItems;

		const header = isReady ? <Header {...order}/> : null;

		const orderDetails = isReady ? (
			<OrderItems
				eventDetails={eventDetails}
				order={order}
				items={orderItems}
				refreshOrder={this.refreshOrder}
				toggleMobileTicketsView={this.toggleMobileTicketsView.bind(this)}
				showMobileTicketsView={showMobileTicketsView}
			/>
		) : null;

		if (showMobileTicketsView) {
			return (
				<React.Fragment>
					<BackLink onClick={this.toggleMobileTicketsView.bind(this)}>
						Back to order
					</BackLink>
					{orderDetails}
				</React.Fragment>
			);
		}

		return (
			<React.Fragment>
				<BackLink to={`/admin/events/${this.eventId}/dashboard/orders/manage`}>
					Back to sales
				</BackLink>

				{!isReady ? <Loader/> : null}

				{/*DESKTOP*/}
				<Hidden smDown>
					{isReady ? (
						<Card className={classes.root}>
							{header}
							<Divider style={{ marginTop: 20, marginBottom: 20 }}/>
							{orderDetails}
						</Card>
					) : null}
				</Hidden>

				{/*MOBILE*/}
				<Hidden mdUp>
					{header}
					{orderDetails}
				</Hidden>

				{isReady ? (
					<React.Fragment>
						<Typography className={classes.heading}>Order history</Typography>
						{orderHistory ? (
							<OrderHistory
								orderHistory={orderHistory}
								userId={order.user_id}
								eventDetails={eventDetails}
							/>
						) : (
							<Loader>Loading history...</Loader>
						)}

						<Typography className={classes.heading}>Add note</Typography>
						<CreateNote orderId={order.id} onSuccess={this.loadOrderHistory}/>

						<Typography className={classes.heading}>Related orders</Typography>
						<RelatedOrders
							eventId={this.eventId}
							organizationId={organizationId}
							timezone={timezone}
							user={order.user}
							eventId={eventDetails.id}
							orderId={order.id}
						/>
					</React.Fragment>
				) : null}
			</React.Fragment>
		);
	}
}

SingleOrder.propTypes = {
	classes: PropTypes.object.isRequired,
	match: PropTypes.object.isRequired,
	timezone: PropTypes.string.isRequired,
	organizationId: PropTypes.string.isRequired
};

const Index = observer(props => {
	const { currentOrgTimezone, currentOrganizationId } = user;

	if (!currentOrgTimezone) {
		return <Loader>Loading organization details...</Loader>;
	}

	return (
		<SingleOrder
			{...props}
			timezone={currentOrgTimezone}
			organizationId={currentOrganizationId}
		/>
	);
});

export default withStyles(styles)(Index);
