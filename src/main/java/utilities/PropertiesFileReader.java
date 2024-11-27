package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileReader {
	private static Properties prop = null;

	private static void loadpropValues() {
		String projectPath = System.getProperty("user.dir");
		String propFilePath = projectPath + "/src/main/java/Config/Config.properties";
		prop = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(propFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static String getValue(String key) {
		if(prop==null) {
			loadpropValues();
		}
	//	System.out.println(prop.getProperty(key));
		return prop.getProperty(key);
	}
	
	public static String getValue(String key,String defaultValue) {
		if(prop==null) {
			loadpropValues();
		}
		System.out.println(prop.getProperty(key,defaultValue));
		return prop.getProperty(key,defaultValue);
	}
}
