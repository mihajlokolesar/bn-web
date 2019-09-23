package pages.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class ManageOrdersTicketRowComp extends BaseComponent {
	
	private WebElement row;
	
	private String relativeCheckboxXPath = "./div/span[1]/span/div";
	
	public ManageOrdersTicketRowComp(WebDriver driver, WebElement row) {
		super(driver);
		this.row = row;
	}
	
	public void clickOnCheckBox() {
		WebElement checkbox = SeleniumUtils.getChildElementFromParentLocatedBy(row, By.xpath(relativeCheckboxXPath), driver);
		explicitWaitForVisibilityAndClickableWithClick(checkbox);
	}
	

}
