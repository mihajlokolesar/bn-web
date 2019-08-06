import React, { Component } from "react";
import PropTypes from "prop-types";
import classnames from "classnames";
import Dialog from "@material-ui/core/Dialog";
import Slide from "@material-ui/core/Slide";
import { Collapse, withStyles } from "@material-ui/core";
import Typography from "@material-ui/core/Typography";

import { fontFamilyDemiBold, secondaryHex } from "../../config/theme";
import Divider from "../common/Divider";

const styles = theme => ({
	root: {
		borderRadius: 4
	},
	paper: {
		backgroundColor: "transparent",
		outline: "none",
		boxShadow: "none",
		overflowY: "auto",
		maxHeight: "100%",
		width: "100%",
		height: "100%",
		display: "flex",
		flexDirection: "column",
		justifyContent: "flex-end"
	},
	onCloseDiv: {
		backgroundColor: "transparent",
		flex: 1
	},
	topOption: {
		borderRadius: "8px 8px 0px 0px"
	},
	option: {
		backgroundColor: "white",
		height: 64,
		textAlign: "center",
		display: "flex",
		justifyContent: "center",
		alignItems: "center",
		cursor: "pointer"
	},
	confirmOption: {
		backgroundColor: secondaryHex
	},
	confirmOptionDisabled: {
		backgroundColor: "#bababa"
	},
	optionText: {
		fontFamily: fontFamilyDemiBold,
		fontSize: 17
	},
	optionDisabledText: {
		color: "#bababa"
	},
	optionTextConfirm: {
		color: "white"
	},
	dividerContainer: {
		backgroundColor: "white"
	},
	icon: {
		width: 14,
		height: 14,
		marginRight: 12,
		marginBottom: 4
	}
});

const DialogTransition = props => {
	return <Slide direction="up" {...props}/>;
};

class MobileOptionsControlDialog extends Component {
	constructor(props) {
		super(props);

		this.state = {
			isConfirming: false
		};

		this.onClose = this.onClose.bind(this);
	}

	componentDidUpdate(prevProps, prevState, snapshot) {
		if (prevProps.open && !this.props.open) {
			this.reset();
		}
	}

	reset() {
		setTimeout(() => this.setState({ isConfirming: false }), 200);
	}

	onClose() {
		this.props.onClose();
		this.reset();
	}

	onConfirm(index) {
		this.setState({ isConfirming: true }, () => {
			const { options } = this.props;

			if (options && options[index]) {
				const { onClick } = options[index];
				if (onClick) {
					onClick();
				}
			}
		});
	}

	render() {
		const { classes, options, open } = this.props;
		const { isConfirming } = this.state;

		const controlOptions = [];

		if (options) {
			options.forEach((o, index) => {
				controlOptions.push(
					<React.Fragment key={index}>
						<div
							className={classnames({
								[classes.option]: true,
								[classes.topOption]: index === 0
							})}
							onClick={
								!isConfirming && !o.disabled
									? () => this.onConfirm(index)
									: null
							}
						>
							<Typography
								className={classnames({
									[classes.optionText]: true,
									[classes.optionDisabledText]: o.disabled || isConfirming
								})}
							>
								{o.label}
							</Typography>
						</div>
						{index !== options.length - 1 ? (
							<div className={classes.dividerContainer}>
								<Divider
									marginTop={0}
									style={{ paddingRight: 20, paddingLeft: 20 }}
								/>
							</div>
						) : null}
					</React.Fragment>
				);
			});
		}

		return (
			<Dialog
				fullScreen
				TransitionComponent={DialogTransition}
				onClose={this.onClose}
				aria-labelledby="dialog-title"
				PaperProps={{
					className: classes.paper
				}}
				BackdropProps={{ style: { backgroundColor: "transparent" } }}
				open={open}
			>
				<div onClick={this.onClose} className={classes.onCloseDiv}/>
				{controlOptions}

				<div
					className={classnames({
						[classes.option]: true,
						[classes.confirmOption]: true,
						[classes.confirmOptionDisabled]: isConfirming
					})}
					onClick={!isConfirming ? this.onClose.bind(this) : null}
				>
					<Typography
						className={classnames({
							[classes.optionText]: true,
							[classes.optionTextConfirm]: true
						})}
					>
						Cancel
					</Typography>
				</div>
			</Dialog>
		);
	}
}

MobileOptionsControlDialog.propTypes = {
	classes: PropTypes.object.isRequired,
	open: PropTypes.bool.isRequired,
	onClose: PropTypes.func.isRequired,
	options: PropTypes.arrayOf(
		PropTypes.shape({
			label: PropTypes.string.isRequired,
			onClick: PropTypes.func.isRequired,
			disabled: PropTypes.bool
		})
	).isRequired
};

export default withStyles(styles)(MobileOptionsControlDialog);
