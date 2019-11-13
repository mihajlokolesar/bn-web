import React, { Component } from "react";
import { observer } from "mobx-react";
import { withStyles, Typography } from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import { fontFamilyBebas, fontFamilyBold } from "../../../../config/theme";
import ResultsRegionFilter from "./ResultsRegionFilter";
import eventResults from "../../../../stores/eventResults";
import EventResultCard from "../../../elements/event/EventResultCard";
import Button from "../../../elements/Button";
import servedImage from "../../../../helpers/imagePathHelper";
import HoldRow from "../../admin/events/dashboard/holds/children/ChildRow";
import getUrlParam from "../../../../helpers/getUrlParam";
import { urlPageParam } from "../../../elements/pagination";
import Hidden from "@material-ui/core/Hidden";
import { Link } from "react-router-dom";
import analytics from "../../../../helpers/analytics";

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
		<Button variant="callToAction" onClick={onClear}>
			Available events
		</Button>
	</div>
);

const EventsList = ({ events }) => {
	return (
		<Grid container spacing={24}>
			{events.map(({ venue, ...event }) => {
				if (!event) {
					console.error("Not found: ");
					return null;
				}
				event.door_time = event.door_time || event.event_start;
				const { timezone, address } = venue;

				return (
					<Grid item xs={12} sm={6} lg={4} key={event.id}>
						<EventResultCard
							venueTimezone={timezone}
							address={address}
							{...event}
						/>
					</Grid>
				);
			})}
		</Grid>
	);
};

@observer
class Results extends Component {
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
				{events
					.slice(0, this.state.shownEvents)
					.map(({ venue, ...event }, index) => {
						if (!event) {
							console.error("Not found: ");
							return null;
						}
						event.door_time = event.door_time || event.event_start;
						const { timezone, name, city, state } = venue;

						return (
							<Grid item xs={12} sm={6} md={4} lg={4} key={event.id}>
								<EventResultCard
									venueTimezone={timezone}
									venueName={name}
									city={city}
									state={state}
									list="Search Results"
									position={index + 1}
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
		const events = eventResults.filteredEvents;
		const { shownEvents } = this.state;

		let hasResults = null;
		if (events === null) {
			hasResults = null;
		} else if (events instanceof Array) {
			if (events.length > 0) {
				hasResults = true;
			} else {
				hasResults = false;
			}

			analytics.addImpressions(events, "Search Results");
		}

		return (
			<div className={classes.root}>
				<Typography className={classes.eventHeading}>
					Upcoming events
				</Typography>

				{hasResults === true ? this.renderEventList(events) : null}

				{hasResults === false ? (
					<NoResults
						classes={classes}
						onClear={() => eventResults.clearFilter()}
					/>
				) : null}

				{hasResults === null ? (
					<Typography className={classes.subHeading}>Searching...</Typography>
				) : null}

				{events.length > shownEvents || events.length === shownEvents ? (
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

export default withStyles(styles)(Results);
