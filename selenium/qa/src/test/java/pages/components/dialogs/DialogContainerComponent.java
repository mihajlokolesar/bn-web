package pages.components.dialogs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class DialogContainerComponent extends BaseComponent{
	
	@FindBy(xpath = "//body//div[@role='dialog' and @aria-labelledby='dialog-title']")
	private WebElement dialogContainer;

	public DialogContainerComponent(WebDriver driver) {
		super(driver);
	}
	
	public boolean isVisible() {
		return isExplicitlyWaitVisible(dialogContainer);
	}

}
