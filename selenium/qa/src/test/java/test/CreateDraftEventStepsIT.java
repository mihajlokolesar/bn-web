package test;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Event;
import model.User;
import pages.LoginPage;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class CreateDraftEventStepsIT extends BaseSteps {
	
	@Test(dataProvider = "event_save_as_draft", priority = 11, retryAnalyzer = utils.RetryAnalizer.class)
	public void createDraftEvent(User superuser, Event event) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		maximizeWindow();
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);

		LoginPage loginPage = loginFacade.givenAdminUserIsLogedIn(superuser);
		boolean isOrgPresent = organizationFacade.givenOrganizationExist(event.getOrganization());
		Assert.assertTrue(isOrgPresent, "Organization: " + event.getOrganization().getName() + " does not exist");
		adminEventFacade.givenUserIsOnAdminEventsPage();

		boolean isEventSavedToDraft = adminEventFacade.whenUserEntesDataAndClicksOnSaveDraft(event);
		Assert.assertTrue(isEventSavedToDraft, "Event: " + event.getEventName() + " is not saved to draft");
		adminEventFacade.givenUserIsOnAdminEventsPage();

		isEventSavedToDraft = isEventSavedToDraft && adminEventFacade.thenEventShouldBeDrafted(event);

		Assert.assertTrue(isEventSavedToDraft, "Draft event: " + event.getEventName() + " is not found on events list");
		loginPage.logOut();
	}
	
	@DataProvider(name = "event_save_as_draft")
	public static Object[][] data() {
		User superuser = User.generateSuperUser();
		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY,
				"TestNameEventDraft", true, 1, 2);
		event.setEndDate(null);
		event.setEndTime(null);
		event.setStartDate(null);
		event.setStartTime(null);
		event.setDoorTime(null);
		event.setTicketTypes(new ArrayList<>());

		return new Object[][] { {superuser, event } };

	}

}
