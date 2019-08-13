package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;

public class LoginWithFacebookStepsIT extends BaseSteps {

	@Test(dataProvider = "user_fb_credentials", priority = 3)
	public void loginTestWithFacebook(String username, String password) {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.navigate();
		Assert.assertTrue(loginPage.isAtPage());
		boolean isLogedIn = loginPage.loginWithFacebookUsingMail(username, password);
		Assert.assertTrue(isLogedIn);
		HomePage homePage = new HomePage(driver);
		Assert.assertTrue(homePage.isAtPage());
		homePage.logOut();
	}

	@DataProvider(name = "user_fb_credentials")
	public static Object[][] data() {
		return new Object[][] { { "tusertrqa@gmail.com", "test/1111/" } };
	}
}
