package pages.components.admin;

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
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class AdminEventComponent extends BaseComponent {

	private static final long serialVersionUID = 3498843919257980311L;

	// NOTE: all relative paths are relative to event field

	private WebElement event;

	private String relativeDropDownXpath = ".//button[@type='button']";

	private String relativeIsCanceledParagraphXpath = ".//div[p[contains(text(),'Cancelled')]]";

	private String relativeIsDraftParagraphPath = ".//div/p[contains(text(),'Draft')]";
	
	private String relativeIsPublishedXPath = ".//p[contains(text(),'Published')]";

	private String relativeIsOnSaleXPath = ".//p[contains(text(),'On sale')]";

	private String relativeVenueParagraphPath = ".//a[contains(@href,'/dashboard')]/following-sibling::p[1]";

	private String relativeDateTimeParagraphPath = ".//a[contains(@href,'/dashboard')]/following-sibling::p[2]";
	
	private String relativeImageXPath = ".//a[contains(@href,'/admin/events/')]/div";
	
	private String relativeSoldToDivXPath = "./div/div[2]/div[2]/div[2]/div/div[p[text()='Sold']]/p[2]";

	private By dropDownCancelEvent = By
			.xpath("//body//div[@id='long-menu']//ul/li[div[span[contains(text(),'Cancel event')]]]");

	private By dropDownEditEvent = By
			.xpath("//body//div[@id='long-menu']//ul/li[div[span[contains(text(),'Edit event')]]]");

	private By dropDownDeleteEvent = By
			.xpath("//body//div[@id='long-menu']//ul/li[div[span[contains(text(),'Delete event')]]]");

	public AdminEventComponent(WebDriver driver, WebElement event) {
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

	public void cancelEvent() {
		openDropDown();
		findActionAndClickInDropDown(dropDownCancelEvent);
		waitForTime(2000);
		new CancelEventDialog(driver).clickOnCancelEventButton();
	}

	public DeleteEventDialog deleteEvent(Event event) {
		openDropDown();
		findActionAndClickInDropDown(dropDownDeleteEvent);
		waitForTime(2000);
		DeleteEventDialog deleteDialog = new DeleteEventDialog(driver);
		deleteDialog.clickOnDeleteButton(event.getEventName());
		return deleteDialog;
		
	}

	public void editEvent(Event event) {
		openDropDown();
		findActionAndClickInDropDown(dropDownEditEvent);
	}
	
	public void clickOnEvent() {
		WebElement image = SeleniumUtils.getChildElementFromParentLocatedBy(event, By.xpath(relativeImageXPath), driver);
		explicitWaitForVisibilityAndClickableWithClick(image);
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
		explicitWaitForVisibilityAndClickableWithClick(dropDown);
		return dropDown;
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
