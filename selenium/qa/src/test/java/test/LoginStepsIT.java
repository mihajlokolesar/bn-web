package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.LoginPage;

public class LoginStepsIT extends BaseSteps {

	@Test(dataProvider = "user_credentials")
	public void regularLogin(String username, String password) {
		LoginPage loginPage = new LoginPage(driver);
		boolean retVal = loginPage.login(username, password);
		Assert.assertTrue(retVal);
	}
	
	@DataProvider(name = "user_credentials")
	public static Object[][] data() {
		return new Object[][] { { "testuser@mailinator.com", "test1111" } };
	}
}
