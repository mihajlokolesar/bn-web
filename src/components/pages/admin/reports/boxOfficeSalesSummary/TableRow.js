import React from "react";
import PropTypes from "prop-types";
import { withStyles, Typography } from "@material-ui/core";
import classNames from "classnames";
import { fontFamilyDemiBold, primaryHex } from "../../../../../config/theme";

const styles = theme => {
	return {
		root: {
			paddingLeft: theme.spacing.unit * 2,
			paddingRight: theme.spacing.unit * 2,
			paddingTop: theme.spacing.unit,
			paddingBottom: theme.spacing.unit,
			display: "flex"
		},
		default: {},
		gray: {
			backgroundColor: "#f5f7fa"
		},
		heading: {
			backgroundColor: "#000000",
			borderTopLeftRadius: 8,
			borderTopRightRadius: 8
		},
		subHeading: {
			backgroundColor: "#f5f7fa",
			fontFamily: fontFamilyDemiBold
		},
		headingText: {
			fontSize: theme.typography.fontSize,
			color: "#FFFFFF",
			textTransform: "capitalize"
		},
		total: {
			backgroundColor: primaryHex,
			borderBottomLeftRadius: 8,
			borderBottomRightRadius: 8
		},
		totalText: {
			color: "#FFFFFF",
			borderRadius: 4,
			fontFamily: fontFamilyDemiBold,
			textTransform: "uppercase"
		},
		text: {
			fontSize: theme.typography.fontSize
		},
		printText: {
			fontSize: theme.typography.fontSize * 0.8
		},
		subTotal1: {
			backgroundColor: "#a6becd"
		},
		subTotal2: {
			backgroundColor: "#97a3c5"
		}
	};
};

const TableRow = props => {
	const {
		columnStyles,
		heading,
		subHeading,
		subTotal1,
		subTotal2,
		gray,
		children,
		classes,
		total,
		printVersion,
		...rest
	} = props;

	const columns = children.map((text, index) => {
		return (
			<Typography
				noWrap
				className={classNames({
					[classes.headingText]: heading || subTotal1 || subTotal2,
					[classes.subHeading]: subHeading,
					[classes.text]: !printVersion && !heading,
					[classes.totalText]: total,
					[classes.printText]: printVersion
				})}
				key={index}
				style={columnStyles[index]}
			>
				{text}
			</Typography>
		);
	});

	return (
		<div
			className={classNames({
				[classes.root]: true,
				[classes.gray]: gray,
				[classes.total]: total,
				[classes.heading]: heading,
				[classes.subHeading]: subHeading,
				[classes.subTotal1]: subTotal1,
				[classes.subTotal2]: subTotal2
			})}
			{...rest}
		>
			{columns}
		</div>
	);
};

TableRow.propTypes = {
	classes: PropTypes.object.isRequired,
	columnStyles: PropTypes.array.isRequired,
	children: PropTypes.array.isRequired,
	gray: PropTypes.bool,
	total: PropTypes.bool,
	subTotal1: PropTypes.bool,
	subTotal2: PropTypes.bool,
	heading: PropTypes.bool,
	subHeading: PropTypes.bool,
	printVersion: PropTypes.bool
};

export default withStyles(styles)(TableRow);
