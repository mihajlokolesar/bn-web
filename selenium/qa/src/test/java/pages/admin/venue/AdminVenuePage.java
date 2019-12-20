package pages.admin.venue;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.admin.venues.AdminVenueComponent;
import utils.Constants;

public class AdminVenuePage extends BasePage {

	@FindBy(xpath = "//a[@href='/admin/venues/create']/button[span[text()='Create venue']]")
	private WebElement createVenueButton;

	@FindAll({
			@FindBy(xpath = "//main//div[div[p[contains(text(),'Venues')]]]/following-sibling::div/div[not(a[@href='/admin/venues/create'])]") })
	private List<WebElement> venuesList;

	public AdminVenuePage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminVenues());
	}

	public AdminVenueComponent findVenue(Predicate<AdminVenueComponent> predicate) {
		Optional<AdminVenueComponent> optional = venuesList.stream().map(v -> new AdminVenueComponent(driver, v))
				.filter(predicate).findFirst();
		return optional.isPresent() ? optional.get() : null;
	}

	public boolean isVenueWithNamePresent(String venueName) {
		return isExplicitlyWaitVisible(4, findVenuesByXpathWithName(venueName));
	}
	
	public AdminVenueComponent findVenueByName(String venueName) {
		WebElement venueEl = findVenueElementWithName(venueName);
		if (venueEl == null) {
			return null;
		} else {
			return new AdminVenueComponent(driver, venueEl);
		}
	}
	
	public List<AdminVenueComponent> findVenuesByName(String venueName) {
		List<WebElement> elements = findVenueElementsWithName(venueName);
		return elements.stream().map(v -> new AdminVenueComponent(driver, v)).collect(Collectors.toList());
	}

	private List<WebElement> findVenueElementsWithName(String venueName) {
		List<WebElement> list = explicitWaitForVisiblityForAllElements(findVenuesByXpathWithName(venueName));
		return list;
	}
	
	private WebElement findVenueElementWithName(String venueName) {
		WebElement el = explicitWait(15, ExpectedConditions.visibilityOfElementLocated(findVenuesByXpathWithName(venueName)));
		return el;
	}

	private By findVenuesByXpathWithName(String venueName) {
		return By.xpath(
		"//main//div[div[p[contains(text(),'Venues')]]]/following-sibling::div/div[div[div[h1[contains(text(),'" + venueName + "')]]]]");
	}

	public void clickOnCreateVenueButton() {
		explicitWaitForVisibilityAndClickableWithClick(createVenueButton);
	}

}