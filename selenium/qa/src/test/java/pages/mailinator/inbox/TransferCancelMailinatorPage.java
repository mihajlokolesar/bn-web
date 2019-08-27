package pages.mailinator.inbox;

import org.openqa.selenium.WebDriver;

import pages.BasePage;
import pages.components.TransferTicketSenderMailFrame;
import utils.SeleniumUtils;

public class TransferCancelMailinatorPage extends MailinatorInboxPage {

	public TransferCancelMailinatorPage(WebDriver driver) {
		super(driver);
	}
	
	public String clickOnCancelTransfer(String receiversMail) {
		goToMail("Tickets Sent... Important information about your transfer");
		waitForTime(1000);
		String parentHandle = driver.getWindowHandle();
		checkMessagePageAndSwitchToFrame();
		TransferTicketSenderMailFrame transferTicketFrame = new TransferTicketSenderMailFrame(driver);
		if (transferTicketFrame.confirmReceiversMail(receiversMail)) {
			transferTicketFrame.clickOnCancelTransferTicket();
			
			SeleniumUtils.switchToParentWindow(parentHandle, driver);
			driver.switchTo().parentFrame();
			SeleniumUtils.switchToChildWindow(parentHandle, driver);
			return parentHandle;
		} else {
			return null;
		}

	}
	
	public boolean checkCancelConfirmationMail(String receiversMail) {
		goToMail("Transfer Cancellation Confirmation");
		waitForTime(1000);
		checkMessagePageAndSwitchToFrame();
		TransferTicketSenderMailFrame transferTicketFrame = new TransferTicketSenderMailFrame(driver);
		boolean retVal =  transferTicketFrame.confirmReceiversMail(receiversMail);
		driver.switchTo().parentFrame();
		return retVal;
	}
	
	public void checkReceiversTransferCancelationMail() {
		goToMail("has cancelled the ticket transfer");
	}
}
