package Utilities.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Configuration {

    class MyException extends Exception {

        public String toString() {
            return "CustomException";
        }
    }

    /**
     * Get the values from environment file
     *
     * @param env
     * @param key
     * @return
     */
    public String getAppEnvProperty(String env, String key) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(System.getProperty("user.dir") + "/data/environment/" + env + ".properties");
            prop.load(input);
            return prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    public String getXpathProperty(String env, String key) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(System.getProperty("user.dir") + "/data/environment/" + env + ".properties");
            prop.load(input);
            Set set = prop.entrySet();

            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                Map.Entry entery = (Map.Entry)itr.next();
                //System.out.println(entery.getKey() + " = " + entery.getValue());
                Object keyStartsWith = entery.getKey();
                if (String.valueOf(keyStartsWith).contains(key)) {
                    return entery.getKey() + " = " + entery.getValue();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }
}
