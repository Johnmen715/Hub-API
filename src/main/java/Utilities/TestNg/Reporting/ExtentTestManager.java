package Utilities.TestNg.Reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestManager {
    static Map extentTestMap = new ConcurrentHashMap();
    private static ExtentReports extentReports = ExtentReportsManager.getReporter();


    public static synchronized ExtentTest startTest(String testName, String disc) {
        ExtentTest test = extentReports.createTest(testName, disc);
        extentTestMap.put(getThreadId(), test);
        return test;
    }

    public static int getThreadId() {

        return (int) Thread.currentThread().getId();
    }

    public static synchronized void endTest() {
        extentReports.flush();
        extentTestMap.remove(getThreadId());
    }
}
