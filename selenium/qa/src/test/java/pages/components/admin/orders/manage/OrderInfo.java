package pages.components.admin.orders.manage;

import java.math.BigDecimal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import model.Venue;
import pages.BaseComponent;
import utils.ProjectUtils;

public class OrderInfo extends BaseComponent {
	
	@FindBy(xpath = "//p[contains(text(),'Order total:')]")
	private WebElement ticketTotal;
	
	@FindBy(xpath = "//p[contains(text(),'Order total:')]/span")
	private WebElement feesTotal;
	
	@FindBy(xpath = "//div[div[p[contains(text(),'Order #')]]]/div[4]/div/div/span[3]")
	private WebElement qty;
	
	@FindBy(xpath = "//div[div[p[contains(text(),'Order #')]]]/div[4]/div/div/span[4]")
	private WebElement total;
	
	@FindBy(xpath = "//div[div[p[contains(text(),'Order #')]]]/div[4]/div/div/span[1]/p[2]")
	private WebElement venueInfo;
	
	public OrderInfo(WebDriver driver) {
		super(driver);
	}
	
	public BigDecimal getTicketTotal() {
		explicitWaitForVisiblity(ticketTotal);
		String text = getAccessUtils().getTextOfElement(ticketTotal);
		String total = text.split(":")[1];
		String ticketTotal = total.split("\\+")[0];
		return ProjectUtils.getBigDecimalMoneyAmount(ticketTotal.trim());
	}
	
	public BigDecimal getFeesTotal() {
		explicitWaitForVisiblity(feesTotal);
		String text = getAccessUtils().getTextOfElement(feesTotal);
		String replaced = text.replace("+", "");
		replaced = replaced.replace("FEES", "");
		return ProjectUtils.getBigDecimalMoneyAmount(replaced.trim());
	}
	
	public BigDecimal getQty() {
		explicitWaitForVisiblity(qty);
		String text = getAccessUtils().getTextOfElement(qty);
		return new BigDecimal(text);
	}
	
	public BigDecimal getOrderTotal() {
		explicitWaitForVisiblity(total);
		return getAccessUtils().getBigDecimalMoneyAmount(total);
	}
	
	public Venue getVenueInfo() {
		explicitWaitForVisiblity(venueInfo);
		String text = getAccessUtils().getTextOfElement(venueInfo);
		Venue venue = new Venue();
		venue.setName(text.split(",")[0].trim());
		return venue;
	}
}