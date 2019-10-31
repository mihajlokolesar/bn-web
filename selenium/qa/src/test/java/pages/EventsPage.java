package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Constants;
import utils.SeleniumUtils;

public class EventsPage extends BasePage {
	
	@FindBy(xpath = "//div/p[contains(text(),'Upcoming events')]/following-sibling::div")
	private WebElement eventListContainer;

	@FindBy(xpath = "//body//main//header")
	private WebElement dropHeader;

	@FindBy(linkText = "View map")
	private WebElement viewMapLink;
	
	@FindBy(xpath = "//a[parent::div]/button[span[contains(text(),'Purchase Tickets')]]")
	private WebElement purchaseButton;

	private By purchaseButton(String urlPath) {
		return By.xpath("//body//main/div/div/div/div[2]/div//a[contains(@href,'" + urlPath + "')]/button");
	}

	public EventsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getEventsBigNeon());
	}

	@Override
	public void navigate() {
		super.navigate();
		driver.get(getUrl());
	}

	public boolean isEventPresent(String eventName) {
		boolean retVal = false;
		try {
			if (findEvent(eventName) != null) {
				retVal = true;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public WebElement findEvent(String eventName) {
		String lowerCaseName = eventName.toLowerCase();
		explicitWait(25, ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(eventListContainer)));
		WebElement event = explicitWait(25, ExpectedConditions.elementToBeClickable(
				eventListContainer.findElement(By.xpath(".//div/a[contains(@href, '" + lowerCaseName + "')]/div"))));
		return event;
	}

	public void clickOnEvent(String eventName) {
		WebElement event = findEvent(eventName);
		waitVisibilityAndBrowserCheckClick(event);
	}

	public void clickOnViewMap() {
		explicitWaitForVisiblity(viewMapLink);
		String url = viewMapLink.getAttribute("href");
		String parentHandle = SeleniumUtils.openNewTabWithLink(url, driver);
		waitForTime(2000);
		driver.close();
		waitForTime(2000);
		driver.switchTo().window(parentHandle);

	}

	public void purchaseTicketLinkClick() throws Exception {
		waitVisibilityAndBrowserCheckClick(purchaseButton);
	}
	
	private boolean isPurchaseButtonVisible(String urlPath) {
		return isExplicitlyWaitVisible(purchaseButton(urlPath));
	}

}
