package pages.mailinator.inbox;

import org.openqa.selenium.WebDriver;

import pages.components.mailframes.BaseConfirmationFrame;
import pages.components.mailframes.TotalRefundConfirmationFrame;

public class RefundConfirmationMailinatorPage extends ConfirmationMailPage {

	private final String SUBJECT = "Your order has been refunded";

	public RefundConfirmationMailinatorPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public BaseConfirmationFrame getConfirmationFrame() {
		return new TotalRefundConfirmationFrame(driver)	;
	}

}
