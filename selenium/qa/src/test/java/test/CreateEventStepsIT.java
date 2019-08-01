package test;

import org.testng.annotations.Test;

import pages.LoginPage;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.Header;

public class CreateEventStepsIT extends BaseSteps{
	
	@Test
	public void createEvent() {
		
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
		//some other steps
		createEvent.enterArtistName("The Testers");
		createEvent.enterEventName("TestNameEvent");
		createEvent.selectVenue("MSG");
		
		createEvent.enterDatesAndTimes("08/14/2019", "08/18/2019", "0830PM", "1000PM", "0.5");
		
		createEvent.addNewTicketType("GA", "20", "10");
		
		
	}

}
