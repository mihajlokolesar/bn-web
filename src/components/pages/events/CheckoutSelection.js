import React, { Component } from "react";
import { withStyles } from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import Hidden from "@material-ui/core/Hidden";
import { observer } from "mobx-react";
import PropTypes from "prop-types";
import moment from "moment-timezone";
import { Redirect, Link } from "react-router-dom";

import OrgAnalytics from "../../common/OrgAnalytics";
import Button from "../../elements/Button";
import notifications from "../../../stores/notifications";
import TicketSelection from "./TicketSelection";
import selectedEvent from "../../../stores/selectedEvent";
import cart from "../../../stores/cart";
import user from "../../../stores/user";
import EventHeaderImage from "../../elements/event/EventHeaderImage";
import { fontFamilyDemiBold, secondaryHex } from "../../../config/theme";
import EventDetailsOverlayCard from "../../elements/event/EventDetailsOverlayCard";
import InputWithButton from "../../common/form/InputWithButton";
import Meta from "./Meta";
import Loader from "../../elements/loaders/Loader";
import PrivateEventDialog from "./PrivateEventDialog";
import optimizedImageUrl from "../../../helpers/optimizedImageUrl";
import Divider from "../../common/Divider";
import TwoColumnLayout from "./TwoColumnLayout";
import EventDescriptionBody from "./EventDescriptionBody";
import getUrlParam from "../../../helpers/getUrlParam";
import analytics from "../../../helpers/analytics";
import getAllUrlParams from "../../../helpers/getAllUrlParams";
import ellipsis from "../../../helpers/ellipsis";
import FormattedAdditionalInfo from "./FormattedAdditionalInfo";

const AUTO_SELECT_TICKET_AMOUNT = 2;

const styles = theme => ({
	root: {},
	desktopContent: {
		backgroundColor: "#FFFFFF"
	},
	desktopCardContent: {
		padding: theme.spacing.unit * 2
	},
	mobileContainer: {
		background: "#FFFFFF"
	},
	mobileTicketSelectionContainer: {
		padding: theme.spacing.unit * 2,
		paddingBottom: theme.spacing.unit * 10
	},
	mobileHeading: {
		fontSize: theme.typography.fontSize * 1.25,
		fontFamily: fontFamilyDemiBold,
		marginTop: theme.spacing.unit,
		marginBottom: theme.spacing.unit
	},
	mobileEventDetailsContainer: {
		padding: theme.spacing.unit * 1.5,
		display: "flex",
		alignItems: "center",
		boxShadow: "0px 2px 10px 0px rgba(157, 163, 180, 0.2)"
	},
	mobilePromoImage: {
		width: 70,
		height: 70,
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundPosition: "center",
		marginRight: theme.spacing.unit,
		borderRadius: 6
	},
	mobileEventName: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 0.875,
		color: "#2c3136",
		overflow: "hidden",
		textOverflow: "ellipsis"
	},
	mobileVenueName: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 0.8125,
		color: "#9da3b4"
	},
	mobileEventTime: {
		fontSize: theme.typography.fontSize * 0.8125,
		color: "#9da3b4"
	},
	mobileViewDetailsLinkText: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 0.75,
		color: secondaryHex
	}
});

@observer
class CheckoutSelection extends Component {
	constructor(props) {
		super(props);

		this.eventId = this.props.match.params.id;

		this.state = {
			errors: {},
			ticketSelection: null,
			isSubmitting: false,
			isSubmittingPromo: false,
			overlayCardHeight: 600
		};
	}

