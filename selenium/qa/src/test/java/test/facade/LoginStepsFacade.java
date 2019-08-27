package test.facade;

import org.openqa.selenium.WebDriver;

import model.User;
import pages.LoginPage;

public class LoginStepsFacade extends BaseFacadeSteps {
	
	private LoginPage loginPage;

	public LoginStepsFacade(WebDriver driver) {
		super(driver);
		this.loginPage = new LoginPage(driver);
	}

	public LoginPage givenUserIsLogedIn(User user) {
		loginPage = new LoginPage(driver);
		loginPage.confirmedLogin(user);
		return loginPage;
	}

	public LoginPage givenAdminUserIsLogedIn(User user) {
		loginPage = new LoginPage(driver);
		loginPage.login(user);
		return loginPage;
	}
}
