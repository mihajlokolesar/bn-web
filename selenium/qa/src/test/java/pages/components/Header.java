package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;

public class Header extends BasePage {
	
	@FindBy(css = "header form input")
	private WebElement searchEvents;

	@FindBy(xpath = "//body//header//span[@aria-owns='menu-appbar']/img[contains(@src,'down-active.svg')]")
	private WebElement profileOptions;
	
	public Header(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		
	}
	
	public void searchEvents(String event) {
		if (event != null) {
			searchEvents.sendKeys(event);
			searchEvents.submit();
		}
	}
	
	public WebElement openProfileOptions() {
		WebElement profileDropDownMenu = null;
		explicitWait(10, ExpectedConditions.elementToBeClickable(profileOptions));
		profileOptions.click();
		try {
			profileDropDownMenu = explicitWait(10, ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div[2]")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileDropDownMenu;
		
	}
	
	public void logOut() {
		openProfileOptions();
		ProfileMenuDropDown  profileMenu = new ProfileMenuDropDown(driver);
		profileMenu.logout();
	}
	
	public boolean checkLogedInFirstNameInHeader(String firstName) {
		String xpath = "/html/body/div[1]/div/header//h3[contains(text(),'"+firstName+"')]";
		WebElement name =  explicitWait(5, 200, ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		return true;
	}

}
