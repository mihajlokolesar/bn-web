package test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;
import model.Event;
import model.Organization;
import model.User;
import pages.components.admin.AdminEventComponent;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;

public class RefundStepsIT extends BaseSteps {

//	boxneousera@mailinator.com,test1111,neotestbox,officeuser - registered box office user (both on develop, and neon)
	@Test(dataProvider = "refund_data")
	public void refundSteps(User superuser, Event event) throws Exception {

		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(superuser);
		organizationFacade.givenOrganizationExist(event.getOrganization());

		adminEventFacade.givenUserIsOnAdminEventsPage();
		AdminEventComponent eventComponent = adminEventFacade.givenEventIsOpenedAndHasSoldItem(event);
		Assert.assertNotNull("No Event with name: " + event.getEventName() + " found", eventComponent);
		eventComponent.clickOnEvent();
		adminEventFacade.thenUserIsOnEventDashboardPage();
		adminEventFacade.whenUserSelectManageOrdersFromToolsDropDown();
		adminEventFacade.thenUserIsOnManageOrdersPage();
		adminEventFacade.whenUserSelectsTicketForRefundAndClicksOnRefundButton();
		adminEventFacade.whenUserGetConfirmationsOfSuccessfulRefund();
		boolean isTicketPresent = adminEventFacade.thenTicketShouldNotBePresent();
		Assert.assertFalse(isTicketPresent);
		loginFacade.logOut();

	}

	@DataProvider(name = "refund_data")
	public Object[][] refundDataProvider() {
		Event event = Event.generatedEvent(1, 5, "TestNameEvent", false);
		return new Object[][] { { User.generateSuperUser(), event } };
	}

}
