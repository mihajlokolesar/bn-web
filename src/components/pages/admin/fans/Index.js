import React, { Component } from "react";
import { Link } from "react-router-dom";
import { withStyles, Typography, Hidden, Grid } from "@material-ui/core";
import moment from "moment-timezone";

import notifications from "../../../../stores/notifications";
import Bigneon from "../../../../helpers/bigneon";
import PageHeading from "../../../elements/PageHeading";
import layout from "../../../../stores/layout";
import FanRow from "./FanRow";
import FanRowMobile from "./FanRowMobile";
import Card from "../../../elements/Card";
import { fontFamilyDemiBold, primaryHex } from "../../../../config/theme";
import user from "../../../../stores/user";
import Button from "../../../elements/Button";
import downloadCSV from "../../../../helpers/downloadCSV";
import { Pagination, urlPageParam } from "../../../elements/pagination";
import Loader from "../../../elements/loaders/Loader";
import servedImage from "../../../../helpers/imagePathHelper";
import SearchBox from "../../../elements/SearchBox";

const imageSize = 40;

const styles = theme => ({
	header: {
		display: "flex",
		justifyContent: "space-between",
		alignItems: "flex-end"
	},
	content: {
		padding: theme.spacing.unit * 6,
		paddingLeft: theme.spacing.unit * 8,
		paddingRight: theme.spacing.unit * 8
	},
	mobiContent: {
		paddingLeft: theme.spacing.unit,
		paddingRight: theme.spacing.unit,
		maxWidth: "95vw"
	},
	spacer: {
		marginTop: theme.spacing.unit * 4
	},
	heading: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 0.95
	},
	itemText: {
		lineHeight: 1.1
	},
	nameProfileImage: {
		display: "flex",
		alignItems: "center"
	},
	profileImageBackground: {
		width: imageSize,
		height: imageSize,
		borderRadius: 100,
		backgroundSize: "cover",
		backgroundRepeat: "no-repeat",
		backgroundPosition: "50% 50%"
	},
	missingProfileImageBackground: {
		backgroundColor: primaryHex,
		width: imageSize,
		height: imageSize,
		borderRadius: 100,
		display: "flex",
		justifyContent: "center",
		alignItems: "center"
	},
	searchHolder: {
		display: "flex",
		flexDirection: "row-reverse"
	},
	searchStyle: {
		display: "flex",
		width: "25%"
	},
	missingProfileImage: {
		width: imageSize * 0.45,
		height: "auto"
	},
	mobiPaginationStyle: {
		width: "90vw"
	},
	mobiSearchStyle: {
		marginTop: theme.spacing.unit,
		width: "100%"
	}
});

class FanList extends Component {
	constructor(props) {
		super(props);

		this.state = {
			users: null,
			paging: {},
			isExporting: false,
			isLoading: true
		};
		this.onSearch = this.onSearch.bind(this);
	}

	componentDidMount() {
		layout.toggleSideMenu(true);
		this.refreshFans();
	}

	componentWillUnmount() {
		if (this.timeout) {
			clearTimeout(this.timeout);
		}
	}

