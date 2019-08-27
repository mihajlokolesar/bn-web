package pages.mailinator.inbox;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.SeleniumUtils;

public class ResetPasswordMailinatorPage extends MailinatorInboxPage {

	public ResetPasswordMailinatorPage(WebDriver driver) {
		super(driver);
	}

	public void clickOnResetPasswordLinkInMail() {
		goToMail("Reset Your Password");
		String parentHandle = driver.getWindowHandle();
		checkMessagePageAndSwitchToFrame();
		WebElement resetLink = null;
		try {
			resetLink = explicitWait(10, 500,
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Reset Password']")));
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,400)");
		}
		waitVisibilityAndClick(resetLink);
		// go back to mailpage and delete mail
		waitForTime(1500);

		SeleniumUtils.switchToParentWindow(parentHandle, driver);
		driver.switchTo().parentFrame();
		deleteMail();
		driver.navigate().refresh();

		// switch to new tab
		SeleniumUtils.switchToChildWindow(parentHandle, driver);
	}

}
