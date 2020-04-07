package test.event.clone;

import model.Event;
import model.User;
import org.apache.commons.lang3.SerializationUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.components.admin.events.EventSummaryComponent;
import test.BaseSteps;
import test.facade.FacadeProvider;
import utils.DataConstants;

public class CloneEventStepIT extends BaseSteps {


	private static final int DATE_OFFSET = 0;
	private static final int DATE_RANGE = 0;
	private static final String EVENT_CLONE_TEMPLATE_NAME = "TestCloneTemplateName";
	private Event event;

	@Test(dataProvider = "clone_event_data_fixture", priority = 110)
	public void prepareDataFixture(Event event, User user) {
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
		this.event = event;
		fp.getLoginFacade().logOut();
	}

	@Test(dataProvider = "clone_event", priority = 112)
	public void cloneEvent(User user) {
		FacadeProvider fp = new FacadeProvider(driver);
		SoftAssert sa = new SoftAssert();
		fp.getLoginFacade().givenAdminUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		Event clone = SerializationUtils.clone(this.event);
		clone.setDates(DATE_OFFSET + 1, DATE_RANGE);
		clone.setStartTime("10:30 PM");
		clone.setEndTime("11:30 PM");
		clone.setEventName("Clone " + clone.getEventName());
		EventSummaryComponent eventCard = fp.getAdminEventStepsFacade().findEventWithName(event);
		eventCard.clickOnCloneEvent(clone);
		fp.getAdminEventStepsFacade().thenUserIsAtEditPage();
		fp.getAdminEventStepsFacade().whenUserChecksDataOnUpdatePage(clone, sa);
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		EventSummaryComponent clonedCard = fp.getAdminEventStepsFacade().findEventWithName(clone);
		clonedCard.clickOnDeleteEvent(clone);
		sa.assertAll();
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
