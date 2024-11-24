package Base;

import utilities.PropertiesFileReader;

public class AmazonConfig {
	
	public String websiteURL;
	public String userName;
	public String password;
	
	
	
	public static String getEnvironment() {
			return PropertiesFileReader.getValue("Environment");
	}
	public static String getWebsiteUrl() {
		return PropertiesFileReader.getValue("WebsiteURL");
		
	}
	public static boolean isSharedReport() {
		return PropertiesFileReader.getValue("SharedReport").equalsIgnoreCase("Y");
	}
	public static String getSharedPath() {
		return PropertiesFileReader.getValue("SharedPath");
	}
	public static String getBrowserName() {
		return PropertiesFileReader.getValue("Browser");
	}
	

}
