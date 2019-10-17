package test;

import org.testng.Assert;

import model.Event;
import model.Purchase;
import model.User;
import pages.components.admin.AdminEventComponent;
import pages.components.admin.orders.manage.ManageOrderRow;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import test.facade.AdminEventDashboardFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.EventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;

public class OrderManageOrderFeeRefundStepsIT extends AbstractRefundFeeSteps {

	public void refundJustAnOrderFeeFromOrder(Purchase purchase, User user) throws Exception {
		createEvent(user, purchase.getEvent());
		purchaseTickets(purchase);
		navigateToOrderManage(purchase.getEvent());

		getEventDashboardFacade().whenUserClicksOnOrderFeeCheckBox();
		boolean isRefundButtonAmountCorrect = getEventDashboardFacade().thenRefundButtonAmountShouldBeCorrect();
		Assert.assertTrue(isRefundButtonAmountCorrect, "Refund amount on refund button incorect");

		getEventDashboardFacade().whenUserClicksOnRefundButton();
		boolean isRefundDialogVisible = getEventDashboardFacade().thenRefundDialogShouldBeVisible();
		Assert.assertTrue(isRefundDialogVisible, "Refund dialog not visible");
		boolean isRefundDialogAmountCorrect = getEventDashboardFacade().thenRefundTotalOnRefundDialogShouldBeCorrect();
		Assert.assertTrue(isRefundDialogAmountCorrect);
		getEventDashboardFacade().whenUserSelectRefundReasonAndClicksOnConfirmButton(RefundReason.OTHER);
		getEventDashboardFacade().whenUserClicksOnGotItButtonOnRefundSuccessDialog();

		boolean isAtSelectedOrderPage = getEventDashboardFacade().thenUserIsOnSelecteOrderPage();
		Assert.assertTrue(isAtSelectedOrderPage, "After refund user is not on correct page");
		getEventDashboardFacade().whenUserClicksOnOrderFeeCheckBox();
		boolean isRefundButtonVisible = getEventDashboardFacade().thenRefundButtonShouldBeVisible();
		Assert.assertFalse(isRefundButtonVisible,
				"Refund button on per order fee after already refunded should not be visible");

		cancelEvent(purchase.getEvent());
	}

}
