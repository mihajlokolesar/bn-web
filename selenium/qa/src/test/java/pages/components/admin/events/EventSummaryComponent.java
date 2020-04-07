package pages.components.admin.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import model.Event;
import pages.BaseComponent;
import pages.components.dialogs.CancelEventDialog;
import pages.components.dialogs.DeleteEventDialog;
import pages.components.dialogs.clone.CloneEventDialog;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class EventSummaryComponent extends BaseComponent {

	private static final long serialVersionUID = 3498843919257980311L;

	// NOTE: all relative paths are relative to event field

	private WebElement event;

	private String relativeDropDownXpath = ".//button[@type='button']";

	private String relativeIsCanceledParagraphXpath = ".//div[p[contains(text(),'Cancelled')]]";

	private String relativeIsDraftParagraphPath = ".//div/p[contains(text(),'Draft')]";

	private String relativeIsPublishedXPath = ".//p[contains(text(),'Published')]";

	private String relativeIsOnSaleXPath = ".//p[contains(text(),'On sale')]";

	private String relativeEventNameXpath = "./div/div[2]/div[1]/div[1]/a/p";

	private String relativeVenueParagraphPath = ".//a[contains(@href,'/dashboard')]/following-sibling::p[1]";

	private String relativeDateTimeParagraphPath = ".//a[contains(@href,'/dashboard')]/following-sibling::p[2]";

	private String relativeImageXPath = ".//a[contains(@href,'/admin/events/')]/div";

	private String relativeSoldToDivXPath = "./div/div[2]/div[2]/div[2]/div/div[p[text()='Sold']]/p[2]";

	private final String VIEW_EVENT_DD_ACTION = "View event";
	private final String DELETE_EVENT_DD_ACTION = "Delete event";
	private final String EDIT_EVENT_DD_ACTION = "Edit event";
	private final String EVENT_OVERVIEW_DD_ACTION = "Event overview";
	private final String CANCEL_EVENT_DD_ACTION = "Cancel event";
	private final String CLONE_EVENT_DD_ACTION = "Clone event";

	public EventSummaryComponent(WebDriver driver, WebElement event) {
		super(driver);
		this.event = event;
	}

	public boolean isEventCanceled() {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(event, By.xpath(relativeIsCanceledParagraphXpath),
				driver);
	}

	public boolean isEventDrafted() {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(event, By.xpath(relativeIsDraftParagraphPath),
				driver);
	}

	public boolean isEventPublished() {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(event, By.xpath(relativeIsPublishedXPath),
				driver);
	}

	public boolean isEventOnSale() {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(event, By.xpath(relativeIsOnSaleXPath), driver);
	}

	public boolean isSoldToAmountGreaterThan(int amount) {
		int intElAmount = getSoldToAmount();
		if (amount < intElAmount) {
			return true;
		} else {
			return false;
		}
	}

	private int getSoldToAmount() {
		WebElement amountElement = SeleniumUtils.getChildElementFromParentLocatedBy(event, By.xpath(relativeSoldToDivXPath), driver);
		String elementAmount = amountElement.getText();
		int intElAmount = Integer.parseInt(elementAmount);
		return intElAmount;
	}

	public boolean clickOnCancelEvent() {
		openDropDown();
		findActionAndClickInDropDown(dropDownXpathElement(CANCEL_EVENT_DD_ACTION));
		CancelEventDialog dialog = new CancelEventDialog(driver);
		dialog.clickOnCancelEventButton();
		return dialog.isInvisible(1000);
	}

	public DeleteEventDialog clickOnDeleteEvent(Event event) {
		openDropDown();
		findActionAndClickInDropDown(dropDownXpathElement(DELETE_EVENT_DD_ACTION));
		waitForTime(2000);
		DeleteEventDialog deleteDialog = new DeleteEventDialog(driver);
		deleteDialog.clickOnDeleteButton(event.getEventName());
		return deleteDialog;
	}

	public void clickOnEventOverview() {
		openDropDown();
		findActionAndClickInDropDown(dropDownXpathElement(EVENT_OVERVIEW_DD_ACTION));
	}

	public void clickOnEditEvent() {
		openDropDown();
		findActionAndClickInDropDown(dropDownXpathElement(EDIT_EVENT_DD_ACTION));
	}

	public void clickOnCloneEvent(Event clone) {
		openDropDown();
		findActionAndClickInDropDown(dropDownXpathElement(CLONE_EVENT_DD_ACTION));
		CloneEventDialog cloneEventDialog = new CloneEventDialog(driver);
		cloneEventDialog.isVisible();
		cloneEventDialog.fillFields(clone);
		cloneEventDialog.clickOnCreateButton();
	}

	public String getEventName() {
		return SeleniumUtils.getChildElementFromParentLocatedBy(event, By.xpath(relativeEventNameXpath), driver).getText();
	}



	public void viewEvent() {
		openDropDown();
		findActionAndClickInDropDown(dropDownXpathElement(VIEW_EVENT_DD_ACTION));
	}

	public void clickOnEvent() {
		WebElement image = SeleniumUtils.getChildElementFromParentLocatedBy(event, By.xpath(relativeImageXPath), driver);
		waitVisibilityAndBrowserCheckClick(image);
	}

	public boolean checkIfDatesMatch(String startDate) {
		LocalDateTime eventDateTime = getEventDateTime();
		LocalDateTime localStartDateTime = ProjectUtils
				.getDateTime(ProjectUtils.parseDate(ProjectUtils.DATE_FORMAT, startDate));
		LocalDate eventDate = eventDateTime.toLocalDate();
		LocalDate localStartDate = localStartDateTime.toLocalDate();
		return eventDate.isEqual(localStartDate);
	}

	private WebElement openDropDown() {
		WebElement dropDown = SeleniumUtils.getChildElementFromParentLocatedBy(event, By.xpath(relativeDropDownXpath),
				driver);
		waitVisibilityAndBrowserCheckClick(dropDown);
		return dropDown;
	}

	private By dropDownXpathElement(String actionName) {
		return By.xpath("//ul/li[div[span[contains(text(),'" + actionName + "')]]]");
	}

	private void findActionAndClickInDropDown(By by) {
		List<WebElement> elements = explicitWaitForVisiblityForAllElements(by);
		for (int i = elements.size() - 1; i >= 0; i--) {
			WebElement e = elements.get(i);
			if (isExplicitlyWaitVisible(e)) {
				explicitWaitForClickable(e);
				try {
					e.click();
					break;
				} catch (Exception exc) {
					System.out.println();
				}
			}
		}
	}

	private LocalDateTime getEventDateTime() {
		WebElement dateTime = SeleniumUtils.getChildElementFromParentLocatedBy(event,
				By.xpath(relativeDateTimeParagraphPath), driver);
		String date = dateTime.getText();
		LocalDateTime localDateTime = ProjectUtils.parseDateTime(ProjectUtils.ADMIN_EVENT_DATE_TIME_FORMAT, date);
		return localDateTime;
	}

}
