package config;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

public abstract class DriverManager {
	
	protected WebDriver driver;
	protected abstract void startService();
	protected abstract void stopService();
	protected abstract void createDriver() throws MalformedURLException, Exception;
	
	
	public void quitDriver() {
		if (driver !=  null) {
			driver.quit();
			driver = null;
		}
	}
	
	public WebDriver getDriver() throws Exception {
		if (driver == null) {
			startService();
			createDriver();
		}
		return driver;
	}
	

}
