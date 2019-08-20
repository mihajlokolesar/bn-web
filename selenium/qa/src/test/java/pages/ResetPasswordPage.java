package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Constants;
import utils.MsgConstants;

public class ResetPasswordPage extends BasePage {

	@FindBy(id = "password")
	private WebElement newPasswordField;

	@FindBy(id = "confirmPassword")
	private WebElement confirmPasswordField;

	@FindBy(id = "confirmPassword-error-text")
	private WebElement confirmErrorText;

	@FindBy(xpath = "//main//form//button[@type='submit']")
	private WebElement resetButton;

	public ResetPasswordPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {

	}

	@Override
	public boolean isAtPage() {
		return explicitWait(10, 400, ExpectedConditions.urlContains(Constants.getResetPasswordBigNeon()));
	}

	public void fillForm(String password, String confirmPassword) {
		waitForTime(1500);
		explicitWait(10, 200, ExpectedConditions.visibilityOf(newPasswordField));
		newPasswordField.sendKeys(password);
		confirmPasswordField.sendKeys(confirmPassword);
	}

	public void clickResetButton() {
		resetButton.click();
	}

	public boolean isUnmatchedPasswordError() {
		String errorMsg = confirmErrorText.getText();
		if (errorMsg.contains(MsgConstants.RESET_PASS_UNMATCHED_PASS_ERROR)) {
			return true;
		} else {
			return false;
		}
	}

}
