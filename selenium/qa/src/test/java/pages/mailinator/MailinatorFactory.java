package pages.mailinator;

import org.openqa.selenium.WebDriver;

import config.MailinatorEnum;
import pages.mailinator.inbox.BOSellPagePurchaseWithCashMailinatorPage;
import pages.mailinator.inbox.MailinatorInboxPage;
import pages.mailinator.inbox.ResetPasswordMailinatorPage;
import pages.mailinator.inbox.TransferCancelMailinatorPage;

public class MailinatorFactory {

	public static MailinatorInboxPage getInboxPage(MailinatorEnum type, WebDriver driver, String userEmail) {
		MailinatorHomePage homePage = new MailinatorHomePage(driver);
		MailinatorInboxPage inbox = null;
		homePage.goToInbox(userEmail);
		switch (type) {
		case TICKET_TRANSFER_CANCEL:
			inbox = new TransferCancelMailinatorPage(driver);
			break;

		case RESET_PASSWORD:
			inbox = new ResetPasswordMailinatorPage(driver);
			break;
		case BO_SELL_WITH_CASH:
			inbox = new BOSellPagePurchaseWithCashMailinatorPage(driver);
			break;
		}
		return inbox;
	}
}
