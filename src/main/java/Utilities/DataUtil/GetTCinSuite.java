package Utilities.DataUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class GetTCinSuite {

    public static LinkedHashMap<Integer, String> getTCnumberInSuite(String suitePathName) throws IOException {

        BufferedReader in = new BufferedReader(
                new FileReader(System.getProperty("user.dir") + "/data/suite/" + suitePathName + ".suite"));
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        String line = "";
        int i = 0;
        while (line != null) {
            line = in.readLine();
            map.put(i, line);
            i++;
        }

        map.values().removeAll(Arrays.asList(null, ""));

        return map;

    }
}
