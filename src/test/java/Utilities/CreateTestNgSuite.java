package Utilities;

import Utilities.Testrail.CreateTestNgXML;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateTestNgSuite {

    /**
     * Read DataUtil and create TestNG file from using test cases in Suite file
     *
     * @param suiteFileNames
     * @param users
     */
    @Parameters({"suiteFileNames", "environment", "users", "setResultsOnTestRail", "autoProjectsName"})
    @Test
    public void createXMLfromSuiteFile(String suiteFileNames,
                                       String environment,
                                       String users,
                                       String setResultsOnTestRail,
                                       String autoProjectsName) {

        CreateTestNgXML.createXml(suiteFileNames,
                environment,
                users,
                setResultsOnTestRail,
                autoProjectsName);
    }
}
