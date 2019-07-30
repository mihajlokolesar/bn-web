package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.admin.AdminEventsPage;
import pages.admin.organizations.AdminOrganizationsPage;
import pages.admin.organizations.CreateOrganizationPage;
import pages.components.AdminSideBar;
import pages.components.Header;
import utils.ProjectUtils;

public class CreateOrganizationStepsIT extends BaseSteps {

	@Test
	public void createOrganization() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.login("superuser@test.com", "password");
		AdminEventsPage eventPage = new AdminEventsPage(driver);
		Assert.assertTrue(eventPage.isAtPage());
		Header header = new Header(driver);
		header.clickOnBoxOfficeLink();
		header.clickOnToStudioLink();
		AdminSideBar sideBar = new AdminSideBar(driver);
		AdminOrganizationsPage organizationPage = sideBar.clickOnOrganizations();
		CreateOrganizationPage createOrganization = organizationPage.clickOnCreateOrganizationButton();
		Integer randomNum = ProjectUtils.generateRandomInt(1000000);
		String name = "Auto test " + randomNum;
		createOrganization.fillFormAndConfirm(name, "1111111111", "Africa/Johannesburg", "Johannesburg, South Africa");
		boolean retVal = createOrganization.checkPopupMessage();
		header.logOut();
		Assert.assertEquals(retVal, true);
	}

}
