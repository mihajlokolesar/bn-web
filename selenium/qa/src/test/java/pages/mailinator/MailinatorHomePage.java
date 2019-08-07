package pages.mailinator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import utils.Constants;
import utils.SeleniumUtils;

public class MailinatorHomePage extends BasePage {

	@FindBy(id = "inboxfield")
	private WebElement searchBox;

	public MailinatorHomePage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.MAILINATOR_BASE_URL);
	}

	public void navigate() {
		driver.get(getUrl());
		explicitWait(10, 500, ExpectedConditions.urlToBe(getUrl()));
	}
	
	
	public void navigateToInNewTab(String url) {
		String parentHandler = driver.getWindowHandle();
		String jsScript = "window.open('" + url + "','_blank');";
		explicitWait(15, ExpectedConditions.javaScriptThrowsNoExceptions(jsScript));
		SeleniumUtils.switchToChildWindow(parentHandler, driver);
	}

	public void searchForUser(String userInboxName) {
		try {
			explicitWaitForVisiblity(searchBox);
			explicitWaitForClickable(searchBox);
			searchBox.sendKeys(userInboxName);
			WebElement clickGoButton = driver.findElement(By.xpath("//body//div//span/button[@id='go_inbox1']"));
			waitVisibilityAndClick(clickGoButton);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkIfOnUserInboxPage(String userInboxName) {
		explicitWait(10, 500, ExpectedConditions.urlContains(userInboxName));
		explicitWait(10, 1000,
				ExpectedConditions.presenceOfElementLocated(By.xpath("//table//thead//th[contains(text(),'From')]")));

	}

	public MailinatorInboxPage goToUserInbox(String userEmail) {
		SeleniumUtils.openNewTabWithLink(getUrl(), driver);
		String username = userEmail.split("@")[0];
		searchForUser(username);
		checkIfOnUserInboxPage(username);
		return new MailinatorInboxPage(driver);
	}

}
