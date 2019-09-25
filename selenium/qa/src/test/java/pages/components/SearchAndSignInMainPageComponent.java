package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class SearchAndSignInMainPageComponent extends BaseComponent{

	@FindBy(xpath = "//a[not(ancestor::header) and @href='/login']/button[last()]")
	private WebElement signInButton;
	
	@FindBy(xpath = "//div/p[contains(text(),'Future of Ticketing')]/following-sibling::div/form/input")
	private WebElement searchField;
	
	@FindBy(xpath = "//div[not(ancestor::header)]/span[contains(text(),'Welcome back')]")
	private WebElement profileOptions;
	
	public SearchAndSignInMainPageComponent(WebDriver driver) {
		super(driver);
	}
	
	public void searchForEvents(String value) {
		waitVisibilityAndSendKeys(searchField, value);
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

}