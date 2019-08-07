package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.TicketsConfirmationPage;
import pages.TicketsPage;
import pages.TicketsSuccesPage;
import pages.components.Header;
import pages.mailinator.MailinatorHomePage;
import pages.mailinator.MailinatorInboxPage;
import utils.ProjectUtils;

public class PurchaseStepsIT extends BaseSteps {

	@Test(dataProvider = "purchase_data")
	public void purchaseSteps(User user, Purchase purchase) throws Exception {

		EventsPage eventsPage = new EventsPage(driver);
		maximizeWindow();
		eventsPage.eventsPageSteps(purchase.getEvent());

		TicketsPage ticketsPage = new TicketsPage(driver);
		ticketsPage.ticketsPageStepsWithLogin(user.getEmailAddress(), user.getPass(), purchase.getNumberOfTickets());

		TicketsConfirmationPage confirmationPage = new TicketsConfirmationPage(driver);
		confirmationPage.ticketsConfirmationPageSteps(purchase.getCreditCard());

		TicketsSuccesPage successPage = new TicketsSuccesPage(driver);
		successPage.isAtPage();
//		successPage.enterPhoneNumber(purchase.getPhoneNumber());
		Header header = new Header(driver);
		header.logOut();

		MailinatorHomePage mailinatorHomePage = new MailinatorHomePage(driver);
		MailinatorInboxPage inboxPage = mailinatorHomePage.goToUserInbox(user.getEmailAddress());
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
		purchase.setNumberOfTickets(2);
		// TODO: replace with some other number
		purchase.setPhoneNumber("14422460151");
		return new Object[][] { { User.generateUser(), purchase } };
	}

}
