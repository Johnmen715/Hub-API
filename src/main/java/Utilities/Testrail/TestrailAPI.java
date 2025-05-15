package Utilities.Testrail;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class TestrailAPI {

    private static APIClient client;
    protected static Long runId;
    private static int setTestRailSuites;
    private static String strProjectName;

    public static void setTestCaseResult(String caseNumber, int runStatus, String runComment, String attachmentPath, boolean tcFailed) {
        JSONObject strResult = null;
        String resultId = null;

        connectToTestRail("atpautomationhub@gmail.com", "Password11#");

        Map data = new HashMap();
        data.put("status_id", runStatus);
        data.put("comment", "Test Executed - " + runComment);
        System.out.println("runStatus " + runStatus + " runComment " + runComment);

        try {
            strResult = (JSONObject) client.sendPost("add_result_for_case/" + String.valueOf(runId) + "/" + caseNumber + "", data);
            resultId = String.valueOf(strResult.get("id"));
            if (tcFailed) {
                addAttachment(resultId, attachmentPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    public static void addAttachment(String strResultId, String attachPath) throws IOException, APIException {
        client.sendPost("add_attachment_to_result/" + strResultId + "", attachPath);
    }

    public static void updateTestRun(String suiteFileName) {
        connectToTestRail("atpautomationhub@gmail.com", "Password11#");
        Map data = new HashMap();
        data.put("include_all", new Boolean(false));
        List cases = new ArrayList();

        String[] myArray = suiteFileName.split(",");

        for (String string : myArray) {
            System.out.println(string);

            LinkedHashMap<Integer, String> suitefileArray = new LinkedHashMap<Integer, String>();
            try {
                suitefileArray = GetTCinTestRailSuite.getTestRailTCInSuite(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(suitefileArray.values().toString());

            for (int i = 1; i <= suitefileArray.size(); i++) {
                String tcnovalue = (new ArrayList<String>(suitefileArray.values()).get(i - 1));
                System.out.println(tcnovalue);
                cases.add(new Integer(tcnovalue));
            }
        }
        data.put("case_ids", cases);

        try {
            client.sendPost("update_run/46", data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    private static void setTestRailSuitesMaping(String suiteFileName) {
        if (suiteFileName.equals("ContentAPI")) {
            setTestRailSuites = 122;
        }
    }

    public static void createNewTestRun(String testRunName, String suiteFileName) {
        setTestRailSuitesMaping(suiteFileName);
        connectToTestRail("atpautomationhub@gmail.com", "Password11#");
        Map data = new HashMap();
        data.put("name", testRunName);
        data.put("suite_id", setTestRailSuites);
        data.put("include_all", new Boolean(false));
        List cases = new ArrayList();
        String[] myArray = suiteFileName.split(",");

        for (String string : myArray) {
            System.out.println(string);

            LinkedHashMap<Integer, String> suitefileArray = new LinkedHashMap<Integer, String>();
            try {
                suitefileArray = GetTCinTestRailSuite.getTestRailTCInSuite(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(suitefileArray.values().toString());

            for (int i = 1; i <= suitefileArray.size(); i++) {
                String tcnovalue = (new ArrayList<String>(suitefileArray.values()).get(i - 1));
                //System.out.println(tcnovalue);
                cases.add(new Integer(tcnovalue));
            }
        }
        data.put("case_ids", cases);

        try {
            runId = (Long) ((JSONObject) client.sendPost("add_run/6", data)).get("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    public static void connectToTestRail(String trUsername, String trPassword) {
        client = new APIClient("https://atpcasebank.testrail.io/");
        client.setUser(trUsername);
        client.setPassword(trPassword);
    }

    public static String getTestCaseTitle(String caseNumber) {
        connectToTestRail("atpautomationhub@gmail.com", "Password11#");

        JSONObject c = null;
        try {
            c = (JSONObject) client.sendGet("get_case/" + caseNumber);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
        return String.valueOf(c);
    }


}
