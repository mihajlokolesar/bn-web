package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.User;
import pages.LoginPage;
import utils.DataConstants;
import utils.DataReader;

public class LoginStepsIT extends BaseSteps {

	@Test(dataProvider = "user_credentials", priority = 1, retryAnalyzer = utils.RetryAnalizer.class)
	public void regularLogin(User user) {
		maximizeWindow();
		LoginPage loginPage = new LoginPage(driver);
		boolean retVal = loginPage.confirmedLogin(user.getEmailAddress(), user.getPass());
		loginPage.logOut();
		Assert.assertTrue(retVal);
	}

	@Test(dataProvider = "wrong_user_mail_credentials", priority = 1, retryAnalyzer = utils.RetryAnalizer.class)
	public void wrongEmailLogin(User user) {
		maximizeWindow();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(user.getEmailAddress(), user.getPass());
		Assert.assertTrue(loginPage.isMailOrPassIncorrectMessageDisplayed());
	}

	@Test(dataProvider = "wrong_pass_credentials", priority = 1, retryAnalyzer = utils.RetryAnalizer.class)
	public void wrongPasswordLogin(User user) {
		maximizeWindow();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(user.getEmailAddress(), user.getPass());
		Assert.assertTrue(loginPage.isMailOrPassIncorrectMessageDisplayed());
	}

	@DataProvider(name = "user_credentials")
	public static Object[][] data() {
		return new Object[][] { { User.generateUserFromJson(DataConstants.LOGIN_TEST_USER_CRED_KEY) } };
	}

	@DataProvider(name = "wrong_user_mail_credentials")
	public static Object[][] wrongMailCredentials() {
		return new Object[][] { { User.generateUserFromJson(DataConstants.LOGIN_TEST_WRONG_USER_CRED_KEY) } };
	}

	@DataProvider(name = "wrong_pass_credentials")
	public static Object[][] wrongPasswordCredentials() {
		return new Object[][] { { User.generateUserFromJson(DataConstants.LOGIN_TEST_WRONG_PASS_CRED_KEY) } };
	}
}
