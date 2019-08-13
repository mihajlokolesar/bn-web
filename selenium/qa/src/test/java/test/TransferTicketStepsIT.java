package test;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.User;
import pages.LoginPage;
import pages.SignUpPage;
import pages.TicketTransferPage;
import pages.components.user.EventComponent;
import pages.mailinator.MailinatorHomePage;
import pages.mailinator.MailinatorInboxPage;
import pages.user.MyEventsPage;
import utils.MsgConstants;
import utils.RetryAnalizer;

public class TransferTicketStepsIT extends BaseSteps {

	@Test(dataProvider = "ticket_transfer_to_new_user_data", priority = 10)
	public void transferTicketToNewUserSteps(User sender, User receiver) {
		LoginPage login = new LoginPage(driver);
		maximizeWindow();
//		login.confirmedLogin(sender.getEmailAddress(), sender.getPass());
//
//		MyEventsPage myEventsPage = login.getHeader().clickOnMyEventsInProfileDropDown();
//		myEventsPage.isAtPage();
//		// get event name of ticket that is being transfered
//		WebElement selectedEventElement = myEventsPage.clickOnFirstOneViewMyTicketsButton();
//		String eventName = myEventsPage.getEventName(selectedEventElement);
//
//		EventComponent selectedEvent = myEventsPage.getSelectedEvent();
//		String ticketNumber = selectedEvent.getTicketNumber();
//		String orderNumber = selectedEvent.getOrderNumber();
//		selectedEvent.clickOnTransfer();
//
//		myEventsPage.enterReceiversMail(receiver);
//		login.logOut();
		MyEventsPage myEventsPage = new MyEventsPage(driver);
		EventComponent selectedEvent = null;
		String eventName = "eventName";
		String ticketNumber = "123123";
		String orderNumber = "78979";
		receiver.setEmailAddress("seleniumtest214090@mailinator.com");
		// do some kind of factory and template method combination for this mailinator
		// pages.
		MailinatorHomePage mailinatorHomePage = new MailinatorHomePage(driver);
		MailinatorInboxPage inboxPage = mailinatorHomePage.goToUserInbox(receiver.getEmailAddress());
		inboxPage.goToMail("you tickets! Action Required!");
		inboxPage.clickOnClaimTicket();

		TicketTransferPage ticketTransferPage = new TicketTransferPage(driver);
		SignUpPage signUpPage = ticketTransferPage.clickOnContinueWithEmail();
		signUpPage.createAccount(receiver);
		signUpPage.getHeader().clickOnMyEventsInProfileDropDown();
		WebElement event = myEventsPage.findEventByName(eventName);
		selectedEvent = myEventsPage.clickOnViewMyTicketOfEvent(event);
		boolean isTicketTransfered = selectedEvent.isTicketNumberPresent(ticketNumber);
		isTicketTransfered = isTicketTransfered && selectedEvent.isOrderNumberPresent(orderNumber);
		Assert.assertTrue(isTicketTransfered, "Ticket not transfered to receiver account");
		login.logOut();
	}

//	@Test(dataProvider = "ticket_transfer_to_old_user_data", priority = 9, retryAnalyzer = RetryAnalizer.class)
	public void transferTicketToExistingUserSteps(User sender, User receiver) {
		LoginPage login = new LoginPage(driver);
		maximizeWindow();
		login.confirmedLogin(sender.getEmailAddress(), sender.getPass());

		MyEventsPage myEventsPage = login.getHeader().clickOnMyEventsInProfileDropDown();
		myEventsPage.isAtPage();
		// get event name of ticket that is being transfered
		WebElement selectedEventElement = myEventsPage.clickOnFirstOneViewMyTicketsButton();
		String eventName = myEventsPage.getEventName(selectedEventElement);

		EventComponent selectedEvent = myEventsPage.getSelectedEvent();
		// get ticket number and order number to retrieve later in receivers events
		String ticketNumber = selectedEvent.getTicketNumber();
		String orderNumber = selectedEvent.getOrderNumber();
		selectedEvent.clickOnTransfer();

		myEventsPage.enterReceiversMail(receiver);
		boolean retVal = myEventsPage
				.isNotificationDisplayedWithMessage(MsgConstants.TICKET_TRANSFER_EMAIL_LINK_SENT_SUCCESS);
		Assert.assertTrue(retVal, "Notification not displayed");
		login.logOut();

		// Login with receiver

		Assert.assertTrue(login.confirmedLogin(receiver), "Login with receiver account failed: " + receiver);
		myEventsPage = login.getHeader().clickOnMyEventsInProfileDropDown();
		myEventsPage.isAtPage();
		WebElement event = myEventsPage.findEventByName(eventName);
		selectedEvent = myEventsPage.clickOnViewMyTicketOfEvent(event);
		boolean isTicketTransfered = selectedEvent.isTicketNumberPresent(ticketNumber);
		isTicketTransfered = isTicketTransfered && selectedEvent.isOrderNumberPresent(orderNumber);
		Assert.assertTrue(isTicketTransfered, "Ticket not transfered to receiver account");
		login.logOut();

	}

	@DataProvider(name = "ticket_transfer_to_new_user_data")
	public static Object[][] data_new_user() {
		User sender = User.generateUser();
		User receiver = User.generateRandomUser();

		return new Object[][] { { sender, receiver } };
	}

	@DataProvider(name = "ticket_transfer_to_old_user_data")
	public static Object[][] data_existing_user() {
		User sender = User.generateUser();
		User receiver = new User();
		receiver.setEmailAddress("altbluetestneouser@mailinator.com");
		receiver.setFirstName("testalt");
		receiver.setLastName("testqa");
		receiver.setPass("test1111");
		receiver.setPassConfirm("test1111");
		return new Object[][] { { sender, receiver } };
	}

}
