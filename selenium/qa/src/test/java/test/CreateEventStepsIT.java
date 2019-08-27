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

public class CreateEventStepsIT extends BaseSteps {

	@Test(dataProvider = "create_event_data", priority = 6, retryAnalyzer = utils.RetryAnalizer.class)
	public void createEvent(User superuser, Event event) throws Exception {
		
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		maximizeWindow();
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		
		//given
		LoginPage loginPage = loginFacade.givenAdminUserIsLogedIn(superuser);
		boolean isOrgPresent = organizationFacade.givenOrganizationExist(event.getOrganization());
		Assert.assertTrue(isOrgPresent, "Organization: " + event.getOrganization().getName() + " does not exist");
		adminEventFacade.givenUserIsOnAdminEventsPage();
		
		//when
		boolean isEventCreated = adminEventFacade.createEvent(event);
		
		//then
		Assert.assertTrue(isEventCreated, "Event: " + event.getEventName() + " with additional options not created");
		
		loginPage.logOut();

	}
	
	@DataProvider(name = "create_event_data")
	public static Object[][] data() {
		return new Object[][] { { User.generateSuperUser(), Event.generateEvent()} };
	}
}
