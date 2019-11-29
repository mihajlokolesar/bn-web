package pages.components.tickets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class DownloadAppComponent extends BaseComponent {

	@FindBy(xpath = "//a[span[div[contains(text(),'APP STORE')]]]")
	private WebElement appStore;

	@FindBy(xpath = "//a[span[div[contains(text(),'GOOGLE PLAY')]]]")
	private WebElement googlePlay;

	public DownloadAppComponent(WebDriver driver) {
		super(driver);
	}

	public boolean isAppStoreButtonLinkValid() {
		return isLinkValid(appStore, "https://apps.apple.com.*big-neon|bigneon.*");
	}
	
	public boolean isGooglePlayButtonLinkValid() {
		return isLinkValid(googlePlay, "https://play.google.com.*big-neon|bigneon.*");
	}
	
	private boolean isLinkValid(WebElement button, String regExPattern) {
		String parentUrl = driver.getCurrentUrl();
		String parentHandle = driver.getWindowHandle();
		waitVisibilityAndBrowserCheckClick(button);
		waitForTime(2200);
		boolean isStoreUrlValid = explicitWait(15, ExpectedConditions.urlMatches(regExPattern));
		SeleniumUtils.switchWindow(parentHandle, driver);
		
		driver.close();
//		SeleniumUtils.switchToParentWindow(parentHandle, driver);
		waitForTime(600);
		driver.switchTo().window(parentHandle);
		boolean isOnParentPage = explicitWait(15, ExpectedConditions.urlToBe(parentUrl));
		return isStoreUrlValid && isOnParentPage;
	}

}
