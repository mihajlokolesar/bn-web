package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.AdditionalOptionsTicketType.SaleEnd;
import model.AdditionalOptionsTicketType.SaleStart;
import model.Event;
import model.TicketType;
import model.User;
import pages.LoginPage;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;
import utils.ProjectUtils;

public class CreateEventWithAdditionalOptionsStepsIT extends BaseSteps {

	@Test(dataProvider = "event_with_additional_opt_different_sales_date", priority = 10, retryAnalyzer = utils.RetryAnalizer.class)
	public void createEventWithAdditionalOptionsDifferentSalesDate(User superuser, Event event) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		maximizeWindow();

		// given
		LoginPage loginPage = loginFacade.givenAdminUserIsLogedIn(superuser);
		boolean isOrgPresent = organizationFacade.givenOrganizationExist(event.getOrganization());
		Assert.assertTrue(isOrgPresent, "Organization: " + event.getOrganization().getName() + " does not exist");
		adminEventFacade.givenUserIsOnAdminEventsPage();

		// when
		boolean isEventCreated = adminEventFacade.createEvent(event);

		// then
		Assert.assertTrue(isEventCreated, "Event: " + event.getEventName() + " with additional options not created");

		loginPage.logOut();

	}

	@DataProvider(name = "event_with_additional_opt_different_sales_date")
	public static Object[][] data() throws Exception {
		User superuser = User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY);
		Event event = (Event) Event.generateEventFromJson("event_data_with_additional_steps_std", true, 1, 4);
		String[] dates = ProjectUtils.getAllDatesWithinGivenRangeAndOffset(2, 4);
		event.setStartDate(dates[0]);
		event.setEndDate(dates[3]);
		TicketType atSpecificType = event.getTicketTypes().stream()
				.filter(tt -> tt.getAdditionalOptions().getSaleStart().equals(SaleStart.AT_SPECIFIC_TIME)
						&& tt.getAdditionalOptions().getSaleEnd().equals(SaleEnd.AT_SPECIFIC_TIME)).findFirst()
				.get();
		atSpecificType.getAdditionalOptions().setStartSaleDate(dates[1]);
		atSpecificType.getAdditionalOptions().setEndSaleDate(dates[1]);
		return new Object[][] { { superuser, event } };

	}
}
