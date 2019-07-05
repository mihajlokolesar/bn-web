import React, { Component } from "react";
import { withStyles, Grid } from "@material-ui/core";
import { observer } from "mobx-react";

import Bigneon from "../../../../../../helpers/bigneon";
import notifications from "../../../../../../stores/notifications";
import GuestRow from "./GuestRow";
import BottomRefundBar from "./BottomRefundBar";
import ConfirmRefundDialog from "./ConfirmRefundDialog";
import PageHeading from "../../../../../elements/PageHeading";
import Loader from "../../../../../elements/loaders/Loader";
import CancelTransferDialog from "../../../../myevents/transfers/CancelTransferDialog";
import {
	Pagination,
	resetUrlPageParam,
	urlPageParam
} from "../../../../../elements/pagination";
import SearchInput from "../../../../../elements/SearchBox";

const styles = theme => ({
	root: {},
	filterOptions: {
		marginBottom: theme.spacing.unit * 2
	}
});

@observer
class Refunds extends Component {
	constructor(props) {
		super(props);

		this.state = {
			eventName: "",
			filteredGuests: null,
			searchQuery: "",
			expandedUserId: null,
			selectedTickets: {},
			isRefunding: false,
			showConfirmRefund: false,
			refundComplete: false,
			orderPaging: null,
			cancelTransferKey: null
		};

		this.onExpandChange = this.onExpandChange.bind(this);
		this.onTicketSelect = this.onTicketSelect.bind(this);
		this.onOpenCancelTransferDialog = this.onOpenCancelTransferDialog.bind(
			this
		);
	}

	componentDidMount() {
		this.refreshGuests();

		const eventId = this.props.match.params.id;

		Bigneon()
			.events.read({ id: eventId })
			.then(response => {
				const { name } = response.data;
				this.setState({ eventName: name });
			})
			.catch(error => {
				console.error(error);
				notifications.showFromErrorResponse({
					defaultMessage: "Loading event details failed.",
					error
				});
			});
	}

	componentWillUnmount() {
		if (this.timeout) {
			clearTimeout(this.timeout);
		}
	}

	onOpenCancelTransferDialog(cancelTransferKey) {
		this.setState({ cancelTransferKey });
	}

	resetSelected(e) {
		this.setState({
			selectedTickets: {},
			selectedHolds: {}
		});
	}

	changePage(page = urlPageParam()) {
		this.resetSelected();
		this.refreshGuests(page, this.state.searchQuery);
	}

	onSearch(query) {
		this.resetSelected();
		resetUrlPageParam();
		this.setState({ searchQuery: query }, () => this.refreshGuests(0, query));
	}

	refreshGuests(page = 0, query = "") {
		const event_id = this.props.match.params.id;
		Bigneon()
			.events.guests.index({ event_id, query, limit: 100, page })
			.then(response => {
				const { data, paging } = response.data;
				const guests = {};

				data.forEach(
					({
						user_id,
						email,
						first_name,
						last_name,
						phone,
						...ticketDetails
					}) => {
						if (!guests[user_id]) {
							guests[user_id] = {
								email,
								first_name,
								last_name,
								phone,
								tickets: [ticketDetails]
							};
						} else {
							guests[user_id].tickets = [
								...guests[user_id].tickets,
								ticketDetails
							];
						}
					}
				);
				this.setState({ guests, orderPaging: paging });
			})
			.catch(error => {
				console.error(error);

				notifications.showFromErrorResponse({
					defaultMessage: "Loading guests failed.",
					error
				});
			});
	}

	stringContainedInArray(strList, searchQuery) {
		for (let index = 0; index < strList.length; index++) {
			const str = strList[index] ? strList[index].toLowerCase() : "";

			if (str.includes(searchQuery.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	filterGuestsOnQuery(e) {
		this.setState({
			searchQuery: e.target.value,
			expandedUserId: null,
			selectedTickets: {}
		});
	}

	filteredGuests() {
		const { guests } = this.state;
		if (!guests) {
			return {};
		}

		const { searchQuery } = this.state;

		//Filtering required
		const filteredGuests = {};
		Object.keys(guests).forEach(user_id => {
			const { first_name, last_name, email, phone, tickets } = guests[user_id];
			const ticketIds = [];
			const orderIds = [];

			tickets.forEach(({ id, order_id }) => {
				ticketIds.push(id);
				orderIds.push(order_id);
			});

			filteredGuests[user_id] = guests[user_id];

			// if (
			// 	this.stringContainedInArray(
			// 		[first_name, last_name, email, phone, ...ticketIds, ...orderIds],
			// 		searchQuery.replace("#", "")
			// 	)
			// ) {
			// 	filteredGuests[user_id] = guests[user_id];
			// }
		});

		return filteredGuests;
	}

	onExpandChange(expandedUserId) {
		this.setState({ expandedUserId, selectedTickets: {} });
	}

	onTicketSelect({ id, ...ticket }) {
		this.setState(({ selectedTickets }) => {
			if (selectedTickets[id]) {
				delete selectedTickets[id];
			} else {
				selectedTickets[id] = ticket;
			}

			return { selectedTickets };
		});
	}

	async refundSingleOrder({ id, items }) {
		return new Promise(function(resolve, reject) {
			Bigneon()
				.orders.refund({
					id,
					items
				})
				.then(response => {
					resolve({ result: response });
				})
				.catch(error => {
					resolve({ error });
				});
		});
	}

	async onRefundConfirm() {
		this.setState({ isRefunding: true });

		const { selectedTickets } = this.state;

		//Group by order_id as we might need to do multiple refund calls per order
		const orders = {}; //order_id: [items]
		Object.keys(selectedTickets).forEach(ticket_instance_id => {
			const { order_id, order_item_id } = selectedTickets[ticket_instance_id];

			if (!orders[order_id]) {
				orders[order_id] = [];
			}

			orders[order_id].push({
				order_item_id,
				ticket_instance_id
			});
		});

		let foundError = false;

		const ordersIds = Object.keys(orders);

		for (let index = 0; index < ordersIds.length; index++) {
			const order_id = ordersIds[index];
			const items = orders[order_id];

			const { error } = await this.refundSingleOrder({
				id: order_id,
				items
			});

			if (error) {
				foundError = true;
				console.error(error);
				this.setState({ isRefunding: false });
				notifications.showFromErrorResponse({
					defaultMessage: "Refunding order failed.",
					error
				});
			}
		}

		this.refreshGuests();

		this.setState({
			isRefunding: false,
			refundComplete: !foundError,
			selectedTickets: {}
		});
	}

	onRefund() {
		this.setState({ showConfirmRefund: true });
	}

	renderBottomBar() {
		const { selectedTickets } = this.state;

		const count = Object.keys(selectedTickets).length;

		let value_in_cents = 0;

		Object.keys(selectedTickets).forEach(id => {
			const { price_in_cents } = selectedTickets[id];
			value_in_cents += price_in_cents;
		});

		return (
			<BottomRefundBar
				value_in_cents={value_in_cents}
				count={count}
				onSubmit={this.onRefund.bind(this)}
			/>
		);
	}

	render() {
		//TODO make sure this is for org members

		const {
			eventName,
			searchQuery,
			expandedUserId,
			selectedTickets,
			showConfirmRefund,
			isRefunding,
			refundComplete,
			cancelTransferKey,
			orderPaging
		} = this.state;

		const { classes } = this.props;

		const filteredGuests = this.filteredGuests();

		if (filteredGuests === null) {
			return <Loader/>;
		}

		return (
			<div>
				<CancelTransferDialog
					transferKey={cancelTransferKey}
					onClose={() => this.setState({ cancelTransferKey: null })}
					onSuccess={() => this.refreshGuests()}
				/>
				<Grid className={classes.filterOptions} container>
					<Grid item xs={12} sm={12} md={6} lg={8}>
						<PageHeading iconUrl="/icons/events-multi.svg">
							Manage orders - {eventName}
						</PageHeading>
					</Grid>
					<Grid item xs={12} sm={12} md={6} lg={4}>
						<SearchInput
							placeholder="Search by guest name or order #"
							onSearch={this.onSearch.bind(this)}
						/>
					</Grid>
				</Grid>

				{Object.keys(filteredGuests).map((id, index) => {
					const expanded = id === expandedUserId;
					return (
						<GuestRow
							key={id}
							index={index}
							userId={id}
							{...filteredGuests[id]}
							onExpandChange={this.onExpandChange}
							expanded={expanded}
							onTicketSelect={this.onTicketSelect}
							selectedTickets={expanded ? selectedTickets : {}}
							onCancelTransfer={this.onOpenCancelTransferDialog}
						/>
					);
				})}

				<ConfirmRefundDialog
					open={showConfirmRefund}
					onClose={() =>
						this.setState({ showConfirmRefund: false, refundComplete: false })
					}
					isRefunding={isRefunding}
					refundComplete={refundComplete}
					selectedTickets={selectedTickets}
					onConfirm={this.onRefundConfirm.bind(this)}
				/>

				{orderPaging !== null ? (
					<Pagination
						isLoading={false}
						paging={orderPaging}
						onChange={this.changePage.bind(this)}
					/>
				) : (
					<div/>
				)}

				{this.renderBottomBar()}
			</div>
		);
	}
}

export default withStyles(styles)(Refunds);
