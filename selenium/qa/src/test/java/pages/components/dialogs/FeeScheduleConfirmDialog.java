package pages.components.dialogs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FeeScheduleConfirmDialog extends DialogContainerComponent {
	
	@FindBy(xpath = "//button[span[contains(text(),'Cancel')]]")
	public WebElement cancelDialogButton;
	
	@FindBy(xpath = "//button[span[contains(text(),'I Am Sure')]]")
	public WebElement updateFeeSchedule;

	public FeeScheduleConfirmDialog(WebDriver driver) {
		super(driver);
	}
	
	public void clickOnIAmSureUpdateButton() {
		if (isVisible()) {
			waitVisibilityAndBrowserCheckClick(updateFeeSchedule);
		}
	}
}