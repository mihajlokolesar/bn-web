package pages.admin.organizations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import pages.BasePage;
import pages.components.admin.organization.settings.FeeScheeduleComponent;
import pages.components.admin.organization.settings.OtherFeesComponent;
import pages.interfaces.Visible;
import utils.Constants;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class EditOrganizationPage extends BasePage {
	
	@FindBy(xpath = "//div[p[a[span[contains(text(),'Details')]]]]")
	private WebElement navContainer;
	
	private String orgId;
	
	private OrganizationSettingNavHeader navHeader;

	public EditOrganizationPage(WebDriver driver, String orgId) {
		super(driver);
		this.orgId = orgId;
		presetUrl();
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminOrganizations() + "/" + orgId);
	}
	
	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlMatches(getUrl()));
	}
	
	public String getId() {
		return this.orgId;
	}
	
	public OrganizationSettingNavHeader getSettingNavHeader() {
		if(navHeader == null) {
			explicitWaitForVisiblity(navContainer);
			OrganizationSettingNavHeader navHeader = new OrganizationSettingNavHeader(driver, navContainer);
			this.navHeader = navHeader;
			return navHeader;
		} else {
			return navHeader;
		}
	}
	
	public OtherFeesComponent getOtherFeesComponent() {
		OtherFeesComponent otherFees = new OtherFeesComponent(driver);
		return (OtherFeesComponent) ProjectUtils.getVisibleComponent(otherFees);
	}
	
	public FeeScheeduleComponent getFeeScheeduleComponent() {
		FeeScheeduleComponent feeScheedule = new FeeScheeduleComponent(driver);
		return (FeeScheeduleComponent) ProjectUtils.getVisibleComponent(feeScheedule);
	}
		
	public class OrganizationSettingNavHeader extends BaseComponent {
		
		private WebElement container;
		
		private String navDetailsNameValue = "Details";
		private String navTeamNameValue = "Team";
		private String navFeesNameValue = "Fees";
		private String navFeesScheeduleNameValue = "Fees schedule (Admin)";
		private String navOtherFeesNameValue = "Other fees (Admin)";

		public OrganizationSettingNavHeader(WebDriver driver, WebElement container) {
			super(driver);
			this.container = container;
		}
		
		public void clickOnDetails() {
			WebElement details = getNavElement(navDetailsNameValue);
			waitVisibilityAndBrowserCheckClick(details);
		}
		
		public void clickOnFeesSchedule() {
			WebElement feesScheedule = getNavElement(navFeesScheeduleNameValue);
			waitVisibilityAndBrowserCheckClick(feesScheedule);
		}
		
		public void clickOnOtherFees() {
			WebElement otherFees = getNavElement(navOtherFeesNameValue);
			waitVisibilityAndBrowserCheckClick(otherFees);
		}
		
		private WebElement getNavElement(String name) {
			WebElement el =SeleniumUtils.getChildElementFromParentLocatedBy(container, 
					By.xpath("./p/a[span[text()='" + name + "']]"), driver);
			return el;
		}
	}
}