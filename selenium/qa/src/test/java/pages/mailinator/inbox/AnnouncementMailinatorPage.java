package pages.mailinator.inbox;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import model.AnnouncementMail;
import model.Event;
import model.Venue;
import pages.components.mailframes.AnnouncementMailFrame;
import utils.formatter.VenueFormatter;

public class AnnouncementMailinatorPage extends MailinatorInboxPage{
	
	public static final String MAIL_KEY = "mail_key";
	public static final String FIXTURE_EVENT_KEY = "event_key";
	public static final String SOFT_ASSERT_KEY = "soft_assert_key";
	public static final String FULL_REFUND_KEY = "full_refund_key";
	
	public AnnouncementMailinatorPage(WebDriver driver) {
		super(driver);
	}
	
	public void openMailAndCheckValidity(Map<String,Object> data) {
		AnnouncementMail mail = (AnnouncementMail) data.get(MAIL_KEY);
		By by = By.xpath(".//table//tbody//tr[td[contains(text(),'Big Neon')] and td/a[contains(text(),'" +
				mail.getSubject() + "')]]/td[contains(text(),'Big Neon')]");
		try {
			goToMail(by);
		} catch (Exception e) {
			boolean isFullRefund = (boolean) data.get(FULL_REFUND_KEY);
			if (isFullRefund) {
				SoftAssert sa = (SoftAssert) data.get(SOFT_ASSERT_KEY);
				sa.assertTrue(true);
				return;
			} else {
				throw e;
			}
		}
		isCorrectMail(data);
		deleteMail();
	}
	
	public void isCorrectMail(Map<String,Object> data) {
		AnnouncementMail mail = (AnnouncementMail) data.get(MAIL_KEY);
		SoftAssert softAssert = (SoftAssert) data.get(SOFT_ASSERT_KEY);
		Event fixtureEvent = (Event) data.get(FIXTURE_EVENT_KEY);
		checkMessagePageAndSwitchToFrame();
		AnnouncementMailFrame frame = new AnnouncementMailFrame(driver);
		String eventInfo = frame.getEventInformation();
		String[] eventInfoTokens = eventInfo.split("\\n");
		softAssert.assertTrue(eventInfoTokens[0].equalsIgnoreCase(fixtureEvent.getEventName()));
		
		String locationInfo = frame.getLocationInfo();
		String replacedVenueInfo = locationInfo.replaceAll("\\n", ", ");
		VenueFormatter formater = new VenueFormatter("N, L, C, Sa, Z");
		Venue parsedVenue = formater.parse(replacedVenueInfo);
		Venue fixtureVenue = fixtureEvent.getVenue();
		softAssert.assertTrue(parsedVenue.getName().equalsIgnoreCase(fixtureVenue.getName()), "Venue name not the same");
		softAssert.assertTrue(fixtureVenue.getAddress().contains(parsedVenue.getAddress()), "Venue address not the same");
		softAssert.assertTrue(parsedVenue.getCity().equalsIgnoreCase(fixtureVenue.getCity()), "Venue city not the same");
		softAssert.assertTrue(parsedVenue.getStateAbbr().equalsIgnoreCase(fixtureVenue.getStateAbbr()), "Venue State abbrivation not the same");
		softAssert.assertTrue(frame.isBodyContentValid(mail.getBody()), "Body content of mail not what expected");
	}

}
