package Base;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.IHookCallBack;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.lang.annotation.Annotation;

import com.aventstack.extentreports.MediaEntityBuilder;

import utilities.TestInfo;
import utilities.TestInfoContext;

public class AmazonExecutionHandler implements ITestListener {
	private static final String SOFT_ASSERT = "softAssert";

	
    @Override
    public void onTestFailure(ITestResult result) {
        // Capture exception details
        Throwable exception = result.getThrowable();
        String exceptionMessage = exception != null ? exception.toString() : "No Exception";

        // Capture screenshot
        String screenshotPath = AmazonLog.captureScreenShot();

        // Log the failure in the Extent Report
        try {
            AmazonLog.getTest().fail(
                "Test Failed: " + exceptionMessage,
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            AmazonLog.getTest().fail("Failed to attach screenshot: " + e.getMessage());
        }

        // Optionally log stack trace
        if (exception != null) {
            AmazonLog.getTest().fail(exception);
        }
    }

    // Other ITestListener methods can remain empty or be implemented as needed
    @Override
    public void onTestStart(ITestResult result) {
    	  // Get the test method and extract the annotation
        Annotation[] annotations = result.getMethod().getConstructorOrMethod().getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof TestInfo) {
                TestInfo testInfo = (TestInfo) annotation;
                
                // Store TestInfo in a static holder for later access
                TestInfoContext.setTestInfo(testInfo);
            }
        }
    }
    

    @Override
    public void onTestSuccess(ITestResult result) {}

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    	// Set the SOFT_ASSERT attribute to the test result
        result.setAttribute(SOFT_ASSERT, result);

        // Run the test method
        IHookCallBack callBack = (IHookCallBack) result.getAttribute("callBack");
        if (callBack != null) {
            callBack.runTestMethod(result);
        }

        // Validate soft assertions
        try {
            AmazonLog.getSoftAsserts().assertAll();
        } catch (AssertionError e) {
            // Log the failure and set the test result status to failure
            result.setThrowable(e);
            result.setStatus(ITestResult.FAILURE);
        }
    }

    @Override
    public void onStart(org.testng.ITestContext context) {}

    @Override
    public void onFinish(org.testng.ITestContext context) {}


}

