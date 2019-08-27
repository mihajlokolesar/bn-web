package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class GenericDropDown extends BaseComponent{
	
	private WebElement activateDropDown;
	
	private WebElement dropDownContainer;

	public GenericDropDown(WebDriver driver, WebElement activateDropDown, WebElement dropDownContainer) {
		super(driver);
		this.activateDropDown = activateDropDown;
		this.dropDownContainer = dropDownContainer;
	}
	
	public void selectElementFromDropDown(By relativeToContainer, String value) {
		waitVisibilityAndClick(activateDropDown);
		explicitWaitForVisiblity(dropDownContainer);
		WebElement selectedElement = dropDownContainer.findElement(relativeToContainer);
		explicitWaitForVisiblity(selectedElement);
		explicitWaitForClickable(selectedElement);
		waitForTime(500);
		selectedElement.click();
		explicitWait(5, ExpectedConditions.attributeToBe(activateDropDown, "value", value));
	}
	
	public void selectElementFromDropDownHiddenInput(By relativeToContainer, String value) {
		waitForTime(500);
		waitVisibilityAndClick(activateDropDown);
		explicitWaitForVisiblity(dropDownContainer);
		WebElement selectedElement = dropDownContainer.findElement(relativeToContainer);
		explicitWaitForVisiblity(selectedElement);
		explicitWaitForClickable(selectedElement);
		waitForTime(500);
		selectedElement.click();
		explicitWait(5, ExpectedConditions.textToBePresentInElement(activateDropDown, value));
	}

}
