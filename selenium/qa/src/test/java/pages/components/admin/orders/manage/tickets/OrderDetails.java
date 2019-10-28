package pages.components.admin.orders.manage.tickets;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class OrderDetails extends BaseComponent {
	
	

	@FindBy(xpath = "//main//div[div[div[div[div[p[text()='Ticket #']]]]]]")
	private WebElement container;

	private final String IS_EXPANDED_CONDITION = "min-height: 0px; height: auto; transition-duration: 300ms;";

	private final String IS_CLOSED_CONDITION = "min-height: 0px; height: 0px; transition-duration: 300ms;";

	@FindAll(value = { @FindBy(xpath = "//main//div[p[text()='Ticket #']]/following-sibling::div[not(@class)]") })
	private List<WebElement> rows;
	
	private PerOrderFeeComponent perOrderFee;

	public OrderDetails(WebDriver driver) {
		super(driver);
		this.perOrderFee = new PerOrderFeeComponent(driver);
	}

	public boolean isExpanded() {
		return isExplicitConditionTrue(5,
				ExpectedConditions.attributeContains(container, "style", IS_EXPANDED_CONDITION));
	}

	public boolean isClosed() {
		return isExplicitConditionTrue(5,
				ExpectedConditions.attributeContains(container, "style", IS_CLOSED_CONDITION));
	}

	public TicketRow findTicketRow(Predicate<TicketRow> predicate) {
		Optional<TicketRow> row = rows.stream().map(e -> new TicketRow(driver, e)).filter(predicate).findFirst();
		return row.isPresent() ? row.get() : null;
	}
	
	public List<TicketRow> findAllTicketRows() {
		return rows.stream().map(el-> new TicketRow(driver, el)).collect(Collectors.toList());
	}

	public PerOrderFeeComponent getPerOrderFee() {
		return this.perOrderFee;
	}

	public class PerOrderFeeComponent extends BaseComponent {

		@FindBy(xpath = "//div[div[p[contains(text(),'Per order fee')]]]")
		private WebElement container;

		private String relativeCheckBoxXpath = "./div/div/div";
		
		private String relativeCheckedBoxXpath = relativeCheckBoxXpath + "/img";

		private String relativeTotalMoneyAmountXpath = "./p[1]";

		private String relativaStatusXpath = "./p[2]";

		public PerOrderFeeComponent(WebDriver driver) {
			super(driver);
		}

		public boolean isVisible() {
			return isExplicitlyWaitVisible(5, container);
		}
		
		public void clickOnCheckBox() {
			WebElement checkBox = getCheckBoxElement();
			SeleniumUtils.jsScrollIntoView(checkBox, driver);
			waitVisibilityAndBrowserCheckClick(checkBox);
		}
		
		public boolean isChecked() {
			return getAccessUtils().isChildElementVisibleFromParentLocatedBy(container, By.xpath(relativeCheckedBoxXpath), 3);
		}
		
		public BigDecimal getMoneyAmount() {
			WebElement moneyEl = getMoneyAmountElement();
			Double money = SeleniumUtils.getDoubleAmount(moneyEl, "$", "");
			return new BigDecimal(money);
		}

		private WebElement getCheckBoxElement() {
			return SeleniumUtils.getChildElementFromParentLocatedBy(container, By.xpath(relativeCheckBoxXpath), driver);
		}

		private WebElement getMoneyAmountElement() {
			return SeleniumUtils.getChildElementFromParentLocatedBy(container, By.xpath(relativeTotalMoneyAmountXpath),
					driver);
		}

		private WebElement getStatusElement() {
			return SeleniumUtils.getChildElementFromParentLocatedBy(container, By.xpath(relativaStatusXpath), driver);
		}
	}
}