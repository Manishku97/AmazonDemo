package Base;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;

public class AmazonExecutionHandler implements ITestListener {

	
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
//        if (exception != null) {
//            AmazonLog.getTest().fail(exception);
//        }
    }

    @Override
    public void onTestStart(ITestResult result) {}
    

    @Override
    public void onTestSuccess(ITestResult result) {}

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(org.testng.ITestContext context) {}

    @Override
    public void onFinish(org.testng.ITestContext context) {}


}

