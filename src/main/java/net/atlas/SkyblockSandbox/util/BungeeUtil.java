package net.atlas.SkyblockSandbox.util;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.entity.Player;

import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtil {

    public static void sendPlayer(SBPlayer p,String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (IOException e) {
            // Can never happen
        }
        p.sendPluginMessage(SBX.getInstance(), "BungeeCord", b.toByteArray());
    }
}
