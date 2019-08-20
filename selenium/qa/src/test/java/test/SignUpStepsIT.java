package test;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.User;
import pages.LoginPage;
import pages.SignUpPage;
import utils.Constants;

public class SignUpStepsIT extends BaseSteps {

	@Test(dataProvider = "new_user_data", priority = 3)
	public void singUpToBigNeon(User user) {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.navigate();
		loginPage.clickOnRegisterLink();
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.createAccount(user);
		boolean retVal = false;
		try {
			retVal = new WebDriverWait(driver, 10, 200).until(ExpectedConditions.urlToBe(Constants.getBaseUrlBigNeon()));
			signUpPage.logOut();
		} catch (Exception e) {
			retVal = false;
		}
		Assert.assertTrue(retVal);
	}

	@DataProvider(name = "new_user_data")
	public static Object[][] dataProvider() {
		User user = User.generateRandomUser();
		return new Object[][] {{user}};
	}

}
