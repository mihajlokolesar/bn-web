package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.User;
import pages.HomePage;
import pages.LoginPage;
import utils.DataConstants;
import utils.DataReader;

public class LoginWithFacebookStepsIT extends BaseSteps {

	@Test(dataProvider = "user_fb_credentials", priority = 2 , retryAnalyzer = utils.RetryAnalizer.class)
	public void loginTestWithFacebook(User user) {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.navigate();
		Assert.assertTrue(loginPage.isAtPage());
		boolean isLogedIn = loginPage.loginWithFacebookUsingMail(user.getEmailAddress(), user.getPass());
		Assert.assertTrue(isLogedIn);
		HomePage homePage = new HomePage(driver);
		Assert.assertTrue(homePage.isAtPage());
		homePage.logOut();
	}

	@DataProvider(name = "user_fb_credentials")
	public static Object[][] data() {
		return new Object[][] {{User.generateUserFromJson(DataConstants.LOGIN_FB_CRED_KEY)}};
	}
}
