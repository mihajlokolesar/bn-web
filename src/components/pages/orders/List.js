import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import moment from "moment";
import { observer } from "mobx-react";

import OrderRow from "./OrderRow";
import StyledLink from "../../elements/StyledLink";
import PageHeading from "../../elements/PageHeading";
import orders from "../../../stores/orders";
import Loader from "../../elements/loaders/Loader";
import Order from "./Order";
import PropTypes from "prop-types";

const styles = theme => {
	return {
		paragraph: {
			[theme.breakpoints.down("xs")]: {
				fontSize: "0.6rem"
			}
		}
	};
};

@observer
class OrderList extends Component {
	constructor(props) {
		super(props);
	}

	componentDidMount() {
		orders.refreshOrders();
	}

	renderOrders() {
		const { classes } = this.props;
		const { items, orderCount } = orders;

		if (items === null) {
			return <Loader/>;
		}

		if (orderCount > 0) {
			return (
				<div>
					<OrderRow>
						<Typography className={classes.paragraph} variant="subheading">Date</Typography>
						<Typography className={classes.paragraph}>Order # </Typography>
						<Typography className={classes.paragraph}>Event</Typography>
						<Typography className={classes.paragraph}>Tickets</Typography>
						<Typography className={classes.paragraph}>Total</Typography>
					</OrderRow>
					{items.map(order => {
						const { id, date, total_in_cents, items } = order;

						const formattedDate = moment
							.utc(date, moment.HTML5_FMT.DATETIME_LOCAL_MS)
							.format("MM/DD/YYYY");

						let ticketCount = 0;
						let eventName = ""; //TODO get this when available in the API
						const orderNumber = id.slice(-8); //TODO eventually this will also come in the API
						items.forEach(item => {
							const { item_type, quantity, description } = item;

							if (item_type === "Tickets") {
								ticketCount = ticketCount + quantity;
								if (!eventName) {
									eventName = description;
								}
							}
						});

						return (
							<OrderRow item key={id}>
								<Typography className={classes.paragraph}>{formattedDate}</Typography>
								<Typography className={classes.paragraph}>
									<StyledLink underlined to={`/orders/${id}`}>
										{orderNumber}
									</StyledLink>
								</Typography>
								<Typography className={classes.paragraph}>{eventName}</Typography>
								<Typography className={classes.paragraph}>{ticketCount}</Typography>
								<Typography className={classes.paragraph}>$ {(total_in_cents / 100).toFixed(2)}</Typography>
							</OrderRow>
						);
					})}
				</div>
			);
		} else {
			return <Typography variant="body1">No orders yet</Typography>;
		}
	}

	render() {
		return (
			<div>
				<PageHeading iconUrl="/icons/chart-multi.svg">My orders</PageHeading>
				{this.renderOrders()}
			</div>
		);
	}
}

OrderList.propTypes = {
	classes: PropTypes.object.isRequired
};

export default withStyles(styles)(OrderList);