	componentDidMount() {
		const code = getUrlParam("code");
		code ? (selectedEvent.currentlyAppliedCode = code) : null;

		//If we have a current cart in the store already, load that right away
		if (cart.items && cart.items.length > 0) {
			this.setTicketSelectionFromExistingCart(cart.items);
		} else {
			//Else if we don't have any items in the cart, refresh to make sure
			cart.refreshCart(
				() => {
					this.setTicketSelectionFromExistingCart(cart.items);
				},
				error => {
					//If they're not logged in, assume an empty cart
					if (user.isAuthenticated) {
						notifications.showFromErrorResponse({
							defaultMessage: "Failed add to existing cart items.",
							error
						});
					}

					if (!this.state.ticketSelection) {
						this.setState({ ticketSelection: {} });
					}
				}
			);
		}

		if (this.eventId) {
			selectedEvent.refreshResult(
				this.eventId,
				errorMessage => {
					notifications.show({
						message: errorMessage,
						variant: "error"
					});
				},
				() => {
					const { id: selectedEventId } = selectedEvent.event;

					analytics.viewContent([selectedEventId], getAllUrlParams());
				}
			);
		} else {
			//TODO return 404
		}
	}

	//Determine the amount to auto add to cart based on increment, limit per person and available tickets
	getAutoAddQuantity(index, ticketTypes) {
		//Check if this ticket type is the only available one. If user has more than one option don't auto select.
		let otherAvailableTickets = false;
		ticketTypes.forEach((tt, ttIndex) => {
			if (ttIndex !== index && tt.status === "Published") {
				otherAvailableTickets = true;
			}
		});

		if (otherAvailableTickets) {
			return 0;
		}

		const { increment, limit_per_person, available, status } = ticketTypes[index];

		//Check that the status of the ticket we
		if (status !== "Published") {
			return 0;
		}

		let quantity = AUTO_SELECT_TICKET_AMOUNT;

		//If the default auto select amount is NOT divisible by the increment amount, rather auto select the first increment
		if (AUTO_SELECT_TICKET_AMOUNT % increment != 0) {
			quantity = increment;
		}

		//If limit_per_person is set don't allow auto selecting more than the user is allowed to buy
		if (limit_per_person && quantity > limit_per_person) {
			quantity = limit_per_person;
		}

		//Will first display `Sold out` for this rule anyways.
		if (available < increment) {
			quantity = 0;
		}

		return quantity;
	}

	setTicketSelectionFromExistingCart(items) {
		const ticketSelection = {};
		const { id } = this.props.match.params;
		if (items && items.length > 0) {
			items.forEach(({ ticket_type_id, quantity, redemption_code }) => {
				if (ticket_type_id) {
					ticketSelection[ticket_type_id] = {
						quantity: ticketSelection[ticket_type_id]
							? ticketSelection[ticket_type_id].quantity + quantity
							: quantity,
						redemption_code: redemption_code
					};
				}
			});
		}

		//Auto add one ticket if there is only one
		const { ticket_types } = selectedEvent;
		if (items === undefined || items.length === 0) {
			if (ticket_types && ticket_types.length > 1) {
				ticket_types.forEach((type, index) => {
					const type_id = type.id;

					if (!ticketSelection[type_id]) {
						ticketSelection[type_id] = {
							quantity: this.getAutoAddQuantity(index, ticket_types)
						};
					}
				});

				this.setState({ ticketSelection });
			} else {
				selectedEvent.refreshResult(
					id,
					errorMessage => {
						notifications.show({
							message: errorMessage,
							variant: "error"
						});
					},
					types => {
						if (types && types.length) {
							for (let i = 0; i < types.length; i++) {
								const type_id = types[i].id;

								//Auto add a ticket after refreshing the event tickets
								if (!ticketSelection[type_id]) {
									ticketSelection[type_id] = {
										quantity: this.getAutoAddQuantity(i, types)
									};
								}
							}
						}
						this.setState({ ticketSelection });
					}
				);
			}
		} else {
			this.setState({ ticketSelection });
		}
	}

