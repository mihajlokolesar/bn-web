package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class ManualAddressEntryComponent extends BaseComponent {
	
	public static final String containerXpath = "//form//div[@style='min-height: 0px; height: auto; transition-duration: 300ms;']";
	
	@FindBy(id = "address")
	private WebElement addressField;

	@FindBy(id = "city")
	private WebElement cityField;

	@FindBy(id = "state")
	private WebElement stateField;

	@FindBy(id = "country")
	private WebElement country;

	@FindBy(id = "zip")
	private WebElement zip;

	@FindBy(id = "latitude")
	private WebElement latitude;

	@FindBy(id = "longitude")
	private WebElement longitude;

	public ManualAddressEntryComponent(WebDriver driver) {
		super(driver);
	}
	
	public boolean checkIfCoordinatesAreFilled() {
		boolean isLatitudeFilled = isElementValueAttributePresent(latitude);
		boolean isLongitudeFilled = isElementValueAttributePresent(longitude);
		return isLatitudeFilled && isLongitudeFilled;
	}

	private boolean isElementValueAttributePresent(WebElement inputElement) {
		explicitWaitForVisiblity(inputElement);
		String text = inputElement.getAttribute("value");
		if (text == null || text.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

}