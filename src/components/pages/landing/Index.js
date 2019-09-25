import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Results from "./cards/Results";
import Hero from "./Hero";
import eventResults from "../../../stores/eventResults";
import notifications from "../../../stores/notifications";
import Meta from "./Meta";
import getUrlParam from "../../../helpers/getUrlParam";
import LandingAppBar from "../../elements/header/LandingAppBar";
import user from "../../../stores/user";
import Hidden from "@material-ui/core/es/Hidden/Hidden";

class Home extends Component {
	constructor(props) {
		super(props);
	}

	componentDidMount() {
		const query = getUrlParam("search") || "";

		eventResults.refreshResults(
			{ query },
			() => {},
			message => {
				notifications.show({
					message,
					variant: "error"
				});
			}
		);
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
				<Grid container justify="center">
					<Grid item xs={11} sm={11} lg={10}>
						<Results/>
					</Grid>
					<div style={{ marginBottom: 40 }}/>
				</Grid>
			</div>
		);
	}
}

export default Home;
