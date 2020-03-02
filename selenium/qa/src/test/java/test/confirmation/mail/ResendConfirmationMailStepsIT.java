package test.confirmation.mail;

import org.testng.annotations.Test;

import model.Purchase;
import model.User;
import pages.components.admin.events.EventSummaryComponent;
import test.BaseSteps;
import test.facade.FacadeProvider;

public class ResendConfirmationMailStepsIT extends BaseSteps{
	
	@Test(dataProvider = "purchase_confirmation_mail_data", priority = 92)
	public void resendPurchaseConfirmationMail(Purchase purchase, User orgAdmin) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenUserIsLogedIn(orgAdmin);
		fp.getOrganizationFacade().givenOrganizationExist(purchase.getEvent().getOrganization());
		EventSummaryComponent eventSummaryComponent =fp.getAdminEventStepsFacade().findEventWithName(purchase.getEvent());
		eventSummaryComponent.clickOnEvent();
		fp.getEventDashboardFacade().whenUserSelectsManageOrdersFromOrdersDropDown();
		fp.getEventDashboardFacade().whenUserClickOnOrderLinkOfFirstOrder(fp.getOrderManageFacade());
		fp.getOrderManageFacade().whenUserClickOnResendConfirmationEmail();
	}
	
	@Test(dataProvider = "purchase_confirmation_mail_data")
	public void prepareDataFixture(Purchase purchase, User orgAdmin, User customer) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenUserIsLogedIn(orgAdmin);
		fp.getOrganizationFacade().givenOrganizationExist(purchase.getEvent().getOrganization());
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		fp.getAdminEventStepsFacade().createEvent(purchase.getEvent());
		fp.getLoginFacade().logOut();
		
		fp.getEventFacade().givenUserIsOnEventPage();
//		fp.getEventFacade().whenUserDoesThePurchses(purchase, customer);
	}

}
