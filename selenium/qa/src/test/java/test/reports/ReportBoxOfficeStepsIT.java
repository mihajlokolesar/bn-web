package test.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Event;
import model.Organization;
import model.Purchase;
import model.User;
import test.BaseSteps;
import test.facade.AdminBoxOfficeFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.EventStepsFacade;
import test.facade.FacadeProvider;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class ReportBoxOfficeStepsIT extends BaseSteps {
	
	
	private final static String PURCHASE_ONE_KEY = "purchase_one";
	private final static String PURCHASE_TWO_KEY = "purchase_two";
	private final static String PURCHASE_THREE_KEY = "purchase_three";
	private final static String STANDARD_CUSTOMER_KEY = "standard_customer_key";
	private final static String CUSTOMER_KEY = "customer_key";
	private Purchase firstBOPurchase;
	private Purchase secondBOPurchase;
	private Purchase notBoxOfficePurchase;
	

	@Test(dataProvider = "prepare_box_offce_report_data_fixture")
	public void reportShouldOnlyContainBoxOfficeTransactions(Map<String,Object> data) throws Exception {
		Organization org = firstBOPurchase.getEvent().getOrganization();
		User orgAdmin = org.getTeam().getOrgAdminUser();
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenUserIsLogedIn(orgAdmin);
		fp.getOrganizationFacade().givenOrganizationExist(org);
		fp.getReportsFacade().givenUserIsOnReportsPage();
		fp.getReportsFacade().whenUserSelectBoxOfficeTab();
		fp.getReportsBoxOfficeFacade().enterDates();
		boolean isEventPresent = fp.getReportsBoxOfficeFacade().whenUserSearchesForEventInBoxOfficeReport(notBoxOfficePurchase.getEvent());
		Assert.assertFalse(isEventPresent,"There should be not tickets sold for this event in box office report" + notBoxOfficePurchase.getEvent().getEventName());
	}
	
	@DataProvider(name = "box_office_reports")
	public static Object[][] boxOfficeReportsData() {
		User standardCustomer = User.generateUserFromJson(DataConstants.USER_STANDARD_KEY);
		return new Object[][] {{
			standardCustomer
		}};
	}
	
	@Test(dataProvider = "prepare_box_offce_report_data_fixture")
	public void boxOfficeReportPrepareDataFixture(Map<String, Object> data) throws Exception {
		//create 3 events with orgadmin user (event1, event2, event3)
		//do box office sell (event1) with orgadmin user to standard user -cash
		//do box office sell (evnet2)with orgadmin user to userOne -credit card
		//
		//login with boxoffice user 
		//do box office sell (event2) with boxoffice user to standard user - credit card
		//do box office sell (event1) with boxoffice user to userOne - cash
		//
		//login with standardUser
		//find event (event1) and do the purchase
		
		this.firstBOPurchase = (Purchase) data.get(PURCHASE_ONE_KEY);
		this.secondBOPurchase = (Purchase) data.get(PURCHASE_TWO_KEY);
		this.notBoxOfficePurchase = (Purchase) data.get(PURCHASE_THREE_KEY);
		User orgAdmin = firstBOPurchase.getEvent().getOrganization().getTeam().getOrgAdminUser();
		User boxOfficeUser = firstBOPurchase.getEvent().getOrganization().getTeam().getBoxOfficeUsers().get(0);
		User standardCustomer = (User) data.get(STANDARD_CUSTOMER_KEY);
		User userOneCustomer = (User) data.get(CUSTOMER_KEY);
		
		FacadeProvider fProvider = new FacadeProvider(driver);
		AdminEventStepsFacade adminEventFacade = fProvider.getAdminEventStepsFacade();
		EventStepsFacade eventFacade = fProvider.getEventFacade();
		LoginStepsFacade loginFacade = fProvider.getLoginFacade();
		OrganizationStepsFacade orgFacade = fProvider.getOrganizationFacade();
		AdminBoxOfficeFacade boxOfficeFacade = fProvider.getBoxOfficeFacade();
		
		loginFacade.givenUserIsOnLoginPage();
		//create events
		loginFacade.givenUserIsLogedIn(orgAdmin);
		orgFacade.givenOrganizationExist(firstBOPurchase.getEvent().getOrganization());
		adminEventFacade.givenEventExistAndIsNotCanceled(firstBOPurchase.getEvent());
		adminEventFacade.givenEventExistAndIsNotCanceled(secondBOPurchase.getEvent());
		adminEventFacade.givenEventExistAndIsNotCanceled(notBoxOfficePurchase.getEvent());
		
		//do box office sell with org admin user
		boxOfficeFacade.givenUserIsOnBoxOfficePage();
		boxOfficeFacade.whenUserSellsTicketToCustomer(firstBOPurchase, "cash", standardCustomer);
		boxOfficeFacade.whenUserSellsTicketToCustomer(secondBOPurchase, "card", userOneCustomer);
		loginFacade.logOut();
		
		//do box office sell with box office user role
		loginFacade.givenUserIsLogedIn(boxOfficeUser);
		orgFacade.givenOrganizationExist(firstBOPurchase.getEvent().getOrganization());
		loginFacade.whenUserSelectsMyEventsFromProfileDropDown();
		boxOfficeFacade.givenUserIsOnSellPage();
		boxOfficeFacade.whenUserSellsTicketToCustomer(secondBOPurchase, "card", standardCustomer);
		boxOfficeFacade.whenUserSellsTicketToCustomer(firstBOPurchase, "cash", userOneCustomer);
		loginFacade.logOut();
		
		eventFacade.givenUserIsOnHomePage();
		eventFacade.whenUserDoesThePurchses(notBoxOfficePurchase, standardCustomer);
		loginFacade.logOut();
	}
	
	@DataProvider(name = "prepare_box_offce_report_data_fixture")
	public static Object[][] prepareBoxOffceReportDataFixture() {
		Event estTzEvent = Event.generateEventFromJson(DataConstants.EVENT_EST_TZ_KEY, true, 1, 1);
		Event jstTzEvent = Event.generateEventFromJson(DataConstants.EVENT_JST_TZ_KEY, true, 1, 1);
		Event jastTzEvent = Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY, true, 1, 1);
		Purchase prch1 = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		prch1.setEvent(estTzEvent);
		prch1.setNumberOfTickets(2);
		prch1.setOrderNote("Box office reports");
		
		Purchase prch2 = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		prch2.setEvent(jstTzEvent);
		prch2.setNumberOfTickets(2);
		prch2.setOrderNote("Box office reports");
		
		Purchase prch3 = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		prch3.setEvent(jastTzEvent);
		prch3.setNumberOfTickets(2);
		prch3.setOrderNote("Box office reports");
		
		User standardCustomer = User.generateUserFromJson(DataConstants.USER_STANDARD_KEY);
		User userOneCustomer = User.generateUserFromJson(DataConstants.DISTINCT_USER_ONE_KEY);
		Map<String,Object> data = new HashMap<>();
		data.put(PURCHASE_ONE_KEY, prch1);
		data.put(PURCHASE_TWO_KEY, prch2);
		data.put(PURCHASE_THREE_KEY, prch3);
		data.put(STANDARD_CUSTOMER_KEY, standardCustomer);
		data.put(CUSTOMER_KEY, userOneCustomer);
		return new Object[][] {{
			data
		}};
	}
}