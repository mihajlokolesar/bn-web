package test;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import config.DriverFactory;

public class BaseSteps {
	
	public WebDriver driver;
	
	@BeforeMethod(alwaysRun = true)
	@Parameters(value = { "config", "environment"})
	public void setUp(@Optional String config, @Optional String environment) throws Exception {
		driver = DriverFactory.getDriverManager(config, environment).getDriver();
		
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}
