package test.facade;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import model.User;
import pages.admin.boxoffice.GuestPage;
import pages.admin.boxoffice.SellPage;
import pages.components.admin.AdminBoxOfficeSideBar;

public class AdminBoxOfficeFacade extends BaseFacadeSteps {

	private SellPage sellPage;
	private GuestPage guestPage;
	private AdminBoxOfficeSideBar boxOfficeSideBar;

	public AdminBoxOfficeFacade(WebDriver driver) {
		super(driver);
		this.sellPage = new SellPage(driver);
		this.guestPage = new GuestPage(driver);
		this.boxOfficeSideBar = new AdminBoxOfficeSideBar(driver);
	}

	public void givenUserIsOnBoxOfficePage() {
		sellPage.getHeader().clickOnBoxOfficeLink();
		sellPage.isAtPage();
	}

	public void givenUserIsOnGuestPage() {
		boxOfficeSideBar.clickOnGuestLink();
		guestPage.isAtPage();
	}
	
	public void givenEventIsSelected(String eventName) {
		guestPage.getHeader().selectEventFromAdminDropDown(eventName);
		
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
}
