package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.components.admin.AdminEventComponent;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import test.facade.PurchaseFacade;

public class RefundStepsIT extends BaseSteps {

	@Test(dataProvider = "refund_data", priority = 15, dependsOnMethods = {
			"ensureThatEventWithSoldTicketExists" }, retryAnalyzer = utils.RetryAnalizer.class)
	public void refundSteps(User superuser, Event event) throws Exception {

		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(superuser);
		organizationFacade.givenOrganizationExist(event.getOrganization());

		adminEventFacade.givenUserIsOnAdminEventsPage();
		AdminEventComponent eventComponent = adminEventFacade.findEventIsOpenedAndHasSoldItem(event);
		Assert.assertNotNull(eventComponent, "No Event with name: " + event.getEventName() + " found");
		eventComponent.clickOnEvent();
		adminEventFacade.thenUserIsOnEventDashboardPage();
		adminEventFacade.whenUserSelectManageOrdersFromToolsDropDown();
		adminEventFacade.thenUserIsOnManageOrdersPage();
		adminEventFacade.whenUserSelectsTicketForRefundAndClicksOnRefundButton();
		adminEventFacade.whenUserGetConfirmationsOfSuccessfulRefund();
		boolean isTicketPresent = adminEventFacade.thenTicketShouldNotBePresent();
		Assert.assertFalse(isTicketPresent);
		loginFacade.logOut();

	}

	@Test(dataProvider = "ensure_event_with_sold_ticket_exists_data", priority = 15)
	public void ensureThatEventWithSoldTicketExists(Purchase purchase, User user, User superuser) throws Exception {
		PurchaseFacade purchaseFacade = new PurchaseFacade(driver);
		maximizeWindow();
		purchaseFacade.purchaseSteps(purchase, user);

	}

	@DataProvider(name = "refund_data")
	public static Object[][] refundDataProvider() {
		Event event = Event.generatedEvent(1, 5, "TestPurchaseSearchEventName", false);
		return new Object[][] { { User.generateSuperUser(), event } };
	}

	@DataProvider(name = "ensure_event_with_sold_ticket_exists_data")
	public static Object[][] refundDataPrepare() {
		Event event = Event.generatedEvent(1, 5, "TestPurchaseSearchEventName", false);
		Purchase purchase = new Purchase(event, CreditCard.generateCreditCard(), 2);
		User user = User.generateUser();
		return new Object[][] { { purchase, user, User.generateSuperUser() } };

	}

}
