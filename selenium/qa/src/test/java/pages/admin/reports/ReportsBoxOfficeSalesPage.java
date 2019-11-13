package pages.admin.reports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import pages.BasePage;
import utils.Constants;
import utils.ProjectUtils;

public class ReportsBoxOfficeSalesPage extends BasePage {

	@FindBy(xpath = "//p[contains(text(),'Grand total')]/following-sibling::div[1]/div/div[p[contains(text(),'Cash')]]/p[2]")
	private WebElement grandTotalCashValue;

	@FindBy(xpath = "//p[contains(text(),'Grand total')]/following-sibling::div[1]/div/div[p[contains(text(),'CreditCard')]]/p[2]")
	private WebElement grandTotalCreditCardValue;

	@FindBy(xpath = "//p[contains(text(),'Grand total')]/following-sibling::div[1]/div[p[contains(text(),'Grand total box office sales')]]/p[2]")
	private WebElement gradTotalValue;

	@FindBy(xpath = "//p[contains(text(),'Operator:')]")
	private List<WebElement> listOfOperators;

	@FindBy(xpath = "//p[contains(text(),'Operator:')]/following-sibling::div")
	private List<WebElement> listOfTablesForEachOperator;

	@FindBy(id = "startDate")
	private WebElement startDate;

	@FindBy(id = "endDate")
	private WebElement endDate;
	//this is path to element that displayes selected date range
	private String parentTitleDateXpath = "//p[contains(text(),'Transactions from')]";
	
	private String titleDateFromXpath = parentTitleDateXpath + "/span[1]";
	private String titleDateToXpath = parentTitleDateXpath + "/span[2]";

	public ReportsBoxOfficeSalesPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlContains(Constants.getAdminReportsBoxOfficeSale()));
	}

	public BigDecimal getGrandTotalCashMoneyAmount() {
		return getAccessUtils().getBigDecimalMoneyAmount(grandTotalCashValue);
	}

	public BigDecimal getGrandTotalCreditCardMoneyAmount() {
		return getAccessUtils().getBigDecimalMoneyAmount(grandTotalCreditCardValue);
	}

	public BigDecimal getGrandTotalMoneyAmount() {
		return getAccessUtils().getBigDecimalMoneyAmount(gradTotalValue);
	}

	public BigDecimal getTotalOfAllOperatorTables() {
		BigDecimal total = new BigDecimal(0);
		List<OperatorTable> tables = getAllOperatorTables();
		for (OperatorTable table : tables) {
			total = total.add(table.getOperatorTableTotal());
		}
		return total;
	}

	public boolean isEventInOperatorBoxOfficeSales(String eventName) {
		List<OperatorTable> allOperatorTables = getAllOperatorTables();
		return allOperatorTables.stream().anyMatch(table -> table.isEventInBoxSales(eventName));
	}

	public List<OperatorTable> getAllOperatorTables() {
		List<OperatorTable> retList = listOfTablesForEachOperator.stream().map(el -> new OperatorTable(driver, el))
				.collect(Collectors.toList());
		return retList;
	}
	
	public void enterDateRanges(String from, String to) {
		enterDate(startDate, from);
		waitForTime(1500);
		enterDate(endDate, to);
		waitForTime(1500);
	}
	
	public boolean checkIfDatesAreCorrect(String inputFromDate, String inputToDate) {
		LocalDate fromDate = getTitleFromDate();
		LocalDate toDate = getTitleToDate();
		LocalDate inputedFrom = ProjectUtils.parseDate(ProjectUtils.DATE_FORMAT, inputFromDate);
		LocalDate inputedTo = ProjectUtils.parseDate(ProjectUtils.DATE_FORMAT, inputToDate);
		return (inputedFrom.equals(fromDate) && inputedTo.equals(toDate));
	}
	
	public LocalDate getTitleToDate() {
		return getTitleDate(titleDateToXpath);
	}
	
	public LocalDate getTitleFromDate() {
		return getTitleDate(titleDateFromXpath);
	}
	
	private LocalDate getTitleDate(String xpath) {
		WebElement el = explicitWaitForVisibilityBy(By.xpath(xpath));
		return ProjectUtils.parseDate(ProjectUtils.REPORTS_BOX_OFFICE_TITLE_DATE_FORMAT, el.getText());
	}
	

	public class OperatorTable extends BaseComponent {

		private WebElement container;

		private String relativeEventRowsXpath = "./div/div["
				+ "not(p[contains(text(),'Cash')]) and "
				+ "not(p[contains(text(),'Event name')]) and " 
				+ "not(p[contains(text(),'CreditCard')])]";

		private String relativeCreditCardPayRowXpath = "./div/div[p[contains(text(),'CreditCard')]]";

		private String relativeCashPayRowXpath = "./div/div[p[contains(text(),'Cash')]]";

		private String relativeOperatorTotalRowXpath = "./div[p[contains(text(),'Operator total')]]";

		public OperatorTable(WebDriver driver, WebElement container) {
			super(driver);
			this.container = container;
		}

		public BigDecimal getOperatorTableTotal() {
			// first check if sum and total are the same
			return getOrderTotalRow().getTotalValueMoneyAmount();
		}

		public boolean isTotalEqualToSums() {
			BigDecimal eventsSum = getTotalSumOfRowsByEvents();
			BigDecimal paymentMethodSum = getTotalSumOfRowsByPaymentMethods();
			BigDecimal total = sumTotal(new BigDecimal(0), getOrderTotalRow());
			return total.compareTo(paymentMethodSum) == 0 && total.compareTo(eventsSum) == 0;
		}

		public boolean isSumByPaymentMethodsAndSumEventsEqual() {
			return getTotalSumOfRowsByPaymentMethods().compareTo(getTotalSumOfRowsByEvents()) == 0;
		}

		public BigDecimal getTotalSumOfRowsByPaymentMethods() {
			BigDecimal total = new BigDecimal(0);
			OperatorTableRow creditCardRow = getCreditCardRow();
			OperatorTableRow cashRow = getCashRow();
			total = sumTotal(total, creditCardRow);
			total = sumTotal(total, cashRow);
			return total;
		}

		public BigDecimal getTotalSumOfRowsByEvents() {
			List<OperatorTableRow> eventRows = getAllEventRows();
			BigDecimal total = new BigDecimal(0);
			for (OperatorTableRow row : eventRows) {
				total = sumTotal(total, row);
			}
			return total;
		}

		public boolean isEventInBoxSales(String eventName) {
			return isOperatorRowPresent(row -> row.getEventName().contains(eventName));
		}

		public boolean isOperatorRowPresent(Predicate<OperatorTableRow> predicate) {
			List<OperatorTableRow> tableRows = getAllEventRows();
			if (tableRows != null) {
				boolean retVal = tableRows.stream().anyMatch(predicate);
				return retVal;
			}
			return false;
		}

		public OperatorTableRow findOperatorTableRow(Predicate<OperatorTableRow> predicate) {
			List<OperatorTableRow> tableRows = getAllEventRows();
			if (tableRows != null) {
				Optional<OperatorTableRow> optional = tableRows.stream().filter(predicate).findFirst();
				return optional.isPresent() ? optional.get() : null;
			}
			return null;
		}

		private BigDecimal sumTotal(BigDecimal total, OperatorTableRow row) {
			if (row != null && row.getTotalValueMoneyAmount() != null) {
				total = total.add(row.getTotalValueMoneyAmount());
			}
			return total;
		}

		private List<OperatorTableRow> getAllEventRows() {
			List<WebElement> eventRows = findAllEventRows();
			if (eventRows != null && !eventRows.isEmpty()) {
				return eventRows.stream().map(el -> new OperatorTableRow(driver, el)).collect(Collectors.toList());
			}
			return null;
		}

		private List<WebElement> findAllEventRows() {
			return getAccessUtils().getChildElementsFromParentLocatedBy(container, By.xpath(relativeEventRowsXpath));
		}

		private OperatorTableRow getCreditCardRow() {
			return getOperatorTableRow(By.xpath(relativeCreditCardPayRowXpath));
		}

		private OperatorTableRow getCashRow() {
			return getOperatorTableRow(By.xpath(relativeCashPayRowXpath));
		}

		private OperatorTableRow getOrderTotalRow() {
			return getOperatorTableRow(By.xpath(relativeOperatorTotalRowXpath));
		}

		private OperatorTableRow getOperatorTableRow(By by) {
			if (getAccessUtils().isChildElementVisibleFromParentLocatedBy(container, by)) {
				WebElement row = getAccessUtils().getChildElementFromParentLocatedBy(container, by);
				return new OperatorTableRow(driver, row);
			}
			return null;
		}

		public class OperatorTableRow extends BaseComponent {

			public WebElement container;

			private String relativeTotalValueXpath = "./p[last()]";

			private String relativeBoxOfficeSoldValueXpath = "./p[5]";

			private String relativeRevShareXpath = "./p[4]";

			private String relativeFaceValueXpath = "./p[3]";

			private String relativeDateXpath = "./p[2]";

			private String relativeEventNameXpath = "./p[1]";

			public OperatorTableRow(WebDriver driver, WebElement container) {
				super(driver);
				this.container = container;
			}

			public String getEventName() {
				return getAccessUtils().getText(container, By.xpath(relativeEventNameXpath));

			}

			public BigDecimal getTotalValueMoneyAmount() {
				return getAccessUtils().getBigDecimalMoneyAmount(container, relativeTotalValueXpath);
			}

			public Integer getBoxOfficeQuantity() {
				return getAccessUtils().getIntAmount(container, relativeBoxOfficeSoldValueXpath);
			}

			public BigDecimal getRevShareMoneyAmount() {
				return getAccessUtils().getBigDecimalMoneyAmount(container, relativeRevShareXpath);
			}

			public BigDecimal getFaceValueMoneyAmount() {
				return getAccessUtils().getBigDecimalMoneyAmount(container, relativeFaceValueXpath);
			}

		}

	}

}
