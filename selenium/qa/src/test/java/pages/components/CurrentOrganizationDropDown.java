package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;

public class CurrentOrganizationDropDown extends BaseComponent {

	public CurrentOrganizationDropDown(WebDriver driver) {
		super(driver);
	}

	public WebElement findOrganizationByName(String organizationName) {
		WebElement element = driver
				.findElement(By.xpath("//div[@id='menu-appbar']//ul//li[text()='" + organizationName + "']"));
		return element;
	}

	public void selectOrganizationByName(String organizationName) {
		WebElement organization = findOrganizationByName(organizationName);
		explicitWaitForVisiblity(organization);
		waitForTime(2, 1000);
		organization.click();
	}

}
