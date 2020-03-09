package pages.admin.events;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import utils.Constants;

public class EventOverviewPage extends BasePage {

	private String eventName;

	public EventOverviewPage(WebDriver driver, String eventName) {
		super(driver);
		this.eventName = eventName;
	}

	@Override
	public void presetUrl() {

	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlMatches(Constants.getAdminEvents() + "/*.*/event-overview"));
	}

	public boolean isTitleCorrect() {
		if (eventName != null && !eventName.isEmpty()) {
			return isExplicitlyWaitVisible(By.xpath("//div[img]/p[contains(text(),'" + eventName + "')]"));
		} else {
			return false;
		}
	}
}