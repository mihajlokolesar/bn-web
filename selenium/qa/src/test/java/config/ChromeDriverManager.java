package config;

import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

public class ChromeDriverManager extends DriverManager {

	@Override
	protected void startService() {
	}

	@Override
	protected void stopService() {
	}

	@Override
	protected void createDriver() {
		ChromeOptions options = new ChromeOptions();
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.SEVERE);
		options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		driver = new ChromeDriver(options);
		
	}

}
