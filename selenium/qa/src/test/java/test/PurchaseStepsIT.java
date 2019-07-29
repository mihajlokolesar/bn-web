package test;

import org.testng.annotations.Test;

import pages.EventsPage;
import pages.LoginPage;
import pages.TicketsConfirmationPage;
import pages.TicketsPage;

public class PurchaseStepsIT extends BaseSteps {
	
	
	@Test
	public void purchaseSteps() throws Exception {
		EventsPage eventsPage = new EventsPage(driver);
		maximizeWindow();
		eventsPage.navigate();
		eventsPage.clickOnEvent("james-taylor");
		eventsPage.clickOnViewMap();
		eventsPage.purchaseTicketLinkClick();
		TicketsPage ticketsPage = new TicketsPage(driver);
		ticketsPage.addTicketForLastType();
		ticketsPage.addTicketForLastType();
		ticketsPage.clickOnContinue();
		ticketsPage.clickOnAlreadyHaveAnAccount();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginWithoutNavigate("bluetestneouser@mailinator.com", "test1111");
		TicketsConfirmationPage confirmationPage = new TicketsConfirmationPage(driver);
		confirmationPage.shoppingBasketState();
		confirmationPage.isAtConfirmationPage();
		confirmationPage.enterCreditCardDetails();
		
		
	}

}
