package Base;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.MediaEntityBuilder;

import Amazon.utilities.EmailSendingUtils;
import Amazon.utilities.TestInfo;
import Amazon.utilities.TestInfoContext;
import Amazon.utilities.TestSummary;

import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;


@Listeners(Base.AmazonExecutionHandler.class)

public class BaseTestAuto extends AmazonBrowser{
	
	
	@BeforeClass
	// To open Browser and Launch Website
	public void beforeClass(ITestContext tc) {
		// Get the suite name, fallback to "testsuite" if not available
		String suiteName = null != tc.getName() ? tc.getName() : "testsuite";

		// Get the class name
		String className = this.getClass().getSimpleName();

		// Generate a unique report name by appending a timestamp
		 // Retrieve the PID of the running JVM process
	    String pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

	    // Generate the report name by appending the PID to the class name
	    String reportName = className + "_" + pid;
	//	String reportName = className;

		// Log the report name (for debugging purposes)
		System.out.println("Report Name : "+reportName);

		// Create a unique report and initialize the test
		AmazonLog.CreateReport(reportName, suiteName);
		AmazonLog.CreateTestStep("Test Execution for " + reportName);
		// Open the browser
		openBrowser();
	}

	@AfterClass
	// Quit after Browser Opens
	public void quitDriver() {
		Logger freemarkerLogger = Logger.getLogger("freemark");
		freemarkerLogger.setLevel(Level.ERROR); 
		AmazonLog.getExtentReports().flush();
		quitBrowser();
	}
	
	
	@BeforeMethod
	public void beforesuiteExecution(ITestResult result) {
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
	
	@AfterSuite()
	public void afterSuiteExecution() {
		
		if (!shouldRunAfterSuite()) {
		List<TestSummary> testSummaries = new ArrayList<>();
    	testSummaries.add(new TestSummary(this.getClass().getSimpleName(),true, "2 mins","//Results//"+this.getClass().getName()+".html" ));
    	List<String> recipients = Arrays.asList("ashtonbentlycoc@gmail.com","manishasn97@gmail.com");
		EmailSendingUtils.sendEmail(this.getClass().getSimpleName(),"Regression Suite", "v1.0.0", testSummaries, recipients);
		}
	}

	private boolean shouldRunAfterSuite() {
	    // Add logic to decide if the AfterSuite should run, e.g., check system properties or a specific condition
	    return AmazonConfig.isSuitRun()!= null; // Example condition
	}

}