package test;

import java.net.URISyntaxException;

import model.Event;
import model.Organization;
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
	
	public abstract void customSteps();
		
	public void templateSteps(Purchase purchase, User user) throws Exception {
		maximizeWindow();

		login(user);
		createEvent(user, purchase.getEvent());
		purchaseTickets(purchase);
		selectOrganization(purchase.getEvent().getOrganization());
		navigateToOrderManage(purchase.getEvent());
		
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
	
	public void cancelEvent(Event event) {
		getLoginFacade().whenUserClickOnHeaderLogo();
		getAdminEventsFacade().thenUserIsAtEventsPage();
		AdminEventComponent eventComponent = getAdminEventsFacade().findEventIsOpenedAndHasSoldItem(event);
		eventComponent.cancelEvent();
	}
	
	public void logOut() {
		getLoginFacade().logOut();
	}
	
	public LoginStepsFacade getLoginFacade() {
		return loginFacade != null ? this.loginFacade : (this.loginFacade = new LoginStepsFacade(driver));
	}

	public OrganizationStepsFacade getOrganizationFacade() {
		return organizationFacade != null ? this.organizationFacade : (this.organizationFacade = new OrganizationStepsFacade(driver));
	}

	public AdminEventStepsFacade getAdminEventsFacade() {
		return adminEventsFacade != null ? this.adminEventsFacade : (this.adminEventsFacade = new AdminEventStepsFacade(driver));
	}
	
	public EventStepsFacade getEventFacade() {
		return eventStepsFacade != null ? this.eventStepsFacade : (this.eventStepsFacade = new EventStepsFacade(driver));
	}

	public AdminEventDashboardFacade getEventDashboardFacade() {
		return eventDashboardFacade != null ? this.eventDashboardFacade : (this.eventDashboardFacade = new AdminEventDashboardFacade(driver));
	}
	

}
