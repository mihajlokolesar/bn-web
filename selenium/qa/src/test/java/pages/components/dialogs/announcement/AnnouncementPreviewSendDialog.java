package pages.components.dialogs.announcement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import model.AnnouncementMail;
import pages.components.dialogs.DialogContainerComponent;

public class AnnouncementPreviewSendDialog extends DialogContainerComponent {
	
	@FindBy(id = "email")
	private WebElement emailField;
	
	private final String CANCEL_BUTTON_LABEL = "Cancel";
	
	private final String SEND_PREVIEW_MAIL_BUTTON_LABEL = "Send preview email";

	public AnnouncementPreviewSendDialog(WebDriver driver) {
		super(driver);
	}
	
	public void enterEmail(AnnouncementMail mail) {
		waitVisibilityAndClearFieldSendKeysF(emailField, mail.getAddress());
	}
	
	public void clickOnCancelButton() {
		clickOnButtonWithLabel(CANCEL_BUTTON_LABEL);
	}
	
	public void clickOnSendPreviewMailButton() {
		clickOnButtonWithLabel(SEND_PREVIEW_MAIL_BUTTON_LABEL);
	}

}
