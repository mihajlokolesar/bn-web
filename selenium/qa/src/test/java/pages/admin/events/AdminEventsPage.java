package pages.admin.events;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;
import utils.Constants;

public class AdminEventsPage extends BasePage {
	
	
	@FindBy(linkText = "Upcoming")
	private WebElement upcomingEvents;
	
	@FindBy(linkText = "Past")
	private WebElement pastEvents;
	
	@FindBy(xpath = "//body//main//a[@href='/admin/events/create']/button")
	private WebElement createEventButton;
	
	public AdminEventsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminEvents());
	}
	
	
	public void clickCreateEvent() {
		waitForTime(1000);
		waitVisibilityAndClick(createEventButton);
	}

}
