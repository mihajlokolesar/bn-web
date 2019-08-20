package pages.components.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class TicketConfirmationDetails extends BaseComponent{

	@FindBy(xpath = "//body//div[div[div[p[span[text()='Ticket']]]]]")
	private WebElement ticketsContainer;
	
	private String relativeTicketQuantityAndArtistNameXpath = ".//div/div/div/p";
	
	public TicketConfirmationDetails(WebDriver driver) {
		super(driver);
	}
	
	public String getTicketQuantity() {
		explicitWaitForVisiblity(ticketsContainer);
		WebElement quantityInfoEl = SeleniumUtils.getChildElementFromParentLocatedBy(
				ticketsContainer, By.xpath(relativeTicketQuantityAndArtistNameXpath), driver);
		String text = quantityInfoEl.getText();
		String[] split = text.split(" x ");
		String quantity = split[0];
		return quantity;
	}
	
}
