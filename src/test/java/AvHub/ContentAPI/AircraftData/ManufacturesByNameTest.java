package AvHub.ContentAPI.AircraftData;

import Utilities.Base;
import Utilities.BaseTest;
import Utilities.Testrail.GetTestRailData;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ManufacturesByNameTest extends BaseTest {
    private static String currentClass = ManufacturesByNameTest.class.getName();

    @Inject
    private static GetTestRailData testRailData;

    @Page
    private Base base;

    @Test(dataProvider = "testData",
            description = "Get manufacturers by manufacturer name and optionally component category ID. Verify status code.")
    public void manufacturesByNameTest(String stepData) {
        String strSubUrl = stepData.split("\n")[0].split("=")[1].trim();
        String strComponentCategoryId = stepData.split("\n")[1];
        String strManufacturerNameId = stepData.split("\n")[2].replaceAll(" ","%20").replaceAll(",","%2C");
        String strRequestUrl = baseUrl+subPath+strSubUrl+"?"+strComponentCategoryId+"&"+strManufacturerNameId;

        response = base.GETRequest(strToken,strRequestUrl);
        Assert.assertEquals(response.getStatusCode(), 200,"The status Code is not 200.");
    }

    @DataProvider
    private static Object[][] testData() {
        String stepData = testRailData.getData(currentClass, testRailJson, "step");
        return new Object[][]{{stepData}};

    }

}
