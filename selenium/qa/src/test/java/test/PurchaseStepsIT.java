package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.mailinator.MailinatorHomePage;
import pages.mailinator.inbox.MailinatorInboxPage;
import test.facade.EventStepsFacade;
import utils.ProjectUtils;

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
		Purchase purchase = new Purchase();
		Event event = Event.generateEvent();
		String[] dates = ProjectUtils.getDatesWithSpecifiedRangeInDaysWithStartOffset(7, 30);
		event.setStartDate(dates[0]);
		event.setEndDate(dates[1]);
		event.setEventName("TestNameEvent");
		purchase.setEvent(event);
		purchase.setCreditCard(CreditCard.generateCreditCard());
		purchase.setNumberOfTickets(3);
		// TODO: replace with some other number
		purchase.setPhoneNumber("14422460151");
		return new Object[][] { { User.generateUser(), purchase } };
	}

}
