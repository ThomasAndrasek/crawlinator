package com.thomasandrasek.crawlinator.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputChecker {
    public static boolean useRegex = false;
    public static String regex = "";

    public static String check(String output) {
        if (output == null) {
            return "";
        }
        if (useRegex) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(output);
            if (matcher.find()) {
                return matcher.group(1);
            }
            System.out.println("No Match: " + output);
            return output;
        } else {
            return output;
        }
    }
}
