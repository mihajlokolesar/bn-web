package utils;

import java.util.Random;

public class ProjectUtils {
	
	public static Integer generateRandomInt(int size) {
		Random random = new Random();
		return random.nextInt(size);
	}

}
