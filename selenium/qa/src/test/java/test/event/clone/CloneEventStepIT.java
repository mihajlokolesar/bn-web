package test.event.clone;

import model.AdditionalOptionsTicketType;
import model.Event;
import model.TicketType;
import model.User;
import model.Venue;
import model.interfaces.IAssertableField;
import org.apache.commons.lang3.SerializationUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.components.admin.events.EventSummaryComponent;
import test.BaseSteps;
import test.facade.FacadeProvider;
import utils.DataConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.AdditionalOptionsTicketType.*;

public class CloneEventStepIT extends BaseSteps {


	private static final int DATE_OFFSET = 0;
	private static final int DATE_RANGE = 0;
	private static final String EVENT_CLONE_TEMPLATE_NAME = "TestCloneTemplateName";
	private final String NAME_PREFIX = "Clone ";
	private Event originalEvent;
	private Event clonedEvent;

	@Test(dataProvider = "clone_event_data_fixture", priority = 110)
	public void prepareDataFixture(Event event, User user) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenAdminUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		EventSummaryComponent component = fp.getAdminEventStepsFacade().findEventWithNameAndPredicate(event, card -> card.isEventPublished());
		if (component == null) {
			event.randomizeName();
			fp.getAdminEventStepsFacade().createEvent(event);
		} else {
			event.setEventName(component.getEventName());
		}
		this.originalEvent = event;
		fp.getLoginFacade().logOut();
	}

	@Test(dataProvider = "clone_event", priority = 112)
	public void cloneEvent(User user) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		SoftAssert sa = new SoftAssert();
		fp.getLoginFacade().givenAdminUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(originalEvent.getOrganization());
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		Event clone = SerializationUtils.clone(this.originalEvent);
		clone.setDates(DATE_OFFSET + 1, DATE_RANGE);
		clone.setStartTime("10:30 PM");
		clone.setEndTime("11:30 PM");
		clone.setEventName(NAME_PREFIX + clone.getEventName());
		this.clonedEvent = clone;
		EventSummaryComponent eventCard = fp.getAdminEventStepsFacade().findEventWithName(originalEvent);
		eventCard.clickOnCloneEvent(clone);
		fp.getAdminEventStepsFacade().thenUserIsAtEditPage();
		fp.getAdminEventStepsFacade().whenUserChecksDataOnUpdatePage(clone, sa);
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();

		sa.assertAll();
	}

	@Test(dataProvider = "clone_event_compare_cloned_fields", priority = 114)
	public void confirmTheFollowingFieldsHaveBeenClonedSuccessfully(Map<Class, List<IAssertableField>> compareFields, User user) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		SoftAssert sa = new SoftAssert();

		if (clonedEvent == null || originalEvent == null) {
			throw new IllegalArgumentException("ClonedEvent and/or OriginalEvent object are null, previous methods failed");
		}
		fp.getLoginFacade().givenAdminUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(clonedEvent.getOrganization());
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		EventSummaryComponent eventCard = fp.getAdminEventStepsFacade().findEventWithName(this.clonedEvent);
		fp.getEventOverviewFacade().setEventOverviewPage(eventCard.clickOnEventOverview());
		fp.getEventOverviewFacade().thenUserIsAtOverviewPage();
		fp.getEventOverviewFacade().whenUserComparesGivenFieldsAndEvent(compareFields, originalEvent, sa);
		AdditionalOptionsField.SALE_START.setSaleStartExcluded(new SaleStart[]{});
		AdditionalOptionsField.SALE_END.setSaleEndExluded(new SaleEnd[]{});
		deleteEvent(fp, clonedEvent);
		sa.assertAll();

	}

	@DataProvider(name = "clone_event_compare_cloned_fields")
	public static Object[][] cloneEventFieldThatWereCloned(){
		User orgAdmin = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		IAssertableField[] eventFields = new IAssertableField[]{
				/*Event.EventField.DOOR_TIME*/};
		IAssertableField[] ticketTypeFields = new IAssertableField[]{
				TicketType.TicketTypeField.TICKET_TYPE_NAME, TicketType.TicketTypeField.CAPACITY, TicketType.TicketTypeField.PRICE};

		AdditionalOptionsField saleStart = AdditionalOptionsField.SALE_START;
		saleStart.setSaleStartExcluded(new SaleStart[]{SaleStart.AT_SPECIFIC_TIME});
		AdditionalOptionsField saleEnd = AdditionalOptionsField.SALE_END;
		saleEnd.setSaleEndExluded(new SaleEnd[]{SaleEnd.AT_SPECIFIC_TIME});

		IAssertableField[] additionalOptionsFields = new IAssertableField[]{
				saleStart, saleEnd, AdditionalOptionsField.START_SALE_TICKET_TYPE
		};
		IAssertableField[] venueTypeFields = new IAssertableField[]{
				Venue.VenueField.NAME, Venue.VenueField.ADDRESS, Venue.VenueField.CITY, Venue.VenueField.STATE_ABBR, Venue.VenueField.ZIP};

		Map<Class, List<IAssertableField>>mapClassFields = new HashMap<>();
		mapClassFields.put(Event.class, Arrays.asList(eventFields));
		mapClassFields.put(TicketType.class, Arrays.asList(ticketTypeFields));
		mapClassFields.put(AdditionalOptionsTicketType.class, Arrays.asList(additionalOptionsFields));
		mapClassFields.put(Venue.class, Arrays.asList(venueTypeFields));

		return new Object[][] {{
			mapClassFields, orgAdmin
		}};
	}


	@DataProvider(name = "clone_event")
	public static Object[][] cloneEventData() {
		User orgAdmin = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		return new Object[][]{{orgAdmin}};
	}

	@DataProvider(name = "clone_event_data_fixture")
	public static Object[][] cloneEventDataProvider() {
		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_WITH_ADDITIONAL_STEPS_KEY, EVENT_CLONE_TEMPLATE_NAME, false, DATE_OFFSET, DATE_RANGE);
		User orgAdmin = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		return new Object[][]{{event, orgAdmin}};
	}

}
