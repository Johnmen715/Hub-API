package Utilities.Testrail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class GetTestRailData {

    public static String getData(String className, String json, String resultType) {
        String results = null;
        JsonNode root = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            root = objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode nameNode = root.path("custom_steps_separated");

        ArrayList<String> arrlist = new ArrayList<String>();

        if (nameNode.isArray()) {
            for (JsonNode node : nameNode) {
                String content = node.path("content").asText();
                String expected = node.path("expected").asText();
                if (content.contains("AvHub.")) {
                    String jsonClass = getClassNames(node);
                    if (!arrlist.contains(jsonClass)) {
                        if (resultType.equalsIgnoreCase("step")) {
                            if (content.contains(className) && content.contains("Data:")) {
                                results = getStepsData(node);
                                break;
                            }
                        } else if (resultType.equalsIgnoreCase("expected")) {
                            if (content.contains(className) && expected.contains("Data:")) {
                                results = getExpectedData(node);
                                break;
                            }
                        } else {
                            System.out.println("INVALID REQUEST!");
                        }
                    } else {
                        String jsonClassSize = jsonClass + arrlist.size();
                        String newContent = content.replace(jsonClass, jsonClassSize);
                        if (resultType.equalsIgnoreCase("step")) {
                            if (newContent.contains(className) && newContent.contains("Data:")) {
                                results = getStepsData(node);
                                break;
                            }
                        } else if (resultType.equalsIgnoreCase("expected")) {
                            String newExpected = expected.replace(jsonClass, jsonClassSize);
                            if (newContent.contains(className) && newExpected.contains("Data:")) {
                                results = getExpectedData(node);
                                break;
                            }
                        } else {
                            System.out.println("INVALID REQUEST!");
                        }
                    }
                    arrlist.add(getClassNames(node));
                }
            }
        }

        return results;
    }

    private static String getClassNames(JsonNode node) {
        String[] result = new String[0];
        String content = node.path("content").asText();
        result = content.split("\n", 2);
        //System.out.println(result[0]);
        return result[0];
    }

    private static String getStepsData(JsonNode node) {
        String result = null;
        String content = node.path("content").asText();
        result = content.substring(content.indexOf("Data:\n") + 0);
        String finalResult = result.replace("Data:\n", "");
        return finalResult.replaceAll("<dim>", "").replaceAll("\\/div>", "");

    }

    private static String getExpectedData(JsonNode node) {
        String result = null;
        String expected = node.path("expected").asText();
        result = expected.substring(expected.indexOf("Data:\n") + 0);
        String finalResult = result.replace("Data:\n", "");
        return finalResult.replaceAll("<dim>", "").replaceAll("\\/div>", "");
    }
}
