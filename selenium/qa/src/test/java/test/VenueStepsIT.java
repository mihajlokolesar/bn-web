package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import model.User;
import model.Venue;
import test.facade.LoginStepsFacade;
import test.facade.VenueStepsFacade;
import utils.DataConstants;

public class VenueStepsIT extends BaseSteps {

	@Test(dataProvider = "create_venue_data", priority = 22, retryAnalyzer = utils.RetryAnalizer.class)
	public void createVenue(Venue venue, User superuser) {
		LoginStepsFacade loginFacade = new LoginStepsFacade(driver);
		VenueStepsFacade venueStepsFacade = new VenueStepsFacade(driver);
		maximizeWindow();

		loginFacade.givenAdminUserIsLogedIn(superuser);
		venueStepsFacade.givenUserIsOnVenuesPage();
		venueStepsFacade.givenUserIsOnCreateVenuePage();

		venueStepsFacade.whenUserFillsOutVenueInfo(venue);
		boolean isCoordinatesFilled = venueStepsFacade.thenCoordinatesShouldBeFilled();
		venueStepsFacade.whenUserClicksOnCreateVenue();

		boolean isNotificationVisible = venueStepsFacade.thenNotificationVenueCreatedShouldBeVisible();
		Assert.assertTrue(isNotificationVisible && isCoordinatesFilled);

		loginFacade.logOut();
	}

	@DataProvider(name = "create_venue_data")
	private static Object[][] createVenueData() {
		Venue venue = Venue.generateVenueFromJson(DataConstants.VENUE_STANDARD_KEY);
		User superuser = User.generateSuperUser();
		return new Object[][] { { venue, superuser } };
	}

	@Test(dataProvider = "edit_venue_data", priority = 23, retryAnalyzer = utils.RetryAnalizer.class)
	public void updateVenue(Venue venue, User superuser) {
		LoginStepsFacade loginStepsFacade = new LoginStepsFacade(driver);
		VenueStepsFacade venueStepsFacade = new VenueStepsFacade(driver);
		maximizeWindow();

		loginStepsFacade.givenAdminUserIsLogedIn(superuser);
		venueStepsFacade.givenUserIsOnVenuesPage();
		
		venueStepsFacade.whenUserClicksOnEditButtonOfSelectedVenue(venue.getName());
		venueStepsFacade.whenUserUpdatesVenue(venue);
		boolean isCoordinatesFilled = venueStepsFacade.thenCoordinatesShouldBeFilled();
		venueStepsFacade.whenUserClickOnUpdateButtonOnEditPage();

		boolean isNotificationVisible = venueStepsFacade.thenNotificationVenueUpdatedShoudBeVisible();
		boolean isAtVenuesPage = venueStepsFacade.thenUserIsOnVenuesPage();

		Assert.assertTrue(isNotificationVisible && isAtVenuesPage && isCoordinatesFilled);

		loginStepsFacade.logOut();

	}

	@DataProvider(name = "edit_venue_data")
	private static Object[][] editVenueData() {
		Venue venue = Venue.generateVenueFromJson(DataConstants.VENUE_STANDARD_KEY);
		User superuser = User.generateSuperUser();
		return new Object[][] { { venue, superuser } };
	}

}
