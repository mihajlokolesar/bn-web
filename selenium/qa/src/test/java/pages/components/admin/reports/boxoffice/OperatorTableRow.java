package pages.components.admin.reports.boxoffice;

import java.math.BigDecimal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;

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
