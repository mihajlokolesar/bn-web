package config;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class RemoteDriverManager extends DriverManager {

	private String url;
	private String config_file;
	private String environment;

	public RemoteDriverManager(String url, String config_file, String environment) {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RemoteWebDriver createRemoteDriver(String config_file, String environment) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/"+ config_file));
		JSONObject envs = (JSONObject) config.get("environments");
		DesiredCapabilities capabilities = new DesiredCapabilities();

        Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
        Iterator it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Entry<String, String>) it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
        }
        
        Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
        it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (capabilities.getCapability(pair.getKey().toString()) == null) {
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }
        ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--whitelisted-ips");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-extensions");

		MutableCapabilities capabilities1 = new MutableCapabilities(chromeOptions);
        return new RemoteWebDriver(new java.net.URL(url), capabilities1);
        
	}

	@Override
	protected void createDriver() throws Exception {
		driver = createRemoteDriver(this.config_file, this.environment);
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
