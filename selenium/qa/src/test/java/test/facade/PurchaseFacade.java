package test.facade;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.Purchase;
import model.User;
import pages.EventsPage;

public class PurchaseFacade extends BaseFacadeSteps{

	public PurchaseFacade(WebDriver driver) {
		super(driver);
	}
	
	
	public void purchaseSteps(Purchase purchase, User user) throws Exception {
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

}
