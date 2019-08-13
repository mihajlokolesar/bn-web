package config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
		System.setProperty("webdriver.chrome.driver","/Users/simpletask/Desktop/mihajlo/drivers/chromedriver");
		driver = new ChromeDriver();
		
	}

}
