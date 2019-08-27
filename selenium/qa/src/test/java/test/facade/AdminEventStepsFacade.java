package test.facade;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.Event;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.AdminSideBar;
import pages.components.admin.AdminEventComponent;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class AdminEventStepsFacade extends BaseFacadeSteps {

	private CreateEventPage createEventPage;
	private AdminEventsPage adminEvents;
	private AdminSideBar adminSideBar;
	private Map<String, Object> container = new HashMap<>();

	public AdminEventStepsFacade(WebDriver driver) {
		super(driver);
		this.createEventPage = new CreateEventPage(driver);
		this.adminEvents = new AdminEventsPage(driver);
		this.adminSideBar = new AdminSideBar(driver);
	}

	public void givenUserIsOnAdminEventsPage() {
		adminSideBar.clickOnEvents();
		adminEvents.isAtPage();
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
			
			adminSideBar.clickOnEvents();
			adminEvents.isAtPage();
			selectedEvent = adminEvents.findOpenedEventByName(event.getEventName());
		}
		return selectedEvent;
	}
	
	public AdminEventComponent givenEventExistsAndPredicateCondition(Event event, Predicate<AdminEventComponent> predicate) throws URISyntaxException {
		AdminEventComponent selectedEvent = adminEvents.findEvent(event.getEventName(), predicate);
		if (selectedEvent == null) {
			event.setEventName(event.getEventName() + ProjectUtils.generateRandomInt(10000000));
			boolean retVal = createEvent(event);
			
			Assert.assertTrue(retVal,
					"Event with name: " + event.getEventName() + " does not exist and could not be created");
			String path = SeleniumUtils.getUrlPath(driver);
			retVal = retVal && path.contains("edit");
			adminSideBar.clickOnEvents();
			adminEvents.isAtPage();
			selectedEvent = adminEvents.findEvent(event.getEventName(), predicate);
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
	
	public boolean thenUpdatedEventShoudExist(Event event) {
		AdminEventComponent component = this.adminEvents.findEventByName(event.getEventName());
		if (component != null ) {
			return component.checkIfDatesMatch(event.getStartDate());
		} else {
			return false;
		}
	}
	
	public boolean thenEventShouldBeDrafted(Event event) {
		AdminEventComponent component = adminEvents.findEventByName(event.getEventName());
		return component.isEventDrafted();
	}

	public boolean createEvent(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventFillData(event);
//		createEventPage.clickOnPublish();
		boolean retVal = createEventPage.checkMessage();
		return retVal;
	}
	
	public boolean whenUserEntesDataAndClicksOnSaveDraft(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventFillData(event);
		createEventPage.clickOnSaveDraft();
		boolean retVal = createEventPage.checkSaveDraftMessage();
		return retVal;
		
	}
	
	public void whenUserUpdatesDataOfEvent(Event event) {
		createEventPage.enterEventName(event.getEventName());
		createEventPage.enterDatesAndTimes(event.getStartDate(), event.getEndDate(), null, null, null);
	}
	
	public void whenUserClicksOnUpdateEvent() {
		createEventPage.clickOnUpdateButton();
	}
	
	public boolean thenMessageNotificationShouldAppear(String msg) {
		return createEventPage.isNotificationDisplayedWithMessage(msg);
	}
	
	private void createEventFillData(Event event) {
		createEventPage.clickOnImportSettingDialogNoThanks();
		createEventPage.enterArtistName(event.getArtistName());
		createEventPage.enterEventName(event.getEventName());
		createEventPage.selectVenue(event.getVenueName());
		createEventPage.enterDatesAndTimes(event.getStartDate(), event.getEndDate(), event.getStartTime(),
				event.getEndTime(), event.getDoorTime());
		createEventPage.addTicketTypes(event.getTicketTypes());
	}

	public Map<String, Object> getContainer() {
		return container;
	}

	public void setContainer(Map<String, Object> container) {
		this.container = container;
	}

}
