package pages.mailinator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import utils.Constants;

public class MailinatorHomePage extends BasePage{
	
	@FindBy(id = "inboxfield")
	private WebElement searchBox;

	public MailinatorHomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.MAILINATOR_BASE_URL);
	}
	
	public void navigate() {
		driver.get(getUrl());
		explicitWait(10, 500, ExpectedConditions.urlToBe(getUrl()));
	}
	
	public void searchForUser(String userInboxName) {
		try {
			WebElement searchBox = driver.findElement(By.id("inboxfield"));
			explicitWait(4, ExpectedConditions.elementToBeClickable(searchBox));
			searchBox.sendKeys(userInboxName);
			WebElement clickGoButton = driver.findElement(By.xpath("//body//div//span/button[@id='go_inbox1']"));
			clickGoButton.click(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void checkIfOnUserInboxPage(String userInboxName) {
		explicitWait(10, 500, ExpectedConditions.urlContains(userInboxName));
		explicitWait(10, 1000, ExpectedConditions.presenceOfElementLocated(By.xpath("//table//thead//th[contains(text(),'From')]")));
		
	}
	
	
	

}
