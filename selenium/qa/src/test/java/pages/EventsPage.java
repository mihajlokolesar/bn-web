package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import model.Event;
import model.User;
import pages.components.Header;
import test.wrappers.CreateEventWrapper;
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

	public void eventsPageSteps(Event event) throws Exception {
		navigate();
		if (!isEventPresent(event.getEventName())) {
			createEvent(event);
			navigate();
			driver.navigate().refresh();
		}
		clickOnEvent(event.getEventName());
		clickOnViewMap();
		purchaseTicketLinkClick();
	}

	private void createEvent(Event event) throws Exception {
		LoginPage loginPage = new LoginPage(driver);
		User superuser = User.generateSuperUser();
		loginPage.login(superuser.getEmailAddress(), superuser.getPass());
		CreateEventWrapper createEvent = new CreateEventWrapper();
		boolean retVal = createEvent.createEvent(driver, event);
		if (!retVal) {
			Assert.fail("Event creationg in purchase steps failed");
		} else {
			
		}
		Header header = new Header(driver);
		header.logOut();

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
		explicitWaitForVisiblity(eventListContainer);
		WebElement event = explicitWait(10, ExpectedConditions.elementToBeClickable(
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

	public boolean dropHeaderVisibility() {
		return isExplicitlyWaitVisible(dropHeader);
	}

	public boolean isPurchaseButtonVisible(String urlPath) {
		return isExplicitlyWaitVisible(purchaseButton(urlPath));
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

}
