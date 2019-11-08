package pages.components.admin.organization.settings;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import model.organization.FeesSchedule;
import pages.BaseComponent;
import pages.components.dialogs.FeeScheduleConfirmDialog;
import pages.interfaces.Visible;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class FeeScheduleComponent extends BaseComponent implements Visible {

	@FindBy(xpath = "//form[.//span[contains(text(),'Fee schedule name')]]")
	private WebElement feeScheeduleForm;

	@FindBy(id = "name")
	private WebElement name;

	@FindBys(@FindBy(xpath = "//form/button/preceding-sibling::div[not(span[contains(text(),'Fee schedule name')])]"))
	private List<WebElement> listOfRows;

	@FindBy(xpath = "//form//button[@type='submit' and span[contains(text(),'Update Fee Schedule')]]")
	private WebElement updateFeeScheedule;

	public FeeScheduleComponent(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean isVisible() {
		return isExplicitlyWaitVisible(5, feeScheeduleForm);
	}

	public void enterFeeScheduleName(String value) {
		if (value != null && !value.isEmpty()) {
			waitVisibilityAndSendKeys(name, value);
			waitForTime(1000);
		}
	}

	public void addNewRowAndFillFees(FeesSchedule feesSchedule) {
		FeeScheduleRow row = addNewFeesRow();
		row.fillForm(feesSchedule);
	}

	public void clickOnUpdateButton() {
		waitVisibilityAndBrowserCheckClick(updateFeeScheedule);
		FeeScheduleConfirmDialog dialog = new FeeScheduleConfirmDialog(driver);
		dialog.clickOnIAmSureUpdateButton();
	}

	public FeeScheduleRow addNewFeesRow() {
		if (listOfRows != null) {
			FeeScheduleRow row = removePreexistingRowsAndReturnLastExisting();
			row.clickOnAddNewRow();
			getAccessUtils().refreshElement(listOfRows);
			return getLastRow();
		} 
		return null;
	}
	
	private FeeScheduleRow removePreexistingRowsAndReturnLastExisting() {
		int size = listOfRows.size(); 
		FeeScheduleRow row = getLastRow();
		if (ProjectUtils.isNumberGreaterThan(size, 1)) {
			row.clickOnRemoveRow();
			row = getLastRow();
			return removePreexistingRowsAndReturnLastExisting();
		} else {
			return row;
		}
	}

	private FeeScheduleRow getLastRow() {
		getAccessUtils().refreshElement(listOfRows);
		WebElement rowContainer = listOfRows.get(listOfRows.size() - 1);
		FeeScheduleRow lastRow = new FeeScheduleRow(driver, rowContainer);
		return lastRow;
	}

	public class FeeScheduleRow extends BaseComponent {

		private WebElement container;

		private String relativeMinPriceId = "min_price";

		private String relativeMaxPriceId = "max_price";

		private String relativeClientFeeId = "client_fee";

		private String relativeBigNeonFeeId = "company_fee";

		private String relativeTotalFeeId = "total_fee";

		private String relativeAddNewRowButtonXpath = ".//button[span and @type='button'][last()]";

		private String relativeRemoveRowButtonXpath = ".//button[span and @type='button'][1]";

		public FeeScheduleRow(WebDriver driver, WebElement container) {
			super(driver);
			this.container = container;
		}

		public void fillForm(FeesSchedule feeSchedule) {
			enterMinimumPrice(feeSchedule.getMinimumPrice());
			enterClientFee(feeSchedule.getClientFee());
			enterBigNeonFee(feeSchedule.getBigNeonFee());
		}

		private BigDecimal getMinimumPrice() {
			BigDecimal value = SeleniumUtils.getBigDecimalValue(getMinimumPriceElement());
			return value;
		}

		public void enterMinimumPrice(BigDecimal value) {
			enterValue(getMinimumPriceElement(), value);
		}

		public void enterClientFee(BigDecimal value) {
			enterValue(getClientFeeElement(), value);
		}

		public void enterBigNeonFee(BigDecimal value) {
			enterValue(getBigNeonFeeElement(), value);
		}

		private void enterValue(WebElement element, BigDecimal value) {
			waitVisibilityAndClearFieldSendKeys(element, String.valueOf(value));
			waitForTime(700);
		}

		public void clickOnAddNewRow() {
			WebElement addElement = getAddNewRowButtonElement();
			waitVisibilityAndBrowserCheckClick(addElement);
		}

		public void clickOnRemoveRow() {
			WebElement removeElement = getRemoveRowButtonElement();
			if (removeElement != null) {
				waitVisibilityAndBrowserCheckClick(removeElement);
			}
		}

		private WebElement getMinimumPriceElement() {
			return getAccessUtils().getChildElementFromParentLocatedBy(container, By.id(relativeMinPriceId));
		}

		private WebElement getMaximumPriceElement() {
			return getAccessUtils().getChildElementFromParentLocatedBy(container, By.id(relativeMaxPriceId));
		}

		private WebElement getClientFeeElement() {
			return getAccessUtils().getChildElementFromParentLocatedBy(container, By.id(relativeClientFeeId));
		}

		private WebElement getBigNeonFeeElement() {
			return getAccessUtils().getChildElementFromParentLocatedBy(container, By.id(relativeBigNeonFeeId));
		}

		private WebElement getAddNewRowButtonElement() {
			return getAccessUtils().getChildElementFromParentLocatedBy(container,
					By.xpath(relativeAddNewRowButtonXpath));
		}

		private WebElement getRemoveRowButtonElement() {
			if (getAccessUtils().isChildElementVisibleFromParentLocatedBy(
					container, By.xpath(relativeRemoveRowButtonXpath), 5)) {
				return getAccessUtils().getChildElementFromParentLocatedBy(container,
						By.xpath(relativeRemoveRowButtonXpath));
			} else {
				return null;
			}
		}
	}
}