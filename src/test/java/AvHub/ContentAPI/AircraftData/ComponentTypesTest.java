package AvHub.ContentAPI.AircraftData;

import Utilities.Base;
import Utilities.BaseTest;
import Utilities.Testrail.GetTestRailData;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ComponentTypesTest extends BaseTest {
    private static String currentClass = ComponentTypesTest.class.getName();

    @Inject
    private static GetTestRailData testRailData;

    @Page
    private Base base;

    @Test(dataProvider = "testData",
            description = "Get Component Categories. Verify status code.")
    public void componentTypesTest(String stepData) {
        String strSubUrl = stepData.split("=")[1].trim();
        String strRequestUrl = baseUrl+subPath+strSubUrl;

        response = base.GETRequest(strToken,strRequestUrl);
        Assert.assertEquals(response.getStatusCode(), 200,"The status Code is not 200.");
    }

    @DataProvider
    private static Object[][] testData() {
        String stepData = testRailData.getData(currentClass, testRailJson, "step");

        return new Object[][]{{stepData}};

    }

}
