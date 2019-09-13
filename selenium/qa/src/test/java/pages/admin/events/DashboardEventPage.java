package pages.admin.events;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.GenericDropDown;
import utils.Constants;

public class DashboardEventPage extends BasePage {

	@FindBy(xpath = "//body//main//a/span[contains(text(),'Tools')]")
	private WebElement toolsLink;

	@FindBy(id = "menu-appbar")
	private WebElement toolsDropDownContainer;
	
	@FindBy(xpath = "//body//main//a/span[contains(text(),'Orders')]")
	private WebElement ordersLink;
	
	@FindBy(id = "menu-appbar")
	private WebElement ordersDropDownContainer;

	public DashboardEventPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {

	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlMatches(Constants.getAdminEvents() + "/*.*/dashboard"));
	}
	
	public void selectManageOrdersFromTools() {
		GenericDropDown dropDown = new GenericDropDown(driver, toolsLink, toolsDropDownContainer);
		dropDown.selectElementFromDropDownNoValueCheck(getByXpathForToolsDropDown("Manage orders"));
	}
	
	public void selectManageOrdersFromOrdersTab() {
		GenericDropDown dropDown = new GenericDropDown(driver, ordersLink, ordersDropDownContainer);
		dropDown.selectElementFromDropDownNoValueCheck(getByXpathForToolsDropDown("Manage orders"));
	}

	private By getByXpathForToolsDropDown(String value) {
		return By.xpath(".//ul//a/li[contains(text(),'" + value + "')]");
	}
}
