package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class ProfileMenuDropDown extends BaseComponent {
	
	@FindBy(xpath = "//body/div[@id='menu-appbar']//ul/a[@href='/admin/events']/li")
	private WebElement adminEvents;
	
	@FindBy(xpath = "//body/div[@id='menu-appbar']//ul/a[@href='/my-events']/li")
	private WebElement myEvents;

	@FindBy(xpath = "//body/div[@id='menu-appbar']//ul/a[@href='/orders']/li")
	private WebElement myOrders;

	@FindBy(xpath = "//body/div[@id='menu-appbar']//ul/a[@href='/account']/li")
	private WebElement account;

	@FindBy(xpath = "/html/body//ul//li[contains(text(),'Logout')]")
	private WebElement logout;
	
	@FindBy(xpath = "//body/div[@id='menu-appbar']")
	private WebElement profileMenuDropDownContainer;
	
	public ProfileMenuDropDown(WebDriver driver) {
		super(driver);
	}

	public void myEventsClick() {
		explicitWaitForVisibilityAndClickableWithClick(myEvents);
	}

	public void myOrdersClick() {
		explicitWaitForVisibilityAndClickableWithClick(myOrders);
	}

	public void myAccountClick() {
		explicitWaitForVisibilityAndClickableWithClick(account);
	}

	public void logout() {
		explicitWaitForClickable(logout);
		logout.click();
	}

	

}
