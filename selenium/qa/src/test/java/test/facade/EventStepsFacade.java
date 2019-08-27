package test.facade;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.LoginPage;
import pages.TicketsConfirmationPage;
import pages.TicketsPage;
import pages.TicketsSuccesPage;

public class EventStepsFacade extends BaseFacadeSteps {

	private EventsPage eventsPage;
	private TicketsPage ticketPage;
	private TicketsConfirmationPage ticketsConfirmationPage;
	private TicketsSuccesPage succesPage;
	private LoginPage loginPage;

	public EventStepsFacade(WebDriver driver) {
		super(driver);
		//TODO: do some lazy proxying magic here, give it proxy and only when called by some function do the instance creation
		this.eventsPage = new EventsPage(driver);
		this.ticketsConfirmationPage = new TicketsConfirmationPage(driver);
		this.ticketPage = new TicketsPage(driver);
		this.succesPage = new TicketsSuccesPage(driver);
		this.loginPage = new LoginPage(driver);
	}

	public EventsPage givenThatEventExist(Event event,User user) throws Exception {
		if (!eventsPage.isEventPresent(event.getEventName())) {
			if(!loginPage.getHeader().isLoggedOut()) {
				loginPage.logOut();
			}
			
			createEventAndOrganizationIfNotPresent(event);
			loginPage.login(user);
			
			eventsPage.navigate();
			driver.navigate().refresh();
		}
		return eventsPage;
	}

	private void createEventAndOrganizationIfNotPresent(Event event) throws Exception {

		if (!loginPage.getHeader().isLoggedOut()) {
			loginPage.logOut();
		}

		User superuser = User.generateSuperUser();
		loginPage.login(superuser.getEmailAddress(), superuser.getPass());
		AdminEventStepsFacade createEvent = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);

		organizationFacade.givenOrganizationExist(event.getOrganization());
		boolean retVal = createEvent.createEvent(event);
		
		if (!retVal) {
			Assert.fail("Event creationg in steps failed");
		} 
		
		loginPage.logOut();

	}
	
	public void whenUserLogsInOnTicketsPage(User user) {
		ticketPage.clickOnAlreadyHaveAnAccount();
		ticketPage.login(user.getEmailAddress(), user.getPass());
		if (ticketPage.checkIfMoreEventsAreBeingPurchased()) {
			ticketPage.getHeader().clickOnShoppingBasketIfPresent();
		}
	}

	public void whenUserChangesTicketOptions(Purchase purchase) {
		ticketsConfirmationPage.clickOnChangeTicketLink();
		ticketPage.isAtPage();
		ticketPage.removeNumberOfTickets(purchase.getRemoveNumberOfTickets());
		ticketPage.clickOnContinue();
	}
	
	public void whenUserExecutesEventPagesSteps(Event event) throws Exception {
		whenUserClicksOnEvent(event);
		whenUserClickOnViewMap();
		whenUserClicksOnPurchaseTicketLink();
	}

	public void givenUserIsOnEventPage() {
		eventsPage.navigate();
	}

	public boolean whenShoppingBasketIsPresentAndClickOnBasket() {
		return eventsPage.getHeader().clickOnShoppingBasketIfPresent();
	}

	public void whenSearchingForEvent(Purchase purchase) {
		eventsPage.getHeader().searchEvents(purchase.getEvent().getArtistName());
	}

	public void whenUserSelectsNumberOfTicketsAndClicksOnContiue(Purchase purchase) {
		ticketPage.selectTicketNumberAndClickOnContinue(purchase.getNumberOfTickets());
	}

	public void whenUserEntersCreditCardDetailsAndClicksOnPurchase(CreditCard card) {
		this.ticketsConfirmationPage.ticketsConfirmationPageSteps(card);
	}
	
	private void whenUserClicksOnEvent(Event event) {
		eventsPage.clickOnEvent(event.getEventName());
	}

	private void whenUserClickOnViewMap() {
		eventsPage.clickOnViewMap();
	}

	private void whenUserClicksOnPurchaseTicketLink() throws Exception {
		eventsPage.purchaseTicketLinkClick();
	}

	
	public int thenTicketQuantityIs() {
		return ticketsConfirmationPage.getTicketQuantity();
	}

	public boolean thenUserIsAtTicketsPage() {
		return ticketPage.isAtPage();
	}

	public boolean thenUserIsAtConfirmationPage() {
		return ticketsConfirmationPage.isAtConfirmationPage();
	}

	public boolean thenUserIsAtTicketPurchaseSuccessPage() {
		return this.succesPage.isAtPage();
	}

}
