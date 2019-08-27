package test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.AdditionalOptionsTicketType;
import model.AdditionalOptionsTicketType.SaleStart;
import model.Event;
import model.TicketType;
import model.User;
import pages.LoginPage;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.ProjectUtils;

public class CreateEventWithAdditionalOptionsStepsIT extends BaseSteps {

	@Test(dataProvider = "event_with_additional_opt_different_sales_date", priority = 10, retryAnalyzer = utils.RetryAnalizer.class)
	public void createEventWithAdditionalOptionsDifferentSalesDate(User superuser, Event event) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		maximizeWindow();
		
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

	@DataProvider(name = "event_with_additional_opt_different_sales_date")
	public static Object[][] data() {
		User superuser = User.generateSuperUser();
		Event event = Event.generateEvent();
		String[] dates = ProjectUtils.getAllDatesWithinGivenRangeAndOffset(1, 4);
		event.setStartDate(dates[0]);
		event.setEndDate(dates[3]);
		
		TicketType ticketType1 = new TicketType("GA", "30", "1");
		AdditionalOptionsTicketType immediateStartOptions = new AdditionalOptionsTicketType();
		immediateStartOptions.setSaleStart(SaleStart.IMMEDIATELY);
		ticketType1.setAdditionalOptions(immediateStartOptions);

		TicketType ticketType2 = new TicketType("VIP", "40", "2");
		AdditionalOptionsTicketType atSpecificTime = new AdditionalOptionsTicketType();
		atSpecificTime.setSaleStart(SaleStart.AT_SPECIFIC_TIME);
		atSpecificTime.setStartSaleDate(dates[1]);
		atSpecificTime.setStartSaleTime("07:00 AM");
		ticketType2.setAdditionalOptions(atSpecificTime);

		TicketType ticketType3 = new TicketType("Special", "50", "1");
		AdditionalOptionsTicketType whenSalesEnd = new AdditionalOptionsTicketType();
		whenSalesEnd.setSaleStart(SaleStart.WHEN_SALES_END_FOR);
		whenSalesEnd.setStartSaleTicketType(ticketType1.getTicketTypeName());
		ticketType3.setAdditionalOptions(whenSalesEnd);
		
		List<TicketType> ticketTypes = new ArrayList<TicketType>();
		ticketTypes.add(ticketType1);
		ticketTypes.add(ticketType2);
		ticketTypes.add(ticketType3);
		event.setTicketTypes(ticketTypes);

		return new Object[][] { {superuser, event } };

	}
}
