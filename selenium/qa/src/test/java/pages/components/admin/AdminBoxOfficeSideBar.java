package pages.components.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class AdminBoxOfficeSideBar extends BaseComponent{
	
	
	@FindBy(xpath = "//body//nav//a[@href='/box-office/sell']/div")
	private WebElement sellLink;
	
	@FindBy(xpath = "//body//nav//a[@href='/box-office/guests']/div")
	private WebElement guestsLink;

	public AdminBoxOfficeSideBar(WebDriver driver) {
		super(driver);
	}
	
	public void clickOnSellLink() {
		explicitWaitForVisibilityAndClickableWithClick(sellLink);
	}
	
	public void clickOnGuestLink() {
		explicitWaitForVisibilityAndClickableWithClick(guestsLink);
		waitForTime(2000);
	}

}
