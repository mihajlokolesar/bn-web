import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import PropTypes from "prop-types";
import moment from "moment-timezone";
import { Link } from "react-router-dom";
import classNames from "classnames";
import Card from "../../../elements/Card";
import { fontFamilyDemiBold, secondaryHex } from "../../../../config/theme";
import MaintainAspectRatio from "../../../elements/MaintainAspectRatio";
import optimizedImageUrl from "../../../../helpers/optimizedImageUrl";
import Settings from "../../../../config/settings";
import getPhoneOS from "../../../../helpers/getPhoneOS";
import HoldRow from "../../admin/events/dashboard/holds/children/ChildRow";
import CustomButton from "../../../elements/Button";

const styles = theme => ({
	card: {
		maxWidth: 1008,
		boxShadow: "0 2px 7.5px 1px rgba(112, 124, 237, 0.07)",
		display: "flex",
		borderRadius: "3px",
		flexDirection: "row"
	},
	media: {
		height: 141,
		width: 255,
		backgroundImage: "linear-gradient(255deg, #e53d96, #5491cc)",
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundPosition: "center",

		padding: theme.spacing.unit * 2,
		paddingBottom: theme.spacing.unit,
		display: "flex",
		justifyContent: "flex-end",
		alignItems: "flex-start",
		flexDirection: "column"
	},
	name: {
		marginTop: theme.spacing.unit,
		color: "#000000",
		fontFamily: fontFamilyDemiBold,
		fontSize: 21,
		lineHeight: "21px",
		textAlign: "left",
		lineClamp: 2,
		display: "-webkit-box",
		WebkitLineClamp: 3,
		WebkitBoxOrient: "vertical",
		overflow: "hidden",
		textOverflow: "ellipsis"
	},
	abbr: {
		textDecoration: "none",
		marginTop: theme.spacing.unit,
		color: "#000000",
		fontFamily: fontFamilyDemiBold,
		fontSize: 21,
		lineHeight: "21px",
		borderBottom: "none"
	},
	detailsContent: {
		display: "flex",
		flexDirection: "column",
		width: "100%",
		justifyContent: "space-around",
		paddingLeft: theme.spacing.unit * 2,
		paddingRight: theme.spacing.unit * 2,
		paddingTop: theme.spacing.unit * 2,
		paddingBottom: theme.spacing.unit * 2
	},
	detailsContentSegment: {
		display: "flex",
		flexDirection: "row",
		alignItems: "center",
		justifyContent: "space-between"
	},
	singleDetail: {
		flex: 1,
		display: "flex",
		flexDirection: "column",
		justifyContent: "center"
	},
	label: {
		fontSize: theme.typography.fontSize,
		textTransform: "uppercase",
		color: "#cccfd9"
	},
	dateDetails: {
		marginTop: theme.spacing.unit,
		display: "flex",
		justifyContent: "space-between",
		width: "50%"
	},
	date: {
		color: "#545455",
		fontSize: 14,
		fontWeight: 500,
		lineHeight: "16px"
	},
	value: {
		color: "#9DA3B4",
		fontWeight: 500,
		fontSize: 16,
		lineHeight: "18px"
	},
	valueTitle: {
		fontSize: theme.typography.fontSize,
		color: "#CCCFD9",
		textTransform: "uppercase",
		fontWeight: 500
	},
	priceTagText: {
		color: "#3C383F",
		fontSize: 24,
		fontWeight: 600,
		lineHeight: "48px"
	},
	hoverCard: {
		"&:hover": {
			boxShadow: "5px 5px 5px 0 rgba(0,0,0,0.15)",
			transition: "box-shadow .3s ease-out"
		},
		boxShadow: "none",
		transition: "box-shadow .3s ease-in"
	},
	noHover: {
		transition: "box-shadow .3s ease-out"
	}
});

