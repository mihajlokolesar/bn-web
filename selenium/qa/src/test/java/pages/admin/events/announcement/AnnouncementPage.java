package pages.admin.events.announcement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.AnnouncementMail;
import pages.BasePage;

public class AnnouncementPage extends BasePage{
	
	@FindBy(id = "subject")
	private WebElement emailSubject;
	
	@FindBy(xpath = "//div[@class='DraftEditor-editorContainer']/div")
	private WebElement emailBody;
	
	@FindBy(xpath = "//p[contains(text(),'Need to make an announcement?')]/following-sibling::p")
	private WebElement announcementText;
	
	private final String  SEND_PREVIEW_BUTTON_LABEL = "Send a preview";
	private final String SEND_EMAIL_BUTTON_LABEL = "Send email";
	
	private final String ANNOUNCEMENT_TEXT_REG_EX = "Email all current ticket holders (0) to announce any major event updates including cancellation, postponement, rescheduled date/time, or new location. Disclaimer: Not intended for marketing purposes.";

	public AnnouncementPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {		
	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15,ExpectedConditions.urlMatches("/announcements$"));
	}
	
	public void enterMailContent(AnnouncementMail mail) {
		enterSubject(mail);
		enterBody(mail);
	}
	
	private void enterSubject(AnnouncementMail mail) {
		waitVisibilityAndClearFieldSendKeysF(emailSubject, mail.getSubject());
	}
	
	private void enterBody(AnnouncementMail mail) {
		if(!isContentPresent(mail.getBody())) {
			waitVisibilityAndSendKeys(emailBody, mail.getBody());
		}
	}
	
	private boolean isContentPresent(String content) {
		return isExplicitlyWaitVisible(2,
				By.xpath("//div[span[span[@data-text='true' and contains(text(),'" + content + "')]]]"));
	}
	
	public void clickOnSendPreviewButton() {
		clickOnButtonWithLabel(SEND_PREVIEW_BUTTON_LABEL);
	}
	
	public void clickOnSendEmailButton() {
		clickOnButtonWithLabel(SEND_EMAIL_BUTTON_LABEL);
	}
	
	public boolean isAnnouncementTextValid() {
		explicitWaitForVisiblity(announcementText);
		String splitRegex = "\\(\\+?\\d+\\)";
		String text = getAccessUtils().getTextOfElement(announcementText);
		String[] templateTokens = ANNOUNCEMENT_TEXT_REG_EX.split(splitRegex);
		String[] pageTokens = text.split(splitRegex);
		boolean isValid = true;
		if (pageTokens.length == templateTokens.length) {
			for (int i = 0; i < pageTokens.length; i++) {
				if (!pageTokens[i].equals(templateTokens[i])) {
					isValid = false;
				}
			}
			isValid = true;
		} else {
			isValid = false;
		}
		return isValid;
	}

}
