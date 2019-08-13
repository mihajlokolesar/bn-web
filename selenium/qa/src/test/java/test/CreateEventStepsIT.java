package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Event;
import model.User;
import pages.LoginPage;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.AdminSideBar;
import pages.components.Header;
import test.wrappers.CreateOrganizationWrapper;
import utils.SeleniumUtils;

public class CreateEventStepsIT extends BaseSteps {

	@Test(dataProvider = "create_event_data", priority = 6)
	public void createEvent(User superuser, Event event) throws Exception {

		LoginPage login = new LoginPage(driver);
		maximizeWindow();
		login.login(superuser.getEmailAddress(), superuser.getPass());
		Header header = login.getHeader();
		AdminEventsPage adminEvents = null;
		boolean isOrgPresent = header.isOrganizationPresent(event.getOrganization().getName());
		
		if (!isOrgPresent) {
			CreateOrganizationWrapper wr = new CreateOrganizationWrapper();
			Assert.assertTrue(wr.createOrganization(driver, event.getOrganization()));
			AdminSideBar sideBar = new AdminSideBar(driver);
			adminEvents = sideBar.clickOnEvents();
		} else {
			header.selectOrganizationFromDropDown(event.getOrganization().getName());
			adminEvents = new AdminEventsPage(driver);
			adminEvents.isAtPage();
		}

		adminEvents.clickCreateEvent();

		CreateEventPage createEvent = new CreateEventPage(driver);
		boolean retVal = createEvent.createEventPageSteps(event);
		String path = SeleniumUtils.getUrlPath(driver);
		retVal = retVal && path.contains("edit");
		header.logOut();
		Assert.assertEquals(retVal, true);

	}
	
	@DataProvider(name = "create_event_data")
	public static Object[][] data() {
		return new Object[][] { { User.generateSuperUser(), Event.generateEvent()} };
	}
}
