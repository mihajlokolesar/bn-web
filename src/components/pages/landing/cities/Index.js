import React, { Component } from "react";
import { withStyles, Typography, Grid, Hidden } from "@material-ui/core";
import { observer } from "mobx-react/index";
import LandingAppBar from "../../../elements/header/LandingAppBar";
import user from "../../../../stores/user";
import CityLandingHero from "./Hero";
import Meta from "./Meta";
import slugResults from "../../../../stores/slugResults";
import Loader from "../../../elements/loaders/Loader";
import AltResults from "../cards/AltResults";
import notifications from "../../../../stores/notifications";
import { fontFamilyDemiBold } from "../../../../config/theme";

const styles = theme => ({
	root: {},
	heading: {
		color: "#2C3136",
		fontSize: "36px",
		fontFamily: fontFamilyDemiBold,
		lineHeight: "41px",
		marginTop: theme.spacing.unit * 5,
		marginBottom: theme.spacing.unit * 3,
		[theme.breakpoints.down("sm")]: {
			color: "#3C383F",
			fontSize: 18,
			letterSpacing: "-0.15px",
			lineHeight: "21px"
		}
	},
	cardContainer: {
		[theme.breakpoints.down("sm")]: {
			maxWidth: "100%",
			padding: theme.spacing.unit * 2
		}
	}
});

@observer
class CityLanding extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}

	componentWillMount() {
		if (
			this.props.match &&
			this.props.match.params &&
			this.props.match.params.id
		) {
			const { id } = this.props.match.params;
			// eventResults.changeFilter("city", "New York");
			// eventResults.changeFilter("state", venueFromUrl);
			slugResults.refreshResults(
				id,
				() => {},
				() => {},
				message => {
					notifications.show({
						message,
						variant: "error"
					});
				}
			);
		} else {
			// 404 ....
			console.error("Could not find id");
		}
	}

	render() {
		const { history, classes } = this.props;
		return (
			<div className={classes.root}>
				<Meta
					cityName={slugResults.cityInfo ? slugResults.cityInfo.city : ""}
				/>
				<Hidden smDown>
					<LandingAppBar
						isAuthenticated={user.isAuthenticated}
						history={history}
					/>
				</Hidden>

				{slugResults.isLoading ? (
					<Loader>Finding events...</Loader>
				) : (
					<CityLandingHero
						pageTitle={
							slugResults.cityInfo
								? slugResults.cityInfo.city
								: "No events for this city"
						}
						history={history}
					/>
				)}

				<Grid
					container
					justify="center"
					style={{ maxWidth: 1600, margin: "0 auto" }}
				>
					<Grid item xs={12} sm={11} lg={10}>
						{slugResults.isLoading ? (
							<Loader>Finding events...</Loader>
						) : (
							<div className={classes.cardContainer}>
								<Typography variant={"title"} className={classes.heading}>
									Upcoming Events in {slugResults.cityInfo.city}
								</Typography>
								<AltResults/>
							</div>
						)}
					</Grid>
				</Grid>
			</div>
		);
	}
}

export default withStyles(styles)(CityLanding);
