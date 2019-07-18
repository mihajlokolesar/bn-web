package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FacebookLoginPage extends BasePage{
	
	
	
	@FindBy(id = "email")
	private WebElement emailOrPhoneField;
	
	@FindBy(id = "pass")
	private WebElement passwordField;

	@FindBy(id = "offline_access")
	private WebElement offlineAccess;
	
	@FindBy(id = "u_0_0")
	private WebElement loginButton;
	
	@Override
	public void presetUrl() {
	}
	
	
	public boolean loginToFacebook(String phoneOrMail, String password) {
		explicitWait(5, 250, ExpectedConditions.visibilityOf(emailOrPhoneField));
		emailOrPhoneField.sendKeys(phoneOrMail);
		passwordField.sendKeys(password);
		loginButton.click();
		boolean retVal = explicitWait(5, ExpectedConditions.numberOfWindowsToBe(1)); 
		return retVal;
	}
	
	public FacebookLoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	

}
