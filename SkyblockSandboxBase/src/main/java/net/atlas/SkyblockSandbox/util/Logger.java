package net.atlas.SkyblockSandbox.util;

public class Logger {

    public static void logError(Class<?> clazz,String s) {
        System.out.println("[" + clazz.getName() + "] " + s);
    }
}
