package pages.admin.organizations;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BasePage;
import utils.Constants;

public class AdminOrganizationsPage extends BasePage {

	@FindBy(xpath = "//body//main/div//a[@href='/admin/organizations/create']/button")
	private WebElement createOrganizationButton;

	public AdminOrganizationsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminOrganizations());
	}

	public void clickOnCreateOrganizationButton() {
		explicitWaitForVisibilityAndClickableWithClick(createOrganizationButton);
	}

}
