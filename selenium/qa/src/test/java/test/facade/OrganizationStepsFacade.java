package test.facade;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import model.Organization;
import model.organization.FeesSchedule;
import model.organization.OtherFees;
import pages.admin.events.AdminEventsPage;
import pages.admin.organizations.AdminOrganizationsPage;
import pages.admin.organizations.CreateOrganizationPage;
import pages.admin.organizations.EditOrganizationPage;
import pages.components.Header;
import pages.components.admin.AdminSideBar;
import pages.components.admin.organization.AdminOrganizationComponent;
import pages.components.admin.organization.settings.FeeScheduleComponent;
import pages.components.admin.organization.settings.OtherFeesComponent;
import utils.MsgConstants;

public class OrganizationStepsFacade extends BaseFacadeSteps {
	
	private AdminEventsPage adminEventPage;
	private AdminOrganizationsPage organizationPage;
	private CreateOrganizationPage createOrganizationPage;
	private AdminSideBar adminSideBar;
	
	private String EDIT_ORGANIZATION_PAGE_KEY = "edit_organization_page"; 
	
	private Map<String, Object> data;

	public OrganizationStepsFacade(WebDriver driver) {
		super(driver);
		this.adminEventPage = new AdminEventsPage(driver);
		this.organizationPage = new AdminOrganizationsPage(driver);
		this.createOrganizationPage = new CreateOrganizationPage(driver);
		this.adminSideBar = new AdminSideBar(driver);
		this.data = new HashMap<>();
	}
	
	public boolean givenUserIsOnOrganizationsPage() {
		adminSideBar.clickOnOrganizations();
		return thenUserIsOnOrganizationsPage();
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
	
	public void whenUserPicksOrganizationAndClickOnEdit(Organization org) {
		AdminOrganizationComponent orgComponent = organizationPage.findOrganizationWithName(org.getName());
		String id = orgComponent.getOrgId();
		orgComponent.clickOnEditDetailsButton();
		EditOrganizationPage selectedOrganization = new EditOrganizationPage(driver, id);
		setData(EDIT_ORGANIZATION_PAGE_KEY, selectedOrganization);
	}
	
	public boolean whenUserClickOnFeesScheeduleAndMakesChanges(FeesSchedule feesSchedule) {
		EditOrganizationPage editOrganizationPage = (EditOrganizationPage) getData(EDIT_ORGANIZATION_PAGE_KEY);
		editOrganizationPage.getSettingNavHeader().clickOnFeesSchedule();
		FeeScheduleComponent feeScheeduleComponent = editOrganizationPage.getFeeScheeduleComponent();
		feeScheeduleComponent.addNewRowAndFillFees(feesSchedule);
		feeScheeduleComponent.clickOnUpdateButton();
		return editOrganizationPage.isNotificationDisplayedWithMessage(MsgConstants.ORGANIZAATION_FEE_SCHEDULE_SAVED);
	}
	
	public boolean whenUserClickOnOtherFeesAndMakesChanges(OtherFees otherFees) {
		EditOrganizationPage editOrganizationPage = (EditOrganizationPage) getData(EDIT_ORGANIZATION_PAGE_KEY);
		editOrganizationPage.getSettingNavHeader().clickOnOtherFees();
		OtherFeesComponent otherFeesComp = editOrganizationPage.getOtherFeesComponent();
		otherFeesComp.fillForm(otherFees);
		otherFeesComp.clickOnUpdateButton();
		return editOrganizationPage.isNotificationDisplayedWithMessage(MsgConstants.ORGANIZATION_PER_ORDER_FEE_UPDATED);
		
	}
	
	public boolean thenUserIsOnOrganizationSettingsPage() {
		EditOrganizationPage organizationPage = (EditOrganizationPage) getData(EDIT_ORGANIZATION_PAGE_KEY);
		return organizationPage.isAtPage();
	}
		
	public boolean thenUserIsOnOrganizationsPage() {
		return organizationPage.isAtPage();
	}
	
	@Override
	protected void setData(String key, Object value) {
		this.data.put(key, value);
	}

	@Override
	protected Object getData(String key) {
		return this.data.get(key);
	}

}
