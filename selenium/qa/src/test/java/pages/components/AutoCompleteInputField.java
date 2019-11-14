package pages.components;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class AutoCompleteInputField extends BaseComponent {

	private WebElement inputField;

	private String dropDownElementsXpath = "//div[contains(@class,'autocomplete-dropdown-container')]/div[contains(@class,'suggestion-item')]";

	public AutoCompleteInputField(WebDriver driver, WebElement inputField) {
		super(driver);
		this.inputField = inputField;
	}

	/**
	 * Splis the full addres name, as provided by autocomplete on "comma" as
	 * separator example fullName = "Metropolitan Opera House, Lincoln Center Plaza,
	 * New York, NY, USA" will result in string array of 5 elements then it goes
	 * through the list and enters first value in this case "Metropolitan Opera
	 * House" and looks through the offered list of suggestions trying to find a
	 * match with fullName value.
	 * 
	 * @param fullName
	 */
	public void selectFromAutocomplete(String fullName) {
		getAccessUtils().clearInputField(inputField);
		explicitWaitForVisiblity(inputField);
		waitForTime(2000);
		search(fullName);
	}

	private void search(String fullName) {
		String[] ar = fullName.split(",");
		for (String str : ar) {
			String send = str.trim() + ",";
			inputField.sendKeys(send);
			boolean isAutoCompleteVisible = isExplicitlyWaitVisible(5, By.xpath(dropDownElementsXpath));
			if (isAutoCompleteVisible) {
				List<WebElement> list = explicitWait(5,
						ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(dropDownElementsXpath)));
				WebElement el = searchForText(list, fullName);
				if (el != null) {
					waitForTime(1000);
					waitVisibilityAndBrowserCheckClick(el);
					return;
				}
			}

		}
	}

	private WebElement searchForText(List<WebElement> list, String fullText) {
		for (WebElement e : list) {
			if (fullText.equalsIgnoreCase(e.getText())) {
				return e;
			}
		}
		return null;
	}

}