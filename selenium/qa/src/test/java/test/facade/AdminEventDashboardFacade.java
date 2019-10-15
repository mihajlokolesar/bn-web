package test.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import model.Event;
import model.Purchase;
import model.TicketType;
import model.User;
import pages.admin.events.DashboardEventPage;
import pages.admin.orders.manage.OrdersManageAdminPage;
import pages.admin.orders.manage.SelectedOrderPage;
import pages.components.admin.orders.manage.ManageOrderRow;
import pages.components.admin.orders.manage.ActivityItem;
import pages.components.admin.orders.manage.ActivityItem.ExpandedContent;
import pages.components.admin.orders.manage.ActivityItem.NoteExpandedContent;
import pages.components.admin.orders.manage.ActivityItem.RefundedExpandedContent;
import pages.components.admin.orders.manage.tickets.TicketRow;
import pages.components.dialogs.IssueRefundDialog;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import utils.MsgConstants;
import utils.ProjectUtils;

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
		return ordersManagePage.findOrderRowWithUserName(user.getFirstName() + " " + user.getLastName());
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
	
	public boolean thenThereShouldBeItemsInOrderHistory(int number) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		Integer rows = selectedOrderPage.getNumberOfHistoryItemRows();
		return rows.equals(number);
	}
	
	public boolean thenAllItemsShouldBeClosed() {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		Integer totalNumberOfHistoryRows =  selectedOrderPage.getNumberOfHistoryItemRows();
		Integer totalNumberOfColapsedRows = selectedOrderPage.getNumberOfAllCollapsedRows();
		return totalNumberOfColapsedRows.equals(totalNumberOfHistoryRows);
	}
	
	public boolean thenThereShouldBePurchasedHistoryItem(User user, Purchase purchase) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		String userName = user.getFirstName() + " " + user.getLastName();
		ActivityItem activityItem = selectedOrderPage.getHistoryActivityItem(
				aitem->aitem.isPruchased() && 
				aitem.getUserName().contains(userName) && 
				aitem.getEventName().contains(purchase.getEvent().getEventName()));
		return activityItem != null ? true :false;
		
	}
	
	public boolean thenThereShouldBePurchasedHistoryItemWithNumberOfPurchases(User user, 
			Purchase purchase, int numberOfPurchases) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		String userName = user.getFirstName() + " " + user.getLastName();
		ActivityItem activityItem = selectedOrderPage.getHistoryActivityItem(
				aitem->aitem.isPruchased() && 
				aitem.getUserName().contains(userName) && 
				aitem.getNumberOfPurchases().equals(numberOfPurchases) &&
				aitem.getEventName().contains(purchase.getEvent().getEventName()));
		return activityItem != null ? true : false;
	}
	
	public boolean whenUserExpandsActivityItemAndChecksValidityOfData(Purchase purchase, Integer quantity, TicketType ticketType) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		Event event = purchase.getEvent();
		ActivityItem activityItem = selectedOrderPage.getHistoryActivityItem(
				aitem->aitem.isPruchased() && 
				aitem.getEventName().contains(event.getEventName()));
		if (activityItem != null) {
			activityItem.clickOnShowDetailsLink();
			String location = event.getVenue().getLocation();
			LocalDateTime eventStartDateTime = ProjectUtils
					.getLocalDateTime(ProjectUtils.DATE_FORMAT, event.getStartDate(),
								 ProjectUtils.TIME_FORMAT, event.getStartTime());
					
			BigDecimal totalMoney  = new BigDecimal(quantity.intValue() * Integer.parseInt(ticketType.getPrice()));
			
			ExpandedContent expandedContent = activityItem.getExpandedContent();
			LocalDateTime itemDate = ProjectUtils.parseDateTime(
						ProjectUtils.MANAGE_ORDER_HISTORY_ITEM_DATE_FORMAT, expandedContent.getEventDateAndTime());
			boolean retVal = true;
			retVal = retVal && eventStartDateTime.equals(itemDate);
			retVal = retVal && location.equals(expandedContent.getVenueLocation());
			retVal = retVal && quantity.equals(expandedContent.getQuantity());
			retVal = retVal && totalMoney.equals(expandedContent.getTotalMoneyAmount());
			return retVal;
		} else {
			return false;
		}
	}
	
	public boolean thenThereShouldBeRefundedHistoryItemWithRefundee(User refunder, User refundee) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		String refunderName = refunder.getFirstName() + " " + refunder.getLastName();
		String refundeeName = refundee.getFirstName() + " " + refundee.getLastName();
		ActivityItem activityItem = selectedOrderPage.getHistoryActivityItem(
				aitem->aitem.isRefunded() &&
				aitem.getUserName().contains(refunderName) &&
				aitem.getRefundeeName().contains(refundeeName));
		return activityItem != null ? true : false;
	}
	
	public boolean whenUserExpandsRefundedHistoryItemAndChecksData(Purchase purchase, Integer quantity, TicketType ticketType) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		ActivityItem activityItem = selectedOrderPage.getHistoryActivityItem(
				aitem->aitem.isRefunded());
		if (activityItem != null) {
			activityItem.clickOnShowDetailsLink();
			RefundedExpandedContent refundedContent = activityItem.getRefundedExpandedContent();
			BigDecimal ticketPrice = new BigDecimal(ticketType.getPrice());
			BigDecimal totalMoneyAmount = ticketPrice.multiply(new BigDecimal(quantity));
			boolean retVal = true;
			retVal = retVal && ticketPrice.compareTo(refundedContent.getPerTicketFee()) == 0;
			retVal = retVal && totalMoneyAmount.compareTo(refundedContent.getTotalRefundMoneyAmount()) == 0;
			return retVal;
		} else {
			return false;
		}
	}
	
	public void whenUserAddsANote(String note) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		selectedOrderPage.enterNote(note);
		selectedOrderPage.clickOnAddNoteButton();
	}
	
	public boolean thenNotificationNoteAddedShouldBeVisible() {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		return selectedOrderPage.isNotificationDisplayedWithMessage(MsgConstants.ORDER_MANANGE_ACTIVITY_ITEM_NOTE_ADDED, 5);
	}
	
	public boolean thenUserShouldSeeNoteActivityItem(String note, User user) {
		SelectedOrderPage selectedOrderPage = (SelectedOrderPage) getData(SELECTED_ORDER_PAGE_KEY);
		selectedOrderPage.refreshPage();
		String userName =  user.getFirstName() + " " + user.getLastName();
		ActivityItem activityItem = selectedOrderPage.getHistoryActivityItem(
				aitem-> aitem.isNoteItem() && aitem.isUserNameEqualTo(userName));
		if (activityItem == null) {
			return false;
		} else {
			activityItem.clickOnShowDetailsLink();
			NoteExpandedContent noteContent = activityItem.getNoteExpandedContent();
			String noteText = noteContent.getNoteText();
			return note.equalsIgnoreCase(noteText);
		}	
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