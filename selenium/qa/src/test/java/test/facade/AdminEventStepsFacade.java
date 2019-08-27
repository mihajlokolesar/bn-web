package test.facade;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.Event;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.admin.AdminEventComponent;
import pages.components.admin.AdminSideBar;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class AdminEventStepsFacade extends BaseFacadeSteps {

	private CreateEventPage createEventPage;
	private AdminEventsPage adminEvents;
	private Map<String, Object> container = new HashMap<>();

	public AdminEventStepsFacade(WebDriver driver) {
		super(driver);
		this.createEventPage = new CreateEventPage(driver);
		this.adminEvents = new AdminEventsPage(driver);
	}

	public void givenUserIsOnAdminEventsPage() {
		AdminSideBar adminSideBar = null;
		if (!adminEvents.isAtPage()) {
			adminSideBar = new AdminSideBar(driver);
			adminSideBar.clickOnEvents();
		}
	}

	public AdminEventComponent givenEventExistAndIsNotCanceled(Event event) throws URISyntaxException {
		AdminEventComponent selectedEvent = adminEvents.findOpenedEventByName(event.getEventName());
		if (selectedEvent == null) {
			event.setEventName(event.getEventName() + ProjectUtils.generateRandomInt(10000000));
			boolean retVal = createEvent(event);

			Assert.assertTrue(retVal,
					"Event with name: " + event.getEventName() + " does not exist and could not be created");
			String path = SeleniumUtils.getUrlPath(driver);
			retVal = retVal && path.contains("edit");

			AdminSideBar sideBar = new AdminSideBar(driver);
			sideBar.clickOnEvents();
			adminEvents.isAtPage();
			selectedEvent = adminEvents.findOpenedEventByName(event.getEventName());
		}
		return selectedEvent;
	}

	public boolean thenEventShouldBeCanceled(Event event) {
		AdminEventComponent componentEvent = adminEvents.findEventByName(event.getEventName());
		if (componentEvent != null) {
			return componentEvent.isEventCanceled();
		} else {
			return false;
		}
	}

	public boolean createEvent(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventPage.clickOnImportSettingDialogNoThanks();
		createEventPage.enterArtistName(event.getArtistName());
		createEventPage.enterEventName(event.getEventName());
		createEventPage.selectVenue(event.getVenueName());
		createEventPage.enterDatesAndTimes(event.getStartDate(), event.getEndDate(), event.getStartTime(),
				event.getEndTime(), event.getDoorTime());
		createEventPage.addTicketTypes(event.getTicketTypes());
		createEventPage.clickOnPublish();
		boolean retVal = createEventPage.checkMessage();
		return retVal;

	}

	public Map<String, Object> getContainer() {
		return container;
	}

	public void setContainer(Map<String, Object> container) {
		this.container = container;
	}

}
