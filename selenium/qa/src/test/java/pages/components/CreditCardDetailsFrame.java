package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class CreditCardDetailsFrame extends BaseComponent {

	@FindBy(name = "cardnumber")
	private WebElement cardNumberField;

	@FindBy(name = "exp-date")
	private WebElement expirationDateField;
	
	@FindBy(name = "cvc")
	private WebElement cvcField;

	@FindBy(name = "postal")
	private WebElement zipField;

	public CreditCardDetailsFrame(WebDriver driver) {
		super(driver);
	}

	public void enterCreditCardNumber(String number) {
		waitVisibilityAndSendKeysSlow(cardNumberField, number);
	}

	public void enterExpirationDate(String expirationDate) {
		waitVisibilityAndSendKeysSlow(expirationDateField, expirationDate);
	}

	public void enterCvc(String cvc) {
		waitVisibilityAndSendKeys(cvcField, cvc);
	}

	public void enterZip(String zip) {
		waitForTime(1000);
		waitVisibilityAndSendKeys(zipField, zip);
	}

}
