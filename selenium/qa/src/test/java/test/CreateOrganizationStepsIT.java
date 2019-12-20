package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.Organization;
import model.User;
import test.facade.FacadeProvider;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;

public class CreateOrganizationStepsIT extends BaseSteps {

	private Organization organization;

	@Test(dataProvider = "create_organization_data", priority = 5 , retryAnalyzer = utils.RetryAnalizer.class)
	public void createOrganization(User user, Organization organization) {
		FacadeProvider fp = new FacadeProvider(driver);
		LoginStepsFacade loginFacade = fp.getLoginFacade();
		OrganizationStepsFacade organizationFacade = fp.getOrganizationFacade();
		maximizeWindow();
		loginFacade.givenUserIsLogedIn(user);

		boolean retVal = organizationFacade.createOrganization(organization);
		if (this.organization == null) {
			this.organization = organization;
		}
		loginFacade.logOut();
		Assert.assertEquals(retVal, true);
	}

	@DataProvider(name = "create_organization_data")
	public static Object[][] createData() {
		return new Object[][] { { User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY),
				Organization.generateOrganizationFromJson(DataConstants.ORGANIZATION_TEST_AUTO_KEY, true) } };
	}

	@Test(dataProvider = "edit_organization_data", priority = 5, retryAnalyzer = utils.RetryAnalizer.class)
	public void editOrganization(User superuser, Organization org) {
		FacadeProvider fp = new FacadeProvider(driver);
		maximizeWindow();
		fp.getLoginFacade().givenUserIsLogedIn(superuser);
		fp.getOrganizationFacade().givenUserIsOnOrganizationsPage();
		fp.getOrganizationFacade().whenUserPicksOrganizationAndClickOnEdit(org);
		fp.getOrganizationFacade().thenUserIsOnOrganizationSettingsPage();
		fp.getOrganizationFacade().updateSteps(org, true, false);

	}

	@Test(dataProvider = "edit_organization_data", priority = 5, retryAnalyzer = utils.RetryAnalizer.class)
	public void editOrganizationFees(User user, Organization org) throws Exception {
		FacadeProvider fp = new FacadeProvider(driver);
		LoginStepsFacade loginFacade = fp.getLoginFacade();
		OrganizationStepsFacade organizationFacade = fp.getOrganizationFacade();
		maximizeWindow();
		loginFacade.givenAdminUserIsLogedIn(user);
		organizationFacade.givenUserIsOnOrganizationsPage();
		organizationFacade.whenUserPicksOrganizationAndClickOnEdit(org);
		organizationFacade.updateSteps(org, false, true);
	}

	@DataProvider(name = "edit_organization_data")
	public static Object[][] editData() {
		return new Object[][] { { User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY),
				Organization.generateOrganizationFromJson(DataConstants.ORGANIZATION_TEST_AUTO_KEY, false) } };
	}
}
