package config;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager extends DriverManager{
	
	@Override
	protected void startService() {
	}

	@Override
	protected void stopService() {
	}

	@Override
	protected void createDriver() {
		FirefoxOptions options = new FirefoxOptions();
		driver = new FirefoxDriver();
	}

}
