import React, { Component } from "react";
import { withStyles } from "@material-ui/core";

import InputGroup from "../../../../common/form/InputGroup";
import Button from "../../../../elements/Button";
import user from "../../../../../stores/user";
import notifications from "../../../../../stores/notifications";
import { validPhone } from "../../../../../validators";
import LocationInputGroup from "../../../../common/form/LocationInputGroup";
import addressTypeFromGoogleResult from "../../../../../helpers/addressTypeFromGoogleResult";
import Bigneon from "../../../../../helpers/bigneon";
import removePhoneFormatting from "../../../../../helpers/removePhoneFormatting";
import moment from "moment-timezone";
import SelectGroup from "../../../../common/form/SelectGroup";

const styles = theme => ({});

class OrganizationUpdate extends Component {
	constructor(props) {
		super(props);

		//Check if we're editing an existing organization
		this.state = {
			name: "",
			owner_user_id: "",
			phone: "",
			address: "",
			city: "",
			state: "",
			country: "",
			zip: "",
			timezone: moment.tz.guess(),
			errors: {},
			isSubmitting: false,
			showApiKeys: false
		};
	}

	componentDidMount() {
		const { organizationId } = this.props;

		if (organizationId) {
			Bigneon()
				.organizations.read({ id: organizationId })
				.then(response => {
					const {
						owner_user_id,
						name,
						phone,
						address,
						city,
						state,
						country,
						zip,
						timezone
					} = response.data;

					this.setState({
						name: name || "",
						owner_user_id: owner_user_id || "",
						phone: phone || "",
						address: address || "",
						city: city || "",
						state: state || "",
						country: country || "",
						zip: zip || "",
						timezone: timezone || moment.tz.guess()
					});
				})
				.catch(error => {
					console.error(error);

					this.setState({ isSubmitting: false });
					notifications.showFromErrorResponse({
						defaultMessage: "Loading organization details failed.",
						error
					});
				});
		}
	}

	validateFields() {
		//Don't validate every field if the user has not tried to submit at least once
		if (!this.submitAttempted) {
			return true;
		}

		const { name, address, eventFee, timezone } = this.state;
		const { organizationId } = this.props;
		const phone = removePhoneFormatting(this.state.phone);

		const errors = {};

		if (!name) {
			errors.name = "Missing organization name.";
		}

		if (!address) {
			errors.address = "Missing address.";
		}

		if (!phone) {
			errors.phone = "Missing phone number.";
		} else if (!validPhone(phone)) {
			errors.phone = "Invalid phone number.";
		}

		if (!timezone) {
			errors.timezone = "Missing timezone";
		}

		this.setState({ errors });

		if (Object.keys(errors).length > 0) {
			return false;
		}

		return true;
	}

	createNewOrganization(params, onSuccess) {
		Bigneon()
			.organizations.create(params)
			.then(response => {
				const { id } = response.data;
				onSuccess(id);

				//After a new org is created, assume the user wants to select it
				user.loadAllPossibleOrgs();
				user.setCurrentOrganizationRolesAndScopes(id);
			})
			.catch(error => {
				console.error(error);
				this.setState({ isSubmitting: false });

				notifications.showFromErrorResponse({
					defaultMessage: "Create organization failed.",
					error
				});
			});
	}

	updateOrganization(id, params, onSuccess) {
		//Remove owner_user_id
		Bigneon()
			.organizations.update({ id, ...params })
			.then(() => {
				onSuccess(id);
			})
			.catch(error => {
				this.setState({ isSubmitting: false });

				notifications.showFromErrorResponse({
					defaultMessage: "Update organization failed.",
					error
				});
			});
	}

	onSubmit(e) {
		e.preventDefault();

		this.submitAttempted = true;

		if (!this.validateFields()) {
			this.setState({ showManualEntry: true });
			return false;
		}

		const {
			owner_user_id,
			name,
			phone,
			address,
			city,
			state,
			country,
			zip,
			timezone
		} = this.state;
		const { organizationId } = this.props;

		const orgDetails = {
			name,
			phone,
			address,
			city,
			state,
			country,
			zip,
			timezone
		};

		//If we're updating an existing org
		if (organizationId) {
			this.updateOrganization(organizationId, orgDetails, () => {
				this.setState({ isSubmitting: false });

				notifications.show({
					message: "Organization updated",
					variant: "success"
				});

				this.props.history.push(`/admin/organizations/${organizationId}`);
			});

			return;
		}

		//Got the user ID, now create the organization
		this.createNewOrganization(orgDetails, organizationId => {
			notifications.show({
				message: "Organization created",
				variant: "success"
			});

			this.setState({ isSubmitting: false }, () => {
				this.props.history.push(`/admin/organizations/${organizationId}`);
			});
		});
	}

	renderTimezones() {
		//TODO This is an exact duplicate of src/components/pages/admin/venues/Venue.js lets keep the code DRY
		const { timezone, errors } = this.state;
		const timezones = moment.tz.names().map(name => ({
			value: name,
			label: name
		}));
		return (
			<SelectGroup
				value={timezone}
				items={timezones}
				error={errors.timezone}
				name={"timezone"}
				label={"Timezone"}
				onBlur={this.validateFields.bind(this)}
				onChange={e => this.setState({ timezone: e.target.value })}
			/>
		);
	}

	render() {
		const {
			owner_user_id,
			name,
			address = "",
			city = "",
			state = "",
			country = "",
			zip = "",
			latitude = "",
			longitude = "",
			phone,
			errors,
			isSubmitting,
			showManualEntry
		} = this.state;

		const { organizationId } = this.props;

		const addressBlock = {
			address,
			city,
			state,
			country,
			zip,
			latitude,
			longitude
		};
		const { classes } = this.props;

		return (
			<div>
				<form noValidate autoComplete="off" onSubmit={this.onSubmit.bind(this)}>
					<InputGroup
						error={errors.name}
						value={name}
						name="name"
						label="Organization name"
						type="text"
						onChange={e => this.setState({ name: e.target.value })}
						onBlur={this.validateFields.bind(this)}
					/>

					<InputGroup
						error={errors.phone}
						value={phone}
						name="phone"
						label="Phone number"
						type="phone"
						onChange={e => this.setState({ phone: e.target.value })}
						onBlur={this.validateFields.bind(this)}
					/>

					{this.renderTimezones()}

					<LocationInputGroup
						error={errors.address}
						label="Organization address"
						address={address}
						addressBlock={addressBlock}
						showManualEntry={showManualEntry}
						onError={error => {
							this.setState({ showManualEntry: true });
							notifications.show({
								message: `Google API error: ${error}`, //TODO add more details here
								variant: "error"
							});
						}}
						onAddressChange={address => this.setState({ address })}
						onLatLngResult={latLng => {
							this.setState({
								latitude: latLng.lat,
								longitude: latLng.lng
							});
						}}
						onFullResult={result => {
							const city = addressTypeFromGoogleResult(result, "locality");
							const state = addressTypeFromGoogleResult(
								result,
								"administrative_area_level_1"
							);
							const country = addressTypeFromGoogleResult(result, "country");

							const zip = addressTypeFromGoogleResult(result, "postal_code");

							this.setState({ city, state, country, zip });
						}}
					/>

					<Button
						disabled={isSubmitting}
						type="submit"
						style={{ marginRight: 10, marginTop: 20 }}
						variant="callToAction"
					>
						{isSubmitting
							? organizationId
								? "Creating..."
								: "Updating..."
							: organizationId
								? "Update"
								: "Create"}
					</Button>
				</form>
			</div>
		);
	}
}

export default withStyles(styles)(OrganizationUpdate);
