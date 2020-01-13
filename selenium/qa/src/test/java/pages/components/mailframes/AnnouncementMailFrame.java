package pages.components.mailframes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class AnnouncementMailFrame extends BaseComponent {

	@FindBy(xpath = "//h1[contains(text(), 'Important Announcement')]")
	private WebElement headerText;
	
	@FindBy(xpath = "//h1[contains(text(), 'Important Announcement')]/following-sibling::div/p[2]")
	private WebElement eventInfo;
	
	@FindBy(xpath = "//h1[contains(text(), 'Important Announcement')]/following-sibling::div/p[4]")
	private WebElement locationInfo;
	
	public AnnouncementMailFrame(WebDriver driver) {
		super(driver);
	}
	
	public String getEventInformation() {
		String text = getAccessUtils().getTextOfElement(eventInfo);
		return text;
	}
	
	public String getLocationInfo() {
		String text = getAccessUtils().getTextOfElement(locationInfo);
		return text;
	}
	
	public boolean isBodyContentValid(String bodyText) {
		return isExplicitlyWaitVisible(By.xpath("//div/p[contains(text(),'"+ bodyText +"')]"));
	}
	
}
