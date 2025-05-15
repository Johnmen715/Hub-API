package Utilities.Testrail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GetTestRailFields {

    public static String getfieldValue(String json, String fieldName) {
        JsonNode root = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            root = objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode nameNode = root.path(fieldName);

        return String.valueOf(nameNode).replaceAll("\"", "");
    }

}
