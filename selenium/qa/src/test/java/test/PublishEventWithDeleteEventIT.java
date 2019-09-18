package test;

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

public class PublishEventWithDeleteEventIT extends BaseSteps {

	@Test(dataProvider = "create_delete_event_data", priority = 14, retryAnalyzer = utils.RetryAnalizer.class)
	public void publishEventWithDeleteAfterPublish(User superuser, Event event) throws Exception {

		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		maximizeWindow();
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);

		// given
		LoginPage loginPage = loginFacade.givenAdminUserIsLogedIn(superuser);
		boolean isOrgPresent = organizationFacade.givenOrganizationExist(event.getOrganization());
		Assert.assertTrue(isOrgPresent, "Organization: " + event.getOrganization().getName() + " does not exist");
		adminEventFacade.givenUserIsOnAdminEventsPage();

		// when
		boolean isEventCreated = adminEventFacade.createEvent(event);

		// then
		Assert.assertTrue(isEventCreated, "Event: " + event.getEventName() + " with additional options not created");
		adminEventFacade.givenUserIsOnAdminEventsPage();
		
		boolean isEventDeleted = adminEventFacade.whenUserDeletesEvent(event);
		Assert.assertTrue(isEventDeleted, "Event: " + event.getEventName() + " could not be deleted");
		
		loginPage.logOut();

	}

	@DataProvider(name = "create_delete_event_data")
	public static Object[][] data() {
		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY,"TestDeleteSearchEventName", true, 1, 4);
		return new Object[][] { { User.generateSuperUser(), event } };
	}

}
