package test;

import org.testng.Assert;

import model.Event;
import model.Organization;
import model.Purchase;
import model.User;
import pages.components.admin.AdminEventComponent;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import test.facade.AdminEventDashboardFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.EventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;

public abstract class TemplateRefundFeeSteps extends BaseSteps {

	private LoginStepsFacade loginFacade;
	private OrganizationStepsFacade organizationFacade;
	private AdminEventStepsFacade adminEventsFacade;
	private EventStepsFacade eventStepsFacade;
	private AdminEventDashboardFacade eventDashboardFacade;

	public abstract void customSteps();

	public void templateSteps(Purchase purchase, User user) throws Exception {
		maximizeWindow();

		login(user);
		createEvent(user, purchase.getEvent());
		purchaseTickets(purchase);
		selectOrganization(purchase.getEvent().getOrganization());
		navigateToOrderManage(purchase.getEvent());
		//this method is abstract and must be implemented with custom logic in child classes
		customSteps();
		
		cancelEvent(purchase.getEvent());
		logOut();
	}

	public void login(User user) {
		getLoginFacade().givenAdminUserIsLogedIn(user);
	}

	public void createEvent(User superuser, Event event) throws Exception {
		selectOrganization(event.getOrganization());
		getAdminEventsFacade().createEvent(event);
		getAdminEventsFacade().givenUserIsOnAdminEventsPage();
	}

	public void selectOrganization(Organization org) throws Exception {
		getOrganizationFacade().givenOrganizationExist(org);
	}

	public void purchaseTickets(Purchase purchase) throws Exception {
		getAdminEventsFacade().whenUserClicksOnViewEventOfSelecteEvent(purchase.getEvent());
		getEventFacade().whenUserClicksOnPurchaseTicketLink();
		getEventFacade().whenUserSelectsNumberOfTicketsForEachTicketTypeAndClicksOnContinue(purchase);
		getEventFacade().thenUserIsAtConfirmationPage();
		getEventFacade().whenUserEntersCreditCardDetailsAndClicksOnPurchase(purchase.getCreditCard());
		getLoginFacade().whenUserClickOnHeaderLogo();
	}

	public void navigateToOrderManage(Event event) {
		getAdminEventsFacade().thenUserIsAtEventsPage();
		AdminEventComponent eventCardComp = getAdminEventsFacade().findEventIsOpenedAndHasSoldItem(event);
		eventCardComp.clickOnEvent();
		getEventDashboardFacade().givenUserIsOnManageOrdersPage();
		getEventDashboardFacade().whenUserClickOnOrderLinkOfFirstOrder();
		getEventDashboardFacade().whenUserExpandOrderDetailsAndCheckIfExpanded();
	}

	public void refundSteps(RefundReason refundReason) {
		getEventDashboardFacade().whenUserClicksOnRefundButton();
		boolean isRefundDialogVisible = getEventDashboardFacade().thenRefundDialogShouldBeVisible();
		Assert.assertTrue(isRefundDialogVisible, "Refund dialog not visible");
		boolean isRefundDialogAmountCorrect = getEventDashboardFacade().thenRefundTotalOnRefundDialogShouldBeCorrect();
		Assert.assertTrue(isRefundDialogAmountCorrect);
		getEventDashboardFacade().whenUserSelectRefundReasonAndClicksOnConfirmButton(refundReason);
		getEventDashboardFacade().whenUserClicksOnGotItButtonOnRefundSuccessDialog();

	}

	public void cancelEvent(Event event) {
		try {
			getLoginFacade().whenUserClickOnHeaderLogo();
			getAdminEventsFacade().thenUserIsAtEventsPage();
			getAdminEventsFacade().whenUserRefreshesThePage();
			getAdminEventsFacade().thenUserIsAtEventsPage();
			AdminEventComponent eventComponent = getAdminEventsFacade().findEventWithName(event);
			eventComponent.cancelEvent();
		} catch (Exception e) {
			// log it once logger is added
		}
	}

	public void logOut() {
		try {
			getLoginFacade().logOut();
		}catch (Exception e) {
			// log it once logger is added
		}
	}

	public LoginStepsFacade getLoginFacade() {
		return loginFacade != null ? this.loginFacade : (this.loginFacade = new LoginStepsFacade(driver));
	}

	public OrganizationStepsFacade getOrganizationFacade() {
		return organizationFacade != null ? this.organizationFacade
				: (this.organizationFacade = new OrganizationStepsFacade(driver));
	}

	public AdminEventStepsFacade getAdminEventsFacade() {
		return adminEventsFacade != null ? this.adminEventsFacade
				: (this.adminEventsFacade = new AdminEventStepsFacade(driver));
	}

	public EventStepsFacade getEventFacade() {
		return eventStepsFacade != null ? this.eventStepsFacade
				: (this.eventStepsFacade = new EventStepsFacade(driver));
	}

	public AdminEventDashboardFacade getEventDashboardFacade() {
		return eventDashboardFacade != null ? this.eventDashboardFacade
				: (this.eventDashboardFacade = new AdminEventDashboardFacade(driver));
	}
}