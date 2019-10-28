package pages.admin.organizations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BasePage;
import pages.components.admin.organization.AdminOrganizationComponent;
import utils.Constants;

public class AdminOrganizationsPage extends BasePage {

	@FindBy(xpath = "//body//main/div//a[@href='/admin/organizations/create']/button")
	private WebElement createOrganizationButton;
	
	private String organizationListContainer = "//a[@href='/admin/organizations/create']/following-sibling::div";
	
	public AdminOrganizationsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminOrganizations());
	}

	public void clickOnCreateOrganizationButton() {
		waitVisibilityAndBrowserCheckClick(createOrganizationButton);
	}
	
	public AdminOrganizationComponent findOrganizationWithName(String name) {
		WebElement organization = findOrganizationElementWithName(name);
		AdminOrganizationComponent orgComponent = new AdminOrganizationComponent(driver, organization);
		return orgComponent;
	}
	
	private WebElement findOrganizationElementWithName(String name) {
		By by = By.xpath(organizationListContainer + "/div[.//h1[contains(text(),'" + name + "')]]");
	    WebElement element = explicitWaitForVisibilityBy(by);
	    return element;
	}
}
