package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TicketsSuccesPage extends BasePage{
	
	@FindBy(id = "phone")
	private WebElement mobileNumberField;

	@FindBy(xpath = "//main//form//button[@type='submit']")
	private WebElement sendMeTextButton;
	
	public TicketsSuccesPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
	}
	
	public boolean isAtPage() {
		return explicitWait(20, ExpectedConditions.urlContains("tickets/success?order_id")); 
	}
	
	public void enterPhoneNumberAndClickSend(String phoneNumber) {
		waitVisibilityAndSendKeysSlow(mobileNumberField, phoneNumber);
		waitVisibilityAndClick(sendMeTextButton);
	}

}
