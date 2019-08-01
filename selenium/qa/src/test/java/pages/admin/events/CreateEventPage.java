package pages.admin.events;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.AddTicketTypeComponent;
import utils.Constants;

public class CreateEventPage extends BasePage {

	@FindBy(xpath = "//body//div[@role='dialog']//div//button[span[contains(text(),'No thanks')]]")
	private WebElement dissmisImportSettingDialog;

	@FindBy(xpath = "//body//div//h2[contains(text(),'Upload event image')]")
	private WebElement uploadEventImage;

	@FindBy(xpath = "//body[@id='cloudinary-overlay']//div[@id='cloudinary-navbar']//ul//li[@data-source='url']/span[contains(text(),'Web Address')]")
	private WebElement webAddressLink;

	@FindBy(xpath = "//iframe[contains(@src,'widget.cloudinary.com') and contains(@style,'display: block')]")
	private WebElement imageUploadIframe;

	@FindBy(id = "remote_url")
	private WebElement remoteImageUrlField;

	@FindBy(xpath = "//div[@class='form']//a[contains(text(),'Upload')]")
	private WebElement uploadButton;

	@FindBy(xpath = "//div[@class='upload_cropped_holder']//a[@title='Upload']")
	private WebElement uploadCroppedButton;

	@FindBy(css = "div.css-10nd86i")
	private WebElement artistInputDropDown;

	@FindBy(id = "eventName")
	private WebElement eventNameField;

	@FindBy(xpath = "//main//div[@aria-describedby='%venues-error-text']//div[div[@role='button'] and div[@aria-haspopup='true']]")
	private WebElement venueDropDownSelect;

	@FindBy(id = "eventDate")
	private WebElement startDateField;

	// mm//dd//yyyy
	@FindBy(xpath = "//main//input[@id='endTime' and @placeholder='mm/dd/yyyy']")
	private WebElement endDateField;

	@FindBy(id = "show-time")
	private WebElement showTimeField;

	@FindBy(xpath = "//main//input[@id='endTime' and @name='endTime']")
	private WebElement endTimeField;

	@FindBy(id = "time-menu")
	private WebElement timeMenu;

	@FindBy(xpath = "//main//div//div[div[input[@id='doorTimeHours' and @type='hidden']]]")
	private WebElement doorTimeContainer;

	@FindBy(id = "menu-doorTimeHours")
	private WebElement doorTimeMenuHours;

	@FindBy(xpath = "//main//div[aside[contains(text(),'Add another ticket type')]]")
	private WebElement addTicketTypeButton;

	@FindBy(xpath = "//main//div//button[span[contains(text(),'Publish')]]")
	private WebElement publishButton;

	public CreateEventPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminEventCreate());

	}

	public void clickOnImportSettingDialogNoThanks() {
		waitVisibilityAndClick(dissmisImportSettingDialog);
	}

	public void uploadImage(String imageLink) {
		clickOnUploadImage();
		explicitWait(15, ExpectedConditions.frameToBeAvailableAndSwitchToIt(imageUploadIframe));

		waitVisibilityAndClick(webAddressLink);
		waitVisibilityAndSendKeys(remoteImageUrlField, imageLink);
		waitForTime(1100);
		waitVisibilityAndClick(uploadButton);
		waitForTime(1100);
		explicitWaitForVisiblity(uploadCroppedButton);
		explicitWaitForClickable(uploadCroppedButton);
		uploadCroppedButton.click();
		driver.switchTo().parentFrame();
		while (!imageUploadIframe.isDisplayed()) {
			waitForTime(500);
		}
	}

	/**
	 * 
	 * @param startDate format "mm/dd/yyyy"
	 * @param endDate   format "mm/dd/yyyy"
	 * @param showTime  format "0800AM", "0930PM" ...
	 * @param endTime   format "0800AM", "0930PM" ...
	 * @param doorTime  format "0.5";"1";"2";..;"10"
	 */
	public void enterDatesAndTimes(String startDate, String endDate, String showTime, String endTime, String doorTime) {
		enterDate(startDateField, startDate);
		enterDate(endDateField, endDate);
		selectTime(showTimeField, showTime);
		waitForTime(1000);
		selectTime(endTimeField, endTime);
		waitForTime(1000);
		selectDoorTime(doorTime);

	}

	public void enterEventName(String eventName) {
		waitVisibilityAndClick(eventNameField);
		eventNameField.clear();
		waitVisibilityAndSendKeys(eventNameField, eventName);
	}
	
	public void selectVenue(String venueName) {
		waitVisibilityAndClick(venueDropDownSelect);
		WebElement selectedVenue = selectElementFormVenueDropDown(venueName);
//		System.out.println(selectedVenue.getAttribute("class"));
//		System.out.println(selectedVenue.getText());
//		explicitWaitForVisiblity(selectedVenue);
//		explicitWaitForClickable(selectedVenue);
//		actionsMoveToElement(selectedVenue).click().perform();
		
	}

	private void enterDate(WebElement element, String date) {
		explicitWaitForVisiblity(element);
		element.clear();
		waitVisibilityAndSendKeys(element, date);
	}

	public void enterArtistName(String artistName) {

		waitVisibilityAndClick(artistInputDropDown);
		actionsManualType(artistName).sendKeys(Keys.ENTER).perform();
	}

	public void addNewTicketType(String name, String capacity, String price) {
		waitVisibilityAndClick(addTicketTypeButton);
		AddTicketTypeComponent ticketType = new AddTicketTypeComponent(driver);
		ticketType.addNewTicketType(name, capacity, price);
	}

	private void clickOnUploadImage() {
		waitVisibilityAndClick(uploadEventImage);
	}

	/**
	 * Valid doorTime values are 0.1;1;2;3;4;5;6;7;8;9;10
	 * 
	 * @param doorTime
	 * @return
	 */
	private WebElement selectDoorTime(String doorTime) {
		waitVisibilityAndClick(doorTimeContainer);
		WebElement selectedDoorTime = doorTimeMenuHours
				.findElement(By.xpath(".//ul//li[@data-value='" + doorTime + "']"));
		explicitWaitForVisiblity(selectedDoorTime);
		selectedDoorTime.click();
		return selectedDoorTime;
	}

	private WebElement selectElementFormVenueDropDown(String venue) {
		WebElement selectedElement = driver
				.findElement(By.xpath("//div[@id='menu-venues']//ul//li[contains(text(),'" + venue + "')]"));
		explicitWaitForVisiblity(selectedElement);
		selectedElement.click();
		return selectedElement;
	}

	/**
	 * WebElement element param is one that need to be clicked for timeMenu to
	 * appear. String time is time to be selected in drop down. format of time arg
	 * is 1230AM,1300AM, 1330AM with 30 minutes increments
	 * 
	 * @param element
	 * @param time
	 */
	private void selectTime(WebElement element, String time) {
		waitVisibilityAndClick(element);
		explicitWaitForVisiblity(timeMenu);
		WebElement selectedTime = timeMenu.findElement(By.id(time));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(selectedTime);
//		actions.click().perform();
		explicitWaitForVisiblity(selectedTime);
		explicitWaitForClickable(selectedTime);
		selectedTime.click();
//		actionsMoveToElement(selectedTime).click().perform();
		explicitWait(5, ExpectedConditions.invisibilityOf(selectedTime));
//		waitVisibilityAndClick(selectedTime);

	}

}
