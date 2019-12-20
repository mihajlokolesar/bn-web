import React from "react";
import PropTypes from "prop-types";
import { Typography, withStyles } from "@material-ui/core";
import CardMedia from "@material-ui/core/CardMedia";

import DateTimePickerGroup from "../../../../common/form/DateTimePickerGroup";
import FormatInputLabel from "../../../../elements/form/FormatInputLabel";
import IconButton from "../../../../elements/IconButton";
import CheckBox from "../../../../elements/form/CheckBox";

const styles = theme => ({
	root: {
		display: "flex"
	},
	image: {
		height: 200,
		width: 200,
		borderRadius: 0
	},
	content: {
		padding: 16,
		paddingTop: 24,
		display: "flex",
		flex: 2,

		[theme.breakpoints.down("sm")]: {
			paddingLeft: theme.spacing.unit,
			paddingBottom: theme.spacing.unit / 2
		}
	},
	leftColumn: {
		flex: 1,
		flexDirection: "column",
		justifyContent: "space-between",
		display: "flex",
		[theme.breakpoints.down("sm")]: {
			flex: 4
		}
	},
	rightColumn: {
		flex: 1,
		display: "flex",
		flexDirection: "column",
		alignItems: "flex-end",
		justifyContent: "space-between"
	}
});

const EventArtist = props => {
	const {
		classes,
		imgUrl,
		title,
		typeHeading,
		setTime,
		onChangeSetTime,
		onDelete,
		error,
		onBlur,
		socialAccounts,
		importance,
		onChangeImportance,
		onMoveUp,
		onMoveDown
	} = props;

	return (
		<div className={classes.root}>
			<CardMedia className={classes.image} image={imgUrl} title={"Artist"}/>
			<div className={classes.content}>
				<div className={classes.leftColumn}>
					<div>
						<Typography variant="body1" style={{ marginTop: 10 }}>
							<FormatInputLabel>{typeHeading}</FormatInputLabel>
						</Typography>
						<Typography variant="title">{title}</Typography>
					</div>

					{/*<DateTimePickerGroup*/}
					{/*	margin="dense"*/}
					{/*	error={error ? error.setTime : null}*/}
					{/*	value={setTime}*/}
					{/*	name="setTime"*/}
					{/*	label="Set time"*/}
					{/*	onChange={onChangeSetTime}*/}
					{/*	format="HH:mm"*/}
					{/*	type="time"*/}
					{/*	onBlur={onBlur}*/}
					{/*/>*/}
					<CheckBox
						active={importance === 0}
						onClick={() => {
							onChangeImportance(importance);
						}}
					>
						Headline act
					</CheckBox>
				</div>

				<div className={classes.rightColumn}>
					<div>
						<IconButton
							onClick={onMoveUp}
							iconUrl={`/icons/up-gray.svg`}
							disabled={!onMoveUp}
						>
							Move up
						</IconButton>
						<IconButton
							onClick={onMoveDown}
							iconUrl={`/icons/down-gray.svg`}
							disabled={!onMoveDown}
						>
							Move down
						</IconButton>
						<IconButton onClick={onDelete} iconUrl="/icons/delete-gray.svg">
							Delete
						</IconButton>
					</div>

					<div>
						{/* //TODO place back when we have icons and links */}
						{/* {Object.keys(socialAccounts).map(
							account =>
								socialAccounts[account] ? (
									<SocialIconLink
										color="black"
										key={account}
										icon={account}
										size={30}
									/>
								) : null
						)} */}
					</div>
				</div>
			</div>
		</div>
	);
};

EventArtist.propTypes = {
	classes: PropTypes.object.isRequired,
	imgUrl: PropTypes.string.isRequired,
	title: PropTypes.string.isRequired,
	typeHeading: PropTypes.string.isRequired,
	setTime: PropTypes.object,
	onChangeSetTime: PropTypes.func.isRequired,
	onDelete: PropTypes.func.isRequired,
	error: PropTypes.object,
	onBlur: PropTypes.func,
	socialAccounts: PropTypes.object.isRequired,
	importance: PropTypes.number.isRequired,
	onChangeImportance: PropTypes.func,
	onMoveUp: PropTypes.func,
	onMoveDown: PropTypes.func
};

export default withStyles(styles)(EventArtist);
