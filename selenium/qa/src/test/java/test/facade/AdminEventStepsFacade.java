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
import pages.admin.events.DashboardEventPage;
import pages.admin.events.ManageOrdersAdminPage;
import pages.components.admin.AdminEventComponent;
import pages.components.admin.AdminSideBar;
import pages.components.admin.ManageOrderComp;
import pages.components.dialogs.RefundDialog;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class AdminEventStepsFacade extends BaseFacadeSteps {

	private CreateEventPage createEventPage;
	private AdminEventsPage adminEvents;
	private AdminSideBar adminSideBar;
	private DashboardEventPage dashboardEventPage;
	private ManageOrdersAdminPage manageOrdersPage;

	private final String MANAGE_ORDER_FIRST_NAME_KEY = "mange_order_first_name";
	private final String MANAGE_ORDER_LAST_NAME_KEY = "manage_order_last_name";
	private final String MANAGE_ORDER_TICKET_NUMBER_KEY = "manage_order_ticket_number";

	private Map<String, Object> dataMap;

	public AdminEventStepsFacade(WebDriver driver) {
		super(driver);
		this.createEventPage = new CreateEventPage(driver);
		this.adminEvents = new AdminEventsPage(driver);
		this.adminSideBar = new AdminSideBar(driver);
		this.dashboardEventPage = new DashboardEventPage(driver);
		this.manageOrdersPage = new ManageOrdersAdminPage(driver);
		this.dataMap = new HashMap<>();
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

	public AdminEventComponent givenEventExistsAndPredicateCondition(Event event,
			Predicate<AdminEventComponent> predicate) throws URISyntaxException {
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

	public AdminEventComponent givenEventIsOpenedAndHasSoldItem(Event event) {
		AdminEventComponent selectedEvent = adminEvents.findEvent(event.getEventName(),
				comp -> comp.isEventPublished() && comp.isEventOnSale() && comp.isSoldToAmountGreaterThan(0));
		return selectedEvent;
	}

	public void whenUserSelectManageOrdersFromToolsDropDown() {
		dashboardEventPage.selectManageOrdersFromTools();
	}

	public void whenUserSelectsTicketForRefundAndClicksOnRefundButton() {
		ManageOrderComp orderComponent = manageOrdersPage.openOrderWithIndexNumber(1);
		setData(MANAGE_ORDER_FIRST_NAME_KEY, orderComponent.getFirstNameOfUser());
		setData(MANAGE_ORDER_LAST_NAME_KEY, orderComponent.getLastNameOfUser());
		setData(MANAGE_ORDER_TICKET_NUMBER_KEY, orderComponent.selectTicketForRefund());
		manageOrdersPage.clickOnRefundButton();
		RefundDialog refundDialog = new RefundDialog(driver);
		refundDialog.clickOnDialogRefundButton();
	}

	public void whenUserGetConfirmationsOfSuccessfulRefund() {
		new RefundDialog(driver).confirmRefundIsSuccess();
	}

	public boolean thenTicketShouldNotBePresent() {
		String ticketNumber = (String) getData(MANAGE_ORDER_TICKET_NUMBER_KEY);
		String firstName = (String) getData(MANAGE_ORDER_FIRST_NAME_KEY);
		String lastName = (String) getData(MANAGE_ORDER_LAST_NAME_KEY);
		return manageOrdersPage.isTicketPresentInOrder(ticketNumber, firstName, lastName);
	}

	public void thenUserIsOnManageOrdersPage() {
		manageOrdersPage.isAtPage();
	}

	public void thenUserIsOnEventDashboardPage() {
		dashboardEventPage.isAtPage();
	}

	public void whenUserUpdatesDataOfEvent(Event event) {
		createEventPage.enterEventName(event.getEventName());
		createEventPage.enterDatesAndTimes(event.getStartDate(), event.getEndDate(), null, null, null);
	}

	public void whenUserClicksOnUpdateEvent() {
		createEventPage.clickOnUpdateButton();
	}

	public boolean whenUserEntesDataAndClicksOnSaveDraft(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventFillData(event);
		createEventPage.clickOnSaveDraft();
		boolean retVal = createEventPage.checkSaveDraftMessage();
		return retVal;

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
		if (component != null) {
			return component.checkIfDatesMatch(event.getStartDate());
		} else {
			return false;
		}
	}

	public boolean thenMessageNotificationShouldAppear(String msg) {
		return createEventPage.isNotificationDisplayedWithMessage(msg);
	}

	public boolean thenEventShouldBeDrafted(Event event) {
		AdminEventComponent component = adminEvents.findEventByName(event.getEventName());
		return component.isEventDrafted();
	}

	public boolean createEvent(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventFillData(event);
		createEventPage.clickOnPublish();
		boolean retVal = createEventPage.checkMessage();
		return retVal;
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

	private void setData(String key, Object value) {
		dataMap.put(key, value);
	}

	private Object getData(String key) {
		return dataMap.get(key);
	}
}
