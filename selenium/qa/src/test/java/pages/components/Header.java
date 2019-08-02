package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class Header extends BaseComponent {

	@FindBy(css = "header form input")
	private WebElement searchEvents;

	@FindBy(css = "header form img[alt='Search icon']")
	private WebElement searchImage;

	@FindBy(xpath = "//body//header//span[@aria-owns='menu-appbar']/img[contains(@src,'down-active.svg')]")
	private WebElement profileOptionsLowResolution;

	@FindBy(xpath = "//header//div/span[contains(text(),'Welcome back')]")
	private WebElement profileOptions;

	@FindBy(xpath = "//body//header//a[@href='/box-office/sell']/div")
	private WebElement boxOffice;

	@FindBy(xpath = "//body//header//a[@href='/admin/events']/div")
	private WebElement toStudioButton;

	@FindBy(xpath = "//body//header//div[span[@aria-owns='menu-appbar']//span[contains(text(),'Current organization')]]")
	private WebElement currentOrganizationDropDown;

	public Header(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void searchEvents(String event) {
		if (event != null) {
			searchEvents.sendKeys(event);
			searchEvents.submit();
		}
	}

	public void clickOnBoxOfficeLink() {
		explicitWaitForVisiblity(boxOffice);
		explicitWaitForClickable(boxOffice);
		waitForTime(500);
		boxOffice.click();
	}

	public void clickOnToStudioLink() {
		explicitWaitForVisiblity(toStudioButton);
		toStudioButton.click();
	}

	public WebElement openProfileOptions() {
		WebElement profileDropDownMenu = null;
		explicitWait(10, ExpectedConditions.elementToBeClickable(profileOptions));
		profileOptions.click();
		return profileDropDownMenu;

	}

	public void logOut() {
		openProfileOptions();
		ProfileMenuDropDown profileMenu = new ProfileMenuDropDown(driver);
		profileMenu.logout();
	}

	public boolean checkLogedInFirstNameInHeader(String firstName) {
		String xpath = "/html/body/div[1]/div/header//h3[contains(text(),'" + firstName + "')]";
		WebElement name = explicitWait(5, 200, ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		return true;
	}

	public void selectOrganizationFromDropDown(String organizationName) {
		waitForTime(1000);
		waitVisibilityAndClick(currentOrganizationDropDown);
		CurrentOrganizationDropDown dropDown = new CurrentOrganizationDropDown(driver);
		dropDown.selectOrganizationByName(organizationName);

	}

	public boolean isOrganizationPresent(String organizationName) throws Exception {
		waitVisibilityAndClick(currentOrganizationDropDown);
		CurrentOrganizationDropDown dropDown = new CurrentOrganizationDropDown(driver);
		boolean retVal = false;
		try {
			dropDown.findOrganizationByName(organizationName);
			retVal = true;
		} catch (Exception e) {
			if (e instanceof NoSuchElementException) {
				retVal = false;
			} else {
				throw new Exception(e);
			}
		}

		WebElement element = explicitWait(15, ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='menu-appbar']")));
		element.click();

		return retVal;
	}

}
