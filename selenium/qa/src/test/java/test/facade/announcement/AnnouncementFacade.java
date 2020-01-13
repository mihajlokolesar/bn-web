package test.facade.announcement;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.AnnouncementMail;
import pages.admin.events.announcement.AnnouncementPage;
import pages.components.dialogs.announcement.AnnouncementPreviewSendDialog;
import pages.components.dialogs.announcement.AnnouncementSendMailDialog;
import pages.components.dialogs.announcement.AnnouncementSendToBuyersDialog;
import test.facade.BaseFacadeSteps;

public class AnnouncementFacade extends BaseFacadeSteps{
	
	private AnnouncementPage announcementPage;

	public AnnouncementFacade(WebDriver driver) {
		super(driver);
		this.announcementPage = new AnnouncementPage(driver);
	}
	
	public boolean isOnAnnouncementPage() {
		return this.announcementPage.isAtPage();
	}
	
	public void sendPreviewMail(AnnouncementMail mail) {
		announcementPage.enterMailContent(mail);
		announcementPage.clickOnSendPreviewButton();
		AnnouncementPreviewSendDialog dialog = new AnnouncementPreviewSendDialog(driver);
		dialog.enterEmail(mail);
		dialog.clickOnSendPreviewMailButton();
		AnnouncementSendMailDialog sentDialog = new AnnouncementSendMailDialog(driver);
		Assert.assertTrue(sentDialog.isPreviewTitleValid(), "Not on preview sent mail confirmation dialog");
		sentDialog.clickOnCloseButton();
	}
	
	public void sendEmailToBuyers(AnnouncementMail mail) {
		announcementPage.enterMailContent(mail);
		announcementPage.clickOnSendEmailButton();
		AnnouncementSendToBuyersDialog buyersDialog = new AnnouncementSendToBuyersDialog(driver);
		buyersDialog.clickOnSendMailButton();
		AnnouncementSendMailDialog sentDialog = new AnnouncementSendMailDialog(driver);
		Assert.assertTrue(sentDialog.isAnnouncementTitleValid());
		sentDialog.clickOnCloseButton();
	}
	
	public boolean isAnnouncementTextValid() {
		return announcementPage.isAnnouncementTextValid();
	}
	
	@Override
	protected void setData(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object getData(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
