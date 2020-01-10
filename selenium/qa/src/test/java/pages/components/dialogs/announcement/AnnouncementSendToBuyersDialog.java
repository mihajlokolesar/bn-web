package pages.components.dialogs.announcement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.components.dialogs.DialogContainerComponent;

public class AnnouncementSendToBuyersDialog extends DialogContainerComponent{
	
	@FindBy(xpath = "//p[contains(text(),'Are you sure')]")
	private WebElement contentText;
	
	private final String CANCEL_BUTTON_LABEL = "Cancel";
	private final String SEND_EMAIL_BUTTON_LABEL = "Send Email";

	public AnnouncementSendToBuyersDialog(WebDriver driver) {
		super(driver);
	}
	
	public void clickOnCancelButton() {
		clickOnButtonWithLabel(CANCEL_BUTTON_LABEL);
	}
	
	public void clickOnSendMailButton() {
		clickOnButtonWithLabel(SEND_EMAIL_BUTTON_LABEL);
	}

}
