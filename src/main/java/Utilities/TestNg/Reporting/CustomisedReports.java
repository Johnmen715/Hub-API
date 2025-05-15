package Utilities.TestNg.Reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CustomisedReports implements IReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomisedReports.class);

    private static final String ROW_TEMPLATE = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td></tr>";

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String reportTemplate = initReportTemplate();

        final String body = suites
                .stream()
                .flatMap(suiteToResults())
                .collect(Collectors.joining());

        saveReportTemplate(outputDirectory, reportTemplate.replaceFirst("</tbody>", String.format("%s</tbody>", body)));
    }

    private Function<ISuite, Stream<? extends String>> suiteToResults() {
        return suite -> suite.getResults().entrySet()
                .stream()
                .flatMap(resultsToRows(suite));
    }

    private Function<Map.Entry<String, ISuiteResult>, Stream<? extends String>> resultsToRows(ISuite suite) {
        return e -> {
            ITestContext testContext = e.getValue().getTestContext();

            Set<ITestResult> failedTests = testContext
                    .getFailedTests()
                    .getAllResults();
            Set<ITestResult> passedTests = testContext
                    .getPassedTests()
                    .getAllResults();
            Set<ITestResult> skippedTests = testContext
                    .getSkippedTests()
                    .getAllResults();

            //String suiteName = suite.getName();
            //String suiteName = testContext.getCurrentXmlTest().getParameter("Testcase_s_no");
            String testRail = testContext.getCurrentXmlTest().getParameter("TestRail");
            String testDisc = testContext.getCurrentXmlTest().getParameter("Testcase_desc");

            return Stream
                    .of(passedTests, skippedTests, failedTests)
                    .flatMap(results -> generateReportRows(testRail, testDisc, results).stream())
                    .distinct();
        };
    }

    private List<String> generateReportRows(String testName, String testDisc, Set<ITestResult> allTestResults) {
        return allTestResults.stream()
                .map(testResultToResultRow(testName, testDisc))
                .distinct()
                .collect(toList());
    }

    private Function<ITestResult, String> testResultToResultRow(String testName, String testDisc) {

        return testResult -> {
            switch (testResult.getStatus()) {
                case ITestResult.FAILURE:
                    return String.format(ROW_TEMPLATE, "danger", testName, testDisc, "FAILED");

                case ITestResult.SUCCESS:
                    return String.format(ROW_TEMPLATE, "success", testName, testDisc, "PASSED");//String.valueOf(testResult.getEndMillis() - testResult.getStartMillis()));

                case ITestResult.SKIP:
                    return String.format(ROW_TEMPLATE, "warning", testName, testDisc, "SKIPPED");

                default:
                    return "";
            }
        };
    }

    private String initReportTemplate() {
        String template = null;
        byte[] reportTemplate;
        try {
            reportTemplate = Files.readAllBytes(Paths.get("src/test/resources/reportTemplate.html"));
            template = new String(reportTemplate, "UTF-8");
        } catch (IOException e) {
            LOGGER.error("Problem initializing template", e);
        }
        return template;
    }

    private void saveReportTemplate(String outputDirectory, String reportTemplate) {
        new File(outputDirectory).mkdirs();
        try {
            PrintWriter reportWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir") + "/output/", "my-report.html"))));
            reportWriter.println(reportTemplate);
            reportWriter.flush();
            reportWriter.close();
        } catch (IOException e) {
            LOGGER.error("Problem saving template", e);
        }
    }

}
