package test;

import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.Header;
import utils.SeleniumUtils;

public class CreateEventStepsIT extends BaseSteps {

	@Test
	public void createEvent() throws URISyntaxException {

		LoginPage login = new LoginPage(driver);
		maximizeWindow();
		login.login("superuser@test.com", "password");

		AdminEventsPage adminEvents = new AdminEventsPage(driver);
		adminEvents.isAtPage();

		Header header = new Header(driver);
		header.selectOrganizationFromDropDown("Auto test1");

		adminEvents.clickCreateEvent();

		CreateEventPage createEvent = new CreateEventPage(driver);
		createEvent.isAtPage();
		createEvent.clickOnImportSettingDialogNoThanks();
//		createEvent.uploadImage("https://picsum.photos/id/10/2500/1667");
		createEvent.enterArtistName("The Testers");
		createEvent.enterEventName("TestNameEvent");
		createEvent.selectVenue("MSG");

		createEvent.enterDatesAndTimes("08/14/2019", "08/18/2019", "08:30 PM", "10:00 PM", "1");

		createEvent.addNewTicketType("GA", "100", "1");
		createEvent.addNewTicketType("VIP", "70", "2");
		createEvent.clickOnPublish();
		createEvent.waitForTime(1000);
		boolean retVal = createEvent.checkMessage();
				String path = SeleniumUtils.getUrlPath(driver);
		retVal = retVal && path.contains("edit");
		Assert.assertEquals(retVal, true);

	}

}
