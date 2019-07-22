package pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Constants;

public class LoginPage extends BasePage {

	@FindBy(id = "email")
	private WebElement usernameField;

	@FindBy(id = "password")
	private WebElement passwordField;
	
	@FindBy(xpath = "//a[@href='/sign-up']/button")
	private WebElement registerLink;

	@FindBy(xpath = "//form//button[span/img[contains(@src,'facebook')]]")
	private WebElement loginFacebook;
	
	@FindBy(xpath = "//form//button[@type='submit']")
	private WebElement loginSubmitButton;
	
	@FindBy(xpath = "//main//form/div[3]/button")
	private WebElement forgotPasswordButton;
	
	@FindBy(xpath = "//div[@role='dialog']//form//input[@id='email']")
	private WebElement forgotPasswordEmailField;
	
	@FindBy(xpath = "//div[@role='dialog']//form//button[@type='button']")
	private WebElement forgotPasswordCancelButton;
	
	@FindBy(xpath = "//div[@role='dialog']//form//buton[@type='submit']")
	private WebElement forgotPasswordConfirmButton;
	
	@FindBy(id = "message-id")
	private WebElement dialogMessage;

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getLoginUrlBigNeon());
	}

	public void navigate() {
		driver.get(getUrl());
	}

	public boolean login(String username, String password) {
		navigate();
		usernameField.sendKeys(username);
		passwordField.sendKeys(password);
		WebElement recaptcha = driver.findElement(By.className("g-recaptcha"));
		recaptcha.click();
		loginSubmitButton.click();
		boolean retVal = false;
		try {
			retVal = explicitWait(8, ExpectedConditions.stalenessOf(recaptcha));
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	public boolean loginWithFacebookUsingPhone(String phoneNumber, String password) {
		return loginWithFacebook(phoneNumber, password);
	}

	public boolean loginWithFacebookUsingMail(String mail, String password) {
		return loginWithFacebook(mail, password);
	}

	private boolean loginWithFacebook(String phoneOrMail, String password) {
		String parentWindowHandle = driver.getWindowHandle();
		loginFacebook.click();
		explicitWait(10, ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> allWindows = driver.getWindowHandles();
		for (String handle : allWindows) {
			if (!parentWindowHandle.equalsIgnoreCase(handle)) {
				driver.switchTo().window(handle);
				FacebookLoginPage fbLoginPage = new FacebookLoginPage(driver);
				if (fbLoginPage.loginToFacebook(phoneOrMail, password)) {
					driver.switchTo().window(parentWindowHandle);
					boolean retVal = explicitWait(10, 500, ExpectedConditions.urlToBe(Constants.getBaseUrlBigNeon()));
					return retVal;
				}
			}
		}
		return false;
	}
	
	public void clickOnForgotPassword() {
		explicitWait(5, ExpectedConditions.elementToBeClickable(forgotPasswordButton));
		forgotPasswordButton.click();
		
	}
	
	public void clickOnRegisterLink() {
		explicitWait(5, ExpectedConditions.elementToBeClickable(registerLink));
		registerLink.click();
	}
	
	public boolean enterMailAndClickOnResetPassword(String email) {
		explicitWait(10, 500, ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='dialog']//form")));
		forgotPasswordEmailField.sendKeys(email);
		forgotPasswordConfirmButton.click();
		boolean retVal = explicitWait(10, 500, ExpectedConditions.visibilityOf(dialogMessage));
		return retVal;
	}

}
