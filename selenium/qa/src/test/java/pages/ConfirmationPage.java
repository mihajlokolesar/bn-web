package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConfirmationPage extends BasePage{
	
	
	@FindBy(xpath = "//main//a[./span[contains(text(),'Change tickets')]]")
	public WebElement changeTickets;
	
	@FindBy(xpath = "//main//form//p[contains(text(),'Payment details')]")
	public WebElement paymentDetails;

	public ConfirmationPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
	}
	
	

}
