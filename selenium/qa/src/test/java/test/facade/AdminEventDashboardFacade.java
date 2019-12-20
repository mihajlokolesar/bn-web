package test.facade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import data.holders.reports.boxoffice.ReportsBoxOfficePageData;
import model.User;
import pages.admin.events.DashboardEventPage;
import pages.admin.orders.manage.OrdersManageAdminPage;
import pages.components.admin.orders.manage.ManageOrderRow;
import test.facade.orders.order.OrderManageFacade;
import utils.DateRange;

public class AdminEventDashboardFacade extends BaseFacadeSteps {

	private DashboardEventPage dashboardEventPage;
	private OrdersManageAdminPage ordersManagePage;

	private Map<String, Object> dataMap;

	public AdminEventDashboardFacade(WebDriver driver) {
		super(driver);
		this.dashboardEventPage = new DashboardEventPage(driver);
		this.ordersManagePage = new OrdersManageAdminPage(driver);
		this.dataMap = new HashMap<String, Object>();
	}

	/**
	 * Start assuming that user selected some event
	 */
	public void givenUserIsOnManageOrdersPage() {
		thenUserIsOnEventDashboardPage();
		whenUserSelectsManageOrdersFromOrdersDropDown();
		thenUserIsOnOrderManagePage();
	}

	public void whenUserSelectsManageOrdersFromOrdersDropDown() {
		dashboardEventPage.selectManageOrdersFromOrdersTab();
	}

	public ManageOrderRow getManageOrdersFirstOrder() {
		return ordersManagePage.getFirstRow();
	}

	public ManageOrderRow getManageOrderByName(User user) {
		return ordersManagePage.findOrderRowWithUserName(user.getFirstName());
	}
	
	public ManageOrderRow getOrderRowByOrderId(String orderId) {
		return ordersManagePage.findRowWithOrderId(orderId);
	}
	
	public List<ManageOrderRow> getOrderRowsForDateRange(DateRange range) {
		List<ManageOrderRow> rows = ordersManagePage.findOrderRows(el-> el.isDateBetweenDateRange(range));
		return rows;
	}
	
	public void whenUserComparesEventTotalWithManageOrdersData(ReportsBoxOfficePageData holder, DateRange range, String eventName, int ratio) {
		List<ManageOrderRow> rows = getOrderRowsForDateRange(range);
		BigDecimal totalSum = new BigDecimal(0);
		SoftAssert sa = new SoftAssert();
		for(ManageOrderRow row : rows) {
			totalSum = totalSum.add(row.getOrderValue());
		}
		if (totalSum.compareTo(holder.getEventTotal(eventName)) != 0) {
			sa.fail("Total sum for event on Orders Manage page and total sum for event: "+ eventName +" from reports not the same ");
		}
		BigDecimal numberOfEvents = new BigDecimal(holder.getRows().size());
		BigDecimal cardTotalPerEvent = holder.getCreditCardTotal().divide(numberOfEvents);
		BigDecimal cashTotalPerEvent = holder.getCashTotal().divide(numberOfEvents);
		BigDecimal rationedTotal = totalSum.divide(new BigDecimal(ratio));
		if (cardTotalPerEvent.compareTo(rationedTotal) != 0) {
			sa.fail("card total on reports page (" + cardTotalPerEvent + ") and manage orders (" + rationedTotal
					+ ") not the same");
		}
		if (cashTotalPerEvent.compareTo(rationedTotal) != 0) {
			sa.fail("cash total on reports page (" + cashTotalPerEvent + ") and manage orders (" + rationedTotal + ") not the same");
		}
		sa.assertAll();
	}
	
	public boolean whenUserDoesSearchCheckByFirstname(User user) {
		return ordersManagePage.searchCheck(user.getFirstName(),
				p -> ordersManagePage.getNumberOfAllVisibleOrdersWithName(p));
	}

	public boolean whenUserDoesSearchCheckByLastName(User user) {
		return ordersManagePage.searchCheck(user.getLastName(),
				p -> ordersManagePage.getNumberOfAllVisibleOrdersWithName(p));
	}

	public boolean whenUserDoesSearchCheckByEmail(User user) {
		return ordersManagePage.seachCheckByEmail(user);
	}

	public boolean whenUserDoesSearchCheckByOrderNumber(String ordernumber) {
		return ordersManagePage.searchCheck(ordernumber,
				p -> ordersManagePage.getNumberOfAllVisibleOrdersWithOrderNumber(p));
	}

	public boolean whenUserChecksOrderQuantityForSpecificUser(User user, Integer purchaseQuantity) {
		ordersManagePage.clearSearchFilter();
		ManageOrderRow order = ordersManagePage
				.findOrderRowWithUserName(user.getFirstName() + " " + user.getLastName());
		Integer orderQty = order.getQuantity();
		return orderQty.compareTo(purchaseQuantity) == 0;
	}

	public void whenUserClickOnOrderLinkOfFirstOrder(OrderManageFacade orderManageFacade) {
		ManageOrderRow orderRow = ordersManagePage.getFirstRow();
		navigateToSelectedOrderPage(orderRow, orderManageFacade);
	}

	public void whenUserClicksOnOrderLinkOfGivenUser(User owner, OrderManageFacade orderManageFacade) {
		ManageOrderRow orderRow = ordersManagePage.findOrderRowWithUserName(owner.getFirstName() + " ");
		navigateToSelectedOrderPage(orderRow, orderManageFacade);
	}
	
	public void whenUserClickOnOrderWithOrderNumber(String orderNumber, OrderManageFacade orderManageFacade) {
		ManageOrderRow orderRow = ordersManagePage.findRowWithOrderId(orderNumber);
		navigateToSelectedOrderPage(orderRow, orderManageFacade);
	}

	private void navigateToSelectedOrderPage(ManageOrderRow orderRow, OrderManageFacade orderFacade) {
		String orderId = orderRow.getOrderId();
		orderRow.clickOnOrderNumberLink();
		orderFacade.setSelectedOrderPage(orderId);
	}

	public void thenUserIsOnEventDashboardPage() {
		dashboardEventPage.isAtPage();
	}

	public void thenUserIsOnOrderManagePage() {
		ordersManagePage.isAtPage();
	}

	protected void setData(String key, Object value) {
		dataMap.put(key, value);
	}

	protected Object getData(String key) {
		return dataMap.get(key);
	}

}