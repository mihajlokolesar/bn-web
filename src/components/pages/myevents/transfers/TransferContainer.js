import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core";

const styles = theme => ({
	root: {
		background:
			"linear-gradient(-135deg, rgb(229, 61, 150) 0%, rgb(84, 145, 204) 100%)",
		minHeight: "110vh",
		padding: 12
	}
});

const TransferContainer = props => {
	const { children, classes } = props;

	return <div className={classes.root}>{children}</div>;
};

TransferContainer.propTypes = {
	classes: PropTypes.object.isRequired,
	children: PropTypes.any.isRequired
};

export default withStyles(styles)(TransferContainer);
