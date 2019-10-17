package test;

import model.Event;
import model.Purchase;
import model.User;
import pages.components.admin.AdminEventComponent;
import test.facade.AdminEventDashboardFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.EventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;

public abstract class AbstractRefundFeeSteps extends BaseSteps {
	
	private LoginStepsFacade loginFacade;
	private OrganizationStepsFacade organizationFacade;
	private AdminEventStepsFacade adminEventsFacade;
	private EventStepsFacade eventStepsFacade;
	private AdminEventDashboardFacade eventDashboardFacade;
	
	public void createEvent(User superuser, Event event) throws Exception {
		getLoginFacade().givenAdminUserIsLogedIn(superuser);
		getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		getAdminEventsFacade().createEvent(event);
		getAdminEventsFacade().givenUserIsOnAdminEventsPage();
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
		AdminEventComponent eventCardComp = getAdminEventsFacade().findEventWithName(event);
		eventCardComp.clickOnEvent();
		getEventDashboardFacade().givenUserIsOnManageOrdersPage();
		getEventDashboardFacade().whenUserClickOnOrderLinkOfFirstOrder();
		getEventDashboardFacade().whenUserExpandOrderDetailsAndCheckIfExpanded();
	}
	
	public void cancelEvent(Event event) {
		getLoginFacade().whenUserClickOnHeaderLogo();
		getAdminEventsFacade().thenUserIsAtEventsPage();
		AdminEventComponent eventComponent = getAdminEventsFacade().findEventWithName(event);
		eventComponent.cancelEvent();
		getLoginFacade().logOut();
	}
	
	public LoginStepsFacade getLoginFacade() {
		return loginFacade != null ? this.loginFacade : new LoginStepsFacade(driver);
	}

	public OrganizationStepsFacade getOrganizationFacade() {
		return organizationFacade != null ? this.organizationFacade : new OrganizationStepsFacade(driver);
	}

	public AdminEventStepsFacade getAdminEventsFacade() {
		return adminEventsFacade != null ? this.adminEventsFacade : new AdminEventStepsFacade(driver);
	}
	
	public EventStepsFacade getEventFacade() {
		return eventStepsFacade != null ? this.eventStepsFacade : new EventStepsFacade(driver);
	}

	public AdminEventDashboardFacade getEventDashboardFacade() {
		return eventDashboardFacade != null ? this.eventDashboardFacade : new AdminEventDashboardFacade(driver);
	}
	

}
