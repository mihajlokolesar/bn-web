package pages.admin.organizations;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

	public CreateOrganizationPage clickOnCreateOrganizationButton() {
		explicitWait(15, ExpectedConditions.visibilityOf(createOrganizationButton));
		createOrganizationButton.click();
		return new CreateOrganizationPage(driver);
	}

}
