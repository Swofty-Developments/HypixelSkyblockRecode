package net.atlas.SkyblockSandbox.util;

import com.google.common.base.Strings;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SUtil {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(String... s) {
        List<String> colored = new ArrayList<>();
        for (String b : s) {
            colored.add(ChatColor.translateAlternateColorCodes('&', b));
        }
        return colored;
    }

    public static List<String> colorize(List<String> s) {
        List<String> colored = new ArrayList<>();
        for (String b : s) {
            colored.add(ChatColor.translateAlternateColorCodes('&', b));
        }
        return colored;
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                        ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    public static String getProgressBar(double current, int max, int totalBars, char symbol, ChatColor completedColor,
                                        ChatColor notCompletedColor) {
        if (current > max) {
            current = max;
        }
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    public static void sendActionText(SBPlayer player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    public static String firstLetterUpper(String s) {
        if (s.length() > 1) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            return s;
        }
        return s;
    }

    public static JSONObject readJsonObjFromUrl(String url) throws IOException, JSONException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    public static Color hexToRgb(String colorStr) {
        return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));

    }

}
