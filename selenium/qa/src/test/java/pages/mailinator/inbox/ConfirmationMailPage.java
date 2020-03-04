package pages.mailinator.inbox;

import java.math.BigDecimal;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import pages.components.mailframes.BaseConfirmationFrame;
import pages.components.mailframes.TotalRefundConfirmationFrame;

public abstract class ConfirmationMailPage extends MailinatorInboxPage {

	public static final String ORDER_TOTAL_KEY = "orderTotal";
	public static final String TOTAL_FEES_KEY = "totalFees";
	public static final String SOFT_ASSERT = "soft_assert";

	public ConfirmationMailPage(WebDriver driver) {
		super(driver);
	}

	public abstract String getSubject();

	public void openMailAndCheckValidity(Map<String, Object> data) {
		goToMail(getSubject());
		isCorrectMail(data);
	}

	public abstract BaseConfirmationFrame getConfirmationFrame();

	public void isCorrectMail(Map<String, Object> data)	 {
		checkMessagePageAndSwitchToFrame();
		TotalRefundConfirmationFrame frame = new TotalRefundConfirmationFrame(driver);
		BigDecimal mailTotalFees = frame.getTotalFees();
		BigDecimal mailOrderTotal = frame.getOrderTotal();
		BigDecimal totalFees = (BigDecimal) data.get(TOTAL_FEES_KEY);
		BigDecimal orderTotal = (BigDecimal) data.get(ORDER_TOTAL_KEY);
		SoftAssert sa = (SoftAssert) data.get(SOFT_ASSERT);
		sa.assertTrue(mailTotalFees.compareTo(totalFees) == 0,
				"total fees in mail and on manage order page not the same");
		sa.assertTrue(mailOrderTotal.compareTo(orderTotal) == 0, 
				"order total in mail and on manage order page not the same");
		sa.assertAll();

	}

}
