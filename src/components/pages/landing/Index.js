import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Hidden from "@material-ui/core/Hidden";
import { observer } from "mobx-react";

import Results from "./cards/Results";
import Hero from "./Hero";
import eventResults from "../../../stores/eventResults";
import notifications from "../../../stores/notifications";
import Meta from "./Meta";
import getUrlParam from "../../../helpers/getUrlParam";
import LandingAppBar from "../../elements/header/LandingAppBar";
import user from "../../../stores/user";
import { Pagination, urlPageParam } from "../../elements/pagination";
import Loader from "../../elements/loaders/Loader";

@observer
class Home extends Component {
	constructor(props) {
		super(props);
		this.state = {
			query: getUrlParam("search") || "",
			page: urlPageParam()
		};
	}

	componentDidMount() {
		const { query, page } = this.state;

		eventResults.refreshResults(
			{ query, page },

			() => {},
			message => {
				notifications.show({
					message,
					variant: "error"
				});
			}
		);
	}

	changePage(page = urlPageParam()) {
		const { query } = this.state;

		eventResults.refreshResults({ query, page }, function() {}, function() {});
	}

	render() {
		const { history } = this.props;
		return (
			<div>
				<Meta/>
				<Hidden smDown>
					<LandingAppBar
						isAuthenticated={user.isAuthenticated}
						history={history}
					/>
				</Hidden>

				<Hero history={history}/>
				<Grid
					container
					justify="center"
					style={{ maxWidth: 1400, margin: "0 auto" }}
				>
					<Grid item xs={11} sm={11} lg={10}>
						{eventResults.isLoading ? (
							<Loader>Finding events...</Loader>
						) : (
							<Results/>
						)}
					</Grid>
					<div style={{ marginBottom: 40 }}/>
				</Grid>
			</div>
		);
	}
}

export default Home;
