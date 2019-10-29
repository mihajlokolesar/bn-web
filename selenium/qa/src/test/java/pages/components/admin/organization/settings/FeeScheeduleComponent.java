package pages.components.admin.organization.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import pages.interfaces.Visible;

public class FeeScheeduleComponent extends BaseComponent implements Visible {

	@FindBy(xpath = "//form[.//span[contains(text(),'Fee schedule name')]]")
	private WebElement feeScheeduleForm;

	@FindBy(id = "name")
	private WebElement name;

	@FindBy(id = "min_price")
	private WebElement minPrice;

	@FindBy(id = "max_price")
	private WebElement maxPrice;

	@FindBy(id = "client_fee")
	private WebElement clientFee;

	@FindBy(id = "company_fee")
	private WebElement companyFee;

	@FindBy(id = "total_fee")
	private WebElement totalFee;

	@FindBy(xpath = "//form//button[@type='button']")
	private WebElement buttonAddAnotherFee;

	@FindBy(xpath = "//form//button[@type='submit' and span[contains(text(),'Update Fee Schedule')]]")
	private WebElement updateFeeScheedule;

	public FeeScheeduleComponent(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean isVisible() {
		return isExplicitlyWaitVisible(5, feeScheeduleForm);
	}
}