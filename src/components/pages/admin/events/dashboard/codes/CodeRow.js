import React from "react";
import PropTypes from "prop-types";
import { withStyles, Typography } from "@material-ui/core";
import classNames from "classnames";

const styles = theme => {
	return {
		root: {
			paddingLeft: theme.spacing.unit * 2,
			paddingRight: theme.spacing.unit * 2,

			paddingTop: theme.spacing.unit * 2,
			paddingBottom: theme.spacing.unit * 2,

			display: "flex",
			borderRadius: 4
		},
		default: {},
		gray: {
			backgroundColor: "#f5f7fa"
		},
		active: {
			backgroundColor: theme.palette.secondary.main,
			color: "#FFFFFF"
		},
		text: {},
		activeText: {
			color: "#FFFFFF"
		},
		headingText: {
			fontSize: theme.typography.caption.fontSize
		},
		icon: {
			marginLeft: theme.spacing.unit * 2,
			width: 14,
			height: 14,
			cursor: "pointer"
		}
	};
};

const CodeRow = props => {
	const { heading, gray, active, children, classes, actions, ...rest } = props;

	const columnStyles = [
		{
			flex: 3,
			textAlign: "left",
			overflow: "hidden",
			whiteSpace: "nowrap",
			textOverflow: "ellipsis"
		},
		{
			flex: 3,
			textAlign: "left",
			overflow: "hidden",
			textOverflow: "ellipsis"
		},
		{
			flex: 3,
			textAlign: "left",
			overflow: "hidden",
			whiteSpace: "nowrap",
			textOverflow: "ellipsis"
		},

		{ flex: 2, textAlign: "right" },
		{ flex: 2, textAlign: "right" }
	];
	const actionStyle = { flex: 2, textAlign: "center" };

	const columns = children.map((text, index) => {
		const className = heading
			? classes.headingText
			: active
				? classes.activeText
				: classes.text;
		return (
			<Typography className={className} key={index} style={columnStyles[index]}>
				{text}
			</Typography>
		);
	});

	let actionButtons = <span style={actionStyle}>&nbsp;</span>;
	if (actions) {
		actionButtons = (
			<span style={actionStyle}>
				{actions.map(({ id, name, iconUrl, onClick }) => (
					<span
						key={name}
						onClick={e => {
							// e.stopPropagation();
							// e.nativeEvent.stopImmediatePropagation();
							onClick && onClick(id, name);
						}}
					>
						<img alt={name} src={iconUrl} className={classes.icon}/>
					</span>
				))}
			</span>
		);
	}

	return (
		<div
			className={classNames(
				classes.root,
				gray ? classes.gray : "",
				active ? classes.active : ""
			)}
			{...rest}
		>
			{columns}
			{actionButtons}
		</div>
	);
};

CodeRow.propTypes = {
	classes: PropTypes.object.isRequired,
	children: PropTypes.array.isRequired,
	gray: PropTypes.bool,
	active: PropTypes.bool,
	heading: PropTypes.bool,
	actions: PropTypes.array
};

export default withStyles(styles)(CodeRow);
