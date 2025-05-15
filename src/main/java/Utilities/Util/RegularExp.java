package Utilities.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExp {

    public String getValueUsingRegularExp(String regExp, String text){
        //System.out.println("regExp : " + regExp);
        //System.out.println("text : " + text);
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(text);
        String result = "";
        if(matcher.find()){
            result = matcher.group(1);
        }
        return result;
    }
}
