package pages.components.mailframes;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import model.Event;
import pages.BaseComponent;
import utils.ProjectUtils;

public class TotalRefundConfirmationFrame extends BaseComponent {

	@FindBy(xpath = "//tbody//tr/td[contains(text(),'Fees Total')]")
	private WebElement totalFees;

	@FindBy(xpath = "//tbody//tr/td[contains(text(),'Order Total')]")
	private WebElement orderTotal;

	public TotalRefundConfirmationFrame(WebDriver driver) {
		super(driver);
	}

	public BigDecimal getTotalFees() {
		return getFees(totalFees);
	}

	public BigDecimal getOrderTotal() {
		return getFees(orderTotal);
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

	private BigDecimal getFees(WebElement element) {
		explicitWaitForVisiblity(element);
		String totalFeesText = element.getText();
		return ProjectUtils.getBigDecimalMoneyAmount(totalFeesText.split(":")[1]);
	}

}