const PriceTag = ({ classes, min, max }) => {
	if (!min || !max) {
		return null;
	}

	const minDollars = Math.round(min / 100);
	const maxDollars = Math.round(max / 100);
	let text = `$${minDollars} - $${maxDollars}`;

	if (min === max) {
		text = `$${minDollars}`;
	}

	return (
		<div className={classes.priceTag}>
			<Typography className={classes.priceTagText}>{text}</Typography>
		</div>
	);
};

class AltEventResultCard extends Component {
	constructor(props) {
		super(props);
		this.state = {
			hoverId: null
		};
	}

	render() {
		const {
			classes,
			id,
			name,
			promo_image_url,
			event_start,
			venueName,
			event_end,
			city,
			state,
			door_time,
			min_ticket_price,
			max_ticket_price,
			venueTimezone,
			imgAlt,
			slug
		} = this.props;
		const { hoverId } = this.state;

		const style = {};
		if (promo_image_url) {
			style.backgroundImage = `linear-gradient(to top, #000000, rgba(0, 0, 0, 0)), url(${optimizedImageUrl(
				promo_image_url,
				"low",
				{ w: 430 }
			)})`;
		}

		const newVenueTimezone = venueTimezone || "America/Los_Angeles";
		const eventStartDateMoment = moment.utc(event_start);
		const eventEndDateMoment = moment.utc(event_end);

		const displayEventStartDate = eventStartDateMoment
			.tz(newVenueTimezone)
			.format("ddd, MMM Do");
		const displayShowTime = moment(eventStartDateMoment)
			.tz(newVenueTimezone)
			.format("h:mm A");
		const displayShowEndTime = moment(eventEndDateMoment)
			.tz(newVenueTimezone)
			.format("h:mm A");

		return (
			<Link
				onMouseEnter={e => this.setState({ hoverId: id })}
				onMouseLeave={e => this.setState({ hoverId: null })}
				to={`/events/${slug || id}`}
			>
				<Card
					className={classNames({
						[classes.card]: true,
						[classes.noHover]: true,
						[classes.hoverCard]: hoverId === id
					})}
					borderLess
					variant="default"
				>
					<MaintainAspectRatio aspectRatio={Settings().promoImageAspectRatio}>
						<div title={imgAlt} className={classes.media} style={style}/>
					</MaintainAspectRatio>
					<div className={classes.detailsContent}>
						<div className={classes.detailsContentSegment}>
							<div>
								<Typography
									className={classNames({
										[classes.name]: true
									})}
									variant={"subheading"}
								>
									<abbr className={classes.abbr} title={name}>
										{name}
									</abbr>
								</Typography>
								<Typography className={classes.value}>
									@ {venueName}, {city}, {state}
								</Typography>
							</div>
							<div>
								<PriceTag
									min={min_ticket_price}
									max={max_ticket_price}
									classes={classes}
								/>
							</div>
						</div>

						<div className={classes.detailsContentSegment}>
							<div className={classes.dateDetails}>
								<div>
									<Typography className={classes.valueTitle}>DATE:</Typography>
									<Typography className={classes.date}>
										{displayEventStartDate}
									</Typography>
								</div>
								<div>
									<Typography className={classes.valueTitle}>
										Begins:
									</Typography>
									<Typography className={classes.date}>
										{displayShowTime}
									</Typography>
								</div>
								<div>
									<Typography className={classes.valueTitle}>Ends:</Typography>
									<Typography className={classes.date}>
										{displayShowEndTime}
									</Typography>
								</div>
							</div>
							<CustomButton variant={"secondary"}>Get Tickets</CustomButton>
						</div>
					</div>
				</Card>
			</Link>
		);
	}
}

AltEventResultCard.propTypes = {
	id: PropTypes.string.isRequired,
	name: PropTypes.string.isRequired,
	promo_image_url: PropTypes.string,
	event_start: PropTypes.string.isRequired,
	door_time: PropTypes.string.isRequired,
	min_ticket_price: PropTypes.number.isRequired,
	max_ticket_price: PropTypes.number.isRequired,
	venueTimezone: PropTypes.string
};

export default withStyles(styles)(AltEventResultCard);
