package pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.User;
import utils.Constants;
import utils.MsgConstants;

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

	@FindBy(xpath = "//div[@role='dialog']//form//button[@type='submit']")
	private WebElement forgotPasswordConfirmButton;

	@FindBy(xpath = "//div[@class='g-recaptcha']//iframe[contains(@src,'google')]")
	private WebElement recaptchaIframe;

	@FindBy(id = "recaptcha-anchor")
	private WebElement recaptchaAnchorInFrame;

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

	public void login(User user) {
		login(user.getEmailAddress(), user.getPass());
	}

	public void login(String username, String password) {
		navigate();
		loginWithoutNavigate(username, password);
	}

	public void loginWithoutNavigate(String username, String password) {
		explicitWaitForVisiblity(usernameField);
		usernameField.sendKeys(username);
		explicitWaitForVisiblity(passwordField);
		passwordField.sendKeys(password);
		clickOnRecaptcha();
		explicitWait(10, ExpectedConditions.elementToBeClickable(loginSubmitButton));
		loginSubmitButton.click();
		if (checkForLoginFailedMessage()) {
			loginSubmitButton.click();
		}

	}

	private boolean checkForLoginFailedMessage() {
		boolean retVal = false;
		try {
			explicitWait(3, ExpectedConditions.visibilityOf(dialogMessage));
			String msg = dialogMessage.getText();
			if (msg != null && !msg.isEmpty() && msg.contains(MsgConstants.LOGIN_FAILED_ERROR)) {
				retVal = true;
				System.out.println(msg);
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;

	}

	private void clickOnRecaptcha() {
		WebElement recaptcha = driver.findElement(By.className("g-recaptcha"));
		recaptcha.click();
		explicitWait(10, ExpectedConditions.frameToBeAvailableAndSwitchToIt(recaptchaIframe));
		explicitWait(10, ExpectedConditions.attributeContains(recaptchaAnchorInFrame, "aria-checked", "true"));
		driver.switchTo().parentFrame();
	}

	public boolean isMailOrPassIncorrectMessageDisplayed() {
		explicitWait(10, ExpectedConditions.visibilityOf(dialogMessage));
		String msg = dialogMessage.getText();
		if (msg.contains(MsgConstants.EMAIL_OR_PASS_INCORRECT_ON_LOGIN_ERROR)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean confirmedLogin(String username, String password) {
		login(username, password);
		boolean retVal = false;
		try {
			retVal = explicitWait(10, ExpectedConditions.urlMatches(Constants.getBaseUrlBigNeon()));
		} catch (TimeoutException e) {
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
		String currentWindowHandle = driver.getWindowHandle();
		if (parentWindowHandle.equalsIgnoreCase(currentWindowHandle)) {
			for (String handle : allWindows) {
				if (!parentWindowHandle.equalsIgnoreCase(handle)) {
					driver.switchTo().window(handle);

				}
			}
		}
		FacebookLoginPage fbLoginPage = new FacebookLoginPage(driver);
		if (fbLoginPage.loginToFacebook(phoneOrMail, password)) {
			driver.switchTo().window(parentWindowHandle);
			boolean retVal = explicitWait(10, 500, ExpectedConditions.urlToBe(Constants.getBaseUrlBigNeon()));
			return retVal;
		}
		return false;
	}

	public void clickOnForgotPassword() {
		explicitWait(10, ExpectedConditions.elementToBeClickable(forgotPasswordButton));
		forgotPasswordButton.click();

	}

	public void clickOnRegisterLink() {
		explicitWait(10, ExpectedConditions.elementToBeClickable(registerLink));
		registerLink.click();
	}

	public boolean enterMailAndClickOnResetPassword(String email) {
		explicitWait(10, 500, ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='dialog']//form")));
		forgotPasswordEmailField.sendKeys(email);
		forgotPasswordConfirmButton.click();
		dialogMessage = explicitWait(10, 500, ExpectedConditions.visibilityOf(dialogMessage));
		return true;
	}

}
