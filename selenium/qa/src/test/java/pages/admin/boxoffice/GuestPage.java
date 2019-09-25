package pages.admin.boxoffice;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BasePage;
import utils.Constants;
import utils.SeleniumUtils;

public class GuestPage extends BasePage {

	@FindBy(xpath = "//main//div//input[@name='Search']")
	private WebElement searchField;

	@FindBy(xpath = "//main/div/div/div")
	private WebElement container;

	public GuestPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getBoxOfficeGuest());
	}

	public void enterSearchParameters(String value) {
		SeleniumUtils.clearInputField(searchField, driver);
		waitVisibilityAndSendKeys(searchField, value);
		waitForTime(3000);
	}
	
	public Integer getNumberOfAllGuestOnPage() {
		return searchForAllGuestOnPage().size();
	}

	public List<WebElement> searchForAllGuestOnPage() {
		List<WebElement> elements = container.findElements(By.xpath(".//img[contains(@src,'down-active.svg')]"));
		return elements;
	}
	
	public Integer getNumberOfResultsOfSearch(String searchedValue) {
		return searchForResultsOfSearch(searchedValue).size();
	}

	public List<WebElement> searchForResultsOfSearch(String searchedValue) {
		List<WebElement> elements = container
				.findElements(By.xpath("./div[div//p[contains(text(),'" + searchedValue + "')]]"));
		return elements;
	}
	
	public String getTicketNumber(String searchedValue) {
		WebElement element = container.findElement(
				By.xpath(".//div[p[contains(text(),'" + searchedValue + "')]]/following-sibling::div/span/p"));
		String ticketNumber = element.getText();
		if (ticketNumber != null) {
			String[] ticketNumbers = ticketNumber.split(",");
			ticketNumber = ticketNumbers[0].trim();
			ticketNumber = ticketNumber.replace("#", "");
		}
		return ticketNumber;
	}
	
	public boolean isTicketNumberInGuestResults(String ticketNumber) {
		List<WebElement> elements = container
				.findElements(By.xpath(".//div/span/p[contains(text(),'" + ticketNumber + "')]"));
		if (elements != null && !elements.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}
