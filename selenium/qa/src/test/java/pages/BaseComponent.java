package pages;

import org.openqa.selenium.WebDriver;

public abstract class BaseComponent extends AbstractBase{
	
	private WebDriver driver;
	
	public BaseComponent(WebDriver driver) {
		super(driver);
	}
	

}
