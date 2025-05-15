package AvHub.ContentAPI.AircraftDocuments;

import Utilities.Base;
import Utilities.BaseTest;
import Utilities.Testrail.GetTestRailData;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DocumentsByEffectiveDateTest extends BaseTest {
    private static String currentClass = DocumentsByEffectiveDateTest.class.getName();

    @Inject
    private static GetTestRailData testRailData;

    @Page
    private Base base;

    @Test(dataProvider = "testData",
            description = "Get Documents by manufacturer, model, Issue Number and optionally Effective Date Range. Verify status code.")
    public void documentsByEffectiveDateTestTest(String stepData) {
        String strSubUrl = stepData.split("\n")[0].split("=")[1].trim();
        String strManufactureId = stepData.split("\n")[1];
        String strModelId = stepData.split("\n")[2];
        String strIssueNumber =stepData.split("\n")[3];
        String strExact=stepData.split("\n")[4];
        String strRequestUrl = baseUrl+subPath+strSubUrl+"?"+strManufactureId+"&"+strModelId+"&"+strIssueNumber+"&"+strExact;

        response = base.GETRequest(strToken,strRequestUrl);
        Assert.assertEquals(response.getStatusCode(), 200,"The status Code is not 200.");
    }

    @DataProvider
    private static Object[][] testData() {
        String stepData = testRailData.getData(currentClass, testRailJson, "step");
        return new Object[][]{{stepData}};

    }

}
