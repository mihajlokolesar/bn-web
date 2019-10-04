import React from "react";
import PropTypes from "prop-types";
import {
	withStyles,
	Typography,
	Hidden,
	Tooltip,
	Collapse
} from "@material-ui/core";
import { Link } from "react-router-dom";
import classNames from "classnames";
import {
	fontFamilyDemiBold,
	secondaryHex,
	textColorPrimary
} from "../../../../../../config/theme";
import Card from "../../../../../elements/Card";

import ellipsis from "../../../../../../helpers/ellipsis";
import { dollars } from "../../../../../../helpers/money";
import ColorTag from "../../../../../elements/ColorTag";

const styles = theme => {
	return {
		desktopCard: {
			display: "flex",
			height: 70,
			alignItems: "center",
			marginTop: theme.spacing.unit * 2,
			paddingLeft: theme.spacing.unit * 2,
			paddingRight: theme.spacing.unit * 2
		},
		mobileCard: {
			borderRadius: 6,
			marginTop: theme.spacing.unit * 2,
			padding: theme.spacing.unit * 2
		},
		linkText: {
			fontFamily: fontFamilyDemiBold,
			color: secondaryHex,
			cursor: "pointer"
		},
		emailText: {
			fontFamily: fontFamilyDemiBold
		},

		mobileRow1: {
			display: "flex"
			// justifyContent: "space-between"
		},
		mobileCol1: {
			flex: 4
		},
		mobileCol2: {
			flex: 3,
			textAlign: "right"
		},
		mobileRow2: {
			marginTop: 12,
			display: "flex"
		},
		mobileRow2Col: {
			marginRight: 50
		},
		mobileDate: {
			fontSize: 12,
			color: "#8b94a7"
		},
		mobileOrderNo: {
			fontSize: 14
		},
		mobileUserName: {
			marginTop: 12,
			fontSize: 14,
			color: secondaryHex,
			fontFamily: fontFamilyDemiBold
		},
		mobileEmail: {
			fontSize: 14,
			color: "#8b94a7"
		},
		mobileHeading: {
			fontSize: 12,
			color: "#b2bdd4",
			textTransform: "uppercase",
			fontFamily: fontFamilyDemiBold
		},
		mobileValue: {
			fontSize: 15
		}
	};
};

const OrderRow = props => {
	const {
		classes,
		eventId,
		columnStyles,
		id,
		order_number,
		displayDate,
		ticketCount,
		total_in_cents,
		user,
		on_behalf_of_user,
		platform,
		ticketTypeList,
		...rest
	} = props;
	const { first_name, last_name, email, id: userId } = on_behalf_of_user
		? on_behalf_of_user
		: user;

	const orderPath = `/admin/events/${eventId}/dashboard/orders/manage/${id}`;
	const userPath = `/admin/fans/${userId}`;

	return (
		<React.Fragment>
			{/*DESKTOP*/}
			<Hidden smDown>
				<Card variant={"raisedLight"} className={classes.desktopCard}>
					<Link style={columnStyles[0]} to={orderPath}>
						<Typography className={classes.linkText}>{order_number}</Typography>
					</Link>

					<Typography style={columnStyles[1]}>{displayDate}</Typography>

					<Link style={columnStyles[2]} to={userPath}>
						<Typography>
							<span className={classes.linkText}>
								{ellipsis(`${first_name} ${last_name}`, 15)}
							</span>{" "}
							<span className={classes.emailText}>
								{ellipsis(`(${email})`, 15)}
							</span>
						</Typography>
					</Link>

					<Typography style={columnStyles[3]}>{ticketCount}</Typography>

					<Typography style={columnStyles[4]}>
						{dollars(total_in_cents)}
					</Typography>

					<Typography style={columnStyles[5]}>{platform}</Typography>
				</Card>
			</Hidden>

			{/*MOBILE*/}
			<Hidden mdUp>
				<Link to={orderPath}>
					<Card variant={"block"} className={classes.mobileCard}>
						<div className={classes.mobileRow1}>
							<div className={classes.mobileCol1}>
								<Typography className={classes.mobileDate}>
									{displayDate}
								</Typography>

								<Typography className={classes.mobileUserName}>
									{first_name} {last_name}
								</Typography>

								<Typography className={classes.mobileEmail}>{email}</Typography>
							</div>

							<div className={classes.mobileCol2}>
								<Typography className={classes.mobileOrderNo}>
									#{order_number}
								</Typography>
								{ticketTypeList
									? ticketTypeList.map((tt, index) =>
										tt ? (
											<ColorTag
												size={"small"}
												variant={index % 2 == 0 ? "green" : "secondary"}
												key={tt}
											>
												{tt}
											</ColorTag>
										) : null
									  )
									: null}
							</div>
						</div>

						<div className={classes.mobileRow2}>
							<div className={classes.mobileRow2Col}>
								<Typography className={classes.mobileHeading}>QTY</Typography>
								<Typography className={classes.mobileValue}>
									{ticketCount}
								</Typography>
							</div>

							<div className={classes.mobileRow2Col}>
								<Typography className={classes.mobileHeading}>
									Order value
								</Typography>
								<Typography className={classes.mobileValue}>
									{dollars(total_in_cents)}
								</Typography>
							</div>

							<div className={classes.mobileRow2Col}>
								<Typography className={classes.mobileHeading}>POS</Typography>
								<Typography className={classes.mobileValue}>
									{platform}
								</Typography>
							</div>
						</div>
					</Card>
				</Link>
			</Hidden>
		</React.Fragment>
	);
};

OrderRow.propTypes = {
	classes: PropTypes.object.isRequired,
	eventId: PropTypes.string.isRequired,
	id: PropTypes.string.isRequired,
	columnStyles: PropTypes.arrayOf(PropTypes.object).isRequired,
	ticketTypeList: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default withStyles(styles)(OrderRow);
