package test.facade;

import org.openqa.selenium.WebDriver;
import model.Organization;
import pages.admin.events.AdminEventsPage;
import pages.admin.organizations.AdminOrganizationsPage;
import pages.admin.organizations.CreateOrganizationPage;
import pages.components.Header;
import pages.components.admin.AdminSideBar;

public class OrganizationStepsFacade extends BaseFacadeSteps {
	
	private AdminEventsPage adminEventPage;
	private AdminOrganizationsPage organizationPage;
	private CreateOrganizationPage createOrganizationPage;
	private AdminSideBar adminSideBar;

	public OrganizationStepsFacade(WebDriver driver) {
		super(driver);
		this.adminEventPage = new AdminEventsPage(driver);
		this.organizationPage = new AdminOrganizationsPage(driver);
		this.createOrganizationPage = new CreateOrganizationPage(driver);
		this.adminSideBar = new AdminSideBar(driver);
	}

	public boolean createOrganization(Organization org) {
		boolean retVal = adminEventPage.isAtPage();
		Header header = adminEventPage.getHeader();
		header.clickOnBoxOfficeLink();
		header.clickOnToStudioLink();
		adminSideBar.clickOnOrganizations();
		organizationPage.clickOnCreateOrganizationButton();
		createOrganizationPage.fillFormAndConfirm(org);
		retVal = retVal && createOrganizationPage.checkPopupMessage();
		return retVal;
	}

	public boolean givenOrganizationExist(Organization org) throws Exception {
		Header header = new Header(driver);
		boolean isOrgPresent = header.isOrganizationPresent(org.getName());

		if (!isOrgPresent) {
			return createOrganization(org);
		} else {
			header.selectOrganizationFromDropDown(org.getName());
			return true;
		}
	}

}
