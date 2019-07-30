package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.SignUpPage;
import pages.components.Header;
import utils.Constants;

public class SignUpStepsIT extends BaseSteps {

	private final String firstNameKey = "firstName";
	private final String lastNameKey = "lastName";
	private final String emailAddressKey = "emailAddress";
	private final String passwordKey = "pass";
	private final String passwordConfirmKey = "passConfirm";

	@Test
	public void singUpToBigNeon() {
		LoginPage loginPage = new LoginPage(driver);
		maximizeWindow();
		loginPage.navigate();
		loginPage.clickOnRegisterLink();
		SignUpPage signUpPage = new SignUpPage(driver);
		Map<String, String> map = generateRandomUser();
		signUpPage.createAccount(map.get(firstNameKey), map.get(lastNameKey), map.get(emailAddressKey),
				map.get(passwordKey), map.get(passwordConfirmKey));
		boolean retVal = false;
		try {
			retVal = new WebDriverWait(driver, 10, 200).until(ExpectedConditions.urlToBe(Constants.getBaseUrlBigNeon()));
			Header header = new Header(driver);
			header.logOut();
		} catch (Exception e) {
			retVal = false;
		}
		Assert.assertTrue(retVal);
	}

	private Map<String, String> generateRandomUser() {
		Random random = new Random();
		Integer ran = random.nextInt(1000000);
		String firstName = "seleniumtest";
		String lastName = "qaselenium";
		String emailAddress = firstName + ran + "@mailinator.com";
		String password = "seleniumpassword";
		String confirmPas = password;
		Map<String, String> map = new HashMap<>();
		map.put(firstNameKey, firstName);
		map.put(lastNameKey, lastName);
		map.put(emailAddressKey, emailAddress);
		map.put(passwordKey, password);
		map.put(passwordConfirmKey, confirmPas);
		return map;
	}

}
