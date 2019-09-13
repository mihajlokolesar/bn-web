package test.facade;

import org.openqa.selenium.WebDriver;

import model.User;
import pages.admin.events.DashboardEventPage;
import pages.admin.events.OrdersManageAdminPage;
import pages.components.admin.orders.manage.ManageOrderRow;

public class AdminEventDashboardFacade extends BaseFacadeSteps {

	private DashboardEventPage dashboardEventPage;
	private OrdersManageAdminPage ordersManagePage;

	public AdminEventDashboardFacade(WebDriver driver) {
		super(driver);
		this.dashboardEventPage = new DashboardEventPage(driver);
		this.ordersManagePage = new OrdersManageAdminPage(driver);
	}

//	public void whenUserSelectManageOrdersFromToolsDropDown() {
//		dashboardEventPage.selectManageOrdersFromTools();
//	}

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
		ManageOrderRow order = ordersManagePage.findOrderRowWithUserName(user.getFirstName() + user.getLastName());
		Integer orderQty = order.getQuantity();
		return orderQty.compareTo(purchaseQuantity) == 0;
	}

//
//	public void whenUserSelectsTicketForRefundAndClicksOnRefundButton() {
//		ManageOrderComp orderComponent = manageOrdersPage.openOrderWithIndexNumber(1);
//		setData(MANAGE_ORDER_FIRST_NAME_KEY, orderComponent.getFirstNameOfUser());
//		setData(MANAGE_ORDER_LAST_NAME_KEY, orderComponent.getLastNameOfUser());
//		setData(MANAGE_ORDER_TICKET_NUMBER_KEY, orderComponent.selectTicketForRefund());
//		manageOrdersPage.clickOnRefundButton();
//		RefundDialog refundDialog = new RefundDialog(driver);
//		refundDialog.clickOnDialogRefundButton();
//	}
//
//	public void whenUserGetConfirmationsOfSuccessfulRefund() {
//		new RefundDialog(driver).confirmRefundIsSuccess();
//	}
//
//	public boolean thenTicketShouldNotBePresent() {
//		String ticketNumber = (String) getData(MANAGE_ORDER_TICKET_NUMBER_KEY);
//		String firstName = (String) getData(MANAGE_ORDER_FIRST_NAME_KEY);
//		String lastName = (String) getData(MANAGE_ORDER_LAST_NAME_KEY);
//		return manageOrdersPage.isTicketPresentInOrder(ticketNumber, firstName, lastName);
//	}
//
//	public void thenUserIsOnManageOrdersPage() {
//		manageOrdersPage.isAtPage();
//	}
//
	public void thenUserIsOnEventDashboardPage() {
		dashboardEventPage.isAtPage();
	}

	public void thenUserIsOnOrderManagePage() {
		ordersManagePage.isAtPage();
	}

}
