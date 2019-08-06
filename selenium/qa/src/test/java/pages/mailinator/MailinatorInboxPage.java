package pages.mailinator;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;

public class MailinatorInboxPage extends BasePage {

	@FindBy(id = "trash_but")
	private WebElement trashBin;

	public MailinatorInboxPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {

	}

	public void goToResetMail() {
		for (int i = 0; i < 6; i++) {
			driver.navigate().refresh();
		}
		WebElement mailRowCell = explicitWait(20, 2000, ExpectedConditions.presenceOfElementLocated(By.xpath(
				".//table//tbody//tr[td[contains(text(),'noreply@bigneon.com')] and td/a[contains(text(),'Reset Your Password')]]/td[contains(text(),'noreply@bigneon.com')]")));
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
		waitVisibilityAndClick(resetLink);
		// go back to mailpage and delete mail
		waitForTime(1500);
		String currentHandle = driver.getWindowHandle();
		if (!currentHandle.equalsIgnoreCase(parentHandle)) {
			driver.switchTo().window(parentHandle);
		}
		driver.switchTo().parentFrame();
		deleteCurrentMail();
		driver.navigate().refresh();

		// switch to new tab
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

	private void deleteCurrentMail() {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", trashBin);
			explicitWait(10, ExpectedConditions.visibilityOf(trashBin));
			trashBin.click();
		} catch (Exception e) {

		}
	}
}
