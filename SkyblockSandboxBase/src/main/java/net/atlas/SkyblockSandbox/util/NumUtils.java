package net.atlas.SkyblockSandbox.util;

import java.text.DecimalFormat;

public class NumUtils {

    public static boolean isInt(String o) {
        try {
            Integer i = Integer.parseInt(o);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static String format(double num, String pattern) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return(decimalFormat.format(num));
    }
}
