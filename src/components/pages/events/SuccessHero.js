import TwoColumnLayout from "./TwoColumnLayout";
import { Typography } from "@material-ui/core";
import React from "react";

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
				style={{ maxWidth: 1400, margin: "0 auto" }}
				col1={(
					<div className={classes.desktopHeroContent}>
						<Typography className={classes.desktopHeroTopLine}>
							{firstName}, <br/> Your Big Neon order is confirmed!
						</Typography>
						<Typography className={classes.desktopHeroOrderTag}>
							Order #{order.order_number} |&nbsp;{qty} Tickets
						</Typography>

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
							{venue.address}
						</Typography>
					</div>
				)}
			/>
		</div>
	);
};
export default Hero;
