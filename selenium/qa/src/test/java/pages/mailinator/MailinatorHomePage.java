package pages.mailinator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.mailinator.inbox.MailinatorInboxPage;
import utils.Constants;

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
		goToInbox(userEmail);
		return new MailinatorInboxPage(driver);
	}
	
	public void goToInbox(String userEmail) {
		navigate();
		String username = userEmail.split("@")[0];
		searchForUser(username);
		checkIfOnUserInboxPage(username);
	}

}
