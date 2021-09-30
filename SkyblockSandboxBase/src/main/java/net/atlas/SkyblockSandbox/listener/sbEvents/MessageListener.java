package net.atlas.SkyblockSandbox.listener.sbEvents;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.atlas.SkyblockSandbox.command.commands.Command_forward;
import net.atlas.SkyblockSandbox.command.commands.Command_island;
import net.atlas.SkyblockSandbox.files.CfgFile;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.IslandId;
import net.atlas.SkyblockSandbox.playerIsland.PlayerIslandHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageListener implements PluginMessageListener {
    public static final Map<String, Boolean> exists = new HashMap<>();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!new CfgFile().getConfiguration().getBoolean("island-server"))
            return;
        if (!Objects.equals(channel, Command_forward.VISIT_MESSAGE_CHANNEL))
            return;

        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        String[] message = input.readUTF().split(":");
        switch (message[0]) {
            case "visitRequest": {
                String visitor = message[1];
                String visited = message[2];

                Command_island.visits.put(visitor, visited);
                break;
            }
            case "doesIslandExist": {
                String island = message[1];
                OfflinePlayer sbPlayer = Bukkit.getOfflinePlayer(island);

                if (sbPlayer == null)
                    return;

                if (sbPlayer.isOnline()) {
                    sbPlayer = Bukkit.getPlayer(island);
                }

                System.out.println(sbPlayer.getUniqueId().toString());

                for (FileConfiguration cfg : Data.getAllIslandFiles()) {
                    if (cfg.getString("owner").equals(sbPlayer.getUniqueId().toString()) || cfg.getStringList("members").contains(sbPlayer.getUniqueId().toString()))
                        exists.put(island, true);
                }

                break;
            }
        }
    }
}
