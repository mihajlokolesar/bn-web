package pages.components.admin.orders.manage;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class OrderListContainer extends BaseComponent {

	@FindBy(xpath = "//main//div[div[p[text()='Order management']]]/following-sibling::div[2]")
	private WebElement container;

	public OrderListContainer(WebDriver driver) {
		super(driver);
		getRefreshedContainer();
	}

	public WebElement getContainer() {
		return container;
	}
	
	public WebElement getRefreshedContainer() {
		return explicitWait(6, ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(container)));
	}

	public List<ManageOrderRow> findAllRows() {
		List<ManageOrderRow> rows = findAllOrdersElements().stream().map(e -> new ManageOrderRow(driver, e))
				.collect(Collectors.toList());
		return rows;
	}

	public ManageOrderRow findFirstRow() {
		List<WebElement> list = findAllOrdersElements();
		ManageOrderRow row = list.size() > 0 ? new ManageOrderRow(driver, list.get(0)) : null;
		return row;
	}
	

	public List<WebElement> findAllOrdersElements() {
		List<WebElement> list = SeleniumUtils.getChildElementsFromParentLocatedBy(getRefreshedContainer(), By.xpath("./div[not(p)]"),
				driver);
		return list;
	}

	public ManageOrderRow findOrderWithCustomerName(String user) {
		WebElement el = findRowWithUserName(user);
		return new ManageOrderRow(driver, el);
	}

	private WebElement findRowWithUserName(String user) {
		WebElement row = SeleniumUtils.getChildElementFromParentLocatedBy(getRefreshedContainer(), findByNameDataXpath(user), driver);
		return row;
	}

	public List<WebElement> findOrdersWithUserName(String user) {
		List<WebElement> rows = SeleniumUtils.getChildElementsFromParentLocatedBy(getRefreshedContainer(), findByNameDataXpath(user),
				driver);
		return rows;
	}

	public List<WebElement> findOrdersWithOrderNumber(String orderNum) {
		List<WebElement> rows = SeleniumUtils.getChildElementsFromParentLocatedBy(getRefreshedContainer(),
				findByOrderNumberXpath(orderNum), driver);
		return rows;
	}

	private By findByOrderNumberXpath(String orderNumber) {
		return By.xpath("./div[div[a[1][p[contains(text(),'" + orderNumber + "')]]]]");
	}

	private By findByNameDataXpath(String user) {
		return By.xpath("./div[div/a[2]/p/span[contains(text(),'" + user + "')][1]]");
	}
}