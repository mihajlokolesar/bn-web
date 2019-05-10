import React, { Component } from "react";
import { Hidden, Typography, withStyles, CardMedia } from "@material-ui/core";

import notifications from "../../../../../../../stores/notifications";
import Button from "../../../../../../elements/Button";
import Bigneon from "../../../../../../../helpers/bigneon";
import Divider from "../../../../../../common/Divider";
import HoldRow from "./ChildRow";
import ChildDialog from "./ChildDialog";
import Container from "../../Container";
import Loader from "../../../../../../elements/loaders/Loader";
import user from "../../../../../../../stores/user";
import {
	fontFamilyDemiBold,
	secondaryHex
} from "../../../../../../../config/theme";

const styles = theme => ({
	root: {},
	pageTitle: {
		fontFamily: fontFamilyDemiBold,
		fontSize: theme.typography.fontSize * 1.75
	},
	mobilePageTitleContainer: {
		marginTop: theme.spacing.unit * 2,
		marginBottom: theme.spacing.unit * 2
	}
});

class ChildrenList extends Component {
	constructor(props) {
		super(props);

		this.eventId = this.props.match.params.id;
		this.holdId = this.props.match.params.holdId;

		this.state = {
			activeHoldId: null, //TODO check this is not used and remove if not
			showDialog: null,
			ticketTypes: [],
			children: [],
			holdDetails: {}
		};
	}

	componentDidMount() {
		this.loadEventDetails();

		Bigneon()
			.holds.read({ id: this.holdId })
			.then(response => {
				const holdDetails = response.data;
				this.setState({ holdDetails }, () => this.refreshChildren());
			})
			.catch(error => {
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Failed to load holds."
				});
			});
	}

	loadEventDetails() {
		Bigneon()
			.events.read({ id: this.eventId })
			.then(response => {
				const { name, ticket_types } = response.data;
				this.setState({
					ticketTypes: ticket_types
				});
			})
			.catch(error => {
				console.error(error);
				this.setState({ isSubmitting: false });

				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Loading event details failed."
				});
			});
	}

	refreshChildren() {
		if (this.eventId && this.holdId) {
			Bigneon()
				.holds.children.index({ hold_id: this.holdId })
				.then(response => {
					//TODO Pagination
					this.setState({ children: response.data.data });
				})
				.catch(error => {
					notifications.showFromErrorResponse({
						error,
						defaultMessage: "Refreshing holds failed."
					});
				});
		}
	}

	onAddHold() {
		this.setState({
			activeHoldId: null,
			showDialog: true
		});
	}

	renderDesktopHeadings() {
		const ths = [
			"Name",
			"Code",
			"Status",
			"Total Held",
			"Claimed",
			"Remaining",
			"Action"
		];

		return <HoldRow heading>{ths}</HoldRow>;
	}

	renderList() {
		const { children, hoverId } = this.state;
		const { classes } = this.props;

		if (children === null) {
			return <Loader/>;
		}

		if (children && children.length > 0) {
			const onAction = (id, action) => {
				// if (action === "Edit") {
				// 	this.setState({ activeHoldId: id, showDialog: true, holdType: HOLD_TYPES.EDIT })
				// }
				// if (action === "Split") {
				// 	this.setState({ activeHoldId: id, showDialog: true, holdType: HOLD_TYPES.SPLIT });
				// }
			};

			return (
				<div>
					<Hidden smDown>{this.renderDesktopHeadings()}</Hidden>
					{children.map((ticket, index) => {
						const { id, name, redemption_code, quantity, available } = ticket;

						const claimed = quantity - available;
						const status = claimed === 0 ? "Unclaimed" : "Claimed";

						const tds = [
							name,
							redemption_code,
							status,
							quantity,
							claimed,
							`${quantity - claimed}`
						];

						const active = false; //Might use this later, right now no need to highlight
						const iconColor = active ? "white" : "gray";
						return (
							<HoldRow
								onMouseEnter={e => this.setState({ hoverId: id })}
								onMouseLeave={e => this.setState({ hoverId: null })}
								active={active}
								gray={!(index % 2)}
								key={id}
								actions={[
									{
										id: id,
										name: "Link",
										iconUrl: `/icons/link-${iconColor}.svg`,
										onClick: onAction.bind(this)
									},
									{
										id: id,
										name: "Edit",
										iconUrl: `/icons/edit-${iconColor}.svg`,
										onClick: onAction.bind(this)
									},
									{
										id: id,
										name: "Delete",
										iconUrl: `/icons/delete-${iconColor}.svg`,
										onClick: onAction.bind(this)
									}
								]}
							>
								{tds}
							</HoldRow>
						);
					})}
				</div>
			);
		} else {
			return <Typography variant="body1">No names added yet</Typography>;
		}
	}

	renderDialog() {
		const { ticketTypes, activeHoldId } = this.state;
		const eventId = this.eventId;
		const holdId = this.holdId;
		return (
			<ChildDialog
				open={true}
				eventId={eventId}
				holdId={holdId}
				ticketTypes={ticketTypes}
				onSuccess={id => {
					this.refreshChildren();
					this.setState({ showDialog: null });
				}}
				onClose={() => this.setState({ showDialog: null })}
			/>
		);
	}

	renderMobileContent(createButtonsArray) {
		const { holdDetails } = this.state;
		const { classes } = this.props;
		return (
			<Container
				eventId={this.eventId}
				subheading={"tools"}
				layout={"childrenOutsideNoCard"}
			>
				<div className={classes.mobilePageTitleContainer}>
					<Typography className={classes.pageTitle}>
						{holdDetails.name}
					</Typography>
				</div>
				<div style={{ display: "flex" }}>{createButtonsArray}</div>
				{this.renderList()}
			</Container>
		);
	}

	renderDesktopContent(createButtonsArray) {
		const { holdDetails } = this.state;
		return (
			<Container
				eventId={this.eventId}
				subheading={"tools"}
				layout={"childrenInsideCard"}
			>
				<div style={{ display: "flex" }}>
					<Typography variant="title">{holdDetails.name}</Typography>
					<span style={{ flex: 1 }}/>
					{createButtonsArray}
				</div>

				<Divider style={{ marginBottom: 40 }}/>

				{this.renderList()}
			</Container>
		);
	}

	render() {
		const { showDialog } = this.state;

		const createButtonsArray = [
			user.hasScope("hold:write") ? (
				<Button onClick={e => this.onAddHold()} variant={"secondary"}>
					Assign Name To List
				</Button>
			) : (
				<span/>
			)
		];

		return (
			<div>
				{showDialog && this.renderDialog()}
				<Hidden smDown>{this.renderDesktopContent(createButtonsArray)}</Hidden>
				<Hidden mdUp>{this.renderMobileContent(createButtonsArray)}</Hidden>
			</div>
		);
	}
}

export default withStyles(styles)(ChildrenList);
