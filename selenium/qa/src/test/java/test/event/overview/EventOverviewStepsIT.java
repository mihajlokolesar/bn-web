package test.event.overview;

import java.net.URISyntaxException;

import model.TicketType;
import model.Venue;
import org.apache.commons.lang3.SerializationUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import model.Event;
import model.User;
import pages.admin.events.CreateEventPage;
import pages.components.admin.events.EventSummaryComponent;
import test.BaseSteps;
import test.facade.FacadeProvider;
import utils.DataConstants;
import utils.ProjectUtils;

public class EventOverviewStepsIT extends BaseSteps {

	private static final String EVENT_NAME = "TestOverviewEventName";
	private static Integer DATE_OFFSET = 1;
	private static Integer DATE_RANGE = 0;
	private Event fieldEvent;

	@Test(dataProvider = "event_overview_data_provider", priority = 102)
	public void eventOverviewCompareData(Event event, User admin) throws URISyntaxException {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		SoftAssert sa = new SoftAssert();
		if (this.fieldEvent == null) {
			this.fieldEvent = event;
		}
		loginAndNavigateToOverviewPage(fp, this.fieldEvent, admin);
		fp.getEventOverviewFacade().whenUserComparesInfoOnOverviewWithGivenEvent(this.fieldEvent, sa);
		sa.assertAll();
	}

	@Test(dataProvider = "event_overview_data_provider", priority = 104)
	public void overviewPageShouldUpdateImmediatelyAfterEventIsUpdated(Event event, User admin) throws URISyntaxException {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		SoftAssert sa = new SoftAssert();
		if (this.fieldEvent == null) {
			this.fieldEvent = event;
		}
		loginAndNavigateToOverviewPage(fp, this.fieldEvent, admin);
		Event overviewEventData = fp.getEventOverviewFacade().whenUserRemembersEventInfoFromOverviewPage();
		fp.getEventOverviewFacade().whenUserSelectsEditEvent();
		Event editedEvent = SerializationUtils.clone(this.fieldEvent);
		editedEvent.setDoorTime("5");
		editedEvent.setComparableDoorTime(editedEvent.calculateComparableDoorTime(ProjectUtils.TIME_FORMAT_FULL, editedEvent.getDoorTime()));
		editedEvent.setVenue(Venue.generateVenueFromJson(DataConstants.VENUE_CST));
		CreateEventPage eventPage = fp.getAdminEventStepsFacade().getCreateEventPage();
		eventPage.selectVenue(editedEvent.getVenue().getName());
		eventPage.selectDoorTime(editedEvent.getDoorTime());
		eventPage.clickOnUpdateButton();
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();

		EventSummaryComponent eventCardEdited = fp.getAdminEventStepsFacade().findEventWithName(editedEvent);
		String editedEventName = eventCardEdited.getEventName();
		eventCardEdited.clickOnEventOverview();
		fp.getEventOverviewFacade().setEventOverviewPage(editedEventName);
		fp.getEventOverviewFacade().whenUserComparesInfoOnOverviewWithGivenEvent(editedEvent, sa);
		sa.assertAll();

		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		EventSummaryComponent eventSummaryComponent = fp.getAdminEventStepsFacade().findEventWithName(editedEvent);
		eventSummaryComponent.deleteEvent(editedEvent);
	}

	@Test(dataProvider = "event_overview_data_provider", priority = 100, retryAnalyzer = utils.RetryAnalizer.class)
	public void prepareDataFixture(Event event, User user) {
		FacadeProvider fp = new FacadeProvider(driver);
		maximizeWindow();
		fp.getLoginFacade().givenUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		fp.getAdminEventStepsFacade().createEvent(event);
	}

	private void loginAndNavigateToOverviewPage(FacadeProvider fp, Event event, User user) throws URISyntaxException {
		fp.getLoginFacade().givenAdminUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		EventSummaryComponent component = fp.getAdminEventStepsFacade()
				.givenEventWithNameAndPredicateExists(event, comp->!comp.isEventCanceled());
		String eventFullName = component.getEventName();
		component.clickOnEventOverview();
		fp.getEventOverviewFacade().setEventOverviewPage(eventFullName);
		Assert.assertTrue(fp.getEventOverviewFacade().thenUserIsAtOverviewPage(), "User is not at event overview page for event: " +eventFullName);
	}

	@DataProvider(name = "event_overview_data_provider")
	public static Object[][] dataProvider(){
		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_WITH_ADDITIONAL_STEPS_KEY, EVENT_NAME, false, DATE_OFFSET , DATE_RANGE);

		User user = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		return new Object[][] {{event, user}};
	}

}
