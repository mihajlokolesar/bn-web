package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Organization;
import model.User;
import pages.LoginPage;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class CreateOrganizationStepsIT extends BaseSteps {

	@Test(dataProvider = "create_organization_data", priority = 5 , retryAnalyzer = utils.RetryAnalizer.class)
	public void createOrganization(User user, Organization organization) {
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.login(user.getEmailAddress(), user.getPass());

		
		boolean retVal = organizationFacade.createOrganization(organization);
		loginPage.logOut();
		Assert.assertEquals(retVal, true);
	}

	@DataProvider(name = "create_organization_data")
	public static Object[][] data() {
		return new Object[][] {
			{ User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY),
			  Organization.generateOrganizationFromJson(DataConstants.ORGANIZATION_STD_KEY, true)} };
	}

}
