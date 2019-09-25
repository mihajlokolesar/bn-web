import React from "react";
import PropTypes from "prop-types";
import { Typography, withStyles } from "@material-ui/core";
import moment from "moment-timezone";
import { Link } from "react-router-dom";

import Card from "../Card";
import { fontFamilyDemiBold, secondaryHex } from "../../../config/theme";
import MaintainAspectRatio from "../MaintainAspectRatio";
import optimizedImageUrl from "../../../helpers/optimizedImageUrl";
import Settings from "../../../config/settings";

const styles = theme => ({
	card: {
		maxWidth: 400
	},
	media: {
		height: "100%",
		width: "100%",
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
		color: "#000000",
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.3,
		lineHeight: 1.2
	},
	detailsContent: {
		height: 105,
		display: "flex",
		padding: theme.spacing.unit * 2
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
	date: {
		color: secondaryHex
	},
	value: {
		fontSize: theme.typography.fontSize,
		color: "#9DA3B4"
	},
	priceTag: {
		backgroundColor: "#fff4fb",
		padding: theme.spacing.unit,
		paddingTop: theme.spacing.unit + 3,
		borderRadius: "6px 6px 6px 0px",
		marginBottom: theme.spacing.unit
	},
	priceTagText: {
		color: secondaryHex,
		lineHeight: 0.5,
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 0.75
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

const EventResultCard = ({
	classes,
	id,
	name,
	promo_image_url,
	event_start,
	address,
	door_time,
	min_ticket_price,
	max_ticket_price,
	venueTimezone,
	slug
}) => {
	const style = {};
	if (promo_image_url) {
		style.backgroundImage = `linear-gradient(to top, #000000, rgba(0, 0, 0, 0)), url(${optimizedImageUrl(
			promo_image_url,
			"low",
			{ w: 430 }
		)})`;
	}
	venueTimezone = venueTimezone || "America/Los_Angeles";
	const eventStartDateMoment = moment.utc(event_start);

	const displayEventStartDate = eventStartDateMoment
		.tz(venueTimezone)
		.format("ddd, MMM Do");
	const displayShowTime = moment(eventStartDateMoment)
		.tz(venueTimezone)
		.format("h:mm A");

	return (
		<Link to={`/events/${slug || id}`}>
			<Card borderLess variant="default">
				<MaintainAspectRatio aspectRatio={Settings().promoImageAspectRatio}>
					<div className={classes.media} style={style}/>
				</MaintainAspectRatio>
				<div className={classes.detailsContent}>
					<div className={classes.singleDetail} style={{ textAlign: "left" }}>
						<Typography className={classes.value}>
							<span className={classes.date}>{displayEventStartDate}</span>{" "}
							&middot; {displayShowTime}
						</Typography>
						<Typography className={classes.name}>{name}</Typography>
						<Typography className={classes.value}>@ {address}</Typography>
					</div>
					<div style={{ textAlign: "right" }}>
						<PriceTag
							min={min_ticket_price}
							max={max_ticket_price}
							classes={classes}
						/>
					</div>
				</div>
			</Card>
		</Link>
	);
};

EventResultCard.propTypes = {
	id: PropTypes.string.isRequired,
	name: PropTypes.string.isRequired,
	promo_image_url: PropTypes.string,
	event_start: PropTypes.string.isRequired,
	door_time: PropTypes.string.isRequired,
	min_ticket_price: PropTypes.number.isRequired,
	max_ticket_price: PropTypes.number.isRequired,
	venueTimezone: PropTypes.string
};

export default withStyles(styles)(EventResultCard);
