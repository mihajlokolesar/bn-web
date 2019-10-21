package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.CreditCard;
import model.Event;
import model.Purchase;
import model.User;
import pages.EventsPage;
import pages.admin.orders.manage.SelectedOrderPage;
import pages.components.admin.orders.manage.ActivityItem;
import pages.components.admin.orders.manage.ActivityItem.ExpandedContent;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import test.facade.AdminEventDashboardFacade;
import test.facade.AdminFanManagementFacade;
import test.facade.EventStepsFacade;
import test.facade.LoginStepsFacade;
import utils.DataConstants;

public class FanManagementStepsIT extends BaseSteps {
	
	private static final String EVENT_NAME = "TestFanManagementEventName";
	private static final Integer START_DAY_OFFSET = 1;
	private static final Integer DAYS_RANGE = 2;
	private static final Integer PURCHASE_QUANTITY = 3;
	private Purchase purchase;
	
	
	/**
	 * Automation: Big Neon : Test 32: Fan Management ,View Fan Profile And Event Activity Filtering #1830
	 * @param fan
	 * @param orgAdmin
	 */
	@Test(dataProvider = "fan_profile_data", dependsOnMethods = {"predifinedDataUserPurchasesTickets"},
			priority = 24, retryAnalyzer = utils.RetryAnalizer.class )
	public void viewFanProfileAndEventsFiltering(User fan, User orgAdmin) {
		
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminFanManagementFacade fanManagementFacade = new AdminFanManagementFacade(driver);
		maximizeWindow();
		loginAndNavigationToFanProfilePage(loginFacade, fanManagementFacade, fan, orgAdmin);
		
		boolean isFanInfoValid = fanManagementFacade.whenUserChecksValidityOfFanInformation(fan);
		Assert.assertTrue(isFanInfoValid, "Fan information on fan profile page is not valid for fan: " + fan.getEmailAddress());
		
		fanManagementFacade.whenUserChecksUpcomingEventList();
		boolean isOnPastEventsPage = fanManagementFacade.whenUserClicksOnPastEventsLink();
		Assert.assertTrue(isOnPastEventsPage, "Failed to load past event list for fan: " + fan.getEmailAddress());
		
		boolean checkIfEventsAreDifferent = fanManagementFacade.thenUserComparesUpcomingAndPastEventLists();
		Assert.assertTrue(checkIfEventsAreDifferent, "Events in upcoming and past list are not different");
		loginFacade.logOut();
	}
	
	/**
	 * Automation: Big Neon: Test 33: Fan Management: Event Summary Card Should Contain Data #1837
	 * @param fan
	 * @param orgAdmin
	 */
	@Test(dataProvider = "fan_profile_data", priority = 25, retryAnalyzer = utils.RetryAnalizer.class)
	public void fanProfileEventSummaryCardsContainData(User fan, User orgAdmin) {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminFanManagementFacade fanManagementFacade = new AdminFanManagementFacade(driver);
		maximizeWindow();
		loginAndNavigationToFanProfilePage(loginFacade, fanManagementFacade, fan, orgAdmin);
		
		boolean isSummaryCardDataPresent = fanManagementFacade.thenEventSummaryDataShouldBePresentAndActivitiesCollapsed();
		Assert.assertTrue(isSummaryCardDataPresent);
		
		loginFacade.logOut();
	}
	/**
	 * Automation: Big Neon Test 34: Fan Management: ActivityItem/Purchased #1843
	 * @param fan
	 * @param orgAdmin
	 */
	@Test(dataProvider = "fan_profile_data", priority = 26, retryAnalyzer = utils.RetryAnalizer.class)
	public void fanProfilePurchasedActivityItems(User fan, User orgAdmin) {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminFanManagementFacade fanManagementFacade = new AdminFanManagementFacade(driver);
		maximizeWindow();
		loginAndNavigationToFanProfilePage(loginFacade, fanManagementFacade, fan, orgAdmin);
		ActivityItem item = fanManagementFacade.whenUserClicksOnShowDetailsOfSelectedSummaryCard(purchase.getEvent());
		ExpandedContent expandedContent = item.getExpandedContent();
		boolean isDataVisible = expandedContent.isDataVisible();
		Assert.assertTrue(isDataVisible, "Not all data in expanded purchased activity item is visible");
		
		loginFacade.logOut();
	}
	/**
	 * Automation: Big Neon: Fan Management: Test 35: ActivityItems/Refunded Created
	 * @param fan
	 * @param orgAdmin
	 */
	@Test(dataProvider = "fan_profile_data", priority = 27, retryAnalyzer = utils.RetryAnalizer.class)
	public void fanProfileRefundedActivityItem(User fan, User orgAdmin) {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminFanManagementFacade fanManagementFacade = new AdminFanManagementFacade(driver);
		AdminEventDashboardFacade dashboardFacade = new AdminEventDashboardFacade(driver);
		maximizeWindow();
		
		//given
		loginAndNavigationToFanProfilePage(loginFacade, fanManagementFacade, fan, orgAdmin);
		
		//when
		fanManagementFacade.whenUserPicksEventSummaryCard(this.purchase.getEvent());
		SelectedOrderPage selectedOrderPage = fanManagementFacade.whenUserClicksOnPurchasedActivityItemOrderNumberLink();
		boolean isOnOrderManagePage = dashboardFacade.thenUserIsOnSelectedManageOrderPage(selectedOrderPage);
		Assert.assertTrue(isOnOrderManagePage, "Not on orders manage page");
		dashboardFacade.refundSteps(RefundReason.UNABLE_TO_ATTEND);
		navigateToFanProfilePage(fanManagementFacade, fan);
		fanManagementFacade.whenUserPicksEventSummaryCard(this.purchase.getEvent());
		
		//then
		boolean isRefundDataVisible = fanManagementFacade.thenRefundedActivityItemForSpecificEventShouldBeVisible(orgAdmin, fan);
		Assert.assertTrue(isRefundDataVisible, "Refund data on activity item missing");
		loginFacade.logOut();
		
	}
	
