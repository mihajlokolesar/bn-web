package pages.mailinator.inbox;

import java.math.BigDecimal;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import pages.components.mailframes.TotalRefundConfirmationFrame;

public class TotalRefundConfirmationMailinatorPage extends MailinatorInboxPage {

	private final String SUBJECT = "Your order has been refunded";
	public static final String ORDER_TOTAL_KEY = "orderTotal";
	public static final String TOTAL_FEES_KEY = "totalFees";
	

	public TotalRefundConfirmationMailinatorPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void openMailAndCheckValidity(Map<String, Object> data) {
		goToMail(SUBJECT);
		isCorrectMail(data);

	}
	
	public void isCorrectMail(Map<String,Object> data) {
		checkMessagePageAndSwitchToFrame();
		TotalRefundConfirmationFrame frame = new TotalRefundConfirmationFrame(driver);
		BigDecimal mailTotalFees = frame.getTotalFees();
		BigDecimal mailOrderTotal = frame.getOrderTotal();
	
	}

}
