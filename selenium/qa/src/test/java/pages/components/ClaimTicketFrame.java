package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class ClaimTicketFrame extends BaseComponent {
	
	@FindBy(xpath = "//body//table//a[contains(text(),'Claim Tickets')]")
	private WebElement claimTicketLink;

	public ClaimTicketFrame(WebDriver driver) {
		super(driver);
	}
		
	public void clickOnClaimTicketLink() {
		waitVisibilityAndClick(claimTicketLink);
	}
	
}