	validateFields() {
		//Don't validate every field if the user has not tried to submit at least once
		if (!this.submitAttempted) {
			return true;
		}

		const { ticketSelection } = this.state;
		const { ticket_types } = selectedEvent;

		const errors = {};

		Object.keys(ticketSelection).forEach(ticketTypeId => {
			const selectedTicketCount = ticketSelection[ticketTypeId];
			if (selectedTicketCount && selectedTicketCount.quantity > 0) {
				//Validate the user is buying in the correct increments
				const ticketType = ticket_types.find(({ id }) => {
					return id === ticketTypeId;
				});

				const increment = ticketType ? ticketType.increment : 1;

				if (selectedTicketCount.quantity % increment !== 0) {
					errors[ticketTypeId] = `Please order in increments of ${increment}`;
				}
			}
		});

		this.setState({ errors });

		if (Object.keys(errors).length > 0) {
			return false;
		}

		return true;
	}

	clearAppliedPromoCodes() {
		//Remove codes from selected tickets to not apply them when adding to cart
		this.setState(
			({ ticketSelection }) => {
				if (ticketSelection) {
					Object.keys(ticketSelection).forEach(id => {
						if (ticketSelection[id]) {
							delete ticketSelection[id].redemption_code;
						}
					});
				}

				return { ticketSelection };
			},
			() => {
				//Remove from ticket types in store
				selectedEvent.removePromoCodesFromTicketTypes();

				//Update the ticket types so limit details with no promo code applied are used
				selectedEvent.refreshResult(
					this.eventId,
					errorMessage => {
						notifications.show({
							message: errorMessage,
							variant: "error"
						});
					},
					() => {}
				);
			}
		);
	}

	onSubmitPromo(code) {
		if (!code) {
			return notifications.show({
				message: "Enter a promo code first.",
				variant: "warning"
			});
		}

		this.setState({ isSubmittingPromo: true }, () => {
			selectedEvent.applyRedemptionCode(
				code,
				null,
				appliedCodes => {
					//after applying, update redemption_code in ticketSelection
					if (appliedCodes && Object.keys(appliedCodes).length > 0) {
						this.setState(({ ticketSelection }) => {
							Object.keys(appliedCodes).forEach(id => {
								if (ticketSelection[id]) {
									ticketSelection[id].redemption_code = appliedCodes[id];
								}
							});

							return { ticketSelection, isSubmittingPromo: false };
						});
					}
				},
				() => {
					this.setState({ isSubmittingPromo: false });
				}
			);
		});
	}

	onSubmit() {
		const { id, event } = selectedEvent;
		cart.setLatestEventId(id);
		const { ticketSelection } = this.state;

		this.submitAttempted = true;
		if (!this.validateFields()) {
			console.warn("Validation errors: ");
			console.warn(this.state.errors);
			return false;
		}

		if (!user.isAuthenticated) {
			//Show dialog for the user to signup/login, try again on success
			user.showAuthRequiredDialog(this.onSubmit.bind(this));
			return;
		}

		let emptySelection = true;
		Object.keys(ticketSelection).forEach(ticketTypeId => {
			if (
				ticketSelection[ticketTypeId] &&
				ticketSelection[ticketTypeId].quantity > 0
			) {
				emptySelection = false;
			}
		});

		//If the existing cart is empty and they haven't selected anything
		if (cart.ticketCount === 0 && emptySelection) {
			return notifications.show({
				message: "Select tickets first."
			});
		}

		this.setState({ isSubmitting: true });

		cart.replace(
			ticketSelection,
			data => {
				if (!emptySelection) {
					let cartItems = 0;
					for (let i = 0; i < data.items.length; i++) {
						if (data.items[i].item_type === "Tickets") {
							cartItems += data.items[i].quantity;
						}
					}
					const total = data.total_in_cents / 100;
					analytics.initiateCheckout(
						event.id,
						getAllUrlParams(),
						"USD",
						cartItems,
						total
					);
					this.props.history.push(`/events/${id}/tickets/confirmation`);
				} else {
					//They had something in their cart, but they removed and updated
					this.setState({ isSubmitting: false });
				}
			},
			error => {
				this.setState({ isSubmitting: false });

				const formattedError = notifications.showFromErrorResponse({
					error,
					defaultMessage: "Failed to add to cart.",
					variant: "error"
				});

				console.error(formattedError);
			}
		);
	}

	onOverlayCardHeightChange(overlayCardHeight) {
		this.setState({ overlayCardHeight });
	}

	renderTicketPricing() {
		const { event, ticket_types } = selectedEvent;
		const { ticketSelection, errors } = this.state;
		if (!ticket_types) {
			return <Loader>Loading tickets...</Loader>;
		}

		const eventIsCancelled = !!(event && event.cancelled_at);

		const ticketTypeRendered = ticket_types
			.map(ticketType => {
				const {
					id,
					name,
					ticket_pricing,
					increment,
					limit_per_person,
					start_date,
					end_date,
					redemption_code,
					available,
					description,
					discount_as_percentage,
					status
				} = ticketType;

				let price_in_cents;
				let ticketsAvailable = false;
				let discount_in_cents = 0;
				if (ticket_pricing) {
					price_in_cents = ticket_pricing.price_in_cents;
					ticketsAvailable = available > 0;
					discount_in_cents = ticket_pricing.discount_in_cents || 0;
				} else {
					//description = "(Tickets currently unavailable)";
				}

				//0 is returned for limit_per_person when there is no limit
				const limitPerPerson =
					limit_per_person > 0
						? Math.min(available, limit_per_person)
						: available;

				return (
					<TicketSelection
						key={id}
						name={name}
						description={description}
						ticketsAvailable={ticketsAvailable}
						price_in_cents={price_in_cents}
						error={errors[id]}
						amount={ticketSelection[id] ? ticketSelection[id].quantity : 0}
						increment={increment}
						limitPerPerson={limitPerPerson}
						available={available}
						discount_in_cents={discount_in_cents}
						discount_as_percentage={discount_as_percentage}
						redemption_code={redemption_code}
						onNumberChange={amount =>
							this.setState(({ ticketSelection }) => {
								ticketSelection[id] = {
									quantity: Number(amount) < 0 ? 0 : amount,
									redemption_code
								};
								return {
									ticketSelection
								};
							})
						}
						validateFields={this.validateFields.bind(this)}
						status={status}
						eventIsCancelled={eventIsCancelled}
					/>
				);
			})
			.filter(item => !!item);

		if (!ticketTypeRendered.length) {
			return null;
		}
		return ticketTypeRendered;
	}

