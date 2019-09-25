import React, { Component } from "react";
import { withStyles, Typography, Hidden } from "@material-ui/core";
import classnames from "classnames";
import { fontFamilyBold } from "../../../config/theme";
import servedImage from "../../../helpers/imagePathHelper";
import RightUserMenu from "../../elements/header/RightUserMenu";
import { observer } from "mobx-react";

import SearchToolBarInput from "../../elements/header/SearchToolBarInput";

const styles = theme => ({
	root: {
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundPosition: "center",
		backgroundColor: "#19081e",
		backgroundImage: "url(/images/homepage-bg.png)",
		display: "flex",
		flexDirection: "column",
		minHeight: "60vh",
		[theme.breakpoints.down("sm")]: {
			flexDirection: "column",
			minHeight: "40vh"
		}
	},
	headingContainer: {
		display: "flex",
		flexDirection: "column",
		justifyContent: "center",
		alignItems: "center",
		flex: 1,
		textAlign: "center"
	},
	text: {
		color: "#FFFFFF"
	},
	toolBar: {
		paddingRight: "8vw",
		paddingLeft: "8vw",
		paddingTop: "5vh",
		display: "flex",
		justifyContent: "space-between",
		alignItems: "center",
		height: theme.spacing.unit * 10
	},
	heading: {
		fontSize: theme.typography.fontSize * 4,
		fontFamily: fontFamilyBold,
		[theme.breakpoints.down("sm")]: {
			fontSize: theme.typography.fontSize * 2.9,
			paddingLeft: theme.spacing.unit * 3,
			paddingRight: theme.spacing.unit * 3
		}
	},
	subheading: {
		fontSize: theme.typography.fontSize * 1.6,
		lineSpace: 1,
		[theme.breakpoints.down("sm")]: {
			fontSize: theme.typography.fontSize * 1.4
		}
	},
	iconHolder: {
		display: "flex",
		width: "150px",
		justifyContent: "space-between",
		alignItems: "flex-start",
		paddingBottom: theme.spacing.unit
	},
	availableOn: {
		fontSize: theme.typography.fontSize * 0.9,
		marginRight: theme.spacing.unit
	},
	appLinkContainer: {
		// borderStyle: "solid",
		// borderColor: "red",
		display: "flex",
		justifyContent: "center",
		alignItems: "center",
		marginTop: 25,
		[theme.breakpoints.up("sm")]: {
			justifyContent: "flex-center"
		}
	},
	searchContainer: {
		borderRadius: "10px",
		width: "33vw",
		borderColor: "#fff",
		borderStyle: "solid",
		display: "flex",
		backgroundColor: "#fff",
		padding: theme.spacing.unit * 2,
		justifyContent: "flex-start",
		alignItems: "center",
		marginTop: 25,
		[theme.breakpoints.up("sm")]: {
			justifyContent: "flex-center"
		}
	},
	featureImage: {
		flex: 0,
		width: 380,
		[theme.breakpoints.up("sm")]: {
			width: 600
		},
		[theme.breakpoints.down("xs")]: {
			width: 300
		}
	},
	iconImage: {
		maxHeight: "21px"
	},
	logoImage: {
		maxWidth: 140,
		maxHeight: 43
	},
	downloadBtn: {
		maxWidth: 128,
		marginTop: theme.spacing.unit * 2,
		maxHeight: 38
	}
});

class Hero extends Component {
	constructor(props) {
		super(props);
		this.inputRef = React.createRef();
		this.state = {
			query: "",
			isSearching: false
		};
	}

	handleSearchClick = () => {
		this.inputRef.current.click();
	};

	render() {
		const { history, classes } = this.props;

		return (
			<div className={classes.root}>
				<Hidden smDown>
					<div className={classes.toolBar}>
						<img
							alt="Header logo"
							className={classes.logoImage}
							src={servedImage("/images/logo-white.png")}
						/>
						<span className={classes.rightMenuOptions}>
							<RightUserMenu whiteText={true} history={history}/>
						</span>
					</div>
				</Hidden>
				<div className={classes.headingContainer}>
					<Typography
						className={classnames({
							[classes.text]: true,
							[classes.heading]: true
						})}
					>
						The Future of Ticketing
					</Typography>
					<Hidden smDown>
						<div
							className={classes.searchContainer}
							onClick={this.handleSearchClick}
						>
							<SearchToolBarInput clickRef={this.inputRef} history={history}/>
						</div>
					</Hidden>
					<Hidden smUp>
						<a
							href="https://play.google.com/store/apps/details?id=com.bigneon.mobile"
							target="_blank"
						>
							<img
								className={classes.downloadBtn}
								src={servedImage("/images/appstore-apple.png")}
							/>
						</a>
					</Hidden>
				</div>

				<div className={classes.appLinkContainer}>
					<Hidden xsDown>
						<div className={classes.iconHolder}>
							<Typography
								className={classnames({
									[classes.text]: true
								})}
							>
								Available on:&nbsp;
							</Typography>
							<a
								href="https://play.google.com/store/apps/details?id=com.bigneon.mobile"
								target="_blank"
							>
								<img
									className={classes.iconImage}
									src={servedImage("/images/avail-android-logo.svg")}
								/>
							</a>
							<a
								href="https://apps.apple.com/us/app/big-neon/id1445600728"
								target="_blank"
							>
								<img
									className={classes.iconImage}
									src={servedImage("/images/avail-apple.svg")}
								/>
							</a>
						</div>
					</Hidden>
				</div>
			</div>
		);
	}
}

export default withStyles(styles)(Hero);
