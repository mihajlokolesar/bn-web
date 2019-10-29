import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import { observer } from "mobx-react";

import user from "../../../../stores/user";
import PageHeading from "../../../elements/PageHeading";
import Card from "../../../elements/Card";
import StyledLink from "../../../elements/StyledLink";
import Transactions from "./transactions/Transactions";
import TicketCounts from "./counts/TicketCounts";
import Loader from "../../../elements/loaders/Loader";
import SettlementReport from "./settlement/SettlementReport";
import SettlementReportList from "./settlement/SettlementReportList";
import BoxOfficeSalesSummary from "./boxOfficeSalesSummary/BoxOfficeSalesSummary";

const styles = theme => ({
	content: {
		padding: theme.spacing.unit * 4
	},
	menuContainer: {
		display: "flex"
	},
	menuText: {
		marginRight: theme.spacing.unit * 4
	}
});

@observer
class Reports extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}

	renderMenu() {
		const { classes } = this.props;

		const report = this.props.match.params.report;
		const {
			hasTransactionReports,
			hasTicketCountReports,
			hasOrgBoxOfficeSalesReport,
			hasOrgReconciliationReport,
			hasOrgEventSettlementReport
		} = user;

		return (
			<div className={classes.menuContainer}>
				{hasTicketCountReports ? (
					<Typography className={classes.menuText}>
						<StyledLink
							underlined={!report || report === "ticket-counts"}
							to={`/admin/reports/ticket-counts`}
						>
							Ticket Counts
						</StyledLink>
					</Typography>
				) : null}

				{hasTransactionReports ? (
					<Typography className={classes.menuText}>
						<StyledLink
							underlined={report === "transaction-details"}
							to={`/admin/reports/transaction-details`}
						>
							Transaction Detail
						</StyledLink>
					</Typography>
				) : null}

				{hasOrgBoxOfficeSalesReport ? (
					<Typography className={classes.menuText}>
						<StyledLink
							underlined={report === "box-office-sales-summary"}
							to={`/admin/reports/box-office-sales-summary`}
						>
							Box Office Sales
						</StyledLink>
					</Typography>
				) : null}

				{/*{hasOrgReconciliationReport ? (*/}
				{/*	<Typography className={classes.menuText}>*/}
				{/*		<StyledLink*/}
				{/*			underlined={report === "reconciliation"}*/}
				{/*			to={`/admin/reports/reconciliation`}*/}
				{/*		>*/}
				{/*			Reconciliation*/}
				{/*		</StyledLink>*/}
				{/*	</Typography>*/}
				{/*) : null}*/}

				{hasOrgEventSettlementReport ? (
					<Typography className={classes.menuText}>
						<StyledLink
							underlined={
								report === "settlement-list" || report === "settlement"
							}
							to={`/admin/reports/settlement-list`}
						>
							Settlement reports
						</StyledLink>
					</Typography>
				) : null}
			</div>
		);
	}

	renderContent() {
		const report = this.props.match.params.report;

		const {
			hasTransactionReports,
			hasTicketCountReports,
			currentOrganizationId,
			currentOrgTimezone
		} = user;

		if (!currentOrganizationId || !currentOrgTimezone) {
			return <Loader/>;
		}

		const { classes } = this.props;

		//Add report components here as needed
		switch (report) {
			case undefined:
			case "ticket-counts":
				return hasTicketCountReports ? (
					<Card variant={"block"} className={classes.content}>
						<TicketCounts organizationId={currentOrganizationId}/>
					</Card>
				) : null;
			case "transaction-details":
				return (
					<Card variant={"block"} className={classes.content}>
						<Transactions organizationId={currentOrganizationId}/>
					</Card>
				);
			case "settlement-list":
				if (!currentOrgTimezone) {
					return <Loader/>;
				}

				return (
					<SettlementReportList
						organizationId={currentOrganizationId}
						organizationTimezone={currentOrgTimezone}
					/>
				);
			case "settlement":
				if (!currentOrgTimezone) {
					return <Loader/>;
				}

				return (
					<SettlementReport
						history={this.props.history}
						organizationId={currentOrganizationId}
						organizationTimezone={currentOrgTimezone}
					/>
				);
			case "box-office-sales-summary":
				if (!currentOrgTimezone) {
					return <Loader/>;
				}

				return (
					<Card variant={"block"} className={classes.content}>
						<BoxOfficeSalesSummary
							organizationId={currentOrganizationId}
							organizationTimezone={currentOrgTimezone}
						/>
					</Card>
				);
			default:
				return (
					<Typography>
						This report is not yet available for your organization.
					</Typography>
				);
		}
	}

	render() {
		const { classes } = this.props;

		return (
			<div>
				<PageHeading>Organization Reports</PageHeading>

				<Card variant={"block"} style={{ borderRadius: "6px 6px 0 0" }}>
					<div className={classes.content}>{this.renderMenu()}</div>
				</Card>

				<div style={{ marginBottom: 20 }}/>

				{this.renderContent()}
			</div>
		);
	}
}

export default withStyles(styles)(Reports);
