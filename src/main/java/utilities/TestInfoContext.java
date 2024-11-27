package utilities;

public class TestInfoContext {
    private static final ThreadLocal<TestInfo> testInfoHolder = new ThreadLocal<>();

    public static void setTestInfo(TestInfo testInfo) {
        testInfoHolder.set(testInfo);
    }

    public static TestInfo getTestInfo() {
        TestInfo testInfo = testInfoHolder.get();
        if (testInfo == null) {
            throw new IllegalStateException("No TestInfo available for the current test.");
        }
        return testInfo;
    }

    public static void clear() {
        testInfoHolder.remove();
    }
}
