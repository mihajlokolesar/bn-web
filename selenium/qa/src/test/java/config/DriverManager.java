package config;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

public abstract class DriverManager {
	
	public static final String USERNAME = "mihajlokolesar1";
	public static final String AUTOMATE_KEY = "txnsAiFEM4HVNzswBFtY";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
	private String os = System.getProperty("os.name");
	private final String DRIVERS_BASE_PATH_KEY = "webdrives.base.path";
	public String driversBasePathValue;
	public String extension = "";
	public String osPath;
	
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
//			setDriverBasePathValue("src/test/resources/drivers/");
//			setExtension();
			startService();
			createDriver();
		}
		return driver;
	}
	
	private void setDriverBasePathValue(String value) {
		this.driversBasePathValue = value;
	}
	
	private void setExtension() {
		if (os.contains("Windows")) {
			osPath = "windows";
			extension = ".exe";
		} else if (os.contains("Linux")) {
			osPath = "linux";
		} else {
			osPath = "default";
		}
	}

}
