package pages.components.datepicker;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.ProjectUtils;

public class DatePickerComponent extends BaseComponent {

	@FindBy(xpath = "//div[@id='picker-popover']//button[span/*[local-name()='svg']"
			+ "//*[local-name()='path' and @d='M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z']]")
	private WebElement monthArrowLeft;

	@FindBy(xpath = "//div[@id='picker-popover']//button[span/*[local-name()='svg']"
			+ "//*[local-name()='path' and @d='M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z']]"
			+ "/following-sibling::div[1]/p")
	private WebElement currentMonthAndYearLabel;

	@FindBy(xpath = "//div[@id='picker-popover']//button[span/*[local-name()='svg']"
			+ "//*[local-name()='path' and @d='M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z']]")
	private WebElement monthArrowRight;

	@FindBy(id = "picker-popover")
	private WebElement datePickerContainer;

	private WebElement inputField;
	// relative to input field
	private String relativePopupActivateButtonXpath = "./following-sibling::div/button[span/*[local-name()='svg']]";

	public DatePickerComponent(WebDriver driver, WebElement inputField) {
		super(driver);
		this.inputField = inputField;
	}

	public void selectDate(String target) {
		LocalDate date = ProjectUtils.parseDate(ProjectUtils.DATE_FORMAT, target);
		selectDate(date);
	}

	public void selectDate(LocalDate target) {
		openDatePicker();
		selectMonth(target);
		selectDay(target);
		closeDatePicker();
	}

	private void openDatePicker() {
		WebElement popupOpen = getAccessUtils().getChildElementFromParentLocatedBy(inputField,
				By.xpath(relativePopupActivateButtonXpath));
		waitVisibilityAndBrowserCheckClick(popupOpen);
		waitForTime(1500);
	}

	private void selectMonth(LocalDate target) {
		int diff = diffrenceBetweenTargetAndCurrentDateMonths(target);
		if (diff != 0) {
			if (diff > 0) {
				clickOnMonthArrow(monthArrowRight, diff);
			} else {
				clickOnMonthArrow(monthArrowLeft, Math.abs(diff));
			}
		}
	}

	public void selectDay(LocalDate target) {
		WebElement dayInMonthEl = getDayElement(target.getDayOfMonth());
		waitVisibilityAndBrowserCheckClick(dayInMonthEl);
		waitForTime(500);
	}

	private void closeDatePicker() {
		if (isExplicitlyWaitVisible(2, datePickerContainer))
			waitVisibilityAndBrowserCheckClick(datePickerContainer);
	}

	private int diffrenceBetweenTargetAndCurrentDateMonths(LocalDate target) {
		LocalDate currentMonthAndYear = getCurrentMonthYear();
		int diff = target.getMonthValue() - currentMonthAndYear.getMonthValue();
		return diff;
	}

	private void clickOnMonthArrow(WebElement arrow, int times) {
		for (int i = 0; i < times; i++) {
			waitVisibilityAndBrowserCheckClick(arrow);
			waitForTime(1000);
		}
	}

	private LocalDate getCurrentMonthYear() {
		explicitWaitForVisiblity(currentMonthAndYearLabel);
		String text = currentMonthAndYearLabel.getText();
		return parseDate(text);
	}

	private LocalDate parseDate(String text) {
		List<ChronoField> ignoredChronoUnit = new ArrayList<ChronoField>();
		ignoredChronoUnit.add(ChronoField.DAY_OF_MONTH);

		LocalDate date = ProjectUtils.parseDate(ProjectUtils.DATE_PICKER_MONTH_YEAR_FORMAT, text, ignoredChronoUnit);
		return date;
	}

	private WebElement getDayElement(int dayInMonth) {
		By by = By.xpath("//div[@id='picker-popover']//button[span[text()='" + dayInMonth + "'] and @tabindex='0']");
		return explicitWaitForVisibilityBy(by);
	}
}