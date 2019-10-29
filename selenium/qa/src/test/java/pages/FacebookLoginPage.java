package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FacebookLoginPage extends BasePage {

	@FindBy(id = "email")
	private WebElement emailOrPhoneField;

	@FindBy(id = "pass")
	private WebElement passwordField;

	@FindBy(id = "offline_access")
	private WebElement offlineAccess;

	@FindBy(id = "u_0_0")
	private WebElement loginButton;
	
	public FacebookLoginPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
	}

	public boolean loginToFacebook(String phoneOrMail, String password) {
		explicitWait(10, 250, ExpectedConditions.visibilityOf(emailOrPhoneField));
		emailOrPhoneField.sendKeys(phoneOrMail);
		passwordField.sendKeys(password);
		waitVisibilityAndBrowserCheckClick(loginButton);
		try {
			WebElement firstTimeLoginConfirmButton = explicitWait(10, ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//form//tbody//button[@name='__CONFIRM__']")));
			waitVisibilityAndBrowserCheckClick(firstTimeLoginConfirmButton);
		} catch (Exception e) {
		}
		boolean retVal = explicitWait(5, ExpectedConditions.numberOfWindowsToBe(1));
		return retVal;
	}
}
