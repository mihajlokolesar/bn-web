package test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;
import pages.AccountPage;
import pages.LoginPage;
import pages.ResetPasswordPage;
import pages.mailinator.MailinatorHomePage;
import pages.mailinator.MailinatorInboxPage;

public class ForgotPasswordStepsIT extends BaseSteps {

	@Test(dataProvider = "reset_password")
	public void forgotPasswordFunctionallity(String email, String newPass, String confirmPass, boolean test)
			throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		driver.manage().window().maximize();
		loginPage.navigate();
		loginPage.clickOnForgotPassword();
		boolean mailSent = loginPage.enterMailAndClickOnResetPassword(email);
		Assert.assertEquals(true, mailSent);

		MailinatorHomePage mailinatorHomePage = new MailinatorHomePage(driver);
		mailinatorHomePage.navigate();
		String username = email.split("@")[0];
		mailinatorHomePage.searchForUser(username);
		mailinatorHomePage.checkIfOnUserInboxPage(username);
		MailinatorInboxPage inboxPage = new MailinatorInboxPage(driver);
		inboxPage.goToResetMail();
		inboxPage.clickOnResetPasswordLinkInMail();

		ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
		resetPasswordPage.fillForm(newPass, confirmPass);
		resetPasswordPage.clickResetButton();
		if (!newPass.equals(confirmPass)) {
			boolean isUnamatched = resetPasswordPage.isUnmatchedPasswordError();
			Assert.assertTrue(isUnamatched);
		}
		AccountPage accountPage = new AccountPage(driver);
		if (newPass.equals(confirmPass)) {
			boolean isAccountPage = accountPage.isAtPage();
			Assert.assertEquals(test, isAccountPage);

			accountPage.clickSave();
			boolean isAccountUpdated = accountPage.isAccountUpdatedMsg();
			Assert.assertEquals(test, isAccountUpdated);
		}
	}

	@DataProvider(name = "reset_password")
	public static Object[][] data() {
		return new Object[][] { 
			{ "bluetestneouser@mailinator.com", "test1111", "test2222", false },
			{ "bluetestneouser@mailinator.com", "test1111", "test1111", true } };
	}

}
