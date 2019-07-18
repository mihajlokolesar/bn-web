package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.OperationNotSupportedException;

public class AppProperties {

	private static Properties properties;

	private static volatile AppProperties instance;

	private AppProperties() {
		try {
			String user = System.getProperty("user.dir");
			File file = new File(user + "/src/test/resources/test.properties");
			InputStream in = new FileInputStream(file);
			properties = new Properties();
			properties.load(in);
		} catch (Exception e) {
//			TODO: create logging system
			e.printStackTrace();
		}
	}

	public static AppProperties getInstance() {
		if (instance == null) {
			synchronized (AppProperties.class) {
				instance = new AppProperties();
			}
		}
		return instance;
	}

	public String getProperty(String name) throws OperationNotSupportedException {
		System.out.println("property name: " + name);
		if (properties != null) {
			return properties.getProperty(name);
		} else {
			throw new OperationNotSupportedException("properies not initialized");
		}
	}

}
