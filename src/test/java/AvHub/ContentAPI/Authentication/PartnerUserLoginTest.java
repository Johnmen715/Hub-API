package AvHub.ContentAPI.Authentication;

import Utilities.Base;
import Utilities.BaseTest;
import Utilities.Testrail.GetTestRailData;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PartnerUserLoginTest extends BaseTest {
    private static String currentClass = PartnerUserLoginTest.class.getName();

    @Inject
    private static GetTestRailData testRailData;

    @Page
    private Base base;

    @Test(dataProvider = "testData",
            description = "Authenticate Partner and User. Verify status code.")
    public void partnerUserLoginTest(String stepData) {
        String strSubUrl = stepData.split("=")[1].trim();
        String strRequestUrl = baseUrl+subPath+strSubUrl;

        JSONObject requestBody = new JSONObject();
        requestBody.put("Username",username);
        requestBody.put("Password",password);
        requestBody.put("PartnerId",partnerId);
        requestBody.put("PartnerSecret",partnerSecret);
        response = base.POSTRequestWithJSONFormat(requestBody,strRequestUrl);

        Assert.assertEquals(response.getStatusCode(), 200,"The status Code is not 200.");
    }

    @DataProvider
    private static Object[][] testData() {
        String stepData = testRailData.getData(currentClass, testRailJson, "step");

        return new Object[][]{{stepData}};

    }

}
