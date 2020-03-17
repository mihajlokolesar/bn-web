package pages.mailinator.inbox;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.ClaimTicketFrame;
import pages.components.PurchaseMailFrame;
import utils.SeleniumUtils;

public class MailinatorInboxPage extends BasePage {

	@FindBy(id = "trash_but")
	private WebElement trashBin;

	@FindBy(xpath = "//div//div[@class='x_content']/iframe[@id='msg_body']")
	private WebElement msgContentFrame;

	private String urlMsgPaneValue = "msgpane";

	public MailinatorInboxPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {

	}
	
	public void goToMail(String subjectValue) {
		goToMail(By.xpath(
						".//table//tbody//tr[td[contains(text(),'noreply@bigneon.com')] and td/a[contains(text(),'"
								+ subjectValue + "')]]/td[contains(text(),'noreply@bigneon.com')]"));
	}
	
	public void goToMail(By by) {
		waitForTime(1500);
		for (int i = 0; i < 5; i++) {
			driver.navigate().refresh();
		}
		WebElement mailRowCell = explicitWait(20, 2000,
				ExpectedConditions.presenceOfElementLocated(by));
		mailRowCell.click();
	}

	public boolean openMailAndCheckValidity(String mailSubjectValue, int numberOfTickets, String eventName) {
		goToMail(mailSubjectValue);
		boolean retVal = isCorrectMail(numberOfTickets, eventName);
		if (retVal) {
			deleteMail();
		}
		return retVal;

	}
	
	public void openMailAndCheckValidity(Map<String, Object> data) {}
	
	public boolean isCorrectMail(int numberOfTickets, String eventName) {
		waitForTime(1000);
		explicitWait(10, ExpectedConditions.urlContains("msgpane"));
		driver = explicitWait(15, ExpectedConditions.frameToBeAvailableAndSwitchToIt(msgContentFrame));
		PurchaseMailFrame purchaseMailFrame = new PurchaseMailFrame(driver);

		// TODO: move this logic to PurhchaseMailFrame, it belongs there
		String quantity = purchaseMailFrame.getQuantity();
		String ename = purchaseMailFrame.getEventName();
		driver.switchTo().parentFrame();
		if (("" + numberOfTickets).equals(quantity) && ename.contains(eventName)) {
			return true;
		} else {
			return false;
		}
	}

	public void clickOnClaimTicket() {
		String parentHandler = driver.getWindowHandle();
		waitForTime(1000);
		explicitWait(10, ExpectedConditions.urlContains(urlMsgPaneValue));
		explicitWaitForVisiblity(msgContentFrame);
		driver.switchTo().frame(msgContentFrame);
		waitForTime(1000);
		new ClaimTicketFrame(driver).clickOnClaimTicketLink();

		driver.switchTo().parentFrame();
		SeleniumUtils.switchToParentWindow(parentHandler, driver);
//		deleteMail();
		SeleniumUtils.switchToChildWindow(parentHandler, driver);

	}

	public WebDriver checkMessagePageAndSwitchToFrame() {
		waitForTime(1000);
		explicitWait(10, ExpectedConditions.urlContains(urlMsgPaneValue));
		driver = explicitWait(15, ExpectedConditions.frameToBeAvailableAndSwitchToIt(msgContentFrame));
		return driver;
	}

	protected void deleteMail() {
		try {
			waitVisibilityAndClick(trashBin);
		} catch (Exception e) {
		}
	}
}
