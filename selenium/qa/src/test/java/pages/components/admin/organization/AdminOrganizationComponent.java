package pages.components.admin.organization;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class AdminOrganizationComponent extends BaseComponent {
	
	private WebElement container;

	public AdminOrganizationComponent(WebDriver driver, WebElement container) {
		super(driver);
		this.container = container;
	}
	
	public void clickOnEditDetailsButton() {
		WebElement button = getEditDetailsLinkElement();
		waitVisibilityAndBrowserCheckClick(button);
	}
	
	public String getOrgId() {
		WebElement el = getEditDetailsLinkElement();
		String urlPath = el.getAttribute("href");
		return ProjectUtils.getId(urlPath, "/organizations/");
	}
	
	public WebElement getEditDetailsLinkElement() {
		WebElement el = SeleniumUtils.getChildElementFromParentLocatedBy(container, 
				By.xpath(".//a[button]"), driver);
		return el;
	}
}