package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.mailinator.MailinatorHomePage;
import pages.mailinator.inbox.MailinatorInboxPage;
import test.facade.EventStepsFacade;
import utils.DataConstants;

public class PurchaseStepsIT extends BaseSteps {

	@Test(dataProvider = "purchase_data", priority = 7, retryAnalyzer = utils.RetryAnalizer.class)
	public void purchaseSteps(User user, Purchase purchase) throws Exception {
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

		MailinatorHomePage mailinatorHomePage = new MailinatorHomePage(driver);
		MailinatorInboxPage inboxPage = mailinatorHomePage.goToUserInbox(user.getEmailAddress());
		// then
		boolean retVal = inboxPage.openMailAndCheckValidity("Next Step - Get Your Tickets",
				purchase.getNumberOfTickets(), purchase.getEvent().getEventName());
		Assert.assertTrue(retVal);
	}

	@DataProvider(name = "purchase_data")
	public static Object[][] data() {
		Purchase purchase = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY, false, 7, 30);
		purchase.setEvent(event);
		// TODO: replace with some other number
		purchase.setPhoneNumber("14422460151");
		return new Object[][] { 
			{ User.generateUser(), purchase }}
		;
	}
}
