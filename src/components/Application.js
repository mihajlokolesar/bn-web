import Routes from "./routes/Routes";
import React from "react";
import { Helmet } from "react-helmet";

const Application = () => (
	<React.Fragment>
		<Helmet>
			{process.env.NODE_ENV !== "production" ? (
				<meta name="robots" content="noindex"/>
			) : null}
		</Helmet>
		<Routes/>
	</React.Fragment>
);

export default Application;
