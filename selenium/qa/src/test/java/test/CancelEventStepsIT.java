package test;

import org.apache.commons.lang3.SerializationUtils;
import org.openqa.selenium.NoSuchElementException;
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
import pages.components.admin.AdminEventComponent;
import test.wrappers.CreateOrganizationWrapper;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class CancelEventStepsIT extends BaseSteps {

	@Test(dataProvider = "cancel_event_steps", priority = 9)
	public void cancelEventSteps(User superuser, Event event) throws Exception {
		Event genericEvent = SerializationUtils.clone(event);
		genericEvent.setEventName(genericEvent.getEventName());
		LoginPage login = new LoginPage(driver);
		maximizeWindow();
		login.login(superuser.getEmailAddress(), superuser.getPass());
		Header header = login.getHeader();
		AdminEventsPage adminEvents = null;
		boolean isOrgPresent = header.isOrganizationPresent(genericEvent.getOrganization().getName());
		AdminSideBar sideBar = new AdminSideBar(driver);

		if (!isOrgPresent) {
			CreateOrganizationWrapper wr = new CreateOrganizationWrapper();
			Assert.assertTrue(wr.createOrganization(driver, genericEvent.getOrganization()));
			adminEvents = sideBar.clickOnEvents();
		} else {
			header.selectOrganizationFromDropDown(genericEvent.getOrganization().getName());
			adminEvents = new AdminEventsPage(driver);
			adminEvents.isAtPage();
		}

		AdminEventComponent selectedEvent = adminEvents.findOpenedEventByName(genericEvent.getEventName());
		if (selectedEvent == null) {

			adminEvents.clickCreateEvent();
			CreateEventPage createEvent = new CreateEventPage(driver);
			event.setEventName(event.getEventName() + ProjectUtils.generateRandomInt(10000000));
			boolean retVal = createEvent.createEventPageSteps(event);
			Assert.assertTrue(retVal, "Event with name: " + event.getEventName() + " not created");
			String path = SeleniumUtils.getUrlPath(driver);
			retVal = retVal && path.contains("edit");

			sideBar.clickOnEvents();
			adminEvents.isAtPage();
			selectedEvent = adminEvents.findOpenedEventByName(event.getEventName());
		}

		selectedEvent.cancelEvent();
		login.logOut();
	}

	@DataProvider(name = "cancel_event_steps")
	public static Object[][] data_new_user() {
		User superUser = User.generateSuperUser();
		Event event = Event.generateEvent();
		event.setEventName("TestNameCancelEvent");
		return new Object[][] { { superUser, event } };
	}

}
