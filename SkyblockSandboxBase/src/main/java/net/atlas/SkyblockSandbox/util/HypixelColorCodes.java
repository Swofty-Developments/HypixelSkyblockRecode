package net.atlas.SkyblockSandbox.util;

import net.md_5.bungee.api.ChatColor;

public enum HypixelColorCodes {
    BLACK('0', "%%black%%"),
    DARK_BLUE('1', "%%dark_blue%%"),
    DARK_GREEN('2', "%%dark_green%%"),
    DARK_AQUA('3', "%%dark_aqua%%"),
    DARK_RED('4', "%%dark_red%%"),
    DARK_PURPLE('5', "%%dark_purple%%"),
    GOLD('6', "%%gold%%"),
    GRAY('7', "%%gray%%"),
    DARK_GRAY('8', "%%dark_gray%%"),
    BLUE('9', "%%blue%%"),
    GREEN('a', "%%green%%"),
    AQUA('b', "%%aqua%%"),
    RED('c', "%%red%%"),
    LIGHT_PURPLE('d', "%%light_purple%%"),
    YELLOW('e', "%%yellow%%"),
    WHITE('f', "%%white%%"),
    MAGIC('k', "%%obfuscated%%"),
    BOLD('l', "%%bold%%"),
    STRIKETHROUGH('m', "%%strikethrough%%"),
    UNDERLINE('n', "%%underline%%"),
    ITALIC('o', "%%italic%%"),
    RESET('r', "%%reset%%");

    final char a;
    final String b;
    HypixelColorCodes(char a, String b) {
        this.a = a;
        this.b = b;
    }

    public static String translateHypixelColorCodes(String line) {
        for (HypixelColorCodes color : HypixelColorCodes.values()) {
            if (line.contains(color.b)) {
                line = line.replace(line, "§" + color.a);
            }
        }
        return line;
    }
}
