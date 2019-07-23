package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Constants;
import utils.MsgConstants;

public class AccountPage extends BasePage {

	@FindBy(xpath = "//html//body//main//form//button[.//span[contains(text(),'Save')]]")
	private WebElement saveButton;

	@FindBy(id = "message-id")
	private WebElement dialogMessage;

	public AccountPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);

	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAccountBigNeon());
	}

	public void clickSave() {
		saveButton.click();
	}

	public boolean isAccountUpdatedMsg() {
		explicitWait(10, ExpectedConditions.visibilityOf(dialogMessage));
		String msg = dialogMessage.getText();
		if (msg.contains(MsgConstants.ACCOUNT_UPDATED_NOTIFICATION)) {
			return true;
		} else {
			return false;
		}
	}

}
