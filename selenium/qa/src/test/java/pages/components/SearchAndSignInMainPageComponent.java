package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class SearchAndSignInMainPageComponent extends BaseComponent{

	@FindBy(xpath = "//a[not(ancestor::header) and @href='/login']/button[last()]")
	private WebElement signInButton;
	
	@FindBy(xpath = "//form[not(ancestor::header)]/div[img[@alt='Search icon']]/input")
	private WebElement searchField;
	
	@FindBy(xpath = "//div[not(ancestor::header)]/span[contains(text(),'Welcome back')]")
	private WebElement profileOptions;
	
	public SearchAndSignInMainPageComponent(WebDriver driver) {
		super(driver);
	}
	
	public void searchForEvents(String value) {
		waitVisibilityAndClearFieldSendKeysF(searchField, value);
		searchField.submit();
	}
	
	public void clickOnSignIn() {
		explicitWaitForVisibilityAndClickableWithClick(signInButton);
	}
	
	public void openProfileOptions() {
		explicitWaitForVisibilityAndClickableWithClick(profileOptions);
	}
	
	public boolean isSignInButtonVisible() {
		return isExplicitlyWaitVisible(4, signInButton);
	}
	
	public boolean isSearchFieldVisible() {
		return isExplicitlyWaitVisible(4, searchField);
	}

}