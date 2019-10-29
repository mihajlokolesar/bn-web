package config;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HubRemoteDriverManager extends DriverManager {
	
	private String url;
	private String config_file;
	private String environment;

	public HubRemoteDriverManager(String url, String config_file, String environment) {
		super();
		this.url = url;
		this.config_file = config_file;
		this.environment = environment;
	}

	@Override
	protected void startService() {
	}

	@Override
	protected void stopService() {		
	}
	
	public RemoteWebDriver createRemoteWebDriver(String config_file, String environment) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setBrowserName(environment);
		return new RemoteWebDriver(new URL(this.url), capabilities);
	}

	@Override
	protected void createDriver() throws MalformedURLException, Exception {
		driver = createRemoteWebDriver(this.config_file, this.environment);
	}

	@Override
	public WebDriver getDriver() throws Exception {
		if (driver == null) {
			startService();
			createDriver();
		}
		return driver;
	}
}