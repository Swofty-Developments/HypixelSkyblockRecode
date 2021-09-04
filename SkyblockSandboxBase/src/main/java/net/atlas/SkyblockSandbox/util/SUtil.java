package net.atlas.SkyblockSandbox.util;

import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;

public class SUtil {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static List<String> colorize(String... s) {
        List<String> colored = new ArrayList<>();
        for(String b:s) {
            colored.add(ChatColor.translateAlternateColorCodes('&',b));
        }
        return colored;
    }

    public static List<String> colorize(List<String> s) {
        List<String> colored = new ArrayList<>();
        for(String b:s) {
            colored.add(ChatColor.translateAlternateColorCodes('&',b));
        }
        return colored;
    }

    public static void sendActionText(SBPlayer player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    public static String firstLetterUpper(String s) {
        s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        return s;
    }

}
