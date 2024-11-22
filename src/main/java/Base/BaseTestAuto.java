package Base;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;


@Listeners(Base.AmazonExecutionHandler.class)

public class BaseTestAuto extends AmazonBrowser   {

	private static final String SOFT_ASSERT = "softAssert";

	@BeforeClass
	// To open Browser and Launch Website
	public void beforeClass(ITestContext tc) {
		// Get the suite name, fallback to "testsuite" if not available
		String suiteName = null != tc.getName() ? tc.getName() : "testsuite";

		// Get the class name
		String className = this.getClass().getSimpleName();

		// Generate a unique report name by appending a timestamp
//      String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
//      String reportName = className + "_" + timestamp;
		String reportName = className;

		// Log the report name (for debugging purposes)
		System.out.println(reportName);

		// Create a unique report and initialize the test
		AmazonLog.CreateReport(reportName, suiteName);
		AmazonLog.CreateTestStep("Test Execution for " + reportName);
		// Open the browser
		openBrowser();
	}

	@AfterClass
	// Quit after Browser Opens
	public void quitDriver() {
		AmazonLog.getExtentReports().flush();
		quitBrowser();

	}

	
	
	
	
//	@Override
//	public void run(IHookCallBack callBack, ITestResult testResult) {
//		// TODO Auto-generated method stub
//		testResult.setAttribute(SOFT_ASSERT, testResult);
//		callBack.runTestMethod(testResult);
//		try {
//			AmazonLog.getSoftAsserts().assertAll();
//		} catch (AssertionError e) {
//			testResult.setThrowable(e);
//			testResult.setStatus(ITestResult.FAILURE);
//		}

//	}
//	@Override
//	public void run(IHookCallBack callBack, ITestResult testResult) {
//	    testResult.setAttribute(SOFT_ASSERT, testResult);
//	    callBack.runTestMethod(testResult);
//
//	    if (testResult.getStatus() == ITestResult.FAILURE) {
//	        // Capture the exception
//	        Throwable exception = testResult.getThrowable();
//	        String exceptionMessage = exception != null ? exception.toString() : "No Exception";
//
//	        // Capture the screenshot
//	        String screenshotPath = AmazonLog.captureScreenShot();
//
//	        // Log the failure in the report
//	        try {
//	            AmazonLog.getTest().fail(
//	                "Test Failed: " + exceptionMessage,
//	                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
//	            );
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            AmazonLog.getTest().fail("Failed to attach screenshot: " + e.getMessage());
//	        }
//
//	        // Optionally log stack trace
//	        if (exception != null) {
//	            AmazonLog.getTest().fail(exception);
// 	        }
//	    }
//
//	    try {
//	        AmazonLog.getSoftAsserts().assertAll();
//	    } catch (AssertionError e) {
//	        testResult.setThrowable(e);
//	        testResult.setStatus(ITestResult.FAILURE);
//	    }
//	}
	
}
