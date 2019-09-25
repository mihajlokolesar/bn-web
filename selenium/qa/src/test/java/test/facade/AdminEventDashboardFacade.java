package test.facade;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import model.User;
import pages.admin.events.DashboardEventPage;
import pages.admin.orders.OrdersManageAdminPage;
import pages.admin.orders.SelectedOrderPage;
import pages.components.admin.orders.manage.ManageOrderRow;
import pages.components.admin.orders.manage.tickets.TicketRow;
import pages.components.dialogs.IssueRefundDialog;
import pages.components.dialogs.IssueRefundDialog.RefundReason;

public class AdminEventDashboardFacade extends BaseFacadeSteps {

	private DashboardEventPage dashboardEventPage;
	private OrdersManageAdminPage ordersManagePage;

	private final String SELECTED_ORDER_PAGE_KEY = "selected_order_page";
	private final String ISSUE_REFUND_DIALOG_KEY = "issue_refund_dialog";

	private Map<String, Object> dataMap;

	public AdminEventDashboardFacade(WebDriver driver) {
		super(driver);
		this.dashboardEventPage = new DashboardEventPage(driver);
		this.ordersManagePage = new OrdersManageAdminPage(driver);
		this.dataMap = new HashMap<String, Object>();
	}

	public void whenUserSelectsManageOrdersFromOrdersDropDown() {
		dashboardEventPage.selectManageOrdersFromOrdersTab();
	}

	public ManageOrderRow getManageOrdersFirstOrder() {
		return ordersManagePage.getFirstRow();
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

	public void whenUserClicksOnOrderLinkOfGivenUser(User owner) {
		ManageOrderRow orderRow = ordersManagePage.findOrderRowWithUserName(owner.getFirstName() + " ");
		String orderId = orderRow.getOrderId();
		orderRow.clickOnOrderNumberLink();
		SelectedOrderPage selectedOrderPage = new SelectedOrderPage(driver, orderId);
		selectedOrderPage.isAtPage();
		setData(SELECTED_ORDER_PAGE_KEY, selectedOrderPage);
	}

	public boolean whenUserExpandOrderDetailsAndCheckIfExpanded() {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		selectedOrderPage.expandOrderDetails();
		return selectedOrderPage.getOrderDetails().isExpanded();
	}

	public void whenUserSelectsTicketForRefundAndClicksOnRefundButton() {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		TicketRow row = selectedOrderPage.findTicketRow(r -> r.isTicketPurchased());
		row.clickOnCheckoutBoxInTicket();
		selectedOrderPage.clickOnRefundButton();
	}
	
	public boolean thenRefundDialogShouldBeVisible() {
		IssueRefundDialog refundDialog = new IssueRefundDialog(driver);
		setData(ISSUE_REFUND_DIALOG_KEY, refundDialog);
		return refundDialog.isVisible();
	}
	
	public void whenUserSelectRefundReasonAndClicksOnConfirmButton(RefundReason refundReason) {
		IssueRefundDialog refundDialog = (IssueRefundDialog) getData(ISSUE_REFUND_DIALOG_KEY);
		refundDialog.selectRefundReason(refundReason);
		refundDialog.clickOnContinue();
	}
	
	public void whenUserClicksOnGotItButtonOnRefundSuccessDialog() {
		IssueRefundDialog refundDialog = (IssueRefundDialog) getData(ISSUE_REFUND_DIALOG_KEY);
		refundDialog.isVisible();
		String ticketOwnerInfo = refundDialog.getTicketOwnerInfo();
		refundDialog.clickOnGotItButton();
	}
	

	public void thenUserIsOnEventDashboardPage() {
		dashboardEventPage.isAtPage();
	}

	public void thenUserIsOnOrderManagePage() {
		ordersManagePage.isAtPage();
	}

	private void setData(String key, Object value) {
		dataMap.put(key, value);
	}

	private Object getData(String key) {
		return dataMap.get(key);
	}

}
