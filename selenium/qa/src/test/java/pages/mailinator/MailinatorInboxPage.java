package pages.mailinator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;

public class MailinatorInboxPage extends BasePage {

	public MailinatorInboxPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {

	}

	public void goToResetMail() throws InterruptedException {
		driver.navigate().refresh();
		Thread.sleep(4000);
		driver.navigate().refresh();
		Thread.sleep(3000);
		driver.navigate().refresh();
		Thread.sleep(500);
		WebElement mailRowCell = explicitWait(20, 2000, ExpectedConditions.presenceOfElementLocated(By.xpath(
				".//table//tbody//tr[td[contains(text(),'noreply@bigneon.com')] and td[contains(text(),'Reset Your Password')]]/td[contains(text(),'noreply@bigneon.com')]")));
		mailRowCell.click();
	}

	public void clickOnResetPasswordLinkInMail() {
		String parentHandle = driver.getWindowHandle();
		explicitWait(10, ExpectedConditions.urlContains("msgpane"));
		driver = explicitWait(15, ExpectedConditions
				.frameToBeAvailableAndSwitchToIt(By.xpath("//div//div[@class='x_content']/iframe[@id='msg_body']")));
		WebElement resetLink = null;
		try {
			resetLink = explicitWait(10, 500,
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Reset Password']")));
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,400)");
		}
		resetLink.click();
		List<String> handles = new ArrayList<String>(driver.getWindowHandles());
		String childHandle = driver.getWindowHandle();
		if (childHandle.equals(parentHandle)) {
			for (String handle : handles) {
				if (!parentHandle.equalsIgnoreCase(handle)) {
					driver.switchTo().window(handle);
				}
			}
		}
	}
}
