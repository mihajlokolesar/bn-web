package pages.mailinator;

import org.openqa.selenium.WebDriver;

import config.MailinatorEnum;
import pages.mailinator.inbox.AnnouncementMailinatorPage;
import pages.mailinator.inbox.BOSellPagePurchaseWithCashMailinatorPage;
import pages.mailinator.inbox.MailinatorInboxPage;
import pages.mailinator.inbox.PurchaseConfirmationMailPage;
import pages.mailinator.inbox.ResetPasswordMailinatorPage;
import pages.mailinator.inbox.RefundConfirmationMailinatorPage;
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
		case ANNOUNCEMENT_TO_BUYERS:
			inbox = new AnnouncementMailinatorPage(driver);
			break;
		case TOTAL_REFUND_CONFIRMATION:
			inbox = new RefundConfirmationMailinatorPage(driver);
			break;
		case PURCHASE_CONFIRMATION_MAIL:
			inbox = new PurchaseConfirmationMailPage(driver);
			break;
		}
		return inbox;
	}
}
