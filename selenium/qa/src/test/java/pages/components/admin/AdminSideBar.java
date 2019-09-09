package pages.components.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import pages.admin.events.AdminEventsPage;

public class AdminSideBar extends BaseComponent{
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/events']/div")
	private WebElement adminEventsLink;
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/organizations/']/div")
	private WebElement superAdminOrganizations;

	public AdminSideBar(WebDriver driver) {
		super(driver);
	}
	
	public void clickOnOrganizations() {
		explicitWaitForVisibilityAndClickableWithClick(superAdminOrganizations);
	}
	
	public AdminEventsPage clickOnEvents() {
		waitForTime(500);
		explicitWaitForVisibilityAndClickableWithClick(adminEventsLink);
		return new AdminEventsPage(driver);
	}

}
