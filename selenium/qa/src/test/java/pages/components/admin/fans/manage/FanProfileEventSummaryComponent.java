package pages.components.admin.fans.manage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;
import pages.components.admin.orders.manage.ActivityItem;
import utils.ProjectUtils;

public class FanProfileEventSummaryComponent extends BaseComponent{

	private WebElement container;
	
	private String relativeEventNameXpath = "./div/div/div/div[1]/p[1]/span";
	
	private String relativeEventVenueXpath = "./div/div/div/div[1]/p[2]";
	
	private String relativeEventStartDateAndTimeXpath = "./div/div/div/div[1]/p[3]";
	
	private String relativeShowDetailsButton = ".//div/p[contains(text(),'Show all details')]";
	
	private String relativeActivityItemsListXpath = "./div/div[2]/div/div/div/div/div";
	
	public FanProfileEventSummaryComponent(WebDriver driver, WebElement container) {
		super(driver);
		this.container = container;
	}
	
	public String getEventName() {
		return getEventNameElement().getText();
	}
	
	public String getVenueInfo() {
		return getVenueInfoElement().getText();
	}
	
	public LocalDateTime getEventDateAndTime() {
		String text = getEventDateAndTimeInfoElement().getText();
		LocalDateTime ldt = ProjectUtils.parseDateTime(ProjectUtils.MANAGE_ORDER_HISTORY_ITEM_DATE_FORMAT, text);
		return ldt;
	}
	
	public ActivityItem getActivityItem(Predicate<ActivityItem> predicate) {
		List<WebElement> elementList = getActivityItemsElements();
		Optional<ActivityItem> optionalActivity = elementList.stream()
				.map(el -> new ActivityItem(driver, el))
				.filter(predicate).findFirst();
		return optionalActivity.isPresent() ? optionalActivity.get() : null;
	}
	
	public void clickOnShowDetailsButton() {
		WebElement element = getShowDetailsButtonElement();
		waitVisibilityAndBrowserCheckClick(element);
		waitForTime(2500);
	}
	
	public WebElement getShowDetailsButtonElement() {
		return getAccessUtils().getChildElementFromParentLocatedBy(container, By.xpath(relativeShowDetailsButton));
	}
	
	private WebElement getEventNameElement() {
		return getAccessUtils().getChildElementFromParentLocatedBy(container, 
				By.xpath(relativeEventNameXpath));
	}
	
	private WebElement getVenueInfoElement() {
		return getAccessUtils().getChildElementFromParentLocatedBy(container, 
				By.xpath(relativeEventVenueXpath));
	}
	
	private WebElement getEventDateAndTimeInfoElement() {
		return getAccessUtils().getChildElementFromParentLocatedBy(container, 
				By.xpath(relativeEventStartDateAndTimeXpath));
	}
	
	private List<WebElement> getActivityItemsElements() {
		return getAccessUtils().getChildElementsFromParentLocatedBy(container, By.xpath(relativeActivityItemsListXpath));
	}
}
