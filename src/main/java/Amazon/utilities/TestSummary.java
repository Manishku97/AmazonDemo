package Amazon.utilities;

// TestSummary class to hold test data
public class TestSummary {
    private String className;
    private boolean passed;
    private long executionTime;
    private String reportLink;

    public TestSummary(String className, boolean passed, long executionTime, String reportLink) {
        this.className = className;
        this.passed = passed;
        this.executionTime = executionTime;
        this.reportLink = reportLink;
    }

    public String getClassName() {
        return className;
    }

    public boolean isPassed() {
        return passed;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String getReportLink() {
        return reportLink;
    }
}