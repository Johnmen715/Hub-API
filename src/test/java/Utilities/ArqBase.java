package Utilities;

import Utilities.DateUtil.DateUtil;
import Utilities.TestNg.Reporting.ExtentReportsManager;
import Utilities.TestNg.Reporting.ExtentTestManager;
import Utilities.Testrail.TestrailAPI;
import com.aventstack.extentreports.ExtentTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.testng.Arquillian;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;

import static org.openqa.selenium.logging.LogType.DRIVER;

@RunAsClient
public class ArqBase extends Arquillian {

    protected ITestContext ctx;
    private String annotatedTestName;
    protected static ExtentTest extentTest;
    private String annotatedDescription;
    public static String projectName;
    public static boolean runTestRail;
    protected static int failCaseCount;

    /**
     * @param ctx
     */
    @BeforeClass
    public void setCtx(ITestContext ctx) {
        this.ctx = ctx;
    }

    /**
     * @throws Exception
     */
    @Override
    @AfterClass(groups = "arquillian", inheritGroups = true, alwaysRun = true)
    public void arquillianAfterClass() throws Exception {
        if (ctx.getFailedTests().size() > 0) {
            GrapheneContext.getContextFor(Default.class).getWebDriver().quit();
            super.arquillianAfterClass();
        }
    }

    /**
     * drone Set
     */
    @BeforeMethod
    public void droneSet() {
        if (failCaseCount == 1) {
            throw new SkipException("Skip This Test");
        }
        if (!this.ctx.getAttributeNames().contains(DRIVER)) {
            this.ctx.setAttribute(DRIVER, GrapheneContext.getContextFor(Default.class).getWebDriver());
        }

    }

    /**
     * @throws Exception
     */
    @AfterTest
    public void tearDown() throws Exception {
        ExtentTestManager.endTest();
        GrapheneContext.getContextFor(Default.class).getWebDriver().quit();
        super.arquillianAfterClass();
    }

    /**
     * Create html report beforeSuite.
     * @param iTestContext
     */
    @BeforeSuite
    @Parameters({"environment", "suiteFileName", "setResultsOnTestRail", "autoProjectsName"})
    public void beforesuite(ITestContext iTestContext, String environment, String suiteFileName, boolean setResultsOnTestRail, String autoProjectsName) {
        runTestRail = setResultsOnTestRail;

        projectName = autoProjectsName;
        if (runTestRail) {
            TestrailAPI.createNewTestRun(suiteFileName + " " + environment + " " + DateUtil.setDateTimeTestRail(), suiteFileName);
        }
        String suiteResultsFolder = iTestContext.getCurrentXmlTest().getParameter("suiteFolder");
        ExtentReportsManager.getProjectName(projectName);
        ExtentReportsManager.getProjectEnv(environment);
        ExtentReportsManager.getSuiteFileName(suiteFileName);
        if (suiteResultsFolder != null) {
            ExtentReportsManager.alterReportLocation(suiteResultsFolder);
        }
    }

    /**
     * Close HTML report afterSuite
     */
    @AfterSuite
    public void aftersuite() {
        ExtentReportsManager.closeReporter();
        ExtentReportsManager.nullifyReportLocation();
    }

    /**
     * Set test name on HTML report
     * @param name
     */
    public void setTestName(String name) {
        this.annotatedTestName = name;
    }

    /**
     * Get test name
     * @return
     */
    public String getTestName() {
        return this.annotatedTestName;
    }

    /**
     * Set test description on HTML report
     * @param desc
     */
    public void setTestDescription(String desc) {
        this.annotatedDescription = desc;
    }

    /**
     * Get test description
     * @return
     */
    public String getTestDescription() {
        return this.annotatedDescription;
    }

    /**
     * Set TestName on HTML report
     * @param strTestName
     * @return
     */
    public String generateTestNameByClass(String strTestName) {
        return "[t" + ExtentTestManager.getThreadId() + ": " + strTestName + " ] ";
    }

}
