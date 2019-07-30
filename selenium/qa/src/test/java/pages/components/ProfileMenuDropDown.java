package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pages.BaseComponent;

public class ProfileMenuDropDown extends BaseComponent {
	
	@FindBy(xpath = "a[@href='/admin/events")
	private WebElement adminEvents;
	
	@FindBy(xpath = "a[@href='/my-events]")
	private WebElement myEvents;

	@FindBy(xpath = "a[@href='/orders']")
	private WebElement myOrders;

	@FindBy(xpath = "a[@href='/account']")
	private WebElement account;

	@FindBy(xpath = "/html/body//ul//li[contains(text(),'Logout')]")
	private WebElement logout;
	
	public ProfileMenuDropDown(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void myEventsClick() {
		myEvents.click();
	}

	public void myOrdersClick() {
		myOrders.click();
	}

	public void myAccountClick() {
		account.click();
	}

	public void logout() {
		explicitWaitForClickable(logout);
		logout.click();
	}

	

}
