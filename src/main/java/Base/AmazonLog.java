package Base;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import utilities.CaptureScreenShotUtils;

public class AmazonLog {
	static Map<Long, ExtentTest> extentTestMap = new HashMap<Long, ExtentTest>();
	static Map<Long, ExtentTest> extentTestInfoMap = new HashMap<Long, ExtentTest>();
	static Map<Long, ExtentTest> extentTestStatusMap = new HashMap<Long, ExtentTest>();
	static Map<Long, ExtentReports> extentReportsTestMap = new HashMap<Long, ExtentReports>();
	static Map<Long, SoftAssert> softAssertMap = new HashMap<Long, SoftAssert>();

	/**
	 * Common Methods to generate extent Reports
	 * 
	 */

	public static synchronized void CreateReport(String ReportName, String suiteName) {
		ExtentReports extent = new ExtentReports();
		initializeSoftAsserts();
		boolean isSharedReport = AmazonConfig.isSharedReport();
		String sharedReportBasePath =AmazonConfig.getSharedPath();
		String sharedPath = "Results/";
		if (isSharedReport)
			sharedPath = sharedReportBasePath + "WebSiteAutomationResult_" + suiteName + "_";
		ExtentSparkReporter result = new ExtentSparkReporter(sharedPath + ReportName + ".html");
		extent.attachReporter(result);
		extentReportsTestMap.put(Thread.currentThread().getId(), extent);
		result.config().setTheme(Theme.STANDARD);
		result.config().setReportName(ReportName + "Automation Test Results");
	}

	/**
	 * To get current thread ExtentReports instance
	 * 
	 */

	public static synchronized ExtentReports getExtentReports() {
		return extentReportsTestMap.get(Thread.currentThread().getId());

	}

	public static synchronized void CreateTestStep(String value) {
		ExtentTest test = getExtentReports().createTest(value);
		ExtentTest infoNode = test.createNode("Test Execution Information Message");
		ExtentTest statusNode = test.createNode("Validation Status");

		extentTestInfoMap.put(Thread.currentThread().getId(), infoNode);
		extentTestStatusMap.put(Thread.currentThread().getId(), statusNode);
		extentTestMap.put(Thread.currentThread().getId(), test);

	}

	public static synchronized void initializeSoftAsserts() {
		softAssertMap.put(Thread.currentThread().getId(), new SoftAssert());
	}

	public static synchronized ExtentTest getTest() {
		return extentTestMap.get(Thread.currentThread().getId());
	}

	public static synchronized ExtentTest getTestStatusNode() {
		return extentTestStatusMap.get(Thread.currentThread().getId());
	}

	public static synchronized ExtentTest getStatusNode() {
		return extentTestStatusMap.get(Thread.currentThread().getId());
	}

	public static synchronized ExtentTest getTestInfoNode() {
		return extentTestInfoMap.get(Thread.currentThread().getId());
	}

	public static synchronized SoftAssert getSoftAsserts() {
		return softAssertMap.get(Thread.currentThread().getId());
	}

	/**
	 * To Manage data info extent Reports & Asserts
	 * 
	 */

	public static void Log(Status status, String message, String screenshotPath) {
		Throwable t=new Throwable("Throwable for : " + message);
		System.out.println("<<<<<< " + message + " >>>>>>");
		if (status == Status.INFO)
			getTestInfoNode().log(Status.INFO, message);
		else if (status == status.PASS) {
			getSoftAsserts().assertTrue(true, message);
			if (!screenshotPath.isBlank())
				getTestStatusNode().pass(message,
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			else
				getTestStatusNode().pass(message);
		} else if (status == Status.FAIL) {
			getSoftAsserts().assertTrue(false, message);
			if (!screenshotPath.isBlank())
				getTestStatusNode().fail(t,
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			else
				getTestStatusNode().fail(message);

		}

	}

	/**
	 * To Capture Screen Shot
	 */
	public static String captureScreenShot() {
		String fileName = RandomStringUtils.random(8) + ".jpg";
		TakesScreenshot takesScreenshot = (TakesScreenshot) AmazonBrowser.getWebDriver();
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		boolean isShareReport = false;
		String SharedReportBasePath = "";
		String sharedPath = "./Screenshots/" + (java.time.LocalDate.now()) + "/";
		if (isShareReport)
			sharedPath = SharedReportBasePath + "WebsiteAutomationScreenShot_" + (java.time.LocalDate.now()) + "/";
		File destFile = new File(sharedPath + "/" + fileName);
		try {
			FileUtils.copyFile(sourceFile, destFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return destFile.getAbsolutePath();

	}

	public static void Log(Status status, String message) {
		Log(status, message, " ");

	}
	public static void LogInfo(String messages) {
		Log(Status.INFO, messages);
	}
	public static void LogSuccess(String messages) {
		Log(Status.PASS, messages);
	}
	public static void LogFailure(String messages) {
		Log(Status.FAIL, messages);
	}
	public static void LogSuccessWithImage(String message) {
		Log(Status.PASS, message,captureScreenShot());
	}
	public static void LogFailureWithImage(String message) {
		Log(Status.FAIL, message,captureScreenShot());
	}

}

