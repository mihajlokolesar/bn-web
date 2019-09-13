package test.facade;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import model.User;
import pages.admin.boxoffice.GuestPage;
import pages.admin.boxoffice.SellPage;
import pages.components.admin.AdminBoxOfficeSideBar;
import utils.ProjectUtils;

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
		Integer allGuests = cleanSearchAndGetNumberOfResults();
		guestPage.enterSearchParameters(user.getEmailAddress());
		Integer searchResults = guestPage.getNumberOfResultsOfSearch(user.getFirstName());
		return searchResults.compareTo(allGuests) < 0;
	}
	
	public boolean whenUserSearchesByFirstNameAndTicketNumber(User user) {
		String firstname = user.getFirstName();
		boolean isNameSearchValid = whenUserSearchesByUserParams(firstname);
		String ticketNumber = guestPage.getTicketNumber(firstname);
 		guestPage.enterSearchParameters(ticketNumber);
 		
 		boolean isTicketInSearchResults = guestPage.isTicketNumberInGuestResults(ticketNumber);
 		return isTicketInSearchResults && isNameSearchValid;
		
	}
	
	private boolean whenUserSearchesByUserParams(String param) {
		Integer allGuests = cleanSearchAndGetNumberOfResults();
		guestPage.enterSearchParameters(param);
		Integer searchResults = guestPage.getNumberOfResultsOfSearch(param);
		return searchResults.compareTo(allGuests) < 0;
	}
	
	private Integer cleanSearchAndGetNumberOfResults() {
		guestPage.enterSearchParameters("");
		Integer numberOfAllGuests = guestPage.getNumberOfAllGuestOnPage();
		if (!ProjectUtils.isNumberGreaterThan(numberOfAllGuests, 0)) {
			throw new NoSuchElementException("No guests found on admin guest page");
		}
		return numberOfAllGuests;
	}
	
}
