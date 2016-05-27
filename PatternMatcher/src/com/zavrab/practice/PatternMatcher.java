package com.zavrab.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {

    public static void main(String[] args) {
        String inStr = "Hi, <a href=\'www.a.com\'>www.a.com</a> look at it, also <a href=\"www.t.com\">www.t.com</a>";

        matchPattern(inStr);
    }

    public static void matchPattern(String inStr) {

        Pattern p = Pattern.compile("<a href=(\"|').+?</a>");
        //Pattern p = Pattern.compile("<a href=(\"|').*</a>");

        //Pattern p = Pattern.compile("<a href=\"(.+?)>(.+?)</a>");
        Matcher m = p.matcher(inStr);

        while (m.find()) {

            for (int j = 0; j < m.groupCount(); j++) {
                String str = m.group(j);
                System.out.format("'%s'\n", str);

                System.out.format("Start: '%d', End: '%d'\n", m.start(), m.end());
            }
        }
    }
}
