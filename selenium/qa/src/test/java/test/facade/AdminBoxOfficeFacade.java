package test.facade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import model.User;
import pages.admin.boxoffice.GuestPage;
import pages.admin.boxoffice.SellPage;
import pages.components.admin.AdminBoxOfficeSideBar;
import pages.components.admin.TicketTypeRowComponent;
import pages.components.dialogs.BoxOfficeSellCheckoutDialog;
import pages.components.dialogs.BoxOfficeSellOrderCompleteDialog;

public class AdminBoxOfficeFacade extends BaseFacadeSteps {

	private SellPage sellPage;
	private GuestPage guestPage;
	private AdminBoxOfficeSideBar boxOfficeSideBar;
	private BoxOfficeSellCheckoutDialog checkoutDialog;
	private Map<String, Object> dataMap;

	private final String SELL_ORDER_NUMBER_COMPLETE_KEY = "sell_order_number_complete_key";

	public AdminBoxOfficeFacade(WebDriver driver) {
		super(driver);
		this.sellPage = new SellPage(driver);
		this.guestPage = new GuestPage(driver);
		this.boxOfficeSideBar = new AdminBoxOfficeSideBar(driver);
		this.checkoutDialog = new BoxOfficeSellCheckoutDialog(driver);
		this.dataMap = new HashMap<>();
	}

	public void givenUserIsOnBoxOfficePage() {
		sellPage.getHeader().clickOnBoxOfficeLink();
		thenUserIsAtSellPage();
	}

	public void givenUserIsOnGuestPage() {
		boxOfficeSideBar.clickOnGuestLink();
		guestPage.isAtPage();
	}

	public void givenEventIsSelected(String eventName) {
		guestPage.getHeader().selectEventFromAdminDropDown(eventName);
	}

	public void givenBoxOfficeEventIsSelected(String eventName) {
		sellPage.getHeader().selectEventFromBoxOfficeDropDown(eventName);
	}

	public boolean whenUserSearchesByUserName(User user) {
		String searchValue = user.getFirstName();
		return whenUserSearchesByUserParams(searchValue);
	}

	public boolean whenUserSearchesByLastName(User user) {
		String lastname = user.getLastName();
		return whenUserSearchesByUserParams(lastname);
	}

	public boolean whenUserSearchesByEmail(User user) {
		guestPage.enterSearchParameters("");
		List<WebElement> allGuests = guestPage.searchForAllGuestOnPage();
		if (allGuests == null || allGuests.isEmpty()) {
			return false;
		}

		guestPage.enterSearchParameters(user.getEmailAddress());

		List<WebElement> searchResults = guestPage.searchForResultsOfSearch(user.getFirstName());
		if (searchResults != null && !searchResults.isEmpty()) {
			return searchResults.size() < allGuests.size();
		}
		return false;
	}

	public boolean whenUserSearchesByTicketNumber(User user) {
		String firstname = user.getFirstName();
		guestPage.enterSearchParameters("");
		List<WebElement> allGuests = guestPage.searchForAllGuestOnPage();
		if (allGuests == null || allGuests.isEmpty()) {
			throw new NoSuchElementException("No guests found on admin guest page");
		}
		guestPage.enterSearchParameters(firstname);

		List<WebElement> searchResults = guestPage.searchForResultsOfSearch(firstname);
		if (searchResults == null || searchResults.isEmpty()) {
			throw new NoSuchElementException("No guest found on admin guest page after search");
		}
		String ticketNumber = guestPage.getTicketNumber(firstname);
		String escapedTicketNumber = ticketNumber.replace("#", "");
		guestPage.enterSearchParameters(escapedTicketNumber);

		boolean isTicketInSearchResults = guestPage.isTicketNumberInGuestResults(escapedTicketNumber);
		return isTicketInSearchResults;

	}

	private boolean whenUserSearchesByUserParams(String param) {
		guestPage.enterSearchParameters("");
		List<WebElement> allGuests = guestPage.searchForAllGuestOnPage();
		if (allGuests == null || allGuests.isEmpty()) {
			return false;
		}

		guestPage.enterSearchParameters(param);

		List<WebElement> searchResults = guestPage.searchForResultsOfSearch(param);
		if (searchResults != null && !searchResults.isEmpty()) {
			return searchResults.size() < allGuests.size();
		}
		return false;
	}

	public TicketTypeRowComponent whenUserSelectsTicketType() {
		return sellPage.findTicketTypeRowComponentWithAvailableTickets();
	}

	public void whenUserAddsQuantityAndClicksCheckout(TicketTypeRowComponent row, int addNumberOfTickets) {
		int qtyBefore = row.getCurrentQuantity();
		row.addTickets(addNumberOfTickets);
		int currentQty = row.getCurrentQuantity();
		Assert.assertTrue((qtyBefore + currentQty) == addNumberOfTickets);
		sellPage.clickOnCheckoutButton();
	}

	public void whenUserRemovesQuantityAndClicksCheckout(TicketTypeRowComponent row, int removeNumberOfTickets) {
		int qtyBefore = row.getCurrentQuantity();
		row.removeTickets(removeNumberOfTickets);
		int qtyAfter = row.getCurrentQuantity();
		Assert.assertTrue((qtyBefore - removeNumberOfTickets) == qtyAfter,
				"Tried to remove more tickets that was available");
		sellPage.clickOnCheckoutButton();
	}

	public boolean thenCheckoutDialogIsVisible() {
		return checkoutDialog.isVisible();
	}
	
	public void whenUserClicksOnChangeTicketOnCheckoutDialog() {
		checkoutDialog.clickOnChangeTicketLink();
	}

	public void whenUserPicksCashOption() {
		checkoutDialog.clickOnPayWithCash();
	}

	public boolean whenUserEntersTenderedAndChecksChangeDueIsCorrect(TicketTypeRowComponent row, int tenderedAmount) {
		checkoutDialog.enterAmountToTenderedField(tenderedAmount);
		
		
		Double doubleCheckoutDue = checkoutDialog.getChangeDueAmount();
		Double doubleOrderTotal = checkoutDialog.getOrderTotal();
		
		BigDecimal doubleTendered = new BigDecimal(tenderedAmount);
		BigDecimal checkDue = new BigDecimal(doubleCheckoutDue != null ? doubleCheckoutDue : 0);
		BigDecimal orderTotal = new BigDecimal(doubleOrderTotal != null ? doubleOrderTotal : 0);
		if (orderTotal.compareTo(doubleTendered) < 0) {
			BigDecimal dueCalculated = doubleTendered.subtract(orderTotal);
			return dueCalculated.compareTo(checkDue) == 0;
		} else {
			return true;
		}
	}

	public Double whenUserChecksOrderTotal() {
		return checkoutDialog.getOrderTotal();
	}

	public void whenUserEntersGuestInformationAndClicksOnCompleteOrder(User guest, String orderNote) {
		checkoutDialog.enterFirstName(guest.getFirstName());
		checkoutDialog.enterLastName(guest.getLastName());
		checkoutDialog.enterEmailAddress(guest.getEmailAddress());
		checkoutDialog.enterPhoneNumber(guest.getPhoneNumber());
		checkoutDialog.enterOrderNote(orderNote);
		checkoutDialog.waitForTime(1000);
		checkoutDialog.clickOnCompleteOrderButton();
	}

	public void thenUserShouldSeeOrderCompleteDialogAndGetOrderNumber() {
		BoxOfficeSellOrderCompleteDialog orderComplete = new BoxOfficeSellOrderCompleteDialog(driver);
		Assert.assertTrue(orderComplete.isVisible());
		String orderNum = orderComplete.getOrderNumber();
		setData(SELL_ORDER_NUMBER_COMPLETE_KEY, orderNum);
	}

	public void thenUserIsAtSellPage() {
		this.sellPage.isAtPage();
	}

	private void setData(String key, Object value) {
		dataMap.put(key, value);
	}

	private Object getData(String key) {
		return dataMap.get(key);
	}

}
