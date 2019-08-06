package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pages.BaseComponent;
import pages.admin.events.AdminEventsPage;
import pages.admin.organizations.AdminOrganizationsPage;

public class AdminSideBar extends BaseComponent{
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/events']/div")
	private WebElement adminEventsLink;
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/organizations/']/div")
	private WebElement superAdminOrganizations;

	public AdminSideBar(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public AdminOrganizationsPage clickOnOrganizations() {
		explicitWaitForVisiblity(superAdminOrganizations);
		superAdminOrganizations.click();
		return new AdminOrganizationsPage(driver);
		
	}
	
	public AdminEventsPage clickOnEvents() {
		explicitWaitForVisiblity(adminEventsLink);
		adminEventsLink.click();
		return new AdminEventsPage(driver);
	}

}
