package pages.components.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class EventComponent extends BaseComponent {

	private WebElement selectedEventElement;

	private String relativeTicketNumberXpath = ".//div[span/p[contains(text(),'Ticket #')]]/following-sibling::div/div/span[2]";

	private String relativeOrderNumberXpath = ".//div[span/p[contains(text(),'Ticket #')]]/following-sibling::div/div/span[3]";

	private String relativeTransferLink = ".//div//a[span[contains(text(),'transfer')]]";

	public EventComponent(WebDriver driver) {
		super(driver);
	}

	public EventComponent(WebDriver driver, WebElement selectedEventElement) {
		super(driver);
		this.selectedEventElement = selectedEventElement;
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
		WebElement ticketNumber = SeleniumUtils.getChildElementFromParentLocatedBy(selectedEventElement,
				By.xpath(relativeTicketNumberXpath), driver);
		return ticketNumber.getText();
	}

	public String getOrderNumber() {
		WebElement orderNumber = SeleniumUtils.getChildElementFromParentLocatedBy(selectedEventElement,
				By.xpath(relativeOrderNumberXpath), driver);
		return orderNumber.getText();
	}

	public void clickOnTransfer() {
		WebElement transferButton = SeleniumUtils.getChildElementFromParentLocatedBy(selectedEventElement,
				By.xpath(relativeTransferLink), driver);
		explicitWaitForVisibilityAndClickableWithClick(transferButton);
	}
}
