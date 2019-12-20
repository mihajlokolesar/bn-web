//Base component https://material-ui.com/api/button/
import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import IconButton from "@material-ui/core/IconButton";
import classNames from "classnames";
import servedImage from "../../helpers/imagePathHelper";

const styles = theme => {
	return {
		root: {
			border: 0
		},
		icon: {}
	};
};

const CustomIconButton = props => {
	const { children, classes, iconUrl, ...rest } = props;

	return (
		<IconButton
			classes={{
				root: classNames(classes.root)
			}}
			{...rest}
		>
			<img
				alt={children || ""}
				className={classes.icon}
				src={servedImage(iconUrl)}
			/>
		</IconButton>
	);
};
CustomIconButton.defaultProps = {};

CustomIconButton.propTypes = {
	classes: PropTypes.object.isRequired,
	children: PropTypes.oneOfType([PropTypes.string]),
	iconUrl: PropTypes.string
};

export default withStyles(styles)(CustomIconButton);
