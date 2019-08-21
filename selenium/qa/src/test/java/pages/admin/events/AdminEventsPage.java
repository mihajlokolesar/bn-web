package pages.admin.events;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.admin.AdminEventComponent;
import utils.Constants;

public class AdminEventsPage extends BasePage {
	
	
	@FindBy(linkText = "Upcoming")
	private WebElement upcomingEvents;
	
	@FindBy(linkText = "Past")
	private WebElement pastEvents;
	
	@FindBy(xpath = "//body//main//a[@href='/admin/events/create']/button")
	private WebElement createEventButton;
	
	@FindBy(xpath = "//main//div[div[div[div[div[p[contains(text(),'Dashboard')]]]]]]")
	private WebElement eventsDashboardContainer;
		
	public AdminEventsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminEvents());
	}
	
	public AdminEventComponent findEventByName(String eventName) {
		if (isEventPresent(eventName)) {
			WebElement element = findWebElementEventByName(eventName);
			AdminEventComponent component = new AdminEventComponent(driver, element);
			return component;
		} else {
			return null;
		}
	}
	
	public AdminEventComponent findOpenedEventByName(String eventName) {
		if (isEventPresent(eventName)) {
			List<WebElement> elements = findWebElementsEventByName(eventName);
			for(WebElement webEl : elements) {
				AdminEventComponent component = new AdminEventComponent(driver, webEl);
				if(!component.isEventCanceled()) {
					return component;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	private List<WebElement> findWebElementsEventByName(String name) {
		explicitWaitForVisiblity(eventsDashboardContainer);
		List<WebElement> list = explicitWait(15, ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//main//div[div[div[div[div[a[p[contains(text(),'" + name +"')]]]]]]]")));
		return list;
	}
	
	private WebElement findWebElementEventByName(String name) {
		explicitWaitForVisiblity(eventsDashboardContainer);
		WebElement event = explicitWait(15, ExpectedConditions.visibilityOfElementLocated(By.xpath("//main//div[div[div[div[div[a[p[contains(text(),'" + name +"')]]]]]]]")));
		return event;
	}
		
	private boolean isEventPresent(String eventName) {
		explicitWaitForVisiblity(eventsDashboardContainer);
		return isExplicitlyWaitVisible(6, By.xpath("//main//div[div[div[div[div[a[p[contains(text(),'" + eventName +"')]]]]]]]"));
	}
	
	public void clickCreateEvent() {
		waitForTime(1000);
		waitVisibilityAndClick(createEventButton);
	}

}
