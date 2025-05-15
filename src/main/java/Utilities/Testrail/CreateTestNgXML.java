package Utilities.Testrail;

import Utilities.DataUtil.GetTCinSuite;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.arquillian.graphene.page.Page;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CreateTestNgXML {

    private static final Logger logger = Logger.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    @Page
    private static GetTCinSuite getTCinSuite;

    private static DocumentBuilderFactory docFactory;
    private static Document document;
    private static DocumentBuilder docBuilder;
    private static Document doc;
    private static Element rootElement;
    private static Element appEnvs;
    private static Element user;
    private static Element setResultsOnTestRail;
    private static Element suiteFile;
    private static Element autoProjectsName;
    private static Element listeners;
    private static Element listener;
    private static Element listener1;
    private static Element staff;
    private static Element Testcase_no;
    private static Element TestRail_no;
    private static Element TestcaseDesc;
    private static Element classes;
    private static Element clas2;
    private static Element method;
    private static Element include;
    private static TransformerFactory transformerFactory;
    private static Transformer transformer;
    private static DOMSource source;
    private static StreamResult result;

    /**
     * Read DataUtil datasheet file
     * Create an xml file with data in excel
     * save xml file
     */
    public static void createXml(
            String suiteFileName, String strAppEnv, String users, String setTestRail, String projectsName) {
        String[] myArray = suiteFileName.split(",");
        for (String string : myArray) {
            logger.info(string);
            LinkedHashMap<Integer, String> suitefileArray = new LinkedHashMap<Integer, String>();
            try {
                suitefileArray = getTCinSuite.getTCnumberInSuite(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info(suitefileArray.values().toString());

            try {
                docFactory = DocumentBuilderFactory.newInstance();
                docBuilder = docFactory.newDocumentBuilder();
                document = docBuilder.newDocument();

                createSuite();
                setParameterEnvironment(strAppEnv);
                setParameterUsers(users);
                setParameterResultsOnTestRail(setTestRail);
                setParameterSuiteFileName(suiteFileName);
                setParameterAutoProjectsName(projectsName);
                setListeners(suiteFileName);

                for (int i = 1; i <= suitefileArray.size(); i++) {
                    String tcnovalue = (new ArrayList<String>(suitefileArray.values()).get(i - 1));
                    LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
                    int a = 0;
                    String testRailJson = TestrailAPI.getTestCaseTitle(tcnovalue);
                    logger.info(testRailJson);
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode root = null;
                    try {
                        root = objectMapper.readTree(testRailJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JsonNode titlesNode = root.path("title");
                    String titleNode = String.valueOf(titlesNode).replaceAll("\"", "");
                    JsonNode nameNode = root.path("custom_steps_separated");

                    createTest(i);
                    setParameterTestcase_no(tcnovalue);
                    setParameterTestRail(tcnovalue);
                    setParameterTestcase_desc(titleNode);
                    createClasses();

                    ArrayList<String> arrlist = new ArrayList<String>();

                    if (nameNode.isArray()) {
                        for (JsonNode node : nameNode) {
                            String content = node.path("content").asText();
                            if (content.contains("AvHub.")) {
                                String className = getClassNames(node);
                                //System.out.println(className);
                                if (!arrlist.contains(className)) {
                                    map.put(a, className);
                                    //System.out.println("I am only one");
                                } else {
                                    //System.out.println("I am only two");
                                    File currentFile = new File(
                                            System.getProperty("user.dir") + "\\src\\test\\java\\" + className.replace(".", "\\") + ".java");
                                    File copyFile = new File(
                                            System.getProperty("user.dir")
                                                    + "\\src\\test\\java\\"
                                                    + className.replace(".", "\\")
                                                    + arrlist.size() + ".java");
                                    try {
                                        FileUtils.copyFile(currentFile, copyFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String oldClassName = className.substring(className.lastIndexOf(".")).replaceAll("\\.", "");
                                    String newClassName = oldClassName + arrlist.size();
                                    modifyFile(String.valueOf(copyFile), oldClassName, newClassName);
                                    map.put(a, className + arrlist.size());
                                }
                                arrlist.add(getClassNames(node));

                            }
                            a++;
                        }
                    }

                    for (int b = 1; b <= map.size(); b++) {
                        String classNames = (new ArrayList<String>(map.values()).get(b - 1));
                        String[] className = classNames.split(",");
                        for (String arrClass : className) {
                            createClass(arrClass);
                        }
                    }
                }

                doc.appendChild(rootElement);

                // write the content into xml file
                transformerFactory = TransformerFactory.newInstance();
                transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://testng.org/testng-1.0.dtd");
                source = new DOMSource(doc);
                result = new StreamResult(
                        new File(System.getProperty("user.dir") + "/data/xml/" + string + ".xml"));

                transformer.transform(source, result);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (TransformerException tfe) {
                tfe.printStackTrace();
            }
        }

    }

    private static String getClassNames(JsonNode node) {
        String[] result = new String[0];
        String content = node.path("content").asText();
        result = content.split("\n", 2);
        //System.out.println(result[0]);
        return result[0];
    }

    private static void createSuite() {
        // root elements
        doc = docBuilder.newDocument();
        rootElement = doc.createElement("suite");
        rootElement.setAttribute("name", "Suite");
    }

    private static void setParameterAppEnv(String strAppEnv) {
        // set parameter elements
        appEnvs = doc.createElement("parameter");
        appEnvs.setAttribute("name", "appEnv");
        appEnvs.setAttribute("value", strAppEnv);
        rootElement.appendChild(appEnvs);
    }

    private static void setParameterEnvironment(String strAppEnv) {
        // set parameter elements
        appEnvs = doc.createElement("parameter");
        appEnvs.setAttribute("name", "environment");
        appEnvs.setAttribute("value", strAppEnv);
        rootElement.appendChild(appEnvs);
    }

    private static void setParameterUsers(String strUsers) {
        // set parameter elements
        user = doc.createElement("parameter");
        user.setAttribute("name", "users");
        user.setAttribute("value", strUsers);
        rootElement.appendChild(user);
    }

    private static void setParameterResultsOnTestRail(String strSetTestRail) {
        // set parameter elements
        setResultsOnTestRail = doc.createElement("parameter");
        setResultsOnTestRail.setAttribute("name", "setResultsOnTestRail");
        setResultsOnTestRail.setAttribute("value", strSetTestRail);
        rootElement.appendChild(setResultsOnTestRail);
    }

    private static void setParameterSuiteFileName(String strSuiteFileName) {
        // set parameter elements
        suiteFile = doc.createElement("parameter");
        suiteFile.setAttribute("name", "suiteFileName");
        suiteFile.setAttribute("value", strSuiteFileName);
        rootElement.appendChild(suiteFile);
    }

    private static void setParameterAutoProjectsName(String strProjectsName) {
        // set parameter elements
        autoProjectsName = doc.createElement("parameter");
        autoProjectsName.setAttribute("name", "autoProjectsName");
        autoProjectsName.setAttribute("value", strProjectsName);
        rootElement.appendChild(autoProjectsName);
    }

    private static void setListeners(String strSuiteFileName) {
        listeners = doc.createElement("listeners");
        rootElement.appendChild(listeners);

        listener = doc.createElement("listener");
        listener.setAttribute("class-name", "Utilities.TestNg.RetryListenerClass");
        listeners.appendChild(listener);

        if (strSuiteFileName.contains("SmokeTest")) {
            listener1 = doc.createElement("listener");
            listener1.setAttribute("class-name", "Utilities.TestNg.Reporting.CustomisedReports");
            listeners.appendChild(listener1);
        }
    }

    private static void createTest(int intTestNum) {
        // CreateTest
        staff = doc.createElement("test");
        staff.setAttribute("name", "test" + intTestNum);
        rootElement.appendChild(staff);
    }

    private static void setParameterTestcase_no(String strTestcase_s_no) {
        // set parameter elements
        Testcase_no = doc.createElement("parameter");
        Testcase_no.setAttribute("name", "Testcase_s_no");
        Testcase_no.setAttribute("value", strTestcase_s_no);
        staff.appendChild(Testcase_no);
    }

    private static void setParameterTestRail(String strTestRail) {
        // set parameter elements
        TestRail_no = doc.createElement("parameter");
        TestRail_no.setAttribute("name", "TestRail");
        TestRail_no.setAttribute("value", strTestRail);
        staff.appendChild(TestRail_no);
    }

    private static void setParameterTestcase_desc(String strTestcase_desc) {
        // set parameter elements
        TestcaseDesc = doc.createElement("parameter");
        TestcaseDesc.setAttribute("name", "Testcase_desc");
        TestcaseDesc.setAttribute("value", strTestcase_desc);
        staff.appendChild(TestcaseDesc);
    }

    private static void createClasses() {
        classes = doc.createElement("classes");
        staff.appendChild(classes);
    }

    private static void createClass(String strArrClass) {
        clas2 = doc.createElement("class");
        clas2.setAttribute("name", strArrClass);
        classes.appendChild(clas2);
    }

    private static void createIncludeMethod(String strIncludeMethod) {
        method = doc.createElement("methods");

        include = doc.createElement("include");
        include.setAttribute("name", strIncludeMethod);
        method.appendChild(include);
        clas2.appendChild(method);
    }

    private static void modifyFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent

            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent

            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
