package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class TransferTicketSenderMailFrame extends BaseComponent {
	
	@FindBy(xpath = "//td//div/a[contains(text(),'Cancel transfer')]")
	private WebElement cancelTransfer;
	
	public TransferTicketSenderMailFrame(WebDriver driver) {
		super(driver);
	}
	
	public boolean confirmReceiversMail(String receiversMail) {
		return isExplicitlyWaitVisible(By.xpath("//td//span[contains(text(),'" + receiversMail +"')]"));
		
	}
	
	public void clickOnCancelTransferTicket() {
		explicitWaitForVisiblity(cancelTransfer);
		String hrefLink = cancelTransfer.getAttribute("href");
		SeleniumUtils.openNewTabWithLink(hrefLink, driver);
	}
	
}
