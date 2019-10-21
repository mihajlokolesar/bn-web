import React from "react";
import { Helmet } from "react-helmet";
import Settings from "../../../../config/settings";

//Reference: https://github.com/nfl/react-helmet

const Meta = props => {
	return (
		<Helmet
			title={`Concert Tickets ${props.venueName} ${props.cityName}- Big Neon`}
			meta={[
				{
					property: "og:title",
					content: "Big Neon - The new standard in event ticketing"
				},
				{ property: "og:type", content: "website" },
				{ property: "og:url", content: Settings().webUrl },
				{
					property: "og:description",
					content:
						"Big Neon - Mobile-first event ticketing. We’re relentlessly focused on serving the needs of independent live music promoters."
				},
				{
					property: "og:image",
					content: `${Settings().webUrl}/site/images/bigneon-screen-app.png`
				},
				{ name: "twitter:site", content: "bigneon.com" },
				{ name: "twitter:creator", content: "bigneon" },
				{
					name: "twitter:title",
					content: "Big Neon - The new standard in event ticketing"
				},
				{
					name: "twitter:image",
					content: `${Settings().webUrl}/site/images/bigneon-screen-app.png`
				},
				{
					name: "description",
					content: `Concert Tickets ${props.venueName} ${
						props.cityName
					}- Find tickets to live events and concerts on Big Neon.`
				}
			]}
			link={[
				{ rel: "canonical", href: Settings().webUrl },
				{
					rel: "image_src",
					href: `${Settings().webUrl}/site/images/home-logo.png`
				}
			]}
		/>
	);
};

export default Meta;