	render() {
		const { classes } = this.props;
		const { isSubmitting, isSubmittingPromo, ticketSelection } = this.state;

		const { event, venue, artists, organization, id } = selectedEvent;
		const eventIsCancelled = !!(event && event.cancelled_at);

		if (event === null) {
			return (
				<div>
					<PrivateEventDialog/>
					<Loader style={{ height: 400 }}/>
				</div>
			);
		}

		if (event === false) {
			return <Typography variant="subheading">Event not found.</Typography>;
		}

		if (event.is_external) {
			return <Redirect to={`/events/${id}`}/>;
		}

		const {
			name,
			displayEventStartDate,
			additional_info,
			top_line_info,
			age_limit,
			displayDoorTime,
			displayShowTime,
			eventStartDateMoment,
			tracking_keys
		} = event;

		const promo_image_url = event.promo_image_url
			? optimizedImageUrl(event.promo_image_url)
			: null;

		const mobilePromoImageStyle = {};
		if (promo_image_url) {
			mobilePromoImageStyle.backgroundImage = `url(${promo_image_url})`;
		}

		const promoCodeApplied = !!selectedEvent.currentlyAppliedCode;

		let sharedContent;

		if (ticketSelection === null) {
			sharedContent = (
				<Loader style={{ marginTop: 40, marginBottom: 40 }}>
					Loading cart...
				</Loader>
			);
		} else {
			sharedContent = (
				<div>
					<Typography className={classes.mobileHeading}>
						Select tickets
					</Typography>

					<Divider style={{ margin: 0 }}/>

					{this.renderTicketPricing()}

					<InputWithButton
						value={selectedEvent.currentlyAppliedCode}
						clearText={"Remove"}
						onClear={this.clearAppliedPromoCodes.bind(this)}
						successState={promoCodeApplied}
						showClearButton={promoCodeApplied}
						iconUrl={promoCodeApplied ? "/icons/checkmark-active.svg" : null}
						iconStyle={{ height: 15, width: "auto" }}
						style={{ marginBottom: 20, marginTop: 20 }}
						name={"promoCode"}
						placeholder="Enter a promo code"
						buttonText="Apply"
						onSubmit={this.onSubmitPromo.bind(this)}
						disabled={isSubmittingPromo}
						inputDisabled={promoCodeApplied}
						toUpperCase
					/>

					<Button
						disabled={isSubmitting || eventIsCancelled}
						onClick={() => this.onSubmit()}
						size="large"
						style={{ width: "100%" }}
						variant="callToAction"
					>
						{isSubmitting
							? "Adding..."
							: eventIsCancelled
								? "Cancelled"
								: "Continue"}
					</Button>
				</div>
			);
		}

		//On mobile we need to move the description and artist details down. But we don't know how much space the overlayed div will take.
		const { overlayCardHeight } = this.state;
		const overlayCardHeightAdjustment = overlayCardHeight - 150;

		return (
			<div>
				<OrgAnalytics trackingKeys={tracking_keys}/>
				<Meta
					{...event}
					venue={venue}
					artists={artists}
					additional_info={additional_info}
					organization={organization}
					doorTime={displayDoorTime}
					showTime={displayShowTime}
					type={"selection"}
				/>
				{/*DESKTOP*/}
				<Hidden smDown>
					<EventHeaderImage
						{...event}
						artists={artists}
						organization={organization}
						venue={venue}
					/>
					<TwoColumnLayout
						containerClass={classes.desktopContent}
						containerStyle={{ minHeight: overlayCardHeightAdjustment }}
						col1={(
							<EventDescriptionBody
								organization={organization}
								eventIsCancelled={eventIsCancelled}
								artists={artists}
							>
								<FormattedAdditionalInfo>
									{additional_info}
								</FormattedAdditionalInfo>
							</EventDescriptionBody>
						)}
						col2={(
							<EventDetailsOverlayCard
								style={{
									width: "100%",
									top: -310,
									position: "relative"
								}}
								imageSrc={promo_image_url}
								onHeightChange={this.onOverlayCardHeightChange.bind(this)}
							>
								<div className={classes.desktopCardContent}>
									{sharedContent}
								</div>
							</EventDetailsOverlayCard>
						)}
					/>
				</Hidden>

				{/*MOBILE*/}
				<Hidden mdUp>
					<div className={classes.mobileContainer}>
						<div className={classes.mobileEventDetailsContainer}>
							<div
								className={classes.mobilePromoImage}
								style={{ backgroundImage: `url(${promo_image_url})` }}
							/>
							<div style={{ flex: 1 }}>
								<Typography
									noWrap
									variant={"display1"}
									className={classes.mobileEventName}
								>
									{ellipsis(name, 45)}
								</Typography>
								<div
									style={{
										display: "flex",
										justifyContent: "space-between",
										flex: 1
									}}
								>
									<div>
										<Typography className={classes.mobileVenueName}>
											{venue.name}
											{venue.city ? ` - ${venue.city}` : ""}
										</Typography>
										<Typography className={classes.mobileEventTime}>
											{displayEventStartDate} - {displayShowTime}
										</Typography>
									</div>
									<Link
										to={`/events/${id}`}
										style={{
											display: "flex",
											alignItems: "flex-end"
										}}
									>
										<Typography className={classes.mobileViewDetailsLinkText}>
											View details
										</Typography>
									</Link>
								</div>
							</div>
						</div>
						<div className={classes.mobileTicketSelectionContainer}>
							{sharedContent}
						</div>
					</div>
				</Hidden>
			</div>
		);
	}
}

CheckoutSelection.propTypes = {
	match: PropTypes.object.isRequired,
	classes: PropTypes.object.isRequired
};

export default withStyles(styles)(CheckoutSelection);
