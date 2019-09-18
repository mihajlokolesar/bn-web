package pages.components.admin.orders.manage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class OrderManageSearchHeader extends BaseComponent {
	
	@FindBy(xpath = "//main//div[div[p[text()='Order management']]]")
	private WebElement container;
	
	@FindBy(xpath = "//main//div/input[@name='Search']")
	private WebElement searchField;

	public OrderManageSearchHeader(WebDriver driver) {
		super(driver);
	}
	
	public void enterSearchValue(String value) {
		SeleniumUtils.clearInputField(searchField, driver);
		waitVisibilityAndSendKeys(searchField, value);
	}

}