package com.dev.firdous.startupguidance.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Firdous on 3/23/2018.
 */

public class Util {

    static Pattern emailPattern = Pattern.compile("[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");

    public static boolean isValidEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        return !matcher.matches();
    }
}
