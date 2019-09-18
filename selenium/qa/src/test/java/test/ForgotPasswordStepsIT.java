package test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import config.MailinatorEnum;
import junit.framework.Assert;
import model.User;
import pages.AccountPage;
import pages.LoginPage;
import pages.ResetPasswordPage;
import pages.mailinator.MailinatorFactory;
import pages.mailinator.inbox.ResetPasswordMailinatorPage;
import utils.DataConstants;
import utils.ProjectUtils;

public class ForgotPasswordStepsIT extends BaseSteps {

	@Test(dataProvider = "reset_password", priority = 3, retryAnalyzer = utils.RetryAnalizer.class)
	public void forgotPasswordFunctionallity(User user) {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.navigate();
		loginPage.clickOnForgotPassword();
		boolean mailSent = loginPage.enterMailAndClickOnResetPassword(user.getEmailAddress());
		Assert.assertEquals(true, mailSent);

		ResetPasswordMailinatorPage resetPassInbox = (ResetPasswordMailinatorPage) MailinatorFactory
				.getInboxPage(MailinatorEnum.RESET_PASSWORD, driver, user.getEmailAddress());
		resetPassInbox.clickOnResetPasswordLinkInMail();

		ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
		resetPasswordPage.fillForm(user.getPass(), user.getPassConfirm());
		resetPasswordPage.clickResetButton();
		if (!user.getPass().equals(user.getPassConfirm())) {
			boolean isUnamatched = resetPasswordPage.isUnmatchedPasswordError();
			Assert.assertTrue(isUnamatched);
		} else {
			AccountPage accountPage = new AccountPage(driver);

			boolean isAccountPage = accountPage.isAtPage();
			accountPage.clickSave();
			boolean isAccountUpdated = accountPage.isAccountUpdatedMsg();
			Assert.assertEquals(user.isTest(), isAccountUpdated && isAccountPage);

			accountPage.logOut();
		}

	}

	@DataProvider(name = "reset_password")
	public static Object[][] data() {
		Object[] users = User.generateUsersFromJson(DataConstants.FORGOT_PASS_CREDS_KEY);
		Object[][] data = ProjectUtils.composeData(new Object[users.length][1], users, 0);
		return data;
	}

}
