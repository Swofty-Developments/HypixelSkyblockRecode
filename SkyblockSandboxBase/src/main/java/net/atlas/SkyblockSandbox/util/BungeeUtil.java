package net.atlas.SkyblockSandbox.util;

import com.google.common.collect.ImmutableList;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.entity.Player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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

    public static String sendRandom(SBPlayer player, List<String> servers) {
        int r = new Random().nextInt(servers.size());
        String server = servers.get(r);

        sendPlayer(player, server);

        return server;
    }
}
