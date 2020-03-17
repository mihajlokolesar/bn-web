import React, { Component } from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import linkifyHtml from "linkifyjs/html";
import classnames from "classnames";

import { secondaryHex, fontFamilyDemiBold } from "../../../config/theme";
import "./rich-event-description.css";

const styles = theme => ({
	root: {},
	readMoreLink: {
		font: "inherit",
		color: secondaryHex,
		cursor: "pointer",
		[theme.breakpoints.up("md")]: {
			display: "none"
		}
	},
	shortenedTextBlock: {
		[theme.breakpoints.down("sm")]: {
			overflow: "hidden",
			display: "-webkit-box",
			WebkitLineClamp: "3",
			WebkitBoxOrient: "vertical",
			textOverflow: "ellipsis"
		}
	},
	descriptionLink: {
		font: "inherit",
		color: secondaryHex,
		cursor: "pointer",
		fontFamily: fontFamilyDemiBold
	}
});

class FormattedAdditionalInfo extends Component {
	constructor(props) {
		super(props);

		this.state = {
			showAllAdditionalInfo: false
		};
	}

	showHideMoreAdditionalInfo() {
		this.setState(({ showAllAdditionalInfo }) => ({
			showAllAdditionalInfo: !showAllAdditionalInfo
		}));
	}

	render() {
		const { classes, children } = this.props;

		if (!children) {
			return null;
		}

		const { showAllAdditionalInfo } = this.state;

		const options = {
			nl2br: true,
			className: classes.descriptionLink
		};

		const linkifiedText = linkifyHtml(children, options);

		return (
			<div className={classes.root}>
				<span
					className={classnames({
						[classes.shortenedTextBlock]: !showAllAdditionalInfo
					})}
				>
					<div
						className={"rich-edit-content"}
						dangerouslySetInnerHTML={{ __html: linkifiedText }}
					/>
				</span>

				<span
					className={classes.readMoreLink}
					onClick={this.showHideMoreAdditionalInfo.bind(this)}
				>
					{showAllAdditionalInfo ? " Read less" : " Read more"}
				</span>
			</div>
		);
	}
}

FormattedAdditionalInfo.defaultProps = { children: "" };

FormattedAdditionalInfo.propTypes = {
	classes: PropTypes.object.isRequired,
	children: PropTypes.string
};

export default withStyles(styles)(FormattedAdditionalInfo);
