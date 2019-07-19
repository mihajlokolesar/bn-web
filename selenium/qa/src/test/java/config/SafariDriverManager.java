package config;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class SafariDriverManager extends DriverManager {

	@Override
	protected void startService() {
	}

	@Override
	public WebDriver getDriver() throws IOException {
		if (driver == null) {
			createDriver();
		}
		return driver;
	}

	@Override
	protected void stopService() {
	}

	@Override
	protected void createDriver() throws MalformedURLException {
		SafariOptions options = new SafariOptions();
		options.setCapability("browserstack.safari.enablePopups", true);
		driver = new SafariDriver();
	}

}
