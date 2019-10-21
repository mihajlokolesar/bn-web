import React, { Component } from "react";
import { observer } from "mobx-react/index";
import { withStyles, Typography } from "@material-ui/core";
import Grid from "@material-ui/core/Grid/index";
import { fontFamilyBold } from "../../../../config/theme";
import slugResults from "../../../../stores/slugResults";
import { Link } from "react-router-dom";
import AltEventResultCard from "./AltEventResultCard";
import Button from "../../../elements/Button";
import servedImage from "../../../../helpers/imagePathHelper";

const styles = theme => ({
	root: {
		marginBottom: theme.spacing.unit * 10
	},
	subHeading: {
		marginBottom: theme.spacing.unit,
		textAlign: "center"
	},
	noResultsContainer: {
		paddingTop: theme.spacing.unit * 2,
		display: "flex",
		flexDirection: "column",
		alignItems: "center",
		marginBottom: theme.spacing.unit * 10
	},
	noResultsImage: {
		width: 200,
		height: "auto",
		marginBottom: theme.spacing.unit * 2
	},
	noResultText: {
		marginBottom: theme.spacing.unit * 2
	},
	btnContainer: {
		display: "flex",
		alignItems: "center",
		justifyContent: "center",
		marginTop: theme.spacing.unit * 8
	},
	eventHeading: {
		textAlign: "center",
		fontSize: 22,
		letterSpacing: "-0.9px",
		fontFamily: fontFamilyBold,
		textTransform: "uppercase",
		paddingTop: theme.spacing.unit * 4,
		paddingBottom: theme.spacing.unit * 4,

		[theme.breakpoints.down("sm")]: {
			paddingTop: theme.spacing.unit * 2,
			paddingBottom: theme.spacing.unit * 2,
			fontSize: theme.typography.fontSize * 1.4,
			letterSpacing: "3px"
		}
	}
});

const NoResults = ({ classes, onClear }) => (
	<div className={classes.noResultsContainer}>
		<img
			className={classes.noResultsImage}
			alt="No results found"
			src={servedImage("/icons/events-gray.svg")}
		/>
		<Typography className={classes.noResultText}>No results found.</Typography>
		<Link to="/">
			<Button variant="callToAction">Available events</Button>
		</Link>
	</div>
);

@observer
class AltResults extends Component {
	constructor(props) {
		super(props);
		this.state = {
			shownEvents: 30
		};
		this.loadMore = this.loadMore.bind(this);
	}

	loadMore() {
		this.setState(prev => {
			return { shownEvents: prev.shownEvents + 30 };
		});
	}

	renderEventList = events => {
		return (
			<Grid container spacing={24}>
				{events.slice(0, this.state.shownEvents).map(({ venue, ...event }) => {
					if (!event) {
						console.error("Not found: ");
						return null;
					}
					event.door_time = event.door_time || event.event_start;
					const { timezone, name, city, state } = venue;
					const { artists } = event;

					const headlineArtist = artists.find(
						artist => artist.importance === 0
					);
					const headliner = headlineArtist ? headlineArtist.artist.name : null;

					return (
						<Grid item xs={12} sm={12} lg={12} key={event.id}>
							<AltEventResultCard
								venueTimezone={timezone}
								venueName={name}
								city={city}
								imgAlt={headliner}
								state={state}
								{...event}
							/>
						</Grid>
					);
				})}
			</Grid>
		);
	};

	render() {
		const { classes } = this.props;
		const events = slugResults.events;
		const { shownEvents } = this.state;

		let hasResults = null;
		if (events === null) {
			hasResults = false;
		} else if (events instanceof Array) {
			if (events.length > 0) {
				hasResults = true;
			} else {
				hasResults = false;
			}
		}

		return (
			<div className={classes.root}>

				{hasResults === true ? this.renderEventList(events) : null}

				{hasResults === false ? <NoResults classes={classes}/> : null}

				{hasResults === null ? (
					<Typography className={classes.subHeading}>Searching...</Typography>
				) : null}

				{(hasResults && events.length > shownEvents) ||
				(hasResults && events.length === shownEvents) ? (
						<div className={classes.btnContainer}>
							<Button onClick={this.loadMore} variant="pinkBorder" size="large">
							Load More
							</Button>
						</div>
					) : (
						<div/>
					)}
			</div>
		);
	}
}

export default withStyles(styles)(AltResults);
