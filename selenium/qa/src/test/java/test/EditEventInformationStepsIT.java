package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Event;
import model.User;
import pages.components.admin.AdminEventComponent;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.MsgConstants;

public class EditEventInformationStepsIT extends BaseSteps {
	
	@Test(dataProvider = "edit_event_data", priority = 12, retryAnalyzer = utils.RetryAnalizer.class)
	public void editEvent(User superuser, Event event) throws Exception {
		LoginStepsFacade loginStepsFacade = new LoginStepsFacade(driver);
		maximizeWindow();
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);

		loginStepsFacade.givenAdminUserIsLogedIn(superuser);
		organizationFacade.givenOrganizationExist(event.getOrganization());

		adminEventFacade.givenUserIsOnAdminEventsPage();
		AdminEventComponent eventComp = adminEventFacade.givenEventExistsAndPredicateCondition(event,
				component -> !component.isEventDrafted());

		eventComp.editEvent(event);//userIsOnEventPage
		event.setEventName("Updated" + event.getEventName());
		adminEventFacade.whenUserUpdatesDataOfEvent(event);
		adminEventFacade.whenUserClicksOnUpdateEvent();

		boolean isNotificationDisplayed = adminEventFacade.thenMessageNotificationShouldAppear(MsgConstants.EVENT_PUBLISHED);
		adminEventFacade.givenUserIsOnAdminEventsPage();
		boolean isMatchedDate = adminEventFacade.thenUpdatedEventShoudExist(event);

		Assert.assertTrue(isNotificationDisplayed && isMatchedDate);
		loginStepsFacade.logOut();

	}

	@DataProvider(name = "edit_event_data")
	public static Object[][] editEventData() {
		User superuser = User.generateSuperUser();
		Event event = Event.generateEvent();
		event.setEventName("TestNameEvent");
		event.setVenueName("Arcade Empire");
		return new Object[][] { { superuser, event } };
	}



}
