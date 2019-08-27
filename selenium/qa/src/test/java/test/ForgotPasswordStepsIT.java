package test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import config.MailinatorEnum;
import junit.framework.Assert;
import pages.AccountPage;
import pages.LoginPage;
import pages.ResetPasswordPage;
import pages.mailinator.MailinatorFactory;
import pages.mailinator.inbox.ResetPasswordMailinatorPage;

public class ForgotPasswordStepsIT extends BaseSteps {

	@Test(dataProvider = "reset_password", priority = 3, retryAnalyzer = utils.RetryAnalizer.class)
	public void forgotPasswordFunctionallity(String email, String newPass, String confirmPass, boolean test) {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.navigate();
		loginPage.clickOnForgotPassword();
		boolean mailSent = loginPage.enterMailAndClickOnResetPassword(email);
		Assert.assertEquals(true, mailSent);

		ResetPasswordMailinatorPage resetPassInbox = (ResetPasswordMailinatorPage) MailinatorFactory
				.getInboxPage(MailinatorEnum.RESET_PASSWORD, driver, email);
		resetPassInbox.clickOnResetPasswordLinkInMail();

		ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
		resetPasswordPage.fillForm(newPass, confirmPass);
		resetPasswordPage.clickResetButton();
		if (!newPass.equals(confirmPass)) {
			boolean isUnamatched = resetPasswordPage.isUnmatchedPasswordError();
			Assert.assertTrue(isUnamatched);
		} else {
			AccountPage accountPage = new AccountPage(driver);

			boolean isAccountPage = accountPage.isAtPage();
			accountPage.clickSave();
			boolean isAccountUpdated = accountPage.isAccountUpdatedMsg();
			Assert.assertEquals(test, isAccountUpdated && isAccountPage);

			accountPage.logOut();
		}

	}

	@DataProvider(name = "reset_password")
	public static Object[][] data() {
		return new Object[][] { 
				{ "bluetestneouser@mailinator.com", "test1111", "test2222", false },
				{ "bluetestneouser@mailinator.com", "test1111", "test1111", true } };
	}

}
