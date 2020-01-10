package pages.components.dialogs.announcement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.components.dialogs.DialogContainerComponent;

public class AnnouncementSendMailDialog extends DialogContainerComponent {
	
	@FindBy(xpath = "//h1[contains(text(),'Test Sent')]")
	private WebElement previewTitle;
	
	@FindBy(xpath = "//h1[contains(text(),'Email sent!')]")
	private WebElement announcementTitle;
	
	private final String CLOSE_BUTTON_LABEL = "Close";
	
	public AnnouncementSendMailDialog(WebDriver driver) {
		super(driver);
	}
	
	public boolean isPreviewTitleValid() {
		return isExplicitlyWaitVisible(previewTitle);
	}
	
	public boolean isAnnouncementTitleValid() {
		return isExplicitlyWaitVisible(announcementTitle);
	}
	
	public void clickOnCloseButton() {
		clickOnButtonWithLabel(CLOSE_BUTTON_LABEL);
	}

}
