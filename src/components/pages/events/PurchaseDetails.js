import Hidden from "@material-ui/core/Hidden";
import { Typography } from "@material-ui/core";
import user from "../../../stores/user";
import { dollars } from "../../../helpers/money";
import React from "react";

const PurchaseDetails = ({
	classes,
	event,
	venue,
	order,
	displayEventStartDate
}) => {
	const items = order.items;
	let subTotal = 0;
	let allFees = 0;
	items.forEach(item => {
		if (item.item_type === "PerUnitFees" || item.item_type === "EventFees") {
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
			<Typography className={classes.purchaseText}>{venue.address}</Typography>
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
					if (item.item_type !== "Tickets") {
						return null;
					}
					subTotal = subTotal + item.unit_price_in_cents * item.quantity;
					return (
						<div key={index}>
							<Hidden mdDown>
								<div className={classes.purchaseInfo}>
									<div className={classes.leftColumn}>
										<Typography className={classes.purchaseTicketText}>
											{item.description}
										</Typography>
									</div>
									<div className={classes.rightColumn}>
										<Typography className={classes.purchaseTicketText}>
											{dollars(item.unit_price_in_cents)}
										</Typography>{" "}
										<Typography className={classes.purchaseTicketText}>
											{item.quantity}
										</Typography>{" "}
										<Typography className={classes.purchaseTicketText}>
											{dollars(item.unit_price_in_cents * item.quantity)}
										</Typography>
									</div>
								</div>
							</Hidden>
							<Hidden smUp>
								<Typography className={classes.greyTitleDemiBold}>
										Ticket type
								</Typography>
								<Typography className={classes.purchaseTicketText}>
									{item.description}
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
										{dollars(item.unit_price_in_cents)}
									</Typography>{" "}
									<Typography className={classes.purchaseTicketText}>
										{item.quantity}
									</Typography>{" "}
									<Typography className={classes.purchaseTicketText}>
										{dollars(item.unit_price_in_cents * item.quantity)}
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
