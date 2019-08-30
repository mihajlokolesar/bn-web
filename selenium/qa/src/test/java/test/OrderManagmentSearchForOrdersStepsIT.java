package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import test.facade.AdminBoxOfficeFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.EventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class OrderManagmentSearchForOrdersStepsIT extends BaseSteps {

	@Test(dataProvider = "search_orders_data", priority = 13 ,dependsOnMethods = {"userPurchasedTickets"}/*,retryAnalyzer = utils.RetryAnalizer.class */ )
	public void searchForOrdersOnBoxOfficePage(User superuser, Event event, User one) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminBoxOfficeFacade boxOfficeFacade = new AdminBoxOfficeFacade(driver);
		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(superuser);
		adminEventFacade.givenUserIsOnAdminEventsPage();
		organizationFacade.givenOrganizationExist(event.getOrganization());

		boxOfficeFacade.givenUserIsOnBoxOfficePage();
		boxOfficeFacade.givenEventIsSelected(event.getEventName());
		boxOfficeFacade.givenUserIsOnGuestPage();

		boolean isLastNameTest = boxOfficeFacade.whenUserSearchesByLastName(one);
		boolean isTicketInSearchResults = boxOfficeFacade.whenUserSearchesByTicketNumber(one);
		
		Assert.assertTrue(isLastNameTest && isTicketInSearchResults);
		loginFacade.logOut();

	}

	@DataProvider(name = "search_orders_data")
	public static Object[][] dataProvider() {
		User superUser = User.generateSuperUser();

		Event event = Event.generatedEvent(1, 2, "TestPurchaseSearchEventName", false);
		User userOne = User.generateUser(DataConstants.DISTINCT_USER_ONE_FIRST_NAME,
				DataConstants.DISTINCT_USER_ONE_LAST_NAME);
		User userTwo = User.generateUser(DataConstants.DISTINCT_USER_TWO_FIRST_NAME,
				DataConstants.DISTINCT_USER_TWO_LAST_NAME);
		return new Object[][] { { superUser, event, userOne } };

	}
	
	@Test(dataProvider = "purchase_data")
	public void userPurchasedTickets(User user, Purchase purchase) throws Exception {
		maximizeWindow();
		EventStepsFacade eventsFacade = new EventStepsFacade(driver);

		// given
		eventsFacade.givenUserIsOnEventPage();
		EventsPage eventsPage = eventsFacade.givenThatEventExist(purchase.getEvent(), user);

		// when
		eventsFacade.whenUserExecutesEventPagesSteps(purchase.getEvent());
		
		Assert.assertTrue(eventsFacade.thenUserIsAtTicketsPage());
		
		eventsFacade.whenUserSelectsNumberOfTicketsAndClicksOnContinue(purchase);
		eventsFacade.whenUserLogsInOnTicketsPage(user);
		eventsFacade.thenUserIsAtConfirmationPage();
		eventsFacade.whenUserEntersCreditCardDetailsAndClicksOnPurchase(purchase.getCreditCard());

		// then
		eventsFacade.thenUserIsAtTicketPurchaseSuccessPage();
		eventsPage.logOut();

		
	}
	@DataProvider(name = "purchase_data")
	public static Object[][] data() {
		Purchase purchaseOne = preparePurchase();
		
		User one = User.generateUser(DataConstants.DISTINCT_USER_ONE_FIRST_NAME, DataConstants.DISTINCT_USER_ONE_LAST_NAME);
		User two = User.generateUser(DataConstants.DISTINCT_USER_TWO_FIRST_NAME, DataConstants.DISTINCT_USER_TWO_LAST_NAME);
		User three = User.generateUser(DataConstants.DISTINCT_USER_THREE_FIRST_NAME, DataConstants.DISTINCT_USER_THREE_LAST_NAME);
		return new Object[][] { 
			{ one, purchaseOne},
			{ two, purchaseOne},
			{ three, purchaseOne}};
	}
	
	private static Purchase preparePurchase() {
		Purchase purchase = new Purchase();
		purchase.setCreditCard(CreditCard.generateCreditCard());
		purchase.setNumberOfTickets(1);
		purchase.setEvent(Event.generatedEvent(1, 2, "TestPurchaseSearchEventName", false));
		return purchase;
	}

}
