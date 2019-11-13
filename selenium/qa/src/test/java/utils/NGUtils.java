package utils;

import org.testng.asserts.SoftAssert;

public class NGUtils {
	
	public static SoftAssert getSoftAssert() {
		return new SoftAssert();
	}
}