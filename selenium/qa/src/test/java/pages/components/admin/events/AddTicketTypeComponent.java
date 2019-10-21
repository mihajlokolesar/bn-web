package pages.components.admin.events;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.AdditionalOptionsTicketType;
import model.TicketType;
import model.AdditionalOptionsTicketType.SaleEnd;
import model.AdditionalOptionsTicketType.SaleStart;
import pages.BaseComponent;
import pages.components.GenericDropDown;
import pages.components.TimeMenuDropDown;
import utils.SeleniumUtils;

public class AddTicketTypeComponent extends BaseComponent {

	@FindBy(xpath = "//main//button//img[@alt='Edit']")
	private WebElement editSaveButton;

	@FindBy(xpath = "//main//button//img[@alt='Delete']")
	private WebElement cancelDeleteButton;

	@FindBy(id = "name")
	private WebElement ticketNameField;

	// type=number
	@FindBy(id = "capacity")
	private WebElement capacityField;

	// type=number
	@FindBy(id = "value")
	private WebElement priceValueField;

	@FindBy(xpath = "//main//button[@type='button' and span[contains(text(),'Additional options')]]")
	private WebElement additionalOptionsButton;

	@FindBy(xpath = "//main//input[@id='sales-start-times']/preceding-sibling::div[@role='button']")
	private WebElement startSalesOptions;

	@FindBy(id = "menu-sales-start-times")
	private WebElement startSalesOptionsMenuDropDownContainer;

	@FindBy(xpath = "//main//input[@id='parentId']/preceding-sibling::div[@role='button']")
	private WebElement selectTicketTypesForSalesOptions;

	@FindBy(xpath = "//main//input[@id='close-times']/preceding-sibling::div[@role='button']")
	private WebElement endSalesOptions;

	@FindBy(id = "menu-close-times")
	private WebElement endSalesOptionsMenuDropDownContainer;

	@FindBy(id = "menu-parentId")
	private WebElement menuSelectTicketTypesDropDownContainer;

	@FindBy(id = "startDate.date")
	private WebElement startDate;

	@FindBy(id = "startTime")
	private WebElement startTime;

	@FindBy(id = "endDate")
	private WebElement endDate;

	@FindBy(xpath = "//div[div[div[span[span[contains(text(),'Sales end')]]]]]//input[@id='endTime']")
	private WebElement endTime;

	public AddTicketTypeComponent(WebDriver driver) {
		super(driver);
	}

	private WebElement editSaveButtonForTicketType(String ticketTypeName) {
		WebElement editButton = explicitWait(15,
				ExpectedConditions.presenceOfElementLocated(By.xpath("//main//div[h3[contains(text(),'" + ticketTypeName
						+ "')]]/div/button[span[img[@alt='Edit' and contains(@src,'edit-active.svg')]]]")));
		return editButton;
	}

	public void addNewTicketType(TicketType ticketType) {
		waitVisibilityAndSendKeys(ticketNameField, ticketType.getTicketTypeName());
		waitVisibilityAndSendKeys(capacityField, ticketType.getCapacity());
		waitVisibilityAndSendKeys(priceValueField, ticketType.getPrice());

		if (ticketType.getAdditionalOptions() != null) {
			addAdditionalOptions(ticketType.getAdditionalOptions());
		}
		waitVisibilityAndClick(editSaveButtonForTicketType(ticketType.getTicketTypeName()));
		waitForTime(1000);
	}

	private void addAdditionalOptions(AdditionalOptionsTicketType options) {
		clickOnAdditionalOptionsButton();
		explicitWaitForVisiblity(startSalesOptions);
		if (options.getSaleStart() != null) {
			fillOutSaleStartOptions(options);
		}
		if (options.getSaleEnd() != null) {
			fillOutSaleEndOptions(options);
		}
		waitForTime(1000);

	}

	private void fillOutSaleStartOptions(AdditionalOptionsTicketType options) {

		GenericDropDown dropDown = new GenericDropDown(driver, startSalesOptions,
				startSalesOptionsMenuDropDownContainer);
		dropDown.selectElementFromDropDownHiddenInput(
				getByXpathForElementInDropDownList(options.getSaleStart().getLabel()),
				options.getSaleStart().getLabel());

		if (options.getSaleStart().equals(AdditionalOptionsTicketType.SaleStart.AT_SPECIFIC_TIME)) {
			fillOutAtSpecificTimeDetails(startDate, startTime, options.getStartSaleDate(), options.getStartSaleTime());

		} else if (options.getSaleStart().equals(SaleStart.WHEN_SALES_END_FOR)) {
			GenericDropDown drop = new GenericDropDown(driver, selectTicketTypesForSalesOptions,
					menuSelectTicketTypesDropDownContainer);

			drop.selectElementFromDropDownHiddenInput(
					getByXpathForElementInDropDownList(options.getStartSaleTicketType()),
					options.getStartSaleTicketType());
		}
	}

	private void fillOutSaleEndOptions(AdditionalOptionsTicketType options) {
		GenericDropDown dropDown = new GenericDropDown(driver, endSalesOptions, endSalesOptionsMenuDropDownContainer);
		dropDown.selectElementFromDropDownHiddenInput(
				getByXpathForElementInDropDownList(options.getSaleEnd().getLabel()), options.getSaleEnd().getLabel());
		if (options.getSaleEnd().equals(SaleEnd.AT_SPECIFIC_TIME)) {
			fillOutAtSpecificTimeDetails(endDate, endTime, options.getEndSaleDate(), options.getEndSaleTime());
		}
	}

	private void fillOutAtSpecificTimeDetails(WebElement date, WebElement time, String dateValue, String timeValue) {
		enterDate(date, dateValue);
		enterTime(time, timeValue);
	}

	private void enterDate(WebElement element, String value) {
		waitForTime(500);
		SeleniumUtils.clearInputField(element, driver);
		waitForTime(500);
		waitVisibilityAndSendKeys(element, value);
	}

	private void enterTime(WebElement element, String time) {
		TimeMenuDropDown timeDropDown = new TimeMenuDropDown(driver);
		timeDropDown.selectTime(element, time);
	}

	private void clickOnAdditionalOptionsButton() {
		explicitWaitForVisibilityAndClickableWithClick(additionalOptionsButton);
	}

	private By getByXpathForElementInDropDownList(String name) {
		return By.xpath(".//li[contains(text(),'" + name + "')]");
	}

}
