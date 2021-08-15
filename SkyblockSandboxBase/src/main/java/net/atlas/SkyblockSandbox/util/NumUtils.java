package net.atlas.SkyblockSandbox.util;

public class NumUtils {

    public static boolean isInt(String o) {
        try {
            Integer i = Integer.parseInt(o);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
