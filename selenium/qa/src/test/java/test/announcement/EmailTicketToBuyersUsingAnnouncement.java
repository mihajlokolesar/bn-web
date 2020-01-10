package test.announcement;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import data.holders.ticket.order.OrderDetailsData;
import model.AnnouncementMail;
import model.Event;
import model.Purchase;
import model.User;
import pages.components.dialogs.IssueRefundDialog.RefundReason;
import test.BaseSteps;
import test.facade.FacadeProvider;
import utils.DataConstants;

public class EmailTicketToBuyersUsingAnnouncement extends BaseSteps {
	
	@Test(dataProvider = "announcement_data")
	public void testRichboxtest(AnnouncementMail mail, User adminUser, Event event) {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		fp.getLoginFacade().givenAdminUserIsLogedIn(adminUser);
		fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
		fp.getAdminEventStepsFacade().whenUserGoesToEventDashboard(event);
		fp.getEventDashboardFacade().whenUserSelectsAnnouncementFromToolDropDown();
		Assert.assertTrue(fp.getAnnauncementFacade().isOnAnnouncementPage(), "Not on announcement page");
		fp.getAnnauncementFacade().sendPreviewMail(mail);
		fp.getAnnauncementFacade().sendEmailToBuyers(mail);
		
	}
	
	@DataProvider(name = "announcement_data")
	public static Object[][] announcementDataProvider(){
		AnnouncementMail mail = AnnouncementMail.generateAnnouncementFromJson(DataConstants.ANNOUNCEMENT_MAIL_KEY);
		User user = User.generateUserFromJson(DataConstants.ORGANIZATION_ADMIN_USER_KEY);
		return new Object[][] {{mail, user}};
	}
	
	
	@Test(dataProvider = "announcement_prepare_data_fixture")
	public void prepareDataFixture(User superuser, User customer, Purchase purchase) throws Exception {
		maximizeWindow();
		FacadeProvider fp = new FacadeProvider(driver);
		if (!fp.getEventFacade().isEventPresent(purchase.getEvent())) {
			fp.getLoginFacade().givenUserIsLogedIn(superuser);
			fp.getOrganizationFacade().givenOrganizationExist(purchase.getEvent().getOrganization());
			fp.getAdminEventStepsFacade().givenEventExistAndIsNotCanceled(purchase.getEvent());
			fp.getLoginFacade().logOut();

			String orderIdFullRefund = purchaseSteps(fp, purchase, customer);
			String orderIdPartialRefund = purchaseSteps(fp, purchase, customer);
			
			fp.getLoginFacade().givenAdminUserIsLogedIn(superuser);

			navigateAndRefundSteps(fp, orderIdFullRefund, purchase.getEvent(), true);
			fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
			navigateAndRefundSteps(fp, orderIdPartialRefund, purchase.getEvent(), false);
		}
	}
	
	private String purchaseSteps(FacadeProvider fp, Purchase purchase, User customer) throws Exception {
		fp.getEventFacade().givenUserIsOnHomePage();
		fp.getEventFacade().whenUserDoesThePurchses(purchase, customer);
		String orderId = ((OrderDetailsData)fp.getEventFacade().getOrderDetailsData()).getOrderNumber();
		fp.getLoginFacade().logOut();
		return orderId;
	}
	
	private void navigateAndRefundSteps(FacadeProvider fp, String orderId, Event event, boolean isFullRefund) {
		fp.getAdminEventStepsFacade().whenUserGoesToEventDashboard(event);
		fp.getEventDashboardFacade().givenUserIsOnManageOrdersPage();
		fp.getEventDashboardFacade().whenUserClickOnOrderWithOrderNumber(orderId, fp.getOrderManageFacade());
		fp.getOrderManageFacade().refundSteps(RefundReason.OTHER, isFullRefund);
	}
	
	@DataProvider(name = "announcement_prepare_data_fixture")
	public static Object[][] announcementPrepareDataFixture(){
//		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY, 
//				"TestAnnoucementNameEvent", false, 40, 5);
		Event event = Event.generateEventFromJson(DataConstants.EVENT_DATA_STANARD_KEY, 
				"TestAnnoucementNameEvent", false, 1, 1);
		Purchase purchase = Purchase.generatePurchaseFromJson(DataConstants.REGULAR_USER_PURCHASE_KEY);
		purchase.setEvent(event);
		User customer = User.generateUserFromJson(DataConstants.USER_STANDARD_KEY);
		User superuser = User.generateSuperUser();
		return new Object[][] {{superuser, customer, purchase}};
	}

}
