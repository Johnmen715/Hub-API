package Utilities.TestNg.Reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExtentReportsManager {
    private static ExtentReports extentReports;
    public static String DEFAULT_REPORT_LOCATION = "output" + File.separator + "extent" + File.separator + "Hub" + getFormattedDateTime() + ".html";
    private static String reportLocation;
    private static ExtentHtmlReporter htmlReporter;
    private static String projectName;
    private static String projectenv;
    private static String suiteName;

    public synchronized static ExtentReports getReporter() {
        if (reportLocation == null) {
            reportLocation = DEFAULT_REPORT_LOCATION;
        }
        if (extentReports == null) {

            htmlReporter = new ExtentHtmlReporter(reportLocation);

            extentReports = new ExtentReports();
            System.out.println("projectName " + projectName);

            if (projectName.equals("null") || projectName.equals("") || projectName.equals("${ProjectName}")) {
                extentReports.attachReporter(htmlReporter);
            } else {
                ExtentXReporter extentxReporter = new ExtentXReporter("192.168.2.58", 27017);
                // project name
                extentxReporter.config().setProjectName(projectName);

                // report or build name
                extentxReporter.config().setReportName(suiteName + "-" + projectenv);

                // server URL
                extentxReporter.config().setServerUrl("http://192.168.2.58:1337");
                extentReports.attachReporter(htmlReporter, extentxReporter);
            }

        }
        return extentReports;
    }

    /**
     * For use in a BeforeSuite call to alter report location on a per-suite
     * basis.
     *
     * @param suiteName
     */
    public static void alterReportLocation(String suiteName) {
        reportLocation = "output" + File.separator + "extent" + File.separator + "Hub" + getFormattedDateTime() + ".html";

    }

    public static void getProjectName(String strProjectName) {
        projectName = strProjectName;
    }

    public static void getProjectEnv(String strProjectEnv) {
        projectenv = strProjectEnv;
    }

    public static void getSuiteFileName(String strSuiteFileName) {
        suiteName = strSuiteFileName;
    }

    public static String getFormattedDateTime() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HHmmss");
        String today = formatter.format(date);
        return today;
    }

    public static void nullifyReportLocation() {
        reportLocation = null;
    }

    public synchronized static void closeReporter() {
        if (extentReports != null) {

        }
    }
}
