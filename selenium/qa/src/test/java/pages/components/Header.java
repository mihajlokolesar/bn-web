package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import pages.BasePage;

public class Header extends BaseComponent {

	@FindBy(css = "header form input")
	private WebElement searchEvents;

	@FindBy(xpath = "//body//header//span[@aria-owns='menu-appbar']/img[contains(@src,'down-active.svg')]")
	private WebElement profileOptionsLowResolution;

	@FindBy(xpath = "//header//div/span[contains(text(),'Welcome back')]")
	private WebElement profileOptions;

	@FindBy(xpath = "//body//header//a[@href='/box-office/sell']/div")
	private WebElement boxOffice;
	
	@FindBy(xpath = "//body//header//a[@href='/admin/events']/div")
	private WebElement toStudioButton;

	public Header(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void searchEvents(String event) {
		if (event != null) {
			searchEvents.sendKeys(event);
			searchEvents.submit();
		}
	}
	
	public void clickOnBoxOfficeLink() {
		explicitWaitForVisiblity(boxOffice);
		boxOffice.click();
	}
	
	public void clickOnToStudioLink() {
		explicitWaitForVisiblity(toStudioButton);
		toStudioButton.click();
	}

	public WebElement openProfileOptions() {
		WebElement profileDropDownMenu = null;
		explicitWait(10, ExpectedConditions.elementToBeClickable(profileOptions));
		profileOptions.click();
		try {
			profileDropDownMenu = explicitWait(10,
					ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div[2]")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileDropDownMenu;

	}
	
	public void logOut() {
		openProfileOptions();
		ProfileMenuDropDown profileMenu = new ProfileMenuDropDown(driver);
		profileMenu.logout();
	}

	public boolean checkLogedInFirstNameInHeader(String firstName) {
		String xpath = "/html/body/div[1]/div/header//h3[contains(text(),'" + firstName + "')]";
		WebElement name = explicitWait(5, 200, ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		return true;
	}

}
