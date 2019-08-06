import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

const styles = theme => {
	return {
		root: {
			width: "100%",
			display: "flex",
			border: "1px solid #D1D1D1",
			borderRadius: 8,
			alignItems: "center",
			backgroundColor: "#FFFFFF"
		},
		inputContainer: {
			height: 50,
			padding: theme.spacing.unit / 2,
			paddingLeft: theme.spacing.unit * 2,
			flex: 1
		},
		input: {
			fontSize: theme.typography.fontSize * 0.9,
			width: "100%",
			height: "100%",
			borderStyle: "none"
		},
		areaContainer: {
			padding: theme.spacing.unit * 2,
			paddingLeft: theme.spacing.unit * 2,
			flex: 1
		},
		textarea: {
			height: 130,
			fontSize: theme.typography.fontSize * 0.9,
			borderStyle: "none",
			width: "100%",
			outline: "none !important"
		}
	};
};

const BoxInput = props => {
	const { classes, style, variant, ...rest } = props;

	switch (variant) {
		case "textarea": {
			return (
				<div className={classes.root} style={style}>
					<div className={classes.areaContainer}>
						<textarea className={classes.textarea} {...rest}/>{" "}
					</div>
				</div>
			);
		}
		case "input":
		default: {
			return (
				<div className={classes.root} style={style}>
					<div className={classes.inputContainer}>
						<input className={classes.input} {...rest}/>
					</div>
				</div>
			);
		}
	}
};

BoxInput.defaultPropTypes = {
	style: {},
	variant: "input"
};

BoxInput.propTypes = {
	classes: PropTypes.object.isRequired,
	name: PropTypes.string.isRequired,
	placeholder: PropTypes.string,
	style: PropTypes.object,
	variant: PropTypes.oneOf(["input", "textarea"])
};

export default withStyles(styles)(BoxInput);
