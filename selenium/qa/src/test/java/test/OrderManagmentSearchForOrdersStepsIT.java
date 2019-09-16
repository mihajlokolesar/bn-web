package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.components.admin.AdminEventComponent;
import pages.components.admin.orders.manage.ManageOrderRow;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import test.facade.AdminBoxOfficeFacade;
import test.facade.AdminEventDashboardFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.EventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class OrderManagmentSearchForOrdersStepsIT extends BaseSteps {

	private static final String EVENT_NAME = "TestSearchOrdersEventName";
	private static final Integer START_DAY_OFFSET = 1;
	private static final Integer DAYS_RANGE = 2;
	private static final Integer PURCHASE_QUANTITY = 1;
	private Purchase purchase;

	@Test(dataProvider = "guest_page_search_data", priority = 13, 
			 dependsOnMethods = {"userPurchasedTickets"},  retryAnalyzer = utils.RetryAnalizer.class)
	public void aguestPageSearchTest(User superuser, Event event, User one, User two) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminBoxOfficeFacade boxOfficeFacade = new AdminBoxOfficeFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		
		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(superuser);
		adminEventFacade.givenUserIsOnAdminEventsPage();
		organizationFacade.givenOrganizationExist(event.getOrganization());

		boxOfficeFacade.givenUserIsOnBoxOfficePage();
		boxOfficeFacade.givenEventIsSelected(event.getEventName());
		boxOfficeFacade.givenUserIsOnGuestPage();

		boolean isLastNameTest = boxOfficeFacade.whenUserSearchesByLastName(one);
		boolean isTicketInSearchResults = boxOfficeFacade.whenUserSearchesByFirstNameAndTicketNumber(one);
		boolean isEmailSearchTest = boxOfficeFacade.whenUserSearchesByEmail(two);

		Assert.assertTrue(isLastNameTest && isTicketInSearchResults && isEmailSearchTest);
		loginFacade.logOut();

	}

	@DataProvider(name = "guest_page_search_data")
	public static Object[][] guestSearchData() {
		User superUser = User.generateSuperUser();

		Event event = preparePurchase().getEvent();
		User userOne = User.generateUser(DataConstants.DISTINCT_USER_ONE_FIRST_NAME,
				DataConstants.DISTINCT_USER_ONE_LAST_NAME);
		User userTwo = User.generateUser();
		return new Object[][] { { superUser, event, userOne, userTwo } };

	}

	@Test(dataProvider = "manage_orders_page_search_data", priority = 14, retryAnalyzer = utils.RetryAnalizer.class)
	public void bmanageOrdersPageSearchTest(User orgAdmin, User customer, User customerTwo, Event event)
			throws Exception {

		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventDashboardFacade dashboardFacade = new AdminEventDashboardFacade(driver);

		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(orgAdmin);
		organizationFacade.givenOrganizationExist(event.getOrganization());

		adminEventFacade.givenUserIsOnAdminEventsPage();
		AdminEventComponent eventComponent = adminEventFacade.findEventIsOpenedAndHasSoldItem(event);
		Assert.assertNotNull(eventComponent, "No Event with name: " + event.getEventName() + " found");
		eventComponent.clickOnEvent();

		dashboardFacade.thenUserIsOnEventDashboardPage();
		dashboardFacade.whenUserSelectsManageOrdersFromOrdersDropDown();
		dashboardFacade.thenUserIsOnOrderManagePage();

		ManageOrderRow orderRow = dashboardFacade.getManageOrdersFirstOrder();

		String orderNumber = orderRow.getOrderNumber();

		boolean retVal = false;
		retVal = dashboardFacade.whenUserDoesSearchCheckByEmail(customer);
		retVal &= dashboardFacade.whenUserDoesSearchCheckByFirstname(customerTwo);
		retVal &= dashboardFacade.whenUserDoesSearchCheckByLastName(customerTwo);
		retVal &= dashboardFacade.whenUserDoesSearchCheckByOrderNumber(orderNumber);
		retVal &= dashboardFacade.whenUserChecksOrderQuantityForSpecificUser(customer, PURCHASE_QUANTITY);

		Assert.assertTrue(retVal);
		
		loginFacade.logOut();

	}
	
	@Test(dataProvider = "manage_orders_page_search_data", priority = 14)
	public void cmanageOrdersSearchAndRefundTickets(User orgAdmin, User customer, User customerTwo, Event event) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventDashboardFacade dashboardFacade = new AdminEventDashboardFacade(driver);

		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(orgAdmin);
		organizationFacade.givenOrganizationExist(event.getOrganization());

		adminEventFacade.givenUserIsOnAdminEventsPage();
		AdminEventComponent eventComponent = adminEventFacade.findEventIsOpenedAndHasSoldItem(event);
		Assert.assertNotNull(eventComponent, "No Event with name: " + event.getEventName() + " found");
		eventComponent.clickOnEvent();

		dashboardFacade.thenUserIsOnEventDashboardPage();
		dashboardFacade.whenUserSelectsManageOrdersFromOrdersDropDown();
		dashboardFacade.thenUserIsOnOrderManagePage();

		ManageOrderRow orderRow = dashboardFacade.getManageOrdersFirstOrder();

		String orderNumber = orderRow.getOrderNumber();

		boolean retVal = false;
		retVal = dashboardFacade.whenUserDoesSearchCheckByEmail(customer);
		retVal &= dashboardFacade.whenUserDoesSearchCheckByFirstname(customerTwo);
		retVal &= dashboardFacade.whenUserDoesSearchCheckByLastName(customerTwo);
		retVal &= dashboardFacade.whenUserDoesSearchCheckByOrderNumber(orderNumber);
		retVal &= dashboardFacade.whenUserChecksOrderQuantityForSpecificUser(customer, PURCHASE_QUANTITY);
		
		Assert.assertTrue(retVal);
		
		dashboardFacade.whenUserClicksOnOrderLinkOfGivenUser(customer);
		boolean isExpanded = dashboardFacade.whenUserExpandOrderDetailsAndCheckIfExpanded();
		Assert.assertTrue(isExpanded);
		dashboardFacade.whenUserSelectsTicketForRefundAndClicksOnRefundButton();
		dashboardFacade.thenRefundDialogShouldBeVisible();
		//this argument belongs in data provider
		dashboardFacade.whenUserSelectRefundReasonAndClicksOnConfirmButton(RefundReason.OTHER);
		dashboardFacade.thenRefundDialogShouldBeVisible();
		dashboardFacade.whenUserClicksOnGotItButtonOnRefundSuccessDialog();
		
		adminEventFacade.givenUserIsOnAdminEventsPage();
		AdminEventComponent eComponent = adminEventFacade.findEventIsOpenedAndHasSoldItem(event);
		eComponent.cancelEvent();
		
		loginFacade.logOut();

	}

	@DataProvider(name = "manage_orders_page_search_data")
	public static Object[][] dataProvider() {
		Event event = preparePurchase().getEvent();
		User orgAdminUser = new User();
		orgAdminUser.setEmailAddress("orgadmneouser@mailinator.com");
		orgAdminUser.setPass("test1111");
		User one = User.generateUser(DataConstants.DISTINCT_USER_ONE_FIRST_NAME,
				DataConstants.DISTINCT_USER_ONE_LAST_NAME);
		return new Object[][] { { orgAdminUser, User.generateUser(), one, event } };
	}

	@Test(dataProvider = "purchase_data", priority = 13)
	public void userPurchasedTickets(User user, Purchase purchase) throws Exception {
		maximizeWindow();
		EventStepsFacade eventsFacade = new EventStepsFacade(driver);

		// given
		eventsFacade.givenUserIsOnEventPage();

		EventsPage eventsPage = eventsFacade.givenThatEventExist(purchase.getEvent(), user);
		this.purchase = purchase;
		// when
		eventsFacade.whenUserExecutesEventPagesStepsWithoutMapView(purchase.getEvent());

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

		User one = User.generateUser(DataConstants.DISTINCT_USER_ONE_FIRST_NAME,
				DataConstants.DISTINCT_USER_ONE_LAST_NAME);
		User two = User.generateUser(DataConstants.DISTINCT_USER_TWO_FIRST_NAME,
				DataConstants.DISTINCT_USER_TWO_LAST_NAME);
		User three = User.generateUser();
		return new Object[][] { { one, purchaseOne }, { two, purchaseOne }, { three, purchaseOne } };
	}

	private static Purchase preparePurchase() {
		Purchase purchase = new Purchase();
		purchase.setCreditCard(CreditCard.generateCreditCard());
		purchase.setNumberOfTickets(PURCHASE_QUANTITY);
		purchase.setEvent(Event.generatedEvent(START_DAY_OFFSET, DAYS_RANGE, EVENT_NAME, false));
		return purchase;
	}

}
