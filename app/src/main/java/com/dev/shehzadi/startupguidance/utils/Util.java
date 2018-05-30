package com.dev.shehzadi.startupguidance.utils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shehzadi on 3/23/2018.
 */

public class Util {

    static Pattern emailPattern = Pattern.compile("[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");

    public static String HYPHENATED_PATTERN = "dd-MM-yyyy";
    public static String NON_HYPHENATED_PATTERN = "ddMMyyyy";

    public static boolean isValidEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        return !matcher.matches();
    }

    public static String getFormattedDate(String date, String fromPattern) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern(fromPattern);

        LocalDate localDate = dtf.parseLocalDate(date);
        String d = "" + localDate.getDayOfMonth();
        d = (d.length() == 1) ? "0" + d : d;

        String m = "" + localDate.getMonthOfYear();
        m = (m.length() == 1) ? "0" + m : m;

        String y = "" + localDate.getYear();

        return (fromPattern.equals(HYPHENATED_PATTERN)) ? d + m + y : d + "-" + m + "-" + y;
    }
}
