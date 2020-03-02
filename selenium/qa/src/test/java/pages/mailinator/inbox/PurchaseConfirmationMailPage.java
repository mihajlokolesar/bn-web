package pages.mailinator.inbox;

import org.openqa.selenium.WebDriver;

import pages.components.mailframes.BaseConfirmationFrame;
import pages.components.mailframes.TotalPurchaseConfirmationFrame;

public class PurchaseConfirmationMailPage extends ConfirmationMailPage {

	private final String SUBJECT = "Next Step - Get Your Tickets";

	public PurchaseConfirmationMailPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public BaseConfirmationFrame getConfirmationFrame() {
		return new TotalPurchaseConfirmationFrame(driver);
	}

}
