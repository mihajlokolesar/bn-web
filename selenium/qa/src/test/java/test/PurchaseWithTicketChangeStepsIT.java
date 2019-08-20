package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.LoginPage;
import pages.TicketsConfirmationPage;
import pages.TicketsPage;
import pages.TicketsSuccesPage;
import pages.components.Header;
import pages.mailinator.MailinatorHomePage;
import pages.mailinator.MailinatorInboxPage;
import utils.ProjectUtils;

public class PurchaseWithTicketChangeStepsIT extends BaseSteps {

	@Test(dataProvider = "purchase_data", priority = 8)
	private void purchaseStepsWithTicketChange(User user, Purchase purchase) throws Exception {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.login(user);
		Header header = loginPage.getHeader();

		TicketsPage ticketsPage = new TicketsPage(driver);

		if (!header.clickOnShoppingBasketIfPresent()) {
			header.searchEvents(purchase.getEvent().getArtistName());
			EventsPage eventsPage = new EventsPage(driver);
			eventsPage.eventsPageSteps(purchase.getEvent());

			ticketsPage.ticketsPageStepsWithOutLogin(purchase.getNumberOfTickets());
		}

		TicketsConfirmationPage confirmationPage = new TicketsConfirmationPage(driver);
		confirmationPage.isAtConfirmationPage();
		confirmationPage.clickOnChangeTicketLink();

		ticketsPage.isAtPage();
		ticketsPage.removeNumberOfTickets(purchase.getRemoveNumberOfTickets());
		ticketsPage.clickOnContinue();
		int ticketNumbers = confirmationPage.getTicketQuantity();
		confirmationPage.ticketsConfirmationPageSteps(purchase.getCreditCard());

		TicketsSuccesPage successPage = new TicketsSuccesPage(driver);
		successPage.isAtPage();
//		successPage.enterPhoneNumber(purchase.getPhoneNumber());

		header.logOut();

		MailinatorHomePage mailinatorHomePage = new MailinatorHomePage(driver);
		MailinatorInboxPage inboxPage = mailinatorHomePage.goToUserInbox(user.getEmailAddress());
		boolean retVal = inboxPage.openMailAndCheckValidity("Next Step - Get Your Tickets", ticketNumbers,
				purchase.getEvent().getEventName());
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
		purchase.setRemoveNumberOfTickets(1);
		// TODO: replace with some other number
		purchase.setPhoneNumber("14422460151");
		return new Object[][] { { User.generateUser(), purchase } };
	}

}
