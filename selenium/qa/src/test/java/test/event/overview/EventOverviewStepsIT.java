package test.event.overview;

import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Event;
import model.User;
import pages.components.admin.events.EventSummaryComponent;
import test.BaseSteps;
import test.facade.FacadeProvider;
import utils.DataConstants;

public class EventOverviewStepsIT extends BaseSteps {
	
	private static final String EVENT_NAME = "TestOverviewEventName";
	private static Integer DATE_OFFSET = 1;
	private static Integer DATE_RANGE = 0;
	
	@Test(dataProvider = "event_overview_data_provider", priority = 102, retryAnalyzer = utils.RetryAnalizer.class)
	public void navigateToEventOverview(Event event, User user) throws URISyntaxException {
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		EventSummaryComponent component = fp.getAdminEventStepsFacade()
				.givenEventWithNameAndPredicateExists(event, comp->!comp.isEventCanceled());
		String eventFullName = component.getEventName();
		component.clickOnEventOverview();
		fp.getEventOverviewFacade().setEventOverviewPage(eventFullName);
		Assert.assertTrue(fp.getEventOverviewFacade().thenUserIsAtOverviewPage(), "User is not at event overview page for event: " +eventFullName);
	}
	
	@Test(dataProvider = "event_overview_data_provider", priority = 100, retryAnalyzer = utils.RetryAnalizer.class)
	public void prepareDataFixture(Event event, User user) {
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenUserIsLogedIn(user);
		fp.getOrganizationFacade().givenOrganizationExist(event.getOrganization());
		fp.getAdminEventStepsFacade().createEvent(event);
	}
	
	@DataProvider(name = "event_overview_data_provider")
	public static Object[][] dataProvider(){
		Event event = Event.generateEventFromJson(DataConstants.USER_STANDARD_KEY, EVENT_NAME, false, DATE_OFFSET , DATE_RANGE);
		User user = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		return new Object[][] {{event, user}};
	}

}
