package test;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.LoginPage;

public class LoginStepsIT extends BaseSteps {

	private LoginPage loginPage;
	

	@Test(dataProvider = "user_credentials")
	public void regularLogin(String username, String password) {
		loginPage = new LoginPage(driver);
		AssertJUnit.assertTrue(loginPage.login(username, password));

	}
	
	@DataProvider(name = "user_credentials")
	public static Object[][] data() {
		return new Object[][] { { "testuser@mailnator.com", "test1111" } };
	}
}
