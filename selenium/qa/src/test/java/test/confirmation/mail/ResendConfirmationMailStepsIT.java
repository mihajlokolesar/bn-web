package test.confirmation.mail;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import config.MailinatorEnum;
import model.Event;
import model.Purchase;
import model.User;
import pages.components.admin.events.EventSummaryComponent;
import pages.components.admin.orders.manage.OrderInfo;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import pages.mailinator.MailinatorFactory;
import pages.mailinator.inbox.ConfirmationMailPage;
import pages.mailinator.inbox.MailinatorInboxPage;
import pages.mailinator.inbox.PurchaseConfirmationMailPage;
import test.BaseSteps;
import test.facade.FacadeProvider;
import utils.DataConstants;

public class ResendConfirmationMailStepsIT extends BaseSteps {

	private static final Integer PURCHASE_QUANTITY = 2;

	private static final Integer START_DAY_OFFSET = 2;
	private static final Integer DAYS_RANGE = 0;
	private static final String EVENT_NAME = "TestConfirmationEventName";
	
	private Event event;

	@Test(dataProvider = "purchase_confirmation_mail_data", priority = 92)
	public void resendPurchaseConfirmationMail(Purchase purchase, User orgAdmin, User customer) throws URISyntaxException {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		purchase.setEvent(event);
		fp.getLoginFacade().givenUserIsLogedIn(orgAdmin);
		fp.getOrganizationFacade().givenOrganizationExist(purchase.getEvent().getOrganization());
		EventSummaryComponent eventSummaryComponent = fp.getAdminEventStepsFacade()
				.findEventWithName(purchase.getEvent());
		eventSummaryComponent.clickOnEvent();
		fp.getEventDashboardFacade().whenUserSelectsManageOrdersFromOrdersDropDown();
		fp.getEventDashboardFacade().whenUserClickOnOrderLinkOfFirstOrder(fp.getOrderManageFacade());
		fp.getOrderManageFacade().whenUserClickOnResendConfirmationEmail();
		Assert.assertTrue(fp.getOrderManageFacade().thenNotificationResendOrderConfirmationShouldBeVisible(),
				"Notification of resend order not visible");
		OrderInfo orderInfo = fp.getOrderManageFacade().whenUserCollectsOrderInfo();
		BigDecimal totalFees = orderInfo.getFeesTotal();
		BigDecimal orderTotal = orderInfo.getOrderTotal();

		fp.getLoginFacade().logOut();

		PurchaseConfirmationMailPage page = (PurchaseConfirmationMailPage) MailinatorFactory
				.getInboxPage(MailinatorEnum.PURCHASE_CONFIRMATION_MAIL, driver, customer.getEmailAddress());
		Map<String, Object> data = new HashMap<>();
		data.put(ConfirmationMailPage.ORDER_TOTAL_KEY, orderTotal);
		data.put(ConfirmationMailPage.TOTAL_FEES_KEY, totalFees);
		data.put(ConfirmationMailPage.SOFT_ASSERT, new SoftAssert());
		page.openMailAndCheckValidity(data);
	}
	
	@Test(dataProvider = "purchase_confirmation_mail_data", priority = 94)
	public void resendRefundConfirmationMail(Purchase purchase, User orgAdmin, User customer) throws URISyntaxException {
		maximizeWindow();
		purchase.setEvent(this.event);
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenUserIsLogedIn(orgAdmin);
		fp.getOrganizationFacade().givenOrganizationExist(purchase.getEvent().getOrganization());
		EventSummaryComponent eventSummaryComponent = fp.getAdminEventStepsFacade()
				.findEventWithName(purchase.getEvent());
		eventSummaryComponent.clickOnEvent();
		fp.getEventDashboardFacade().whenUserSelectsManageOrdersFromOrdersDropDown();
		fp.getEventDashboardFacade().whenUserClickOnOrderLinkOfFirstOrder(fp.getOrderManageFacade());
		fp.getOrderManageFacade().whenUserIssuesFullRefund(RefundReason.UNABLE_TO_ATTEND);
		OrderInfo orderInfo = fp.getOrderManageFacade().whenUserCollectsOrderInfo();
		BigDecimal totalFees = orderInfo.getFeesTotal();
		BigDecimal orderTotal = orderInfo.getOrderTotal();
		
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		
		fp.getAdminEventStepsFacade().attemptEventCancel(purchase.getEvent());
		
		fp.getLoginFacade().logOut();
		
		Map<String, Object> data = new HashMap<>();
		data.put(ConfirmationMailPage.ORDER_TOTAL_KEY, orderTotal);
		data.put(ConfirmationMailPage.TOTAL_FEES_KEY, totalFees);
		data.put(ConfirmationMailPage.SOFT_ASSERT, new SoftAssert());
		
		MailinatorInboxPage inbox = MailinatorFactory
				.getInboxPage(MailinatorEnum.TOTAL_REFUND_CONFIRMATION, driver, customer.getEmailAddress());
		inbox.openMailAndCheckValidity(data);
	}

	@Test(dataProvider = "purchase_confirmation_mail_data", priority = 90)
	public void prepareDataFixture(Purchase purchase, User orgAdmin, User customer) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		this.event = purchase.getEvent();
		fp.getLoginFacade().givenUserIsLogedIn(orgAdmin);
		fp.getOrganizationFacade().givenOrganizationExist(purchase.getEvent().getOrganization());
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		fp.getAdminEventStepsFacade().createEvent(purchase.getEvent());
		fp.getLoginFacade().logOut();

		fp.getEventFacade().givenUserIsOnEventPage();
		fp.getEventFacade().whenUserDoesThePurchses(purchase, customer);
		fp.getLoginFacade().logOut();
	}

	@DataProvider(name = "purchase_confirmation_mail_data")
	public static Object[][] dataProvider() {
		Purchase purchase = preparePurchase();
		User orgAdmin = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		User customer = User.generateUserFromJson(DataConstants.USER_STANDARD_KEY);
		return new Object[][] { { purchase, orgAdmin, customer } };
	}

	private static Purchase preparePurchase() {
		Purchase purchase = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		purchase.setNumberOfTickets(PURCHASE_QUANTITY);
		purchase.setEvent(Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY, EVENT_NAME, true,
				START_DAY_OFFSET, DAYS_RANGE));
		return purchase;
	}

}
