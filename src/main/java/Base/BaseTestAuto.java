package Base;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import Amazon.utilities.EmailSendingUtils;
import Amazon.utilities.TestInfo;
import Amazon.utilities.TestInfoContext;
import Amazon.utilities.TestSummary;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Listeners(Base.AmazonExecutionHandler.class)
public class BaseTestAuto extends AmazonBrowser {

    // Thread-safe map to store report links and their statuses
    private static final ConcurrentHashMap<String, ClassReport> reportData = new ConcurrentHashMap<>();
    @BeforeMethod
    public void beforesuiteExecution(ITestResult result) {
        Annotation[] annotations = result.getMethod().getConstructorOrMethod().getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof TestInfo) {
                TestInfo testInfo = (TestInfo) annotation;
                TestInfoContext.setTestInfo(testInfo);
            }
        }
    }
    @BeforeClass
    public void beforeClass(ITestContext tc) {
        String suiteName = tc.getName() != null ? tc.getName() : "testsuite";
        String className = this.getClass().getSimpleName();
        String pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String reportName = className + "_" + pid;

        System.out.println("Report Name: " + reportName);

        AmazonLog.CreateReport(reportName, suiteName);
        AmazonLog.CreateTestStep("Test Execution for " + reportName);
        
        // Add initial report entry with status as true (assume passed by default)
        String reportLink = AmazonConfig.getSharedPath()+ "WebSiteAutomationResult_" + suiteName + "_" + reportName + ".html";
        reportData.put(className, new ClassReport(reportLink, true));

        openBrowser();
    }

    @AfterClass
    public void quitDriver(ITestContext context) {
        String className = this.getClass().getSimpleName();
        boolean allPassed = context.getFailedTests().size() == 0;
        
        // Update the class status based on execution results
        ClassReport classReport = reportData.get(className);
        if (classReport != null) {
            classReport.setPassed(allPassed);
            System.out.println(allPassed);
            classReport.setExecutionTime(System.currentTimeMillis() - context.getStartDate().getTime());

        }

        AmazonLog.getExtentReports().flush();
        quitBrowser();
    }

    @AfterSuite
    public void afterSuiteExecution() {
        if (shouldRunAfterSuite()) {
            List<TestSummary> testSummaries = new ArrayList<>();

            // Aggregate the report data with statuses
            for (String className : reportData.keySet()) {
                ClassReport classReport = reportData.get(className);
                System.out.println(classReport.getReportLink());
                System.out.println(classReport.isPassed());
                testSummaries.add(new TestSummary(
                    className, 
                    classReport.isPassed(), 
                    classReport.getExecutionTime(), 
                    classReport.getReportLink()
                ));
            }

            // Recipients for email
            List<String> recipients = Arrays.asList("ashtonbentlycoc@gmail.com", "manishasn97@gmail.com");
            EmailSendingUtils.sendEmail(
                "Regression Suite", 
                "Test Execution Summary with Status", 
                "v1.0.0", 
                testSummaries, 
                recipients
            );
        }
    }

    private boolean shouldRunAfterSuite() {
        return AmazonConfig.isSuitRun();
    }

    // Nested class to represent a report with status
	private static class ClassReport {
		private final String reportLink;
		private boolean passed;
		private long executionTime;

		public ClassReport(String reportLink, boolean passed) {
			this.reportLink = reportLink;
			this.passed = passed;
		}

		public String getReportLink() {
			return reportLink;
		}

		public boolean isPassed() {
			return passed;
		}

		public void setPassed(boolean passed) {
			this.passed = passed;
		}

		public void setExecutionTime(long executionTime) {
			this.executionTime = executionTime;
		}

		public long getExecutionTime() {
			return executionTime;
		}
	}
}
