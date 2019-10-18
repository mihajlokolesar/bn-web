package pages.components.admin.orders.manage.tickets;

import java.math.BigDecimal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import enums.TicketStatus;
import pages.BaseComponent;
import utils.SeleniumUtils;

public class TicketRow extends BaseComponent {

	private WebElement row;

	private String relativeCheckBoxXpath = "./div/div/div[1]/div/div";

	private String relativeAttendeLinkXpath = "./div/div/div[2]/a";

	private String relativeTicketTypeXpath = "./div/div/div[3]/div/p";

	private String relativeQuantityXpath = "./div/div[1]/p[1]";

	private String relativeTotalXpath = "./div/div/p[2]";
	
	private String relativeTotalPerTicketFee = "./div/div[2]/p";

	private String relativeStatusXpath = "./div/div/p[3]";

	public TicketRow(WebDriver driver, WebElement row) {
		super(driver);
		this.row = row;
	}

	public void clickOnCheckoutBoxInTicket() {
		WebElement el = getCheckboxElement();
		SeleniumUtils.jsScrollIntoView(el, driver);
		waitVisibilityAndBrowserCheckClick(el);
		waitForTime(1000);
	}

	/**
	 * This text is offten turncated and can not be used with great degree of
	 * confidence
	 * 
	 * @return
	 */
	public String getAttendeeName() {
		WebElement el = getAttendeeElement();
		String text = el.getText();
		return text;
	}

	public boolean isTicketPurchased() {
		TicketStatus status = getTicketStatus();
		return status.equals(TicketStatus.PURCHASED);
	}
	
	public boolean isTicketRefunded() {
		TicketStatus status = getTicketStatus();
		return status.equals(TicketStatus.REFUNDED);
	}
	
	private TicketStatus getTicketStatus() {
		WebElement purchasedEl = getStatusElement();
		String text = purchasedEl.getText();
		return TicketStatus.getTicketStatus(text);
	}
	
	public BigDecimal getTicketTotalAmount() {
		Double dAmount = SeleniumUtils.getDoubleAmount(row, relativeTotalXpath, driver);
		return new BigDecimal(dAmount);
	}
	
	public BigDecimal getPerTicketFeeAmount() {
		Double dAmount = SeleniumUtils.getDoubleAmount(row, relativeTotalPerTicketFee, driver);
		return new BigDecimal(dAmount);
	}
	
	private WebElement getCheckboxElement() {
		return SeleniumUtils.getChildElementFromParentLocatedBy(row, By.xpath(relativeCheckBoxXpath), driver);
	}

	private WebElement getAttendeeElement() {
		return SeleniumUtils.getChildElementFromParentLocatedBy(row, By.xpath(relativeAttendeLinkXpath), driver);
	}

	private WebElement getStatusElement() {
		return SeleniumUtils.getChildElementFromParentLocatedBy(row, By.xpath(relativeStatusXpath), driver);
	}

}