package pages.components.user;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class EventComponent extends BaseComponent {

	private WebElement selectedEventElement;

	private WebElement selectedRow;

	private String eventName;

	private String relativeTicketNumberXpath = ".//div[span/p[contains(text(),'Ticket #')]]/following-sibling::div/div/span[2]";

	private String relativeOrderNumberXpath = ".//div[span/p[contains(text(),'Ticket #')]]/following-sibling::div/div/span[3]";

	private String relativeToRowTicketNumberXpath = ".//div/span[2]";

	private String relativeToRowOrderNumberXpath = ".//div/span[3]";

	private String relativeToRowActionXpath = ".//div/span[6]/p/a[2]";

	private String relativeTransferLink = ".//div//a[span[text()='transfer']]";

	private String relativeRowWithTransfer = ".//div[div[span[p[a[span[text()='transfer']]]]]]";

	private String rowWithCancelTransferLink = ".//body//main//div[div[span[p[a[span[text()='cancel transfer']]]]]]";

	private String relativeCancelTransferlLink = ".//div//a[span[text()='cancel transfer']]";

	public EventComponent(WebDriver driver) {
		super(driver);
	}

	public EventComponent(WebDriver driver, WebElement selectedEventElement) {
		super(driver);
		this.selectedEventElement = selectedEventElement;
	}

	public WebElement selectTransferableRow() {
		WebElement row = null;
		try {
			row = SeleniumUtils.getChildElementFromParentLocatedBy(selectedEventElement,
					By.xpath(relativeRowWithTransfer), driver);
			this.selectedRow = row;
			return row;
		} catch (Exception e) {
			row = null;
		}
		return row;
	}

	public void clickOnCancelTransfer(String ticketNumber, String orderNumber) {
		List<WebElement> rows = explicitWait(15,
				ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(rowWithCancelTransferLink)));
		for (WebElement row : rows) {
			this.selectedRow = row;
			String ticketNum = getTicketNumber();
			String orderNum = getOrderNumber();
			if (ticketNum.equals(ticketNumber) && orderNum.equals(orderNumber)) {
				WebElement cancelTransfer = SeleniumUtils.getChildElementFromParentLocatedBy(selectedRow,
						By.xpath(relativeCancelTransferlLink), driver);
				cancelTransfer.getAttribute("innerHTML");
				explicitWaitForVisiblity(cancelTransfer);
				cancelTransfer.click();
				break;
			}
		}
	}

	public boolean checkIfSelectedRowTicketTransferCanceled() {
		waitForTime(2000);
		WebElement element = SeleniumUtils.getChildElementFromParentLocatedBy(selectedRow,
				By.xpath(relativeToRowActionXpath), driver);
		String text = element.getText();
		if (text != null && !text.isEmpty() && text.equals("transfer")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isTicketNumberPresent(String ticketNumber) {
		WebElement ticketNumberEl = SeleniumUtils.getChildElementFromParentLocatedBy(selectedEventElement,
				By.xpath(relativeTicketNumberXpath + "/p[contains(text(),'" + ticketNumber + "')]"), driver);
		return ticketNumberEl == null ? false : true;
	}

	public boolean isOrderNumberPresent(String orderNumber) {
		WebElement orderNumberEl = SeleniumUtils.getChildElementFromParentLocatedBy(selectedEventElement,
				By.xpath(relativeOrderNumberXpath + "/p/a/span[contains(text(),'" + orderNumber + "')]"), driver);
		return orderNumberEl == null ? false : true;
	}

	public String getTicketNumber() {
		WebElement ticketNumber = SeleniumUtils.getChildElementFromParentLocatedBy(selectedRow,
				By.xpath(relativeToRowTicketNumberXpath), driver);
		return ticketNumber.getText();
	}

	public String getOrderNumber() {
		WebElement orderNumber = SeleniumUtils.getChildElementFromParentLocatedBy(selectedRow,
				By.xpath(relativeToRowOrderNumberXpath), driver);
		return orderNumber.getText();
	}

	public void clickOnTransfer() {
		WebElement transferButton = SeleniumUtils.getChildElementFromParentLocatedBy(selectedRow,
				By.xpath(relativeTransferLink), driver);
		explicitWaitForVisibilityAndClickableWithClick(transferButton);
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

}
