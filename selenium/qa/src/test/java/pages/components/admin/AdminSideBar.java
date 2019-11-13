package pages.components.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import pages.admin.events.AdminEventsPage;

public class AdminSideBar extends BaseComponent{
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/events']/div")
	private WebElement adminEventsLink;

	@FindBy(xpath = "//body//nav//a[@href='/admin/fans']/div")
	private WebElement adminFansLink;
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/reports']/div")
	private WebElement adminReportsLink;
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/venues']/div")
	private WebElement venuesLink;
	
	@FindBy(xpath = "//body//nav//a[@href='/admin/organizations/']/div")
	private WebElement superAdminOrganizations;
	
	public AdminSideBar(WebDriver driver) {
		super(driver);
	}
	
	public void clickOnEvents() {
		waitForTime(500);
		explicitWaitForVisibilityAndClickableWithClick(adminEventsLink);
	}
	
	public void clickOnOrganizations() {
		waitForTime(500);
		explicitWaitForVisibilityAndClickableWithClick(superAdminOrganizations);
	}
	
	public void clickOnReports() {
		waitForTime(500);
		waitVisibilityAndBrowserCheckClick(adminReportsLink);
	}
	
	public void clickOnVenues() {
		waitForTime(500);
		explicitWaitForVisibilityAndClickableWithClick(venuesLink);
	}
	
	public void clickOnFansPage() {
		waitForTime(500);
		explicitWaitForVisibilityAndClickableWithClick(adminFansLink);
	}

}
