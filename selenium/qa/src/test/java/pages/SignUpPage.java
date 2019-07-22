package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Constants;

public class SignUpPage extends BasePage {

	@FindBy(id = "first_name")
	private WebElement firstNameField;

	@FindBy(id = "last_name")
	private WebElement lastNameField;

	@FindBy(id = "email")
	private WebElement emailField;

	@FindBy(id = "password")
	private WebElement passwordField;

	@FindBy(id = "confirmPassword")
	private WebElement confirmPasswordField;

	@FindBy(css = ".g-recaptcha")
	private WebElement recaptchaToken;

	@FindBy(xpath = "//form//button[@type='submit']")
	private WebElement createAccountButton;

	public SignUpPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getSignUpBigNeon());
	}

	public void createAccount(String firstName, String lastName, String emailAddress, String password,
			String confirmPassword) {
		explicitWait(10, 500, ExpectedConditions.visibilityOf(firstNameField));
		firstNameField.sendKeys(firstName);
		lastNameField.sendKeys(lastName);
		emailField.sendKeys(emailAddress);
		passwordField.sendKeys(password);
		confirmPasswordField.sendKeys(confirmPassword);
		recaptchaToken.click();
		explicitWaitNoPooling(5, ExpectedConditions.elementToBeClickable(createAccountButton));
		createAccountButton.click();
	}

	public void navigate() {
		driver.get(getUrl());
	}
}
