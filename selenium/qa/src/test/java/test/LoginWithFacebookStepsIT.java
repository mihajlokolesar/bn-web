package test;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;

public class LoginWithFacebookStepsIT extends BaseSteps {

	@Test(dataProvider = "user_fb_credentials")
	public void loginTestWithFacebook(String username, String password) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.navigate();
		AssertJUnit.assertTrue(loginPage.isAtPage());
		boolean isLogedIn = loginPage.loginWithFacebookUsingMail(username, password);
		AssertJUnit.assertTrue(isLogedIn);
		HomePage homePage = new HomePage(driver);
		AssertJUnit.assertTrue(homePage.isAtPage());
	}

	@DataProvider(name = "user_fb_credentials")
	public static Object[][] data() {
		return new Object[][] { { "tusertrqa@gmail.com", "test/1111/" } };
	}
}
