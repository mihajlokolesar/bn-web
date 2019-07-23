package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.LoginPage;

public class LoginStepsIT extends BaseSteps {

	@Test(dataProvider = "user_credentials")
	public void regularLogin(String username, String password) {
		maximizeWindow();
		LoginPage loginPage = new LoginPage(driver);
		boolean retVal = loginPage.confirmedLogin(username, password);
		Assert.assertTrue(retVal);
	}
	
	@Test(dataProvider = "wrong_user_mail_credentials")
	public void wrongEmailLogin(String username, String password) {
		maximizeWindow();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		Assert.assertTrue(loginPage.isMailOrPassIncorrectMessageDisplayed());
	}
	
	@Test(dataProvider = "wrong_pass_credentials")
	public void wrongPasswordLogin(String username, String password) {
		maximizeWindow();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		Assert.assertTrue(loginPage.isMailOrPassIncorrectMessageDisplayed());
	}
	
	@DataProvider(name = "user_credentials")
	public static Object[][] data() {
		return new Object[][] { { "testuser@mailinator.com", "test1111" } };
	}
	
	@DataProvider(name = "wrong_user_mail_credentials")
	public static Object[][] wrongMailCredentials() {
		return new Object[][] {
			{"wrongmailuserblue@mailinator.com", "test1111"}
		};
	}
	
	@DataProvider(name = "wrong_pass_credentials")
	public static Object[][] wrongPasswordCredentials() {
		return new Object[][] {
			{"testuser@mailinator.com","wrongpas"}
		};
	}
}
