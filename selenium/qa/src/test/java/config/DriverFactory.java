package config;

public class DriverFactory {


	private static String browser;
	
	public static DriverManager getDriverManager(String config, String environment) {
		DriverManager manager = null;
		
		try {
			browser = System.getProperty("browser");
			BrowsersEnum browserEnum = BrowsersEnum.getEnumForCode(browser);

			switch (browserEnum) {
			case FIREFOX:
				System.out.println("Firefox local");
				manager = new FirefoxDriverManager();
				break;
			case CHROME:
				System.out.println("Chrome local");
				manager = new ChromeDriverManager();
				break;
			case SAFARI:
				System.out.println("Safari local");
				manager = new SafariDriverManager();
				break;
			case REMOTE:
				System.out.println("Remoting");
				String username = System.getProperty("username");
				String accessKey = System.getProperty("key");
				String URL = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";
				manager = new RemoteDriverManager(URL, config, environment);
				break;
			default:
				System.out.println("Local default");
				manager = new FirefoxDriverManager();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return manager;

	}
}
