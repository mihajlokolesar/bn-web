//Base component https://material-ui.com/api/button/
import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import classNames from "classnames";
import servedImage from "../../helpers/imagePathHelper";

import {
	textColorPrimary,
	primaryHex,
	secondaryHex,
	warningHex,
	callToActionBackground,
	fontFamilyDemiBold
} from "../../config/theme";

const styles = theme => {
	return {
		root: {
			borderRadius: 8,
			borderStyle: "none",
			paddingLeft: 20,
			paddingRight: 20,
			paddingTop: 10,
			paddingBottom: 10,
			[theme.breakpoints.down("sm")]: {
				paddingLeft: 10,
				paddingRight: 10
			}
		},
		primary: {
			background: primaryHex,
			color: "#FFF"
		},
		secondary: {
			background: secondaryHex,
			color: "#FFF"
		},
		default: {
			background: `linear-gradient(45deg, #FFF 10%, #FFF 90%)`,
			color: textColorPrimary,
			border: "solid 0.5px #979797"
		},
		callToAction: {
			background: callToActionBackground,
			color: "#FFF",
			boxShadow: "0 2px 7.5px 1px rgba(112, 124, 237, 0.47)",
			backgroundRepeat: "no-repeat"
		},
		whiteCTA: {
			background: "#FFFFFF",
			color: secondaryHex,
			boxShadow: "none",
			backgroundRepeat: "no-repeat",
			borderColor: "#FFFFFF"
		},
		warning: {
			background: `linear-gradient(45deg, ${warningHex} 10%, ${warningHex} 90%)`,
			color: "#FFF",
			backgroundRepeat: "no-repeat"
		},
		additional: {
			height: 20,
			borderRadius: 20,
			backgroundColor: "rgba(112, 124, 237, 0.12)",
			boxShadow: "none"
		},
		addIcon: {
			position: "relative",
			top: -2,
			right: -10
		},
		text: {
			background: "transparent",
			boxShadow: "none"
		},
		label: {
			textTransform: "capitalize"
		},
		boldLabel: {
			textTransform: "capitalize",
			fontFamily: fontFamilyDemiBold
		},
		leftIcon: {
			marginRight: theme.spacing.unit,
			marginBottom: 2,
			width: 25,
			height: 25
		},
		small: {
			height: 30
		},
		medium: {
			paddingBottom: 6
		},
		mediumLarge: {
			height: 50
		},
		large: {
			height: 55
		}
	};
};

const CustomButton = props => {
	const {
		classes,
		children,
		variant,
		disabled,
		iconUrl,
		size,
		style,
		...rest
	} = props;

	const rootStyle = style || {};
	if (size === "small") {
		rootStyle.paddingTop = 8;
	} else if (size === "mediumLarge") {
		rootStyle.paddingTop = 14;
	}

	return (
		<Button
			classes={{
				root: classNames(
					classes.root,
					!disabled ? classes[variant] : classes.default,
					classes[size]
				),
				label:
					variant === "additional" || variant === "text"
						? classes.label
						: classes.boldLabel
			}}
			disabled={disabled}
			style={rootStyle}
			{...rest}
		>
			{iconUrl ? (
				<img
					alt={children}
					className={classes.leftIcon}
					src={servedImage(iconUrl)}
				/>
			) : null}

			{children}
			{variant === "additional" ? (
				<img
					alt={children}
					className={classes.addIcon}
					src={servedImage("/icons/add-active.svg")}
				/>
			) : null}
		</Button>
	);
};
CustomButton.defaultProps = {
	size: "medium",
	variant: "default"
};

CustomButton.propTypes = {
	classes: PropTypes.object.isRequired,
	variant: PropTypes.oneOf([
		"primary",
		"secondary",
		"default",
		"callToAction",
		"warning",
		"text",
		"additional",
		"whiteCTA"
	]),
	size: PropTypes.oneOf(["small", "medium", "mediumLarge", "large"]),
	disabled: PropTypes.bool,
	children: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.element,
		PropTypes.array
	]).isRequired,
	iconUrl: PropTypes.string
};

export default withStyles(styles)(CustomButton);
