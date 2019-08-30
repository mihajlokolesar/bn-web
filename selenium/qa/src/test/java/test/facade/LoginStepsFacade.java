package test.facade;

import org.openqa.selenium.WebDriver;

import model.User;
import pages.HomePage;
import pages.LoginPage;
import pages.SignUpPage;

public class LoginStepsFacade extends BaseFacadeSteps {
	
	private LoginPage loginPage;
	private SignUpPage signUpPage;
	private HomePage homePage;

	public LoginStepsFacade(WebDriver driver) {
		super(driver);
		this.loginPage = new LoginPage(driver);
		this.signUpPage = new SignUpPage(driver);
		this.homePage = new HomePage(driver);
	}

	public LoginPage givenUserIsLogedIn(User user) {
		loginPage.confirmedLogin(user);
		return loginPage;
	}

	public LoginPage givenAdminUserIsLogedIn(User user) {
		loginPage.login(user);
		return loginPage;
	}
	
	public void givenUserIsOnLoginPage() {
		loginPage.navigate();
	}
	
	public boolean whenUserTiesToLogin(User user) {
		loginPage.login(user);
		boolean isLoggedIn = homePage.isExplicitAtPage(5);
		if (!isLoggedIn && loginPage.isMailOrPassIncorrectMessageDisplayed()) {
			return false;
		} else {
			return true;
		}
	}
	
	public void whenUserClicksOnRegisterLink() {
		loginPage.clickOnRegisterLink();
	}
	
	public void whenUserEntersRegistrationDataAndClicksOnCreateAccount(User user) {
		signUpPage.createAccount(user);
	}
	
	public boolean thenUserShouldBeOnHomePage() {
		return homePage.isExplicitAtPage(10);
	}
	
	public void logOut() {
		loginPage.logOut();
	}
}
