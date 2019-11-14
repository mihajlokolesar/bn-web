package pages.admin.organizations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import model.Organization;
import pages.BasePage;
import pages.components.AutoCompleteInputField;
import pages.components.GenericDropDown;
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
		waitVisibilityAndBrowserCheckClick(createButton);
	}

	private void fillForm(Organization org) {
		enterOrganizationName(org.getName());
		enterPhoneNumber(org.getPhoneNumber());
		selectTimeZone(org.getTimeZone());
		enterOrganizationAddress(org.getLocation());
	}

	private void enterOrganizationAddress(String address) {
		AutoCompleteInputField autocomplete = new AutoCompleteInputField(driver, addressAutoSearchField);
		autocomplete.selectFromAutocomplete(address);
	}

	private void enterPhoneNumber(String phoneNumber) {
		waitVisibilityAndClearFieldSendKeysF(phoneNumberField, phoneNumber);
	}

	private void selectTimeZone(String timeZone) {
		selectOnTimeZone(timeZone);
	}

	private void enterOrganizationName(String organizationName) {
		waitVisibilityAndClearFieldSendKeysF(nameField, organizationName);
	}

	private void selectOnTimeZone(String timeZone) {
		GenericDropDown dropDown = new GenericDropDown(driver, timeZoneDropDown, timeZoneList);
		dropDown.selectElementFromDropDownHiddenInput(By.xpath(".//li[@data-value='" + timeZone + "']"), timeZone);

	}

	public boolean checkPopupMessage() {
		explicitWaitForVisiblity(message);
		return isNotificationDisplayedWithMessage(MsgConstants.ORGANIZATION_CREATED_SUCCESS);
	}

}