	exportCSV() {
		const organization_id = user.currentOrganizationId;
		const organization_tz = user.currentOrgTimezone;

		if (!organization_id) {
			return null;
		}

		this.setState({ isExporting: true });

		Bigneon()
			.organizations.fans.index({ organization_id, limit: 99999999 }) //TODO api needs to handle all results queries
			.then(response => {
				const { data } = response.data;

				if (!data || data.length === 0) {
					this.setState({ isExporting: false });
					return notifications.show({
						message: "No fans to export."
					});
				}

				const csvRows = [];

				csvRows.push(["Fans"]);
				csvRows.push([""]);
				csvRows.push([
					"First name",
					"Last name",
					"Email",
					"Last order date",
					"Orders",
					"Revenue",
					"Date added"
				]);

				data.forEach(user => {
					const {
						first_name,
						last_name,
						email,
						last_interaction_time,
						order_count,
						revenue_in_cents,
						first_interaction_time
					} = user;

					csvRows.push([
						first_name,
						last_name,
						email,
						moment
							.utc(last_interaction_time)
							.tz(organization_tz)
							.format("MM/DD/YYYY h:mm:A"),
						order_count,
						`$${Math.round(revenue_in_cents / 100)}`,
						moment
							.utc(first_interaction_time)
							.tz(organization_tz)
							.format("MM/DD/YYYY h:mm:A")
					]);
				});

				this.setState({ isExporting: false });

				downloadCSV(csvRows, "fans");
			})
			.catch(error => {
				this.setState({ isExporting: false });

				console.error(error);
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Listing fans failed."
				});
			});
	}

	refreshFans(query = "", page = urlPageParam()) {
		this.setState({ isLoading: true });
		const organization_id = user.currentOrganizationId;

		if (!organization_id) {
			this.timeout = setTimeout(this.refreshFans.bind(this), 500);
			return;
		}
		const params = {
			organization_id,
			page,
			limit: 20,
			query,
			sort: "LastInteracted"
		};
		Bigneon()
			.organizations.fans.index(params)
			.then(response => {
				const { data, paging } = response.data;
				this.setState({ users: data, paging });

				this.setState({ isLoading: false });
			})
			.catch(error => {
				console.error(error);
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Listing fans failed."
				});

				this.setState({ isLoading: false });
			});
	}

	onSearch(query) {
		this.query = query;
		this.refreshFans(query);
	}

	changePage(page = urlPageParam()) {
		this.refreshFans(this.query || "", page);
	}

	renderUsers() {
		const organization_tz = user.currentOrgTimezone;
		const { users, paging, isLoading } = this.state;
		const { classes } = this.props;

		if (users === null) {
			return <Loader/>;
		}

		if (users.length === 0) {
			return (
				<Card>
					<div className={classes.content}>
						<div className={classes.searchHolder}>
							<div className={classes.searchStyle}>
								<SearchBox
									placeholder="Search name or email"
									onSearch={this.onSearch}
								/>
							</div>
						</div>
						<Typography>No fans currently.</Typography>
					</div>
				</Card>
			);
		}

		return (
			<Card>
				<div className={classes.content}>
					<div className={classes.searchHolder}>
						<div className={classes.searchStyle}>
							<SearchBox
								placeholder="Search name or email"
								onSearch={this.onSearch}
							/>
						</div>
					</div>
					<FanRow>
						<Typography className={classes.heading}>Name</Typography>
						<Typography className={classes.heading}>Email</Typography>
						<Typography className={classes.heading}>Last order date</Typography>
						<Typography className={classes.heading}>Orders</Typography>
						<Typography className={classes.heading}>Revenue</Typography>
						<Typography className={classes.heading}>Date added</Typography>
					</FanRow>
					{users.map((user, index) => {
						const {
							user_id,
							first_name,
							last_name,
							email,
							last_interaction_time,
							order_count,
							first_interaction_time,
							revenue_in_cents,
							thumb_profile_pic_url
						} = user;
						return (
							<Link to={`/admin/fans/${user_id}`} key={user_id}>
								<FanRow shaded={!(index % 2)}>
									<div className={classes.nameProfileImage}>
										{thumb_profile_pic_url ? (
											<div
												className={classes.profileImageBackground}
												style={{
													backgroundImage: `url(${thumb_profile_pic_url})`
												}}
											/>
										) : (
											<div className={classes.missingProfileImageBackground}>
												<img
													className={classes.missingProfileImage}
													src={servedImage(
														"/images/profile-pic-placeholder-white.png"
													)}
													alt={`${first_name} ${last_name}`}
												/>
											</div>
										)}
										&nbsp;&nbsp;
										<Typography className={classes.itemText}>
											{first_name} {last_name}
										</Typography>
									</div>

									<Typography className={classes.itemText}>{email}</Typography>
									<Typography className={classes.itemText}>
										{last_interaction_time
											? moment
												.utc(last_interaction_time)
												.tz(organization_tz)
												.format("MM/DD/YYYY")
											: "-"}
									</Typography>
									<Typography className={classes.itemText}>
										{order_count}
									</Typography>
									<Typography className={classes.itemText}>
										${Math.round(revenue_in_cents / 100)}
									</Typography>
									<Typography className={classes.itemText}>
										{first_interaction_time
											? moment
												.utc(first_interaction_time)
												.tz(organization_tz)
												.format("MM/DD/YYYY")
											: "-"}
									</Typography>
								</FanRow>
							</Link>
						);
					})}

					<br/>
					<Pagination
						isLoading={isLoading}
						paging={paging}
						onChange={this.changePage.bind(this)}
					/>
				</div>
			</Card>
		);
	}

	renderUsersMobile() {
		const { users, paging, isLoading } = this.state;
		const { classes } = this.props;
		if (users === null) {
			return <Loader/>;
		}

		if (users.length === 0) {
			return (
				<Card>
					<div className={classes.mobiContent}>
						<div className={classes.searchHolder}>
							<div className={classes.mobiSearchStyle}>
								<SearchBox
									placeholder="Search name or email"
									onSearch={this.onSearch}
								/>
							</div>
						</div>
						<Typography>No fans currently.</Typography>
					</div>
				</Card>
			);
		}

		return (
			<Card>
				<div className={classes.mobiContent}>
					<div className={classes.searchHolder}>
						<div className={classes.mobiSearchStyle}>
							<SearchBox
								placeholder="Search name or email"
								onSearch={this.onSearch}
							/>
						</div>
					</div>
					<FanRow>
						<Typography className={classes.heading}>Name</Typography>
						<Typography className={classes.heading}>Email</Typography>
					</FanRow>
					{users.map((user, index) => {
						const {
							user_id,
							first_name,
							last_name,
							email,
							thumb_profile_pic_url
						} = user;
						return (
							<Link to={`/admin/fans/${user_id}`} key={user_id}>
								<FanRowMobile shaded={!(index % 2)}>
									<div className={classes.nameProfileImage}>
										{thumb_profile_pic_url ? (
											<div
												className={classes.profileImageBackground}
												style={{
													backgroundImage: `url(${thumb_profile_pic_url})`
												}}
											/>
										) : (
											<div className={classes.missingProfileImageBackground}>
												<img
													className={classes.missingProfileImage}
													src={servedImage(
														"/images/profile-pic-placeholder-white.png"
													)}
													alt={`${first_name} ${last_name}`}
												/>
											</div>
										)}
										&nbsp;&nbsp;
										<Typography className={classes.itemText}>
											{first_name} {last_name}
										</Typography>
									</div>

									<Typography className={classes.itemText}>{email}</Typography>
								</FanRowMobile>
							</Link>
						);
					})}

					<br/>
					<div className={classes.mobiPaginationStyle}>
						<Pagination
							isLoading={isLoading}
							paging={paging}
							onChange={this.changePage.bind(this)}
						/>
					</div>
				</div>
			</Card>
		);
	}

	render() {
		const { paging, isExporting } = this.state;
		const { classes } = this.props;

		return (
			<div>
				<div className={classes.header}>
					<PageHeading
						iconUrl="/icons/my-events-multi.svg"
						subheading={
							paging && paging.total ? `${paging.total} total fans` : null
						}
					>
						Fans
					</PageHeading>
					<Button
						iconUrl="/icons/csv-active.svg"
						variant="text"
						onClick={!isExporting ? this.exportCSV.bind(this) : null}
					>
						{isExporting ? "Exporting..." : "Export CSV"}
					</Button>
				</div>

				<div className={classes.spacer}/>

				<Hidden smDown>{this.renderUsers()}</Hidden>

				<Hidden mdUp>{this.renderUsersMobile()}</Hidden>
			</div>
		);
	}
}

export default withStyles(styles)(FanList);
