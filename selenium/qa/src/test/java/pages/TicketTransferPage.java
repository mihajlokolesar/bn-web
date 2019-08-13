package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.User;
import utils.Constants;

public class TicketTransferPage extends BasePage {
	
	@FindBy(xpath = "//main//button[@type='button' and span[contains(text(),'Continue with email')]]")
	private WebElement continueWithEmail;
	
	@FindBy(xpath = "//main//button[@type='button' and span[contains(text(),'Continue with facebook')]]")
	private WebElement continueWithFacebook;
	
	@FindBy(xpath = "//main//div/button[span[contains(text(),concat('Let',\"'\",'s do this'))]]")
	private WebElement letsDoThisButton;
	
	public TicketTransferPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
	}
	
	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlContains(Constants.getBaseUrlBigNeon() + "/tickets/transfer"));
	}
	
	public SignUpPage clickOnContinueWithEmail() {
		explicitWaitForVisibilityAndClickableWithClick(continueWithEmail);
		return new SignUpPage(driver);
	}
	
	public void clickOnLetsDoIt() {
		explicitWaitForVisibilityAndClickableWithClick(letsDoThisButton);
	}
	
	public boolean checkIfCorrectUser(User user) {
		return isExplicitlyWaitVisible(By.xpath("//main//p[contains(text(),'" + user.getEmailAddress() + "')]"));
	}

}
