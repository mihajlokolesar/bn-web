package pages.mailinator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.PurchaseMailFrame;
import utils.SeleniumUtils;

public class MailinatorInboxPage extends BasePage {

	@FindBy(id = "trash_but")
	private WebElement trashBin;

	@FindBy(xpath = "//div//div[@class='x_content']/iframe[@id='msg_body']")
	private WebElement msgContentFrame;

	public MailinatorInboxPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {

	}

	public void goToMail(String subjectValue) {
		waitForTime(1500);
		for (int i = 0; i < 5; i++) {
			driver.navigate().refresh();
		}
		WebElement mailRowCell = explicitWait(20, 2000,
				ExpectedConditions.presenceOfElementLocated(By.xpath(
						".//table//tbody//tr[td[contains(text(),'noreply@bigneon.com')] and td/a[contains(text(),'"
								+ subjectValue + "')]]/td[contains(text(),'noreply@bigneon.com')]")));
		
		mailRowCell.click();
	}

	public void clickOnResetPasswordLinkInMail() {
		String parentHandle = driver.getWindowHandle();
		explicitWait(10, ExpectedConditions.urlContains("msgpane"));
		driver = explicitWait(15, ExpectedConditions
				.frameToBeAvailableAndSwitchToIt(msgContentFrame));
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
		waitVisibilityAndClick(trashBin);
		driver.navigate().refresh();

		// switch to new tab
		SeleniumUtils.switchToChildWindow(parentHandle, driver);
	}

	public boolean isCorrectMail(int numberOfTickets, String eventName) {
		waitForTime(1000);
		explicitWait(10, ExpectedConditions.urlContains("msgpane"));
		driver = explicitWait(15, ExpectedConditions.frameToBeAvailableAndSwitchToIt(msgContentFrame));
		PurchaseMailFrame purchaseMailFrame = new PurchaseMailFrame(driver);
		String quantity = purchaseMailFrame.getQuantity();
		System.out.println(quantity);
		String ename = purchaseMailFrame.getEventName();
		System.out.println(ename);
		driver.switchTo().parentFrame();
		if (("" + numberOfTickets).equals(quantity) && ename.contains(eventName)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean openMailAndCheckValidity(String mailSubjectValue, int numberOfTickets, String eventName) {
		goToMail(mailSubjectValue);
		boolean retVal = isCorrectMail(numberOfTickets, eventName);
		if(retVal) {
			waitVisibilityAndClick(trashBin);
		}
		return retVal;
		
	}
}
