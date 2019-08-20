import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { observer } from "mobx-react";

import { withStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import Hidden from "@material-ui/core/Hidden";
import MenuIcon from "@material-ui/icons/Menu";

import { secondaryHex, toolBarHeight } from "../../../config/theme";
import RightUserMenu from "./RightUserMenu";
import SearchToolBarInput from "./SearchToolBarInput";
import CartHeaderLink from "../../common/cart/CartHeaderLink";
import CurrentOrganizationMenu from "./CurrentOrganizationMenu";
import BoxOfficeLink from "./BoxOfficeLink";
import AppBarLogo from "./AppBarLogo";
import layout from "../../../stores/layout";

const styles = theme => {
	return {
		headerLinkContainer: {
			[theme.breakpoints.down("sm")]: {
				flex: 1,
				display: "flex",
				justifyContent: "flex-start"
			}
		},
		toolBar: {
			paddingRight: theme.spacing.unit * 2,
			paddingLeft: theme.spacing.unit * 2,
			display: "flex",
			justifyContent: "space-between",
			...toolBarHeight
		},
		rightMenuOptions: {
			alignItems: "center",
			display: "flex"
		},
		menuIcon: {
			color: secondaryHex
		}
	};
};

const CustomAppBar = observer(props => {
	const { classes, handleDrawerToggle, history, homeLink } = props;

	return (
		<AppBar position={"static"}>
			<Toolbar className={classes.toolBar}>
				{handleDrawerToggle ? (
					<Hidden mdUp>
						{layout.showSideMenu ? (
							<IconButton
								color="inherit"
								aria-label="open drawer"
								onClick={handleDrawerToggle}
								className={classes.navIconHide}
							>
								<MenuIcon className={classes.menuIcon}/>
							</IconButton>
						) : (
							<span/>
						)}
					</Hidden>
				) : null}

				<div className={classes.headerLinkContainer}>
					<Link to={homeLink}>
						<AppBarLogo/>
					</Link>
				</div>

				{!layout.showStudioLogo ? (
					<Hidden smDown>
						<SearchToolBarInput history={history}/>
					</Hidden>
				) : null}

				<span className={classes.rightMenuOptions}>
					<Hidden smDown>
						<BoxOfficeLink/>
						<CurrentOrganizationMenu/>
						<CartHeaderLink/>
					</Hidden>
					<RightUserMenu history={history}/>
				</span>
			</Toolbar>
		</AppBar>
	);
});

CustomAppBar.defaultProps = {
	homeLink: "/"
};

CustomAppBar.propTypes = {
	classes: PropTypes.object.isRequired,
	handleDrawerToggle: PropTypes.func,
	history: PropTypes.object.isRequired,
	homeLink: PropTypes.string
};

export default withStyles(styles)(CustomAppBar);
