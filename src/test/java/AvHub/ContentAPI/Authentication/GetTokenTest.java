package AvHub.ContentAPI.Authentication;

import Utilities.BaseTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetTokenTest extends BaseTest {

    @Test(description = "Get Authentication Token. Verify Token is not empty")
    public void getTokenTest() {
        JsonPath jsonPath =response.jsonPath();

        String strTokenValue = jsonPath.getString("Result").split(":")[1];
        strToken = "Token "+strTokenValue.substring(0,strTokenValue.length()-1);

        Assert.assertTrue(!strToken.isEmpty(), "The token is unexpected empty");

    }

}
