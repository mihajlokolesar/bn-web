import React, { Component } from "react";
import { Typography, withStyles, CardMedia } from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import { Link } from "react-router-dom";

import notifications from "../../../../stores/notifications";
import Button from "../../../elements/Button";
import Bigneon from "../../../../helpers/bigneon";
import PageHeading from "../../../elements/PageHeading";
import Loader from "../../../elements/loaders/Loader";
import user from "../../../../stores/user";
import optimizedImageUrl from "../../../../helpers/optimizedImageUrl";
import {
	Pagination,
	urlPageParam,
	urlQueryParam
} from "../../../elements/pagination";
import Settings from "../../../../config/settings";
import SearchBox from "../../../elements/SearchBox";
import changeUrlParam from "../../../../helpers/changeUrlParam";

class ArtistsList extends Component {
	constructor(props) {
		super(props);

		this.state = {
			artists: null,
			paging: null
		};
		this.onSearch = this.onSearch.bind(this);
	}

	componentDidMount() {
		this.refreshArtists();
	}

	refreshArtists( page = urlPageParam(), query = urlQueryParam()) {
		Bigneon()
			.artists.index({ page, limit: Settings().defaultPageLimit, q: query })
			.then(response => {
				const { data, paging } = response.data;
				this.setState({ artists: data, paging });
			})
			.catch(error => {
				console.error(error);
				notifications.showFromErrorResponse({
					defaultMessage: "Error loading artists.",
					error
				});
			});
	}

	onSearch(query) {
		//Set the query in the url so the page can be refreshed
		changeUrlParam("query", query);
		//Reset to page 0
		this.changePage( 0);
	}

	changePage(page = urlPageParam()) {
		changeUrlParam("page", page + 1);
		this.refreshArtists(page);
	}

	renderArtists() {
		const { artists } = this.state;
		const { classes } = this.props;

		if (artists === null) {
			return (
				<Grid item xs={12} sm={12} lg={12}>
					<Loader/>
				</Grid>
			);
		}

		if (artists && artists.length > 0) {
			return artists.map(artist => {
				const {
					id,
					name,
					youtube_video_urls,
					website_url,
					thumb_image_url,
					is_private
				} = artist;
				const videoCount = youtube_video_urls ? youtube_video_urls.length : 0;
				const thumbImageUrl = optimizedImageUrl(thumb_image_url, "low", { w: 150 });
				return (
					<Grid key={id} item xs={12} sm={12} lg={12}>
						<Card className={classes.paper}>
							<CardMedia
								className={classes.media}
								image={thumbImageUrl || "/images/profile-pic-placeholder.png"}
								title={name}
							/>

							<CardContent className={classes.cardContent}>
								<Typography
									variant="display1"
								>
									{name}
								</Typography>
								<Typography variant="body1">
									{videoCount} video
									{videoCount === 1 ? "" : "s"}
								</Typography>

								<a href={website_url} target="_blank">
									<Typography
										variant="body1"
									>
										{website_url}
									</Typography>
								</a>
							</CardContent>

							{is_private || user.isAdmin ? (
								<div className={classes.actionButtons}>
									<Link
										to={`/admin/artists/${id}`}
										style={{ marginRight: 10 }}
									>
										<Button variant="primary">
											Edit details
										</Button>
									</Link>
								</div>
							) : null}
						</Card>
					</Grid>
				);
			});
		} else {
			return (
				<Grid item xs={12} sm={12} lg={12}>
					<Typography variant="body1">No artists</Typography>
				</Grid>
			);
		}
	}

	render() {
		const { paging } = this.state;
		const { classes } = this.props;
		return (
			<div>
				<PageHeading
					iconUrl="/icons/artists-active.svg"
				>
					Artists
				</PageHeading>

				<Grid container spacing={24}>
					<Grid item xs={12} sm={4} lg={4}>
						<Link to={"/admin/artists/create"}>
							<Button variant="callToAction">Create
								artist</Button>
						</Link>
					</Grid>
					<Grid item xs={12} sm={8} lg={8}>
						<div className={classes.searchHolder}>
							<div className={classes.searchStyle}>
								<SearchBox
									placeholder="Search name"
									onSearch={this.onSearch}
								/>
							</div>
						</div>
					</Grid>

					{this.renderArtists()}
					{paging !== null ? (
						<Pagination
							isLoading={false}
							paging={paging}
							onChange={this.changePage.bind(this)}
						/>
					) : (
						<div/>
					)}
				</Grid>
			</div>
		);
	}
}

const styles = theme => ({
	paper: {
		display: "flex"
	},
	cardContent: {
		padding: theme.spacing.unit * 2,
		marginBottom: theme.spacing.unit,
		flex: "1 0 auto"
	},
	media: {
		width: "100%",
		maxWidth: 150,
		height: 150
	},
	actionButtons: {
		display: "flex",
		alignItems: "flex-end",
		padding: theme.spacing.unit
	},
	searchHolder: {
		display: "flex",
		flexDirection: "row-reverse"
	},
	searchStyle: {
		display: "flex",
		width: "25%"
	}
});

export default withStyles(styles)(ArtistsList);
