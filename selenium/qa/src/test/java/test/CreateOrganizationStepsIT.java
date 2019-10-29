package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Organization;
import model.User;
import pages.LoginPage;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class CreateOrganizationStepsIT extends BaseSteps {
	
	private Organization organization;

	@Test(dataProvider = "create_organization_data", priority = 5 , retryAnalyzer = utils.RetryAnalizer.class)
	public void createOrganization(User user, Organization organization) {
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		maximizeWindow();
		loginFacade.givenUserIsLogedIn(user);
		
		boolean retVal = organizationFacade.createOrganization(organization);
		if(this.organization == null) {
			this.organization = organization;
		}
		loginFacade.logOut();
		Assert.assertEquals(retVal, true);
	}
	
	@DataProvider(name = "create_organization_data")
	public static Object[][] createData() {
		return new Object[][] {
			{ User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY),
			  Organization.generateOrganizationFromJson(DataConstants.ORGANIZATION_STD_KEY, true)} };
	}
		
	@Test(dataProvider = "edit_organization_data", priority = 5, retryAnalyzer = utils.RetryAnalizer.class)
	public void editOrganization(User user) throws Exception {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(user);
		organizationFacade.givenUserIsOnOrganizationsPage();
		organizationFacade.whenUserPicksOrganizationAndClickOnEdit(organization);
		organizationFacade.whenUserClickOnOtherFeesAndMakesChanges(organization.getOtherFees());
		boolean isNotificationVisible = organizationFacade.thenUpdateNotificationShouldBeVisible();
		Assert.assertTrue(isNotificationVisible, "Notification, organization updated not displayed");
	}
	
	@DataProvider(name = "edit_organization_data")
	public static Object[][] editData() {
		return new Object[][] {
			{ User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY)} };
	}
}
