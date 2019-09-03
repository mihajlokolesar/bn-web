import React from "react";
import { Helmet } from "react-helmet";

import Settings from "../../config/settings";

const Meta = () => {
	const landingUrl = Settings().webUrl;
	const siteName = "Big Neon";
	const title = `${siteName} | Concert and Live Event Ticketing`;
	const imageUrl = `${landingUrl}/site/images/bigneon-screen-app.png`;
	const homeLogo = `${landingUrl}/site/images/home-logo.png`;
	const description = `Find tickets to live events and concerts on Big Neon.`;

	return (
		<Helmet
			title={title}
			description={description}
			meta={[
				{
					property: "og:title",
					content: title
				},
				{
					property: "og:type",
					content: "website"
				},
				{
					property: "og:url",
					content: landingUrl
				},
				{
					property: "og:site_name",
					content: siteName
				},
				{
					property: "og:description",
					content: description
				},
				{
					property: "og:image",
					content: landingUrl
				},
				{
					name: "twitter:site",
					content: "bigneon.com"
				},
				{
					name: "twitter:creator",
					content: "bigneon"
				},
				{
					name: "twitter:title",
					content: title
				},
				{
					name: "twitter:image",
					content: imageUrl
				},
				{
					name: "description",
					content: description
				}
			]}
			link={[
				{
					rel: "canonical",
					href: landingUrl
				},
				{
					rel: "image_src",
					href: homeLogo
				}
			]}
		/>
	);
};

export default Meta;
