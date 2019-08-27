package test.facade;

import org.openqa.selenium.WebDriver;

public class BaseFacadeSteps {
	
	protected WebDriver driver;
	
	public BaseFacadeSteps(WebDriver driver) {
		this.driver = driver;
	}

}
