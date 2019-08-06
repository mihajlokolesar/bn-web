package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Organization;
import model.User;
import pages.LoginPage;
import pages.components.Header;
import test.wrappers.CreateOrganizationWrapper;

public class CreateOrganizationStepsIT extends BaseSteps {

	@Test(dataProvider = "create_organization_data")
	public void createOrganization(User user, Organization organization) {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.login(user.getEmailAddress(), user.getPass());
		Header header = new Header(driver);

		CreateOrganizationWrapper wr = new CreateOrganizationWrapper();
		boolean retVal = wr.createOrganization(driver, organization);
		header.logOut();
		Assert.assertEquals(retVal, true);
	}

	@DataProvider(name = "create_organization_data")
	public static Object[][] data() {
		return new Object[][] { { User.generateSuperUser(), Organization.generateOrganization() } };
	}

}
