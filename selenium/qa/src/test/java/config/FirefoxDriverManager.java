package config;

import java.util.logging.Level;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

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
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.SEVERE);
		options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		driver = new FirefoxDriver(options);
	}

}
