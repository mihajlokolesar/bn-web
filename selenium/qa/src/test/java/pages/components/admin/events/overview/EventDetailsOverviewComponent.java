package pages.components.admin.events.overview;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.ProjectUtils;

public class EventDetailsOverviewComponent extends BaseComponent {
	
	@FindBy(xpath = "//p[contains(text(),'Event Details')]//following-sibling::div[1]/div")
	private WebElement container;
	
	public enum FirstLineEnum implements IEventDetailsEnum {
		EVENT_NAME("/p[1]"),
		VENUE_NAME("/p[2["),
		TOP_LINE_INFO("/p[3]");
		
		private String value;
		
		
		private FirstLineEnum(String value) {
			this.value = value;
		}
		
		public String getRelativePath() {
			return relativeFirstLineXpath + this.value;  
		}
	}
	private static String relativeFirstLineXpath = "./div[p[contains(text(),'Event name')]]/following-sibling::div[1]";
	
	public enum SecondLineEnum implements IEventDetailsEnum {
		START_DATE("/p[1]"),
		START_TIME("/p[2]"),
		DOOR_TIME("/p[3]"),
		END_DATE("/p[4]"),
		END_TIME("/p[5]");
		
		private String value;
		private SecondLineEnum(String value) {
			this.value = value;
		}
		
		public String getRelativePath() {
			return relativeSecondLineXpath + this.value;
		}
		
	}

	private static final String relativeSecondLineXpath = "./div[p[contains(text(),'Event date')]]/following-sibling::div[1]";
	
	public enum ThirdLineEnum implements IEventDetailsEnum {
		AGE_LIMIT("/p[1]"),
		EVENT_TYPE("/p[2]"),
		ACCESS_CODE("/p[3]"),
		STATUS("/p[4]");
		
		private String value;
		private ThirdLineEnum(String value) {
			this.value = value;
		}
		@Override
		public String getRelativePath() {
			return relativeThirdLineXpath + this.value;
		}
		
		
	}
	private static final String relativeThirdLineXpath = "./div[p[contains(text(),'Age limit')]]/following-sibling::div[1]";
	
	public EventDetailsOverviewComponent(WebDriver driver) {
		super(driver);
	}
	
	public String getStringValue(IEventDetailsEnum detailsEnum) {
		WebElement el = getElement(detailsEnum);
		if (el != null) {
			return el.getText().trim();
		}
		return null;
	}
	
	public LocalDate getDateValue(IEventDetailsEnum detailsEnum) {
		String dateText = getStringValue(detailsEnum);
		if (dateText != null) {
			return ProjectUtils.parseDate(ProjectUtils.DATE_FORMAT, dateText);
		}
		return null;
	}
	
	private WebElement getElement(IEventDetailsEnum detailsEnum) {
		if(getAccessUtils().isChildElementVisibleFromParentLocatedBy(container, 
				By.xpath(detailsEnum.getRelativePath()))) {
			return getAccessUtils().getChildElementFromParentLocatedBy(container, By.xpath(detailsEnum.getRelativePath()));
		}
		return null;
	}
}
