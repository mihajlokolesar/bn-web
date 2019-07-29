package pages;

import java.util.function.Function;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractBase {
	
	public WebDriver driver;
	
	public AbstractBase(WebDriver driver) {
		this.driver = driver;
		
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	@SuppressWarnings("unchecked")
	public <T, V> T explicitWait(int time, long poolingInterval, Function<? super WebDriver, V> condition) throws TimeoutException {
		return (T) new WebDriverWait(driver, time, poolingInterval).until(condition);
	}
	
	public <T, V> T explicitWait(int time, Function<? super WebDriver, V> condition) throws TimeoutException {
		return explicitWait(time, 500, condition);
	}
	
	@SuppressWarnings("unchecked")
	public <T, V> T explicitWaitNoPooling(int time, Function<? super WebDriver, V> condition) throws TimeoutException{
		return (T) new WebDriverWait(driver, time).until(condition);
	}

}
