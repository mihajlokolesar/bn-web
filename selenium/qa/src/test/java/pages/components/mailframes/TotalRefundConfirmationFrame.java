package pages.components.mailframes;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import model.Event;

public class TotalRefundConfirmationFrame extends BaseConfirmationFrame {

	public TotalRefundConfirmationFrame(WebDriver driver) {
		super(driver);
	}

	public BigDecimal getEventFees() {
		return getRowAmount("Event Fees");
	}

	public BigDecimal getCreditCardFees() {
		return getRowAmount("Credit Card Fees");
	}
	
	public BigDecimal getTicketFees() {
		return getRowAmount("Ticket Fees");
	}

	public BigDecimal getEventTicketFees(Event event) {
		List<WebElement> list = getRowAmountElements(event.getEventName());
		if (list != null && !list.isEmpty()) {
			BigDecimal total = new BigDecimal(0);
			for (WebElement element : list) {
				total = total.add(getAccessUtils().getBigDecimalMoneyAmount(element));
			}
			return total;
		} else {
			return null;
		}
	}

	private BigDecimal getRowAmount(String feeType) {
		WebElement el = getRowAmountElement(feeType);
		return getAccessUtils().getBigDecimalMoneyAmount(el);

	}

	private WebElement getRowAmountElement(String feeType) {
		By by = By.xpath("//tbody//tr/th[contains(text(),'" + feeType + "')]/following-sibling::th");
		WebElement el = explicitWaitForVisibilityBy(by);
		return el;
	}

	private List<WebElement> getRowAmountElements(String feeType) {
		By by = By.xpath("//tbody//tr/th[contains(text(),'" + feeType + "')]/following-sibling::th");
		List<WebElement> el = explicitWaitForVisiblityForAllElements(by);
		return el;
	}

}
