import TwoColumnLayout from "./TwoColumnLayout";
import { Typography } from "@material-ui/core";
import React from "react";
import removeCountryFromAddress from "../../../helpers/removeCountryFromAddress";

const Hero = ({
	classes,
	event,
	venue,
	order,
	firstName,
	qty,
	promoImgStyle,
	displayEventStartDate
}) => {
	return (
		<div className={classes.desktopCoverImage}>
			<TwoColumnLayout
				style={{ margin: "0 auto" }}
				containerStyle={{ maxWidth: 956 }}
				col1={(
					<div className={classes.desktopHeroContent}>
						{promoImgStyle ? (
							<div
								className={classes.desktopEventPromoImg}
								style={promoImgStyle}
							/>
						) : null}

						<Typography className={classes.greyTitleBold}>Event</Typography>

						<Typography className={classes.desktopEventDetailText}>
							<span className={classes.boldText}>{event.name}</span>
							<br/>
							{displayEventStartDate}
						</Typography>

						<Typography className={classes.greyTitleBold}>Location</Typography>

						<Typography className={classes.desktopEventDetailText}>
							<span className={classes.boldText}>{venue.name}</span>
							<br/>
							{removeCountryFromAddress(venue.address)}
						</Typography>
					</div>
				)}
			/>
		</div>
	);
};
export default Hero;
