import React, { Component } from "react";
import PropTypes from "prop-types";
import {
	withStyles,
	Grid,
	InputAdornment,
	IconButton,
	Typography
} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import AddIcon from "@material-ui/icons/Add";

import InputGroup from "../../../../common/form/InputGroup";
import Button from "../../../../elements/Button";
import notifications from "../../../../../stores/notifications";
import Bigneon from "../../../../../helpers/bigneon";
import FeeRow from "./FeeRow";
import { fontFamilyDemiBold } from "../../../../../config/theme";
import Dialog from "../../../../elements/Dialog";
import Loader from "../../../../elements/loaders/Loader";

const styles = theme => ({
	dollarValue: {},
	tableHeading: {
		fontFamily: fontFamilyDemiBold
	},
	subHeadingText: {
		fontSize: 18
	},
	actionButtonContainer: {
		display: "flex",
		justifyContent: "space-between",
		paddingLeft: 10,
		paddingRight: 10,
		color: "gray"
	},
	creditCardFee: {
		textAlign: "left"
	},
	subHeadings: {
		display: "flex",
		justifyContent: "space-between",
		paddingLeft: theme.spacing.unit * 2,
		paddingRight: theme.spacing.unit * 6,
		marginTop: theme.spacing.unit * 6
	}
});

const DollarValue = ({ children }) => (
	<Typography>${(children / 100).toFixed(2)}</Typography>
);

class FeeSchedule extends Component {
	constructor(props) {
		super(props);

		this.state = {
			id: "",
			name: "",
			ranges: null,
			areYouSureDialogOpen: false,
			errors: {},
			isSubmitting: false
		};
	}

	componentDidMount() {
		this.refreshFees();
	}

	refreshFees() {
		const { organizationId } = this.props;

		Bigneon()
			.organizations.feeSchedules.index({ organization_id: organizationId })
			.then(response => {
				const { data, paging } = response; //@TODO Implement pagination

				const { id, name, ranges, message } = data;

				const formattedRanges = [];
				ranges.forEach(range => {
					const {
						company_fee_in_cents = 0,
						client_fee_in_cents = 0,
						min_price_in_cents = 0
					} = range;

					const formattedRange = {
						...range,
						min_price: min_price_in_cents / 100,
						total_fee: (company_fee_in_cents + client_fee_in_cents) / 100,
						company_fee: company_fee_in_cents / 100,
						client_fee: client_fee_in_cents / 100
					};
					formattedRanges.push(formattedRange);
				});

				if (id) {
					this.setState(
						{ id, name, ranges: formattedRanges },
						this.addZeroFeeRange.bind(this)
					);
				} else {
					this.addNewRange();
				}

				if (message) {
					notifications.show({ message });
				}

				this.refreshPerOrderFees();
			})
			.catch(error => {
				console.error(error);
				this.setState({ isSubmitting: false });

				let message = "Failed to retrieve existing fee schedule found.";

				if (error.response && error.response.status === 404) {
					message = "No existing fee schedule found.";

					//If there is not schedule, add a blank one
					this.addNewRange();
				}

				notifications.showFromErrorResponse({
					error,
					defaultMessage: message
				});
			});
	}

	addZeroFeeRange() {
		const { ranges } = this.state;

		const firstRange = ranges[0];

		if (firstRange && firstRange.min_price_in_cents !== 0) {
			this.addNewRange(0, {
				min_price: "0",
				company_fee: "0",
				client_fee: "0"
			});
		}
	}

