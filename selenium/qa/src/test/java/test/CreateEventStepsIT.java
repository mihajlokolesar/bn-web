package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.AdminSideBar;
import pages.components.Header;
import test.wrappers.CreateOrganizationWrapper;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class CreateEventStepsIT extends BaseSteps {

	@Test
	public void createEvent() throws Exception {

		LoginPage login = new LoginPage(driver);
		maximizeWindow();
		login.login("superuser@test.com", "password");
		Header header = new Header(driver);
		AdminEventsPage adminEvents = null;
		boolean isOrgPresent = header.isOrganizationPresent("Auto Test12");
		if (!isOrgPresent) {
			CreateOrganizationWrapper wr = new CreateOrganizationWrapper();
			Assert.assertTrue(wr.createOrganization(driver, "Auto Test12", "1111111111", "Africa/Johannesburg",
					"Johannesburg, South Africa"));
			AdminSideBar sideBar = new AdminSideBar(driver);
			adminEvents = sideBar.clickOnEvents();
		} else {
			header.selectOrganizationFromDropDown("Auto Test12");
			adminEvents = new AdminEventsPage(driver);
			adminEvents.isAtPage();
		}

		adminEvents.clickCreateEvent();

		CreateEventPage createEvent = new CreateEventPage(driver);
		createEvent.isAtPage();
		createEvent.clickOnImportSettingDialogNoThanks();

		createEvent.enterArtistName("The Testers");
		createEvent.enterEventName("TestNameEvent" + ProjectUtils.generateRandomInt(10000000));
		createEvent.selectVenue("MSG");
		
		String[] dateSpan = ProjectUtils.getDatesWithSpecifiedRangeInDays(2);
		String startDate = dateSpan[0];
		String endDate = dateSpan[1];
		createEvent.enterDatesAndTimes(startDate, endDate, "08:30 PM", "10:00 PM", "1");

		createEvent.addNewTicketType("GA", "100", "1");
		createEvent.addNewTicketType("VIP", "70", "2");
		
		createEvent.clickOnPublish();
		boolean retVal = createEvent.checkMessage();
				String path = SeleniumUtils.getUrlPath(driver);
		retVal = retVal && path.contains("edit");
		header.logOut();
		Assert.assertEquals(retVal, true);

	}
}
