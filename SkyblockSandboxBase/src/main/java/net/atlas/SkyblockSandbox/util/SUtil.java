package net.atlas.SkyblockSandbox.util;

import com.google.common.base.Strings;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static void unzipIgnoreFirstFolder(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                if (!ze.isDirectory()) {
                    String fileName = ze.getName();
                    fileName = fileName.substring(fileName.split("/")[0].length() + 1);
                    File newFile = new File(destDir + File.separator + fileName);
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Color hexToRgb(String colorStr) {
        return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));

    }

}
