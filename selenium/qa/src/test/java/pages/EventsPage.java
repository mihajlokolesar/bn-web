package pages;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
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
		return By.xpath("//body//main/div/div/div/div[2]/div//a[contains(@href,'" + urlPath + "')]");
	}

	public EventsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getEventsBigNeon());
	}
	
	
	public void navigate() {
		driver.get(getUrl());
	}

	public WebElement findElementFor(String eventName) {
		explicitWait(10, ExpectedConditions.visibilityOf(eventListContainer));
		WebElement event = explicitWait(10, ExpectedConditions
				.elementToBeClickable(eventListContainer.findElement(By.xpath(".//div/a[contains(@href, '" + eventName + "')]/div"))));
		return event;
	}

	public void clickOnEvent(String eventName) {
		WebElement event = findElementFor(eventName);
		explicitWait(10, ExpectedConditions.visibilityOf(event));
		event.click();
	}

	public void clickOnViewMap() {
		explicitWait(10, ExpectedConditions.visibilityOf(viewMapLink));
		String parentHandler = driver.getWindowHandle();
		viewMapLink.click();
		SeleniumUtils.switchToChildWindow(parentHandler, driver);
		driver.close();
		driver.switchTo().window(parentHandler);

	}

	public boolean dropHeaderVisibility() {
		boolean retVal = false;
		try {
			explicitWait(10, ExpectedConditions.visibilityOf(dropHeader));
			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	public boolean isPurchaseButtonVisible(String urlPath) {
		boolean retVal = false;
		try {
			explicitWait(10, ExpectedConditions.visibilityOfElementLocated(purchaseButton(urlPath)));
			retVal = true;
		}catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public void purchaseTicketLinkClick() throws Exception {
		WebElement purchaseLink = null;
		String urlPath = SeleniumUtils.getUrlPath(driver);
		if (isPurchaseButtonVisible(urlPath)) {
			purchaseLink = driver.findElement(purchaseButton(urlPath));
		} else {
			purchaseLink = driver.findElement(By.xpath("//a[contains(@href,'" + urlPath + "')]"));
		}
		
		purchaseLink.click();
	}

}
