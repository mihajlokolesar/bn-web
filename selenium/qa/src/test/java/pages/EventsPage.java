package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Constants;
import utils.SeleniumUtils;

public class EventsPage extends BasePage {
	
	@FindBy(xpath = "//body//main/div/div/div/div[2]/div/div/div[@class='jss63 jss87']")
	private WebElement eventListContainer;

	@FindBy(xpath = "//body//main//header")
	private WebElement dropHeader;

	@FindBy(linkText = "View map")
	private WebElement viewMapLink;

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

	public void navigate() {
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
		explicitWait(10, ExpectedConditions.visibilityOf(event));
		event.click();
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
		WebElement purchaseLink = null;
		String urlPath = SeleniumUtils.getUrlPath(driver);
		if (isPurchaseButtonVisible(urlPath)) {
			purchaseLink = driver.findElement(purchaseButton(urlPath));
		} else {
			purchaseLink = driver.findElement(By.xpath("//a[contains(@href,'" + urlPath + "')]/button"));
		}
		purchaseLink.click();
	}
	
	private boolean isPurchaseButtonVisible(String urlPath) {
		return isExplicitlyWaitVisible(purchaseButton(urlPath));
	}

}
