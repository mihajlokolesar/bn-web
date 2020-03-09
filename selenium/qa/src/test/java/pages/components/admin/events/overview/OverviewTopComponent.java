package pages.components.admin.events.overview;

import java.time.LocalDate;
import java.time.LocalTime;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;
import utils.ProjectUtils;

public class OverviewTopComponent extends BaseComponent {
	
	private WebElement container;
	
	private TimeInfoComponent timeComponent;
	
	private final String relativeVenueInfoXpath = "//div[img[contains(@alt,'Location Icon')]]/div[2]";
	

	public OverviewTopComponent(WebDriver driver, String eventName) {
		super(driver);
		this.container = findContainerElement(eventName);
	}
	
	private WebElement findContainerElement(String eventName) {
		By by = By.xpath("//div[div[div[p[contains(text(),'" + eventName + "')]] and div[contains(@style,'background-image')]]]");
		if(isExplicitlyWaitVisible(by)) {
			return explicitWaitForVisibilityBy(by);
		} else {
			throw new IllegalArgumentException("Overview Header Component element with name: " + eventName + " can not be found");
		}
	}
	
	public TimeInfoComponent getTimeInfo() {
		if (timeComponent == null) {
			WebElement timeCompEl = getAccessUtils()
					.getChildElementFromParentLocatedBy(container, 
							By.xpath(".//div[img[contains(@alt,'Calendar Icon')]]"));
			this.timeComponent = new TimeInfoComponent(driver, timeCompEl);
		}
		return timeComponent;
	}
	
//	public Venue getVenue() {
//		WebElement location = getAccessUtils()
//				.getChildElementFromParentLocatedBy(container, By.xpath(relativeVenueInfoXpath));
//		String text = location.getText();
//		if(text.split(",").length )
//		VenueFormatter venueFormatter = new VenueFormatter("N, A, C, Sa, CTa");
//	}
	
	public class TimeInfoComponent extends BaseComponent {

		private WebElement timeContainer;
		
		private final String relativeDateXpath = "./div/p[1]";
		private final String relativeDoorTimeXpath = "./div/p[2]";
		private final String relativeShowStartXpath = "./div/p[3]";
		
		public TimeInfoComponent(WebDriver driver, WebElement timeContainer) {
			super(driver);
			this.timeContainer = timeContainer;
		}
		
		public LocalDate getEventStartDate() {
			WebElement dateEl = getAccessUtils().getChildElementFromParentLocatedBy(timeContainer, By.xpath(relativeDateXpath));
			LocalDate startDate = ProjectUtils.parseDate(ProjectUtils.EVENT_OVERVIEW_DATE_FORMAT, dateEl.getText());
			return startDate;
		}
		
		public LocalTime getDoorTime() {
			WebElement timeEl =getAccessUtils().getChildElementFromParentLocatedBy(timeContainer, By.xpath(relativeDoorTimeXpath));
			String time =  timeEl.getText().split("at")[1].trim();
			return ProjectUtils.parseTime(ProjectUtils.EVENT_OVERVIEW_TIME_FORMAT, time);
		}
		
		public LocalTime getShowStartTime() {
			WebElement timeEl =getAccessUtils().getChildElementFromParentLocatedBy(timeContainer, By.xpath(relativeShowStartXpath));
			String time =  timeEl.getText().split("at")[1].trim();
			return ProjectUtils.parseTime(ProjectUtils.EVENT_OVERVIEW_TIME_FORMAT, time);
		}
	}
}
