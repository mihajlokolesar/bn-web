package pages.components.admin.events.overview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;

public class TicketTypeEventOverviewComponent extends BaseComponent {

	private WebElement container;
	private String relativeTicketNameXpath = ".//div[p[contains(text(),'Ticket name')]]/following-sibling::div/p[1]";
	private String relativeQuantityXpath = ".//div[p[contains(text(),'Quantity')]]/following-sibling::div/p[2]";
	private String relativePriceXpath = ".//div[p[contains(text(),'Price')]]/following-sibling::div/p[3]";
	private String relativeSalesStartXpath = ".////div[p[contains(text(),'Sales start')]]/following-sibling::div/p[4]";
	private String relativeTypeOrSaleTimeXpath = ".//div[p[contains(text(),'Start time')] or p[contains(text(),'Ticket type')]]/following-sibling::div/p[1]";
//	private String 
	
	public TicketTypeEventOverviewComponent(WebDriver driver, WebElement container) {
		super(driver);
		this.container = container;
	}
	
	

}
