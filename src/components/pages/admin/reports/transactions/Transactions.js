import React, { Component } from "react";
import { Grid, Typography, withStyles } from "@material-ui/core";
import moment from "moment-timezone";
import PropTypes from "prop-types";

import notifications from "../../../../../stores/notifications";
import Bigneon from "../../../../../helpers/bigneon";
import TransactionRow from "./TransactionRow";
import TransactionDialog from "./TransactionDialog";
import Button from "../../../../elements/Button";
import downloadCSV from "../../../../../helpers/downloadCSV";
import Loader from "../../../../elements/loaders/Loader";
import { dollars } from "../../../../../helpers/money";
import ReportsDate from "../ReportDate";
import reportDateRangeHeading from "../../../../../helpers/reportDateRangeHeading";
import SearchInput from "../../../../elements/SearchBox";
import { Pagination, urlPageParam } from "../../../../elements/pagination";
import user from "../../../../../stores/user";
import { observer } from "mobx-react";

const styles = theme => ({
	root: {},
	header: {
		display: "flex",
		minHeight: 60,
		alignItems: "center"
	},
	exportButtonContainer: {
		display: "flex",
		justifyContent: "flex-end"
	}
});

const LINE_LIMIT_PER_PAGE = 20;
const UNLIMITED_LINE_LIMIT = 999999999;

const formatItems = data => {
	const items = [];
	data.forEach(item => {
		const formattedDate = moment
			.utc(item.transaction_date)
			.tz(user.currentOrgTimezone)
			.format("MM/DD/YYYY h:mm A");
		items.push({ ...item, formattedDate });
	});

	items.sort((a, b) => {
		//Gte the dates we need to compare
		if (moment(a.transaction_date).diff(moment(b.transaction_date)) < 0) {
			return 1;
		} else {
			return -1;
		}
	});

	return items;
};

@observer
class Transactions extends Component {
	constructor(props) {
		super(props);

		this.state = {
			items: null,
			isLoading: false,
			paging: null,
			isExportingCSV: false
		};

		this.currentDateParams = {};
	}

	componentDidMount() {
		const { printVersion } = this.props;
		if (printVersion) {
			this.refreshData({}, 0, UNLIMITED_LINE_LIMIT); //TODO api needs to allow for all
		}
	}

	exportCSV() {
		this.setState({ isExportingCSV: true });

		const { eventName, eventId, organizationId } = this.props;

		Bigneon()
			.reports.transactionDetails({
				organization_id: organizationId,
				page: 0,
				limit: UNLIMITED_LINE_LIMIT,
				query: "",
				event_id: eventId
			})
			.then(response => {
				const { data } = response.data;
				const items = formatItems(data);

				const { startDate, endDate } = this.state;

				if (!items || items.length < 1) {
					return notifications.show({
						message: "No rows to export.",
						variant: "warning"
					});
				}

				const csvRows = [];

				let title = "Transaction details report";
				if (eventName) {
					title = `${title} - ${eventName}`;
				}

				csvRows.push([title]);
				csvRows.push([
					`Transactions occurring ${reportDateRangeHeading(startDate, endDate)}`
				]);
				csvRows.push([""]);

				csvRows.push([
					"Order ID",
					"First Name",
					"Last Name",
					"Email",
					"Event",
					"Ticket type",
					"Order type",
					"Transaction date",
					"Payment method",
					"Redemption code",
					"Order type",
					"Payment method",
					"Quantity Sold",
					"Quantity Refunded",
					"Actual Quantity",
					"Unit price",
					"Face value",
					"Service fees",
					"Discount",
					"Gross",
					"Source",
					"Medium",
					"Campaign",
					"Term",
					"Content",
					"Platform"
				]);

				items.forEach(item => {
					const {
						client_fee_in_cents,
						company_fee_in_cents,
						event_fee_gross_in_cents,
						event_fee_company_in_cents,
						event_fee_client_in_cents,
						event_id,
						event_name,
						gross,
						order_id,
						order_type,
						payment_method,
						quantity,
						refunded_quantity,
						redemption_code,
						promo_redemption_code,
						ticket_name,
						formattedDate,
						unit_price_in_cents,
						user_id,
						gross_fee_in_cents_total,
						event_fee_gross_in_cents_total,
						credit_card_fee_gross_in_cents_total,
						first_name,
						last_name,
						email,
						promo_quantity,
						promo_discount_value_in_cents,
						source,
						medium,
						campaign,
						term,
						content,
						platform
					} = item;

					csvRows.push([
						order_id.slice(-8),
						first_name,
						last_name,
						email,
						event_name,
						ticket_name,
						order_type,
						formattedDate,
						payment_method,

						redemption_code || promo_redemption_code,
						order_type,
						payment_method,
						quantity,
						refunded_quantity,
						quantity - refunded_quantity,
						dollars(unit_price_in_cents),
						dollars((quantity - refunded_quantity) * unit_price_in_cents), //Face value
						dollars(
							event_fee_gross_in_cents_total +
								gross_fee_in_cents_total +
								credit_card_fee_gross_in_cents_total
						),
						dollars(promo_quantity * promo_discount_value_in_cents),
						dollars(gross),
						source,
						medium,
						campaign,
						term,
						content,
						platform
					]);
				});

				downloadCSV(csvRows, "transaction-report");
				this.setState({ isExportingCSV: false });
			})
			.catch(error => {
				console.error(error);
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Exporting event transaction report failed."
				});
			});
	}

	onSearch(searchQuery) {
		const {
			startDate = null,
			endDate = null,
			start_utc = null,
			end_utc = null
		} = this.currentDateParams;

		this.refreshData(
			{
				start_utc,
				end_utc,
				startDate,
				endDate
			},
			0,
			LINE_LIMIT_PER_PAGE,
			searchQuery
		);
	}

	changePage(page = urlPageParam()) {
		const {
			startDate = null,
			endDate = null,
			start_utc = null,
			end_utc = null
		} = this.currentDateParams;
		this.refreshData({ start_utc, end_utc, startDate, endDate }, page);
	}

	refreshData(
		dataParams = {
			start_utc: null,
			end_utc: null,
			startDate: null,
			endDate: null
		},
		page = 0,
		limit = LINE_LIMIT_PER_PAGE,
		searchQuery = ""
	) {
		const { startDate, endDate, end_utc } = dataParams;
		let { start_utc } = dataParams;
		this.currentDateParams = dataParams;

		const { eventId, organizationId, onLoad } = this.props;

		if (searchQuery) {
			start_utc = null;
		}

		let queryParams = {
			organization_id: organizationId,
			start_utc,
			end_utc,
			page,
			limit,
			query: searchQuery
		};

		if (eventId) {
			queryParams = { ...queryParams, event_id: eventId };
		}

		this.setState({ startDate, endDate, items: null, isLoading: true });

		Bigneon()
			.reports.transactionDetails(queryParams)
			.then(response => {
				const { data, paging } = response.data;
				const items = formatItems(data);

				this.setState({ items, paging, isLoading: false }, () => {
					onLoad ? onLoad() : null;
				});
			})
			.catch(error => {
				console.error(error);
				this.setState({ items: null, isLoading: false });

				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Loading event transaction report failed."
				});
			});
	}

	renderDialog() {
		const { activeIndex, items } = this.state;

		let userId = null;
		let activeItem = null;

		if (!isNaN(activeIndex) && items) {
			const item = items[activeIndex];
			if (item) {
				activeItem = item;
				const { user_id } = item;
				if (user_id) {
					userId = user_id;
				}
			}
		}

		return (
			<TransactionDialog
				open={!!activeItem}
				userId={userId}
				item={activeItem}
				onClose={() => this.setState({ activeIndex: null })}
			/>
		);
	}

	renderList() {
		const { items, hoverIndex, isLoading } = this.state;
		const { printVersion } = this.props;

		if (isLoading) {
			return <Loader/>;
		}

		if (items === null) {
			//Query failed
			return null;
		}

		if (items.length === 0) {
			return <Typography>No transactions found.</Typography>;
		}

		//If we're showing this on an org level then we need to show event names
		const includeEventName = !this.props.eventId;

		const ths = [
			"Order ID",
			"Name",
			"Email",
			"Date/time",
			"Qty",
			"Gross",
			"Platform"
		];

		if (includeEventName) {
			ths.splice(1, 0, "Event");
		}

		return (
			<div>
				<TransactionRow heading>{ths}</TransactionRow>

				{items.map((item, index) => {
					const {
						event_name,
						gross,
						order_type,
						quantity,
						ticket_name,
						formattedDate,
						unit_price_in_cents,
						gross_fee_in_cents_total,
						event_fee_gross_in_cents_total,
						first_name,
						last_name,
						email,
						refunded_quantity,
						order_id,
						source,
						medium,
						campaign,
						term,
						content,
						platform
					} = item;

					const tds = [
						`#${order_id.slice(-8)}`,
						`${first_name} ${last_name}`,
						email,
						formattedDate,
						quantity - refunded_quantity,
						dollars(gross),
						platform
					];

					if (includeEventName) {
						tds.splice(1, 0, event_name);
					}

					return (
						<TransactionRow
							key={index}
							onClick={() => this.setState({ activeIndex: index })}
							onMouseEnter={() => this.setState({ hoverIndex: index })}
							onMouseLeave={() => this.setState({ hoverIndex: null })}
							active={false}
							gray={!(index % 2)}
							active={hoverIndex === index && !printVersion}
						>
							{tds}
						</TransactionRow>
					);
				})}
			</div>
		);
	}

	render() {
		const { eventId, classes, printVersion, salesStart } = this.props;

		if (printVersion) {
			return this.renderList();
		}

		const { isLoading, paging, isExportingCSV } = this.state;

		const { currentOrgTimezone } = user;

		return (
			<div className={classes.root}>
				<Grid className={classes.header} container>
					<Grid item xs={12} sm={12} md={4} lg={4}>
						<Typography variant="title">
							{eventId ? "Event" : "Organization"} transaction report
						</Typography>
					</Grid>
					<Grid item xs={12} sm={12} md={4} lg={4}>
						<SearchInput
							placeholder="Search by guest name, email or event name"
							onSearch={this.onSearch.bind(this)}
						/>
						{/*<InputWithButton*/}
						{/*// style={{ marginBottom: 20, marginTop: 20 }}*/}
						{/*name={"tx-report-search"}*/}
						{/*placeholder="Search by guest name, email or event name"*/}
						{/*buttonText="Search"*/}
						{/*onSubmit={this.onSearch.bind(this)}*/}
						{/*disabled={isLoading}*/}
						{/*/>*/}
					</Grid>

					<Grid
						item
						xs={12}
						sm={12}
						md={4}
						lg={4}
						className={classes.exportButtonContainer}
					>
						<Button
							iconUrl="/icons/csv-active.svg"
							variant="text"
							onClick={this.exportCSV.bind(this)}
							disabled={isExportingCSV}
						>
							{isExportingCSV ? "Exporting..." : "Export CSV"}
						</Button>
						<Button
							href={`/exports/reports/?type=transactions${
								eventId ? `&event_id=${eventId}` : ""
							}`}
							target={"_blank"}
							iconUrl="/icons/pdf-active.svg"
							variant="text"
						>
							Export PDF
						</Button>
					</Grid>
				</Grid>

				{currentOrgTimezone ? (
					<ReportsDate
						timezone={currentOrgTimezone}
						onChange={this.refreshData.bind(this)}
						defaultStartTimeBeforeNow={{ value: 1, unit: "M" }}
						salesStart={salesStart}
						onChangeButton
						onChangeOnLoad
					/>
				) : null}

				{this.renderDialog()}
				{this.renderList()}

				{paging ? (
					<Pagination
						isLoading={isLoading}
						paging={paging}
						onChange={this.changePage.bind(this)}
					/>
				) : null}
			</div>
		);
	}
}

Transactions.propTypes = {
	classes: PropTypes.object.isRequired,
	organizationId: PropTypes.string.isRequired,
	eventId: PropTypes.string,
	eventName: PropTypes.string,
	printVersion: PropTypes.bool,
	onLoad: PropTypes.func
};

export default withStyles(styles)(Transactions);