	refreshPerOrderFees() {
		const { organizationId } = this.props;

		Bigneon()
			.organizations.read({ id: organizationId })
			.then(response => {
				const {
					client_event_fee_in_cents,
					company_event_fee_in_cents,
					event_fee_in_cents,
					cc_fee_percent,
					settlement_type
				} = response.data;

				this.setState({
					company_event_fee_in_cents,
					client_event_fee_in_cents,
					event_fee_in_cents,
					cc_fee_percent,
					settlement_type
				});
			})
			.catch(error => {
				console.error(error);
				this.setState({ isSubmitting: false });
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Loading organization per order fees failed."
				});
			});
	}

	addNewRange(index, defaultValues = {}) {
		let { ranges } = this.state;
		if (ranges === null) {
			ranges = [];
		}

		let insertAtIndex = 0;
		if (isNaN(index)) {
			insertAtIndex = ranges.length;
		} else if (index <= ranges.length) {
			insertAtIndex = index;
		}

		ranges.splice(insertAtIndex, 0, {
			min_price: "",
			fee: "",
			company_fee: "",
			client_fee: "",
			...defaultValues
		});

		this.setState({ ranges });
	}

	validateFields() {
		//Don't validate every field if the user has not tried to submit at least once
		if (!this.submitAttempted) {
			return true;
		}

		const { name, ranges } = this.state;

		const errors = {};

		if (name === "") {
			errors.name = "Missing fee schedule name.";
		}

		const rangesErrors = {};
		ranges.forEach(({ min_price, company_fee, client_fee }, index) => {
			const missingMinPrice = !min_price && min_price !== 0;
			const missingCompanyFee = !company_fee && company_fee !== 0;
			const missingClientFee = !client_fee && client_fee !== 0;

			if (missingMinPrice || missingCompanyFee || missingClientFee) {
				rangesErrors[index] = {};

				if (missingMinPrice) {
					rangesErrors[index].min_price = "Missing minimum price.";
				}

				if (missingCompanyFee) {
					rangesErrors[index].company_fee = "Missing fee.";
				}

				if (missingClientFee) {
					rangesErrors[index].client_fee = "Missing fee.";
				}
			}

			const previousMinPrice = ranges[index - 1]
				? Number(ranges[index - 1].min_price)
				: 0;
			if (index > 0 && min_price <= previousMinPrice) {
				if (!rangesErrors[index]) {
					rangesErrors[index] = {};
				}

				rangesErrors[
					index
				].min_price = `Minimum price must be more than $${previousMinPrice}.`;
			}
		});

		if (Object.keys(rangesErrors).length > 0) {
			errors.ranges = rangesErrors;
		}

		this.setState({ errors });

		if (Object.keys(errors).length > 0) {
			return false;
		}

		return true;
	}

	onSubmit(e) {
		e.preventDefault();

		this.submitAttempted = true;

		if (!this.validateFields()) {
			return false;
		}

		const { id } = this.state;

		if (id) {
			return this.setState({ areYouSureDialogOpen: true });
		}

		this.saveNewFeeSchedule();
	}

	saveNewFeeSchedule() {
		if (!this.validateFields()) {
			return false;
		}

		const { organizationId } = this.props;

		const { id, name, ranges } = this.state;

		this.setState({ isSubmitting: true });
		const formattedRanges = ranges.map(
			({ min_price, client_fee, company_fee }) => ({
				min_price_in_cents: Math.floor(Number(min_price) * 100),
				client_fee_in_cents: Math.floor(Number(client_fee) * 100),
				company_fee_in_cents: Math.floor(Number(company_fee) * 100)
			})
		);

		Bigneon()
			.organizations.feeSchedules.create({
				organization_id: organizationId,
				name,
				ranges: formattedRanges
			})
			.then(response => {
				this.onDialogClose();
				this.setState({ isSubmitting: false });

				notifications.show({
					message: "Fee schedule saved.",
					variant: "success"
				});

				this.refreshFees();
			})
			.catch(error => {
				this.onDialogClose();
				console.error(error);
				this.setState({ isSubmitting: false });

				notifications.showFromErrorResponse({
					defaultMessage: "Saving fee schedule failed.",
					error
				});
			});
	}

	updateMinPrice(index, min_price) {
		this.setState(({ ranges }) => {
			ranges[index].min_price = min_price;
			return { ranges };
		});
	}

	updateFee(index, isClient, fee) {
		this.setState(({ ranges }) => {
			const key = isClient ? "client_fee" : "company_fee";
			ranges[index][key] = fee;
			return { ranges };
		});
	}

	deleteRange(index) {
		this.setState(({ ranges }) => {
			ranges.splice(index, 1);
			return { ranges };
		});
	}

	onDialogClose() {
		this.setState({ areYouSureDialogOpen: false });
	}

	renderAreYouSureDialog() {
		const { areYouSureDialogOpen } = this.state;

		const onClose = this.onDialogClose.bind(this);

		return (
			<Dialog
				open={areYouSureDialogOpen}
				onClose={onClose}
				title={"Create this new fee schedule?"}
			>
				<div>
					<Typography>
						Adding a new fee schedule archives the previous one but existing
						events will still belong to the fee schedule that was active at the
						time the event was created.
					</Typography>
				</div>
				<div style={{ display: "flex", paddingTop: 20 }}>
					<Button style={{ marginRight: 5, flex: 1 }} onClick={onClose}>
						Cancel
					</Button>
					<Button
						style={{ marginLeft: 5, flex: 1 }}
						variant="callToAction"
						onClick={this.saveNewFeeSchedule.bind(this)}
						autoFocus
					>
						I Am Sure, Update Fee Schedule
					</Button>
				</div>
			</Dialog>
		);
	}

	renderForm() {
		const { name, ranges, errors, isSubmitting } = this.state;
		const { classes } = this.props;

		return (
			<div>
				{this.renderAreYouSureDialog()}
				<form noValidate autoComplete="off" onSubmit={this.onSubmit.bind(this)}>
					<InputGroup
						error={errors.name}
						value={name}
						name="name"
						label="Fee schedule name"
						type="text"
						onChange={e => this.setState({ name: e.target.value })}
						onBlur={this.validateFields.bind(this)}
					/>

					{ranges.map(({ min_price, client_fee, company_fee }, index) => (
						<Grid key={index} spacing={24} container alignItems={"center"}>
							<Grid item xs={12} sm={2} md={2} lg={2}>
								<InputGroup
									InputProps={{
										startAdornment: (
											<InputAdornment position="start">$</InputAdornment>
										)
									}}
									error={
										errors.ranges &&
										errors.ranges[index] &&
										errors.ranges[index].min_price
									}
									disabled={index === 0}
									value={min_price}
									name="min_price"
									label="Minimum price"
									type="number"
									onChange={e => this.updateMinPrice(index, e.target.value)}
									onBlur={this.validateFields.bind(this)}
								/>
							</Grid>
							<Grid item xs={12} sm={2} md={2} lg={2}>
								<InputGroup
									InputProps={{
										startAdornment: (
											<InputAdornment position="start">$</InputAdornment>
										)
									}}
									disabled={true}
									value={
										ranges.length - 1 >= index + 1
											? (ranges[index + 1].min_price < 0.01
												? 0
												: ranges[index + 1].min_price - 0.01
											  ).toFixed(2)
											: "and up"
									}
									onChange={() => {}}
									name="max_price"
									label="Maximum price"
									type="text"
								/>
							</Grid>
							<Grid item xs={12} sm={2} md={2} lg={2}>
								<InputGroup
									InputProps={{
										startAdornment: (
											<InputAdornment position="start">$</InputAdornment>
										)
									}}
									error={
										errors.ranges &&
										errors.ranges[index] &&
										errors.ranges[index].client_fee
									}
									value={client_fee}
									name="client_fee"
									label="Client Fee"
									type="number"
									onChange={e => this.updateFee(index, true, e.target.value)}
									onBlur={this.validateFields.bind(this)}
								/>
							</Grid>
							<Grid item xs={12} sm={2} md={2} lg={2}>
								<InputGroup
									InputProps={{
										startAdornment: (
											<InputAdornment position="start">$</InputAdornment>
										)
									}}
									error={
										errors.ranges &&
										errors.ranges[index] &&
										errors.ranges[index].company_fee
									}
									value={company_fee}
									name="company_fee"
									label="Big Neon Fee"
									type="number"
									onChange={e => this.updateFee(index, false, e.target.value)}
									onBlur={this.validateFields.bind(this)}
								/>
							</Grid>
							<Grid item xs={12} sm={2} md={2} lg={2}>
								<InputGroup
									InputProps={{
										startAdornment: (
											<InputAdornment position="start">$</InputAdornment>
										)
									}}
									disabled={true}
									value={(+company_fee + +client_fee).toFixed(2)}
									name="total_fee"
									label="Total Fee"
									type="number"
									onChange={() => {}}
								/>
							</Grid>

							<Grid
								item
								xs={12}
								sm={2}
								md={2}
								lg={2}
								className={classes.actionButtonContainer}
							>
								{index > 0 ? (
									<IconButton
										onClick={e => this.deleteRange(index)}
										color="inherit"
									>
										<DeleteIcon/>
									</IconButton>
								) : (
									<span/>
								)}

								<IconButton
									onClick={e => this.addNewRange(index + 1)}
									color="inherit"
								>
									<AddIcon/>
								</IconButton>
							</Grid>
						</Grid>
					))}

					<Button disabled={isSubmitting} type="submit" variant="callToAction">
						{isSubmitting ? "Updating..." : "Update Fee Schedule"}
					</Button>
				</form>
			</div>
		);
	}

	renderDisplay() {
		const { classes } = this.props;
		const {
			ranges,
			company_event_fee_in_cents,
			client_event_fee_in_cents,
			event_fee_in_cents,
			cc_fee_percent,
			settlement_type
		} = this.state;

		return (
			<div>
				<Typography>
					Fee schedules can only be modified by your account executive
				</Typography>

				<div className={classes.subHeadings}>
					<Typography className={classes.subHeadingText}>
						Per ticket fees
					</Typography>
					<Typography className={classes.subHeadingText}>
						Settlement type: {settlement_type}
					</Typography>
				</div>
				<FeeRow>
					<Typography className={classes.tableHeading}>Price (From)</Typography>
					<Typography className={classes.tableHeading}>Price (To)</Typography>
					<Typography className={classes.tableHeading}>Company</Typography>
					<Typography className={classes.tableHeading}>Client</Typography>
					<Typography className={classes.tableHeading}>Total</Typography>
				</FeeRow>
				{ranges.map((range, index) => {
					const {
						id,
						client_fee_in_cents,
						company_fee_in_cents,
						min_price_in_cents
					} = range;

					if (!id) {
						//Don't display the ranges not saved
						return null;
					}

					const totalCents = company_fee_in_cents + client_fee_in_cents;

					let maxPrice = false;
					const nextRange = ranges[index + 1];
					if (nextRange) {
						const { min_price_in_cents } = nextRange;
						maxPrice = min_price_in_cents - 1;
					}

					return (
						<FeeRow key={index} shaded={!(index % 2)}>
							<DollarValue>{min_price_in_cents}</DollarValue>
							{maxPrice ? (
								<DollarValue>{maxPrice}</DollarValue>
							) : (
								<Typography>and up</Typography>
							)}
							<DollarValue>{company_fee_in_cents}</DollarValue>
							<DollarValue>{client_fee_in_cents}</DollarValue>
							<DollarValue>{totalCents}</DollarValue>
						</FeeRow>
					);
				})}

				<div className={classes.subHeadings}>
					<Typography className={classes.subHeadingText}>
						Per order fees
					</Typography>
				</div>
				<FeeRow>
					<Typography className={classes.tableHeading}>Company</Typography>
					<Typography className={classes.tableHeading}>Client</Typography>
					<Typography
						className={[classes.tableHeading, classes.creditCardFee].join(" ")}
					>
						Credit Card
					</Typography>
					<Typography className={classes.tableHeading}>&nbsp;</Typography>
					<Typography className={classes.tableHeading}>Total</Typography>
				</FeeRow>
				<FeeRow shaded>
					<DollarValue>{company_event_fee_in_cents}</DollarValue>
					<DollarValue>{client_event_fee_in_cents}</DollarValue>
					<Typography className={classes.creditCardFee}>
						{cc_fee_percent}%
					</Typography>
					<Typography className={classes.tableHeading}>&nbsp;</Typography>
					<Typography>
						${(event_fee_in_cents / 100).toFixed(2)} + {cc_fee_percent}%
					</Typography>
				</FeeRow>
			</div>
		);
	}

	render() {
		const { type } = this.props;
		const { ranges } = this.state;

		if (ranges === null) {
			return <Loader/>;
		}

		if (type === "read-write") {
			return this.renderForm();
		}

		return this.renderDisplay();
	}
}

FeeSchedule.propTypes = {
	classes: PropTypes.object.isRequired,
	organizationId: PropTypes.string.isRequired,
	type: PropTypes.oneOf(["read-write", "read"]).isRequired
};

export default withStyles(styles)(FeeSchedule);
