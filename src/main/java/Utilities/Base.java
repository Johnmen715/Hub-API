package Utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.json.simple.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base {

    private static final Logger logger = Logger.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    @Drone
    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * This method is waiting for element to display
     *
     * @param locator
     * @param timeout
     * @return
     */

    /**
     * @param condition
     * @param timeout
     */

    /**
     * Wait for MilliSec
     *
     * @param MilliSec
     */
    public void Wait(int MilliSec) {
        try {
            Thread.sleep(MilliSec);
        } catch (InterruptedException e) {
        }
    }

    /**
     * @return
     */
    public static long generateRandomNumber() {
        return 0;
    }

    /**
     * Check if object is Visible in UI
     *
     * @param we
     * @return
     */

    /**
     * Take screenshot of current screen
     *
     * @param fileWithPath
     */
    public void getScreen(String fileWithPath) {
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(fileWithPath);
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> extractUrlsFromString(String text) {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public String getValueUsingRegularExp(String regExp, String text) {
        System.out.println("regExp : " + regExp);
        //System.out.println("text : " + text);
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(text);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    // function to generate a random string of length n
    public String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        System.out.println("CODE : " + sb.toString());
        return sb.toString();
    }

    public Response GETRequest(String token,String strRequestUrl) {
        RequestSpecification request = RestAssured.given();
        request.header("Authorization",token);
        //request.header("Content-Type","application/json");

        Response response = request.get(strRequestUrl);
        waitResponse(response);
        return response;
    }
    public Response POSTRequestWithJSONFormat(JSONObject requestBody, String strRequestUrl) {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(requestBody.toJSONString());

        Response response = request.post(strRequestUrl);
        waitResponse(response);
        return response;
    }

    public void waitResponse(Response response) {
        for (int i =0; i<10; i++) {
            if (response == null) {
                Wait(1000);
                System.out.println("wait "+i+"seconds");
            } else {
                Wait(1000);
                break;
            }
        }
    }

    public boolean responseBodyVerify(String strResponseBody, String expectedValue) {
        if (expectedValue == null) {
            return !strResponseBody.isEmpty();
        } else {
            System.out.println("actual result--"+strResponseBody);
            System.out.println("expect result--"+expectedValue);
            String[] strValues = expectedValue.split("\n");
            for (String strValue : strValues) {
                System.out.println(strResponseBody.contains(strValue.trim()));
                if (!strResponseBody.contains(strValue.trim())) return false;
            }
            return true;
        }
    }



}
