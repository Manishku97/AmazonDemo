package Base;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;



public class BaseTestAuto extends AmazonBrowser implements IHookable {
	
    private static final String SOFT_ASSERT="softAssert";

	
	@BeforeClass
	//To open Browser and Launch Website
	public void beforeClass(ITestContext tc) {
		
		String suitName=null!=tc.getName()?tc.getName():"testsuite";
		String reportName=this.getClass().getSimpleName() + Thread.currentThread().getId();
		System.out.println(reportName);
		AmazonLog.CreateReport(reportName, suitName);
		AmazonLog.CreateTestStep("Test Execution for "+reportName);
	//	openBrowser();
	}
	@AfterClass
	//Quit after Browser Opens
	public void quitDriver() {
//		AmazonLog.getExtentReports().flush();
//		quitBrowser();
		
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
