import Hidden from "@material-ui/core/Hidden";
import { Typography } from "@material-ui/core";
import user from "../../../stores/user";
import { dollars } from "../../../helpers/money";
import React from "react";
import cart from "../../../stores/cart";
import removeCountryFromAddress from "../../../helpers/removeCountryFromAddress";

const PurchaseDetails = ({
	classes,
	event,
	venue,
	order,
	displayEventStartDate
}) => {
	const items = order.items;
	let allFees = 0;
	let subTotal = 0;
	let discountForItem;
	items.forEach(item => {
		if (
			item.item_type === "PerUnitFees" ||
			item.item_type === "EventFees" ||
			item.item_type === "CreditCardFees"
		) {
			allFees = allFees + item.unit_price_in_cents * item.quantity;
		}
	});
	return (
		<div className={classes.purchaseInfoBlock}>
			<Hidden mdDown>
				<div className={classes.purchaseInfo}>
					<Typography className={classes.boldText}>{event.name}</Typography>
					<Typography className={classes.boldText}>
						{order.order_number}
					</Typography>
				</div>
				<div className={classes.purchaseInfo}>
					<Typography className={classes.purchaseText}>
						{displayEventStartDate}
					</Typography>
					<Typography className={classes.greyTitleDemiBold}>
						Order no.
					</Typography>
				</div>
			</Hidden>
			<Hidden smUp>
				<Typography className={classes.greyTitleDemiBold}>Order no.</Typography>
				<Typography className={classes.mobiBoldOrder}>
					{order.order_number}
				</Typography>
				<br/>
				<Typography className={classes.boldText}>{event.name}</Typography>
				<Typography className={classes.purchaseText}>
					{displayEventStartDate}
				</Typography>
			</Hidden>
			<br/>
			<Typography className={classes.boldText}>{venue.name}</Typography>
			<Typography className={classes.purchaseText}>
				{removeCountryFromAddress(venue.address)}
			</Typography>
			<div className={classes.divider}/>
			<Typography className={classes.greyTitleDemiBold}>Purchaser</Typography>
			<Typography className={classes.boldText}>
				{user.firstName} {user.lastName}
			</Typography>
			<div className={classes.purchaseInfo}>{user.email}</div>
			<br/>
			<Hidden mdDown>
				<div className={classes.purchaseInfo}>
					<div className={classes.leftColumn}>
						<Typography className={classes.greyTitleDemiBold}>
							Ticket type
						</Typography>
					</div>
					<div className={classes.rightColumn}>
						<Typography className={classes.greyTitleDemiBold}>
							Ticket price
						</Typography>
						<Typography className={classes.greyTitleDemiBold}>Qty</Typography>{" "}
						<Typography className={classes.greyTitleDemiBold}>
							Ticket total
						</Typography>
					</div>
				</div>
			</Hidden>
			{items
				? items.map((item, index) => {
					let ticketSubTotal = 0;
					if (item.item_type !== "Tickets") {
						return null;
					}
					let ticketPrice = item.unit_price_in_cents;
					if (item.item_type === "Tickets") {
						discountForItem = items.find(
							disc =>
								disc.item_type === "Discount" && disc.parent_id === item.id
						);
					}
					if (discountForItem) {
						ticketPrice = ticketPrice + discountForItem.unit_price_in_cents;
					}
					ticketSubTotal = ticketSubTotal + ticketPrice * item.quantity;
					subTotal = subTotal + ticketSubTotal;

					let ticketName = item.description;
					if (ticketName.includes(event.name)) {
						ticketName = ticketName.replace(`${event.name} - `, "");
					}

					return (
						<div key={index}>
							<Hidden mdDown>
								<div className={classes.purchaseInfo}>
									<div className={classes.leftColumn}>
										<Typography className={classes.purchaseTicketText}>
											{ticketName}
										</Typography>
									</div>
									<div className={classes.rightColumn}>
										<Typography className={classes.purchaseTicketText}>
											{dollars(ticketPrice)}
										</Typography>{" "}
										<Typography className={classes.purchaseTicketText}>
											{item.quantity}
										</Typography>{" "}
										<Typography className={classes.purchaseTicketText}>
											{dollars(ticketSubTotal)}
										</Typography>
									</div>
								</div>
							</Hidden>
							<Hidden smUp>
								<Typography className={classes.greyTitleDemiBold}>
										Ticket type
								</Typography>
								<Typography className={classes.purchaseTicketText}>
									{ticketName}
								</Typography>
								<br/>
								<div className={classes.purchaseInfo}>
									<Typography className={classes.greyTitleDemiBold}>
											Ticket price
									</Typography>
									<Typography className={classes.greyTitleDemiBold}>
											Qty
									</Typography>
									<Typography className={classes.greyTitleDemiBold}>
											Ticket total
									</Typography>
								</div>
								<div className={classes.purchaseInfo}>
									<Typography className={classes.purchaseTicketText}>
										{dollars(ticketPrice)}
									</Typography>{" "}
									<Typography className={classes.purchaseTicketText}>
										{item.quantity}
									</Typography>{" "}
									<Typography className={classes.purchaseTicketText}>
										{dollars(ticketSubTotal)}
									</Typography>
								</div>
								<br/>
								<div className={classes.divider}/>
							</Hidden>
						</div>
					);
				  })
				: null}
			<Hidden mdDown>
				<div className={classes.divider}/>
			</Hidden>
			<div className={classes.purchaseInfo}>
				<Typography className={classes.greyTitleDemiBold}>Subtotal</Typography>
				<Typography className={classes.greyTitleDemiBold}>
					{dollars(subTotal)}
				</Typography>
			</div>
			<br/>
			<div className={classes.purchaseInfo}>
				<Typography className={classes.greyTitleDemiBold}>
					Fees Total
				</Typography>
				<Typography className={classes.greyTitleDemiBold}>
					{dollars(allFees)}
				</Typography>
			</div>
			<div className={classes.divider}/>
			<div className={classes.purchaseInfo}>
				<Typography className={classes.orderTotalTitle}>Order Total</Typography>
				<Typography className={classes.orderTotalValue}>
					{dollars(order.total_in_cents)}
				</Typography>
			</div>
		</div>
	);
};

export default PurchaseDetails;
