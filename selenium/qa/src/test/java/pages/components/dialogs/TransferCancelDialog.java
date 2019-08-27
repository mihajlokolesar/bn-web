package pages.components.dialogs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class TransferCancelDialog extends DialogContainerComponent {

	@FindBy(xpath = "//div/button[span[contains(text(),'Confirm')]]")
	private WebElement continueWithCancelTransferButton;

	@FindBy(xpath = "//div/button[span[contains(text(),'Go back')]]")
	private WebElement cancelActionButton;
	
	@FindBy(xpath = "//div//button[span[contains(text(),'Got it!')]]")
	private WebElement gotItButton;

	public TransferCancelDialog(WebDriver driver) {
		super(driver);
	}

	public boolean isDialogVisible() {
		waitForTime(1500);
		return isVisible();
	}

	public void clickOnContinueWithTransfer() {
		if (isDialogVisible()) {
			explicitWaitForVisibilityAndClickableWithClick(continueWithCancelTransferButton);
		}
	}

	public void clickOnGoBack() {
		if (isDialogVisible()) {
			explicitWaitForVisibilityAndClickableWithClick(cancelActionButton);
		}
	}
	
	public void clickOnGotItButton() {
		if(isDialogVisible()) {
			explicitWaitForVisibilityAndClickableWithClick(gotItButton);
		}
	}

}
