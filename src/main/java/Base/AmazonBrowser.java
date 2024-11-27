package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 *  common methods to manage Driver & wait
 *  @Author Manish.Yadav
 * 
 * */

public class AmazonBrowser implements IHookable {
	public static URL url = null;
	private static final String CONFIG_PATH = "/src/main/java/Config/Config.properties";
    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;
    private static final int DEFAULT_IMPLICIT_WAIT = 10;
	private static final String SOFT_ASSERT = "softAssert";

//	static LaunchWebsite wb=new LaunchWebsite();

	static Map<Long, WebDriver> webDriverTestMap = new HashMap<Long, WebDriver>();
	static Map<Long, AppiumDriver> appiumDriverTestMap = new HashMap<Long, AppiumDriver>();
	static Map<Long, WebDriverWait> webDriverwaitTestMap = new HashMap<Long, WebDriverWait>();
	static Map<Long, WebDriverWait> webDriverMiniwaitTestMap = new HashMap<Long, WebDriverWait>();
	static Map<Long, WebDriverWait> appiumDriverwaitTestMap = new HashMap<Long, WebDriverWait>();

	/**
	 * To get Driver for current Thread
	 */

	public static synchronized WebDriver getWebDriver() {
		if (null != webDriverTestMap && null != webDriverTestMap.get(Thread.currentThread().getId()))
			return webDriverTestMap.get(Thread.currentThread().getId());
		else
			openBrowser();
		return webDriverTestMap.get(Thread.currentThread().getId());
	}

	/**
	 * To set WebDriver for current Thread
	 * 
	 */
	private static synchronized void setWebDriver(WebDriver driver) {
		webDriverTestMap.put(Thread.currentThread().getId(), driver);
	}

	/**
	 * To get appium Driver for current Thread
	 */

	public static synchronized WebDriver getAppiumDriver() {
		if (null != appiumDriverTestMap && null != appiumDriverTestMap.get(Thread.currentThread().getId()))
			return appiumDriverTestMap.get(Thread.currentThread().getId());
		else

			return appiumDriverTestMap.get(Thread.currentThread().getId());
	}

	/**
	 * To set appiumDriver for current Thread
	 * 
	 */
	private static synchronized void setAppiumDriver(AppiumDriver appiumdriver) {
		appiumDriverTestMap.put(Thread.currentThread().getId(), (AndroidDriver) appiumdriver);
	}

	/**
	 * To set appiumDriver wait for current Thread
	 * 
	 */
	private static synchronized void setAppiumWait(WebDriverWait appiumwait) {
		appiumDriverwaitTestMap.put(Thread.currentThread().getId(), appiumwait);
	}

	/**
	 * To get appiumDriver wait for current Thread
	 * 
	 */
	public static synchronized WebDriverWait getAppiumWait() {
		return appiumDriverwaitTestMap.get(Thread.currentThread().getId());
	}

	/**
	 * To set WebDriver wait for current Thread
	 * 
	 */
	private static synchronized void setWait(WebDriverWait wait) {
		webDriverwaitTestMap.put(Thread.currentThread().getId(), wait);
	}

	/**
	 * To get WebDriver wait for current Thread
	 * 
	 */
	public static synchronized WebDriverWait getWait() {
		return webDriverwaitTestMap.get(Thread.currentThread().getId());
	}

	/**
	 * To get WebDriver Mini wait for current Thread
	 * 
	 */
	public static synchronized WebDriverWait getMiniWait() {
		return webDriverMiniwaitTestMap.get(Thread.currentThread().getId());
	}


	    public static void openBrowser() {
	        Properties prop = new Properties();
	        String projectPath = System.getProperty("user.dir");

	        // Load properties file with try-with-resources
	        try (InputStream input = new FileInputStream(projectPath + CONFIG_PATH)) {
	            prop.load(input);
	        } catch (IOException e) {
	            System.err.println("Error loading configuration file: " + e.getMessage());
	            return;
	        }

	        // Get browser property or fallback to default
	        String browser = prop.getProperty("Browser", "chrome");
	        String url=prop.getProperty("WebsiteURL");
	        int pageLoadTimeout = Integer.parseInt(prop.getProperty("PageLoadTimeOut", String.valueOf(DEFAULT_PAGE_LOAD_TIMEOUT)));
	        int implicitWait = Integer.parseInt(prop.getProperty("ImplicitWait", String.valueOf(DEFAULT_IMPLICIT_WAIT)));

	        WebDriver driver = null;

	        try {
	            switch (browser.toLowerCase()) {
	                case "chrome":
	                    ChromeOptions chromeOptions = new ChromeOptions();
	                    chromeOptions.setAcceptInsecureCerts(true); // Accept untrusted certificates
	                    WebDriverManager.chromedriver().setup();
	                    driver = new ChromeDriver(chromeOptions);
	                    break;

	                case "edge":
	                    EdgeOptions edgeOptions = new EdgeOptions();
	                    edgeOptions.setAcceptInsecureCerts(true); // Accept untrusted certificates
	                    WebDriverManager.edgedriver().setup();
	                    driver = new EdgeDriver(edgeOptions);
	                    break;

	                default:
	                    throw new IllegalArgumentException("Unsupported browser: " + browser);
	            }

	            // Configure WebDriver
	            driver.manage().window().maximize();
	            driver.manage().deleteAllCookies();
	            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
	            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

	            // Set WebDriver and navigate to the URL
	            setWebDriver(driver);
	            setWait(new WebDriverWait(driver, Duration.ofSeconds(pageLoadTimeout)));
	            driver.navigate().to(url);

	        } catch (Exception e) {
	            System.err.println("Error during browser setup: " + e.getMessage());
	            if (driver != null) {
	                driver.quit();
	            }
	        }
	    }
       //Quit Browser
	    public static void quitBrowser() {
	        try {
	            WebDriver driver = getWebDriver();
	            if (driver != null) {
	                driver.close();
	                driver.quit();
	            }
	        } catch (Exception e) {
	            System.err.println("Error quitting browser: " + e.getMessage());
	        }
	    }
	    
	    
		@Override
		public void run(IHookCallBack callBack, ITestResult testResult) {
			// TODO Auto-generated method stub
			testResult.setAttribute(SOFT_ASSERT, testResult);
			callBack.runTestMethod(testResult);
			try {
				AmazonLog.getSoftAsserts().assertAll();
			} catch (AssertionError e) {
				testResult.setThrowable(e);
				testResult.setStatus(ITestResult.FAILURE);
			}
		}

}
	

	
