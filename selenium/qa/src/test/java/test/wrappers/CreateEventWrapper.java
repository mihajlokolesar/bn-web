package test.wrappers;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.Event;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.AdminSideBar;
import pages.components.Header;
import utils.SeleniumUtils;

public class CreateEventWrapper {
	
	public boolean createEvent(WebDriver driver, Event event) throws Exception {
		Header header = new Header(driver);
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
		return retVal;
	}

}
