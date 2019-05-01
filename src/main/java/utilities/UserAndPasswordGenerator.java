package utilities;

import org.apache.commons.lang.RandomStringUtils;

public class UserAndPasswordGenerator {

	public static void main(String[] args) {
		String generatedText = "";
		for (int i = 0; i < 2000; i++)
			if (i % 2 == 0) {
				generatedText = generatedText + "us_" + RandomStringUtils.randomAlphanumeric(10);
				generatedText = generatedText + ",";
			} else {
				generatedText = generatedText + "pa_" + RandomStringUtils.randomAlphanumeric(10);
				generatedText = generatedText + "\n";
			}

		System.out.println(generatedText);

	}

}
