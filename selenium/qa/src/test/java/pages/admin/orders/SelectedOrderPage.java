package pages.admin.orders;

import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.admin.orders.manage.tickets.OrderDetails;
import pages.components.admin.orders.manage.tickets.TicketRow;
import utils.Constants;

public class SelectedOrderPage extends BasePage {

	private final String orderId;
	
	private OrderDetails orderDetails;

	@FindBy(xpath = "//main//div/p[contains(text(),'Show')]")
	private WebElement showOrdersDetailsExpander;

	@FindBy(xpath = "//main//div/p[contains(text(),'Hide')]")
	private WebElement hideOrderDetailsExpander;
	
	@FindBy(xpath = "//button[@type='button' and span[contains(text(),'Refund')]]")
	private WebElement refundButton;

	public SelectedOrderPage(WebDriver driver, String orderId) {
		super(driver);
		this.orderId = orderId;
	}

	@Override
	public void presetUrl() {
	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15,
				ExpectedConditions.urlMatches(Constants.getAdminEvents() + "/*.*/dashboard/orders/manage/" + orderId));
	}
	
	public void expandOrderDetails() {
		this.orderDetails = new OrderDetails(driver);
		explicitWaitForVisibilityAndClickableWithClick(showOrdersDetailsExpander);
	}
	
	public boolean closeOrderDetails() {
		explicitWaitForVisibilityAndClickableWithClick(hideOrderDetailsExpander);
		boolean retVal = orderDetails.isClosed();
		if (retVal)
			orderDetails = null;
		return retVal;
		
	}
	
	public void clickOnRefundButton() {
		explicitWaitForVisibilityAndClickableWithClick(refundButton);
	}
	
	public TicketRow findTicketRow(Predicate<TicketRow> predicate) {
		return this.orderDetails.findTicketRow(predicate);
	}
	
	public OrderDetails getOrderDetails() {
		return this.orderDetails;
	}

}
