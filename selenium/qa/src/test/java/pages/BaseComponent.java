package pages;

import org.openqa.selenium.WebDriver;

public abstract class BaseComponent {
	
	private WebDriver driver;
	
	public BaseComponent(WebDriver driver) {
		this.driver = driver;
	}
	

}
