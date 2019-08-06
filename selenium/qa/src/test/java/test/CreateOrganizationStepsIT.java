package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.components.Header;
import test.wrappers.CreateOrganizationWrapper;
import utils.ProjectUtils;

public class CreateOrganizationStepsIT extends BaseSteps {

	@Test
	public void createOrganization() {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.login("superuser@test.com", "password");
		Header header = new Header(driver);
		String name = "Auto test " + ProjectUtils.generateRandomInt(1000000);

		CreateOrganizationWrapper wr = new CreateOrganizationWrapper();
		boolean retVal = wr.createOrganization(driver, name, "1111111111", "Africa/Johannesburg",
				"Johannesburg, South Africa");
		header.logOut();
		Assert.assertEquals(retVal, true);
	}

}
