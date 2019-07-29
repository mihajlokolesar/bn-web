package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
		PageFactory.initElements(driver, this);
	}
	
	
	public void enterCreditCardNumber(String number) {
		explicitWait(10, ExpectedConditions.visibilityOf(cardNumberField));
		cardNumberField.sendKeys(number);
	}
	
	public void enterExpirationDate(String expirationDate) {
		explicitWait(10, ExpectedConditions.visibilityOf(expirationDateField));
		expirationDateField.sendKeys(expirationDate);
	}
	
	public void enterCvc(String cvc) {
		explicitWait(10, ExpectedConditions.visibilityOf(cvcField));
		cvcField.sendKeys(cvc);
	}
	
	public void enterZip(String zip) {
		explicitWait(10, ExpectedConditions.visibilityOf(zipField));
		zipField.sendKeys(zip);
	}

}
