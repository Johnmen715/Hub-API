package AvHub;

import Utilities.Base;
import Utilities.BaseTest;
import Utilities.Testrail.GetTestRailData;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ResponseBodyValidationTest extends BaseTest {
    private static String currentClass = ResponseBodyValidationTest.class.getName();

    @Inject
    private static GetTestRailData testRailData;

    @Page
    private Base base;

    @Test(dataProvider = "testData",
            description = "Get response body. Verify response body. If no data, verify the response body is not empty.")
    public void responseBodyValidationTest(String expectedData) {
        String strResponseBody = response.getBody().asString();
        Assert.assertTrue(base.responseBodyVerify(strResponseBody,expectedData), "The response body "+expectedData+" is unexpected.");
        response = null;

    }

    @DataProvider
    private static Object[][] testData() {
        String expectedData = testRailData.getData(currentClass, testRailJson, "expected");

        return new Object[][]{{expectedData}};

    }

}