	private void loginAndNavigationToFanProfilePage(LoginStepsFacade loginFacade, AdminFanManagementFacade fanManagementFacade, User fan, User orgAdmin) {
		loginFacade.givenAdminUserIsLogedIn(orgAdmin);
		navigateToFanProfilePage(fanManagementFacade, fan);
	}
	
	private void navigateToFanProfilePage(AdminFanManagementFacade fanManagementFacade, User fan) {
		fanManagementFacade.givenUserIsOnFansPage();
		fanManagementFacade.whenUserSelectsFanFormList(fan);
	}
	
	
	@DataProvider(name = "fan_profile_data")
	public static Object[][] fanProfileData(){
		Purchase purchase = preparePurchase();
		User fan = User.generateUserFromJson(DataConstants.DISTINCT_USER_THREE_KEY);
		User orgAdmin = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		return new Object[][] {{fan, orgAdmin}};
	}
	
	@Test(dataProvider = "purchase_data", priority = 24)
	public void predifinedDataUserPurchasesTickets(User fan, Purchase purchas) throws Exception {
		maximizeWindow();
		EventStepsFacade eventsFacade = new EventStepsFacade(driver);

		// given
		eventsFacade.givenUserIsOnEventPage();

		eventsFacade.givenThatEventExist(purchas.getEvent(), fan, false);
		if (this.purchase == null ) {
			this.purchase = purchas;
		}
		// when
		eventsFacade.whenUserExecutesEventPagesStepsWithoutMapView(purchas.getEvent());

		Assert.assertTrue(eventsFacade.thenUserIsAtTicketsPage());

		eventsFacade.whenUserSelectsNumberOfTicketsAndClicksOnContinue(purchas);
		eventsFacade.whenUserLogsInOnTicketsPage(fan);
		eventsFacade.thenUserIsAtConfirmationPage();
		eventsFacade.whenUserEntersCreditCardDetailsAndClicksOnPurchase(purchas.getCreditCard());

		// then
		eventsFacade.thenUserIsAtTicketPurchaseSuccessPage();
		eventsFacade.getLoginPage().logOut();
	}

	@DataProvider(name = "purchase_data")
	public static Object[][] data() {
		Purchase purchaseOne = preparePurchase();

		User fan = User.generateUserFromJson(DataConstants.DISTINCT_USER_THREE_KEY);
		
		return new Object[][] { { fan, purchaseOne }};
	}

	private static Purchase preparePurchase() {
		Purchase purchase = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		purchase.setCreditCard(CreditCard.generateCreditCard());
		purchase.setNumberOfTickets(PURCHASE_QUANTITY);
		purchase.setEvent(Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY,
				EVENT_NAME, true, START_DAY_OFFSET, DAYS_RANGE));
		return purchase;
	}
}
