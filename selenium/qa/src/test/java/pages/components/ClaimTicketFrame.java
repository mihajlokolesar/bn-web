package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BaseComponent;
import utils.SeleniumUtils;

public class ClaimTicketFrame extends BaseComponent {

	public ClaimTicketFrame(WebDriver driver) {
		super(driver);
	}

	public void clickOnClaimTicketLink() {
		waitForTime(2000);
		WebElement claimTicketButton = explicitWaitForVisiblity(
				driver.findElement(By.xpath("//table//a[contains(text(),'Claim Tickets')]")));
		String hrefLink = claimTicketButton.getAttribute("href");
		SeleniumUtils.openNewTabWithLink(hrefLink, driver);
	}
}
