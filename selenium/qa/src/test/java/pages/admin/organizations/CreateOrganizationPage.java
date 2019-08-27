package pages.admin.organizations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.Organization;
import pages.BasePage;
import utils.Constants;
import utils.MsgConstants;

public class CreateOrganizationPage extends BasePage {

	@FindBy(id = "name")
	private WebElement nameField;

	@FindBy(id = "phone")
	private WebElement phoneNumberField;

	@FindBy(xpath = "//body//main//div[./p[@id='timezone-error-text']]/div")
	private WebElement timeZoneDropDown;

	@FindBy(id = "menu-timezone")
	private WebElement timeZoneList;

	@FindBy(xpath = "//body//main//form//div[contains(@class, 'location-search-input')]//input")
	private WebElement addressAutoSearchField;

	@FindBy(xpath = "//body//main//form//button[@type='submit']")
	private WebElement createButton;

	public CreateOrganizationPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminOrganizationsCreate());
	}

	public void fillFormAndConfirm(Organization org) {
		fillForm(org);
		explicitWaitForVisibilityAndClickableWithClick(createButton);
	}

	private void fillForm(Organization org) {
		enterOrganizationName(org.getName());
		enterPhoneNumber(org.getPhoneNumber());
		selectTimeZone(org.getTimeZone());
		enterOrganizationAddress(org.getLocation());
	}

	private void enterOrganizationAddress(String address) {
		explicitWait(15, ExpectedConditions.visibilityOf(addressAutoSearchField));
		waitForTime(2000);
		addressAutoSearchField.sendKeys(address);
		WebElement firstInList = explicitWait(15, ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//form//div[contains(@class,'autocomplete-dropdown-container')]/div[contains(@class,'suggestion-item')]")));
		explicitWaitForVisibilityAndClickableWithClick(firstInList);
	}

	private void enterPhoneNumber(String phoneNumber) {
		explicitWaitForVisiblity(phoneNumberField);
		phoneNumberField.sendKeys(phoneNumber);
	}

	private void selectTimeZone(String timeZone) {
		selectOnTimeZone(timeZone);
	}

	private void enterOrganizationName(String organizationName) {
		waitVisibilityAndSendKeys(nameField, organizationName);
	}

	private void selectOnTimeZone(String timeZone) {
		explicitWaitForVisiblity(timeZoneDropDown);
		timeZoneDropDown.click();
		explicitWaitForVisiblity(timeZoneList);
		WebElement selectedTimeZone = timeZoneList.findElement(By.xpath(".//li[@data-value='" + timeZone + "']"));
		explicitWait(10, ExpectedConditions.elementToBeClickable(selectedTimeZone));
		waitForTime(2000);
		selectedTimeZone.click();

	}

	public boolean checkPopupMessage() {
		explicitWaitForVisiblity(message);
		return isNotificationDisplayedWithMessage(MsgConstants.ORGANIZATION_CREATED_SUCCESS);
	}

}
