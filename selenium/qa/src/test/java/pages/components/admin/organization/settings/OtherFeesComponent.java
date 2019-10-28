package pages.components.admin.organization.settings;

import java.math.BigDecimal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import enums.SettlementType;
import model.organization.OtherFees;
import pages.BaseComponent;
import pages.components.GenericDropDown;
import pages.interfaces.Visible;
import utils.SeleniumUtils;

public class OtherFeesComponent extends BaseComponent implements Visible {
	
	@FindBy(xpath = "//form[.//span[span[contains(text(),'Settlement types')]]]")
	private WebElement otherFeesForm;
	
	@FindBy(xpath = "//span[span[contains(text(),'Settlement types')]]/following-sibling::div/div[div[@role='button' and @aria-haspopup='true']]")
	private WebElement settlementTypeDropDownActivate;

	@FindBy(id = "menu-settlementType")
	private WebElement settlementTypeDropDownContainer;
	
	@FindBy(id = "clientEventFee")
	private WebElement perOrderClientFeeField;

	@FindBy(id = "companyEventFee")
	private WebElement perOrderBigNeonFeeField;

	@FindBy(id = "creditCardFeesPercent")
	private WebElement creditCardFeeField;

	@FindBy(xpath = "//form//button[@type='submit' and span[contains(text(),'Update')]]")
	private WebElement updateButton;

	public OtherFeesComponent(WebDriver driver) {
		super(driver);
	}
	
	public boolean isVisible() {
		return isExplicitlyWaitVisible(5, otherFeesForm);
	}
	
	public void fillForm(OtherFees otherFees) {
		enterSettlementTypes(otherFees.getSettlementType());
		enterPerOrderClientFee(otherFees.getPerOrderClientFee());
		enterPerOrderBigNeonFee(otherFees.getPerOrderBigNeonFee());
		enterCreditCardFee(otherFees.getCreditCardFee());
	}
	
	public void enterSettlementTypes(SettlementType settlementType) {
		GenericDropDown genericDropDown = new GenericDropDown(driver, settlementTypeDropDownActivate, settlementTypeDropDownContainer);
		genericDropDown.selectElementFromDropDownHiddenInput(dropDownXpathValue(settlementType.getValue()), settlementType.getLabel());
		waitForTime(1500);
	}
	
	private By dropDownXpathValue(String dataValue) {
		return By.xpath(".//ul//li[@data-value='" + dataValue + "']");
	}
	
	public void enterCreditCardFee(BigDecimal value) {
		if (value != null) {
			waitVisibilityAndClearFieldSendKeys(creditCardFeeField, String.valueOf(value));
			waitForTime(1000);
		}
	}
	
	public void enterPerOrderBigNeonFee(BigDecimal value) {
		if (value != null) {
			waitVisibilityAndClearFieldSendKeys(perOrderBigNeonFeeField, String.valueOf(value));
			waitForTime(1000);
		}
	}
	
	public void enterPerOrderClientFee(BigDecimal value) {
		if (value != null) {
			waitVisibilityAndClearFieldSendKeys(perOrderClientFeeField, String.valueOf(value));
			waitForTime(1000);
		}
		
	}
	
	public void clickOnUpdateButton() {
		waitVisibilityAndBrowserCheckClick(updateButton);
	}

}