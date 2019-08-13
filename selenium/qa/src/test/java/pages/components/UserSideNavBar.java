package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class UserSideNavBar extends BaseComponent{
	
	@FindBy(xpath = "//body/div[@id='root']//nav//a[@href='/my-events']/div[@role='button']")
	private WebElement myEvents;
	
	@FindBy(xpath = "//body/div[@id='root']//nav//a[@href='/account']/div[@role='button']")
	private WebElement myAccount;
	
	@FindBy(xpath = "//body/div[@id='root']//nav//a[@href='/orders']/div[@role='button']")
	private WebElement myOrders;
	
	public UserSideNavBar(WebDriver driver) {
		super(driver);
	}

}
