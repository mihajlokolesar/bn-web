package config;

import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
		if (config_file != null && !config_file.isEmpty()) {
			JSONParser parser = new JSONParser();
			JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/hub/"+ config_file));
			JSONObject envs = (JSONObject) config.get("environments");
	        Iterator it = envs.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String, String> pair = (Entry<String, String>) it.next();
	            System.out.println(pair.getKey()+ " " + pair.getValue());
	            capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
	        }
		} else {
			capabilities.setBrowserName(environment);
		}
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