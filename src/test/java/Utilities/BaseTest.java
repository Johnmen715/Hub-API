package Utilities;

import Utilities.TestNg.Reporting.ExtentReportsManager;
import Utilities.TestNg.Reporting.ExtentTestManager;
import Utilities.Testrail.GetTestRailFields;
import Utilities.Testrail.TestrailAPI;
import Utilities.Util.Configuration;
import Utilities.Util.Decrypt;
import Utilities.Util.TakeScreenShot;
import com.aventstack.extentreports.Status;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;


@RunAsClient
public class BaseTest extends ArqBase {

    private static final Logger logger = Logger.getLogger(new Object() { }.getClass().getEnclosingClass());
    protected static String getEnvPath;
    protected static String baseUrl;
    protected static String subPath;
    protected static String username;
    protected static String password;
    protected static String partnerId;
    protected static String partnerSecret;
    protected static String strToken;
    protected static String Testcase_no;
    protected static String testRailJson;
    protected static Response response = null;

    @Page
    private TakeScreenShot takeScreenShot;

    @Inject
    private Configuration config;

    @Inject
    private GetTestRailFields getField;

    /**
     * BeforeTest : Setting Users and URL
     * @param Testcase_s_no
     * @param Testcase_desc
     * @param users
     * @param TestRail
     */
    @BeforeTest
    @Parameters({"Testcase_s_no", "Testcase_desc", "environment", "users", "TestRail"})
    public void setEnvProp(String Testcase_s_no, String Testcase_desc, String environment, String users, String TestRail) {
        failCaseCount = 0;
        testRailJson = TestrailAPI.getTestCaseTitle(TestRail);

        String Users = "";

        getEnvPath = environment;
        setUserConfig(users, Users);
        this.Testcase_no = Testcase_s_no;

        setTestDescription("");
        setTestName(generateTestNameByClass(Testcase_no + " | " + Testcase_desc));
        extentTest = ExtentTestManager.startTest(getTestName(), getTestDescription());
        extentTest.log(Status.INFO, " URL : " + this.baseUrl +
                " | Username : " + this.username + " | Password : " + this.password);

        logger.info(
                "Testcase_s_no : " + Testcase_s_no +
                        "\nTestRail : " + TestRail +
                        "\nURL : " + this.baseUrl +
                        "\ngetEnvPath : " + this.getEnvPath +
                        "\nUsername : " + this.username +
                        "\nPassword : " + this.password +
                        "\nJSON : " + this.testRailJson
        );

    }

    /**
     * AfterMethod : Output If Test or Method pass / fail
     * @param testResult
     * @param Testcase_s_no
     * @param TestRail
     */
    @AfterMethod
    @Parameters({"Testcase_s_no", "TestRail"})
    public void fetchMostRecentTestResult(ITestResult testResult, String Testcase_s_no, String TestRail) {
        int status = testResult.getStatus();
        ITestNGMethod method = testResult.getMethod();
        String generateRandomNumber = ExtentReportsManager.getFormattedDateTime();
        String filePath = System.getProperty("user.dir") + "/target/Hub_" + generateRandomNumber + ".png";

        switch (status) {
            case ITestResult.SUCCESS:
                failCaseCount = 0;
                updateStatusOnTestRail(TestRail, 1, method.getDescription() + " : " + method.getMethodName() + " : PASS ", filePath, false);
                extentTest.log(Status.PASS, method.getDescription() + " - " + method.getMethodName());
                break;
            case ITestResult.FAILURE:
                failCaseCount = 1;
                logger.error("Failed Method name : " + method.getMethodName() + " : " + testResult.getThrowable());
                takeScreenShot.getScreen(filePath);
                updateStatusOnTestRail(TestRail, 5, method.getDescription() + " : " + method.getMethodName() + " : FAIL ", filePath, true);
                extentTest.log(Status.FAIL, method.getDescription() + " - " + method.getMethodName());
                break;
            case ITestResult.SKIP:
                logger.error("Skipped Method name : " + method.getMethodName() + " : " + testResult.getThrowable());
                updateStatusOnTestRail(TestRail, 3, method.getDescription() + " : " + method.getMethodName() + " : SKIP BLOCKED ", filePath, false);
                extentTest.log(Status.SKIP, method.getDescription() + " - " + method.getMethodName());
                extentTest.createNode(method.getDescription() + " - " + method.getMethodName()).fail(String.valueOf(testResult.getThrowable()));
                break;
            default:

        }

    }

    /**
     * Update Test Rail status after the test.
     * @param TestRail
     * @param intRunStatus
     * @param strRunComment
     * @param filePath
     * @param attach
     */
    private void updateStatusOnTestRail(String TestRail, int intRunStatus, String strRunComment, String filePath, boolean attach) {
        logger.info(strRunComment);
        if (runTestRail) {
            TestrailAPI.setTestCaseResult(TestRail, intRunStatus, strRunComment, filePath, attach);
        }
    }

    /**
     * Set User info before the test
     * @param users
     * @param customUser
     */
    private void setUserConfig(String users, String customUser) {
        config = new Configuration();
        logger.info("*********************************************************************************************");
        Decrypt decrypt = new Decrypt();
        String key = "lockUnlock";

        if (customUser.isEmpty()) {
            this.username = config.getAppEnvProperty(getEnvPath, "content.api.username_" + users);
            this.password = decrypt.decryptXOR(config.getAppEnvProperty(getEnvPath, "content.api.password_" + users), key);
        } else {
            this.username = config.getAppEnvProperty(getEnvPath, "content.api.username_" + customUser);
            this.password = config.getAppEnvProperty(getEnvPath, "content.api.password_" + customUser);
        }

        this.baseUrl = config.getAppEnvProperty(getEnvPath, "content.api.url");
        this.subPath = config.getAppEnvProperty(getEnvPath, "content.api.url.path");
        this.partnerId = config.getAppEnvProperty(getEnvPath,"content.api.PartnerId");
        this.partnerSecret = config.getAppEnvProperty(getEnvPath,"content.api.PartnerSecret");
        //this.outputPath = config.getAppEnvProperty(getEnvPath, "outputPath");
    }
}
