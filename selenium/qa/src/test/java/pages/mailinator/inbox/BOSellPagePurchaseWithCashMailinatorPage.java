package pages.mailinator.inbox;

import org.openqa.selenium.WebDriver;

import pages.components.PurchaseMailFrame;

public class BOSellPagePurchaseWithCashMailinatorPage extends MailinatorInboxPage {

	public BOSellPagePurchaseWithCashMailinatorPage(WebDriver driver) {
		super(driver);
	}
	
	public boolean openMailAndCheckValidity(Double totalOrder) {
		goToMail("Next Step - Get Your Tickets");
		boolean retVal =  isCorrectMail(totalOrder);
		if (retVal) {
			deleteMail();
		}
		return retVal;
	}
	
	public boolean isCorrectMail(Double totalOrder) {
		checkMessagePageAndSwitchToFrame();
		PurchaseMailFrame purchaseMailFrame = new PurchaseMailFrame(driver);
		Double total = purchaseMailFrame.getOrderTotal();
		driver.switchTo().parentFrame();
		return totalOrder.compareTo(total) == 0;
	}

}