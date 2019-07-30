package pages.admin.organizations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

	@FindBy(id = "message-id")
	private WebElement message;

	private void selectOnTimeZone(String timeZone) {
		explicitWaitForVisiblity(timeZoneDropDown);
		timeZoneDropDown.click();
		explicitWaitForVisiblity(timeZoneList);
		WebElement selectedTimeZone = timeZoneList.findElement(By.xpath(".//li[@data-value='" + timeZone + "']"));
		explicitWait(10, ExpectedConditions.elementToBeClickable(selectedTimeZone));
		waitForTime(2, 900);
		selectedTimeZone.click();

	}

	public CreateOrganizationPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminOrganizationsCreate());
	}

	public void fillForm(String name, String phoneNumber, String timeZone, String address) {
		enterOrganizationName(name);
		enterPhoneNumber(phoneNumber);
		selectTimeZone(timeZone);
		enterOrganizationAddress(address);
	}

	public void fillFormAndConfirm(String name, String phoneNumber, String timeZone, String address) {
		fillForm(name, phoneNumber, timeZone, address);
		explicitWaitForVisiblity(createButton);
		createButton.click();
	}

	public boolean checkPopupMessage() {
		explicitWait(15, ExpectedConditions.and(ExpectedConditions.visibilityOf(message),
				ExpectedConditions.elementToBeClickable(message)));
		String msg = message.getText();
		if (msg.contains(MsgConstants.ORGANIZATION_CREATED_SUCCESS)) {
			return true;
		} else {
			return false;
		}
	}

	public void enterOrganizationName(String organizationName) {
		explicitWaitForVisiblity(nameField);
		nameField.sendKeys(organizationName);
	}

	public void enterPhoneNumber(String phoneNumber) {
		explicitWaitForVisiblity(phoneNumberField);
		phoneNumberField.sendKeys(phoneNumber);
	}

	public void selectTimeZone(String timeZone) {
		selectOnTimeZone(timeZone);
	}

	public void enterOrganizationAddress(String address) {
		explicitWait(15, ExpectedConditions.visibilityOf(addressAutoSearchField));
		waitForTime(2, 900);
		addressAutoSearchField.sendKeys(address);
		WebElement firstInList = explicitWait(15, ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//form//div[contains(@class,'autocomplete-dropdown-container')]/div[contains(@class,'suggestion-item')]")));
		explicitWaitForClickable(firstInList);
		firstInList.click();


	}

}
