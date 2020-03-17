package pages.components.admin.venues;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class AdminVenueComponent extends BaseComponent {

	private WebElement component;
	
	private String relativeEditDetailsButtonXpath = ".//div/a/button[@type='button' and span[contains(text(),'Edit details')]]";
	
	private String relativeNameXpath = "./div/div/h1";

	public AdminVenueComponent(WebDriver driver, WebElement component) {
		super(driver);
		this.component = component;
	}
	
	public String getVenueName() {
		WebElement nameEl = getVenueNameElement();
		String text = nameEl.getText();
		return text;
	}
	
	public void clickOnEditButton() {
		WebElement editButton = findEditButtonElement();
		waitVisibilityAndBrowserCheckClick(editButton);
	}
	
	public String getVenueHref() {
		WebElement button = findEditButtonElement();
		WebElement hrefEl = explicitWait(15, ExpectedConditions.visibilityOf(button.findElement(By.xpath("./.."))));
		String href = hrefEl.getAttribute("href");
		return href;
	}
	
	private WebElement getVenueNameElement() {
		return SeleniumUtils.getChildElementFromParentLocatedBy(component, By.xpath(relativeNameXpath), driver);
	}
	
	private WebElement findEditButtonElement() {
		return SeleniumUtils.getChildElementFromParentLocatedBy(component, By.xpath(relativeEditDetailsButtonXpath), driver);
	}

}