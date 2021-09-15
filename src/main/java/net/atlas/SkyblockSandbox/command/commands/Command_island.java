package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.IslandId;
import net.atlas.SkyblockSandbox.util.BungeeUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.DataOutputStream;
import java.io.IOException;

public class Command_island extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_island(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "island", aliases = {"is"}, description = "Command to island lol", usage = "/island <create/coop/delete>")
    public void islandCMD(SBCommandArgs cmd) {
        String[] args = cmd.getArgs();
        SBPlayer player = new SBPlayer(cmd.getPlayer());

        if (args.length == 0) {
            if (!player.hasIsland()) {
                player.sendMessage("§7Creating island...");
                createProcess(player);
                return;
            } else {
                player.sendMessage("§7Teleporting home...");
                BungeeUtil.sendPlayer(player, "islands");
            }
			/*Location teleLoc = player.getPlayerIsland().getCenter();
			while (teleLoc.getBlock().getType()!= Material.AIR) {
				teleLoc.add(0,1,0);
			}
			player.teleport(teleLoc);*/
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create":

                if (player.hasIsland()) {
                    player.sendMessage("§7Your island already exists!");
                } else {
                    player.sendMessage("§7Creating island...");
                    createProcess(player);
                }

                break;
            case "home":
            case "tp":
                if (!player.hasIsland()) {
                    player.sendMessage("§7Creating island...");
                    createProcess(player);
                    return;
                } else {
                    player.sendMessage("§7Teleporting home...");
                    BungeeUtil.sendPlayer(player, "islands");
                }
				/*player.sendPluginMessage(SBX.getInstance(), "BungeeCord", out.toByteArray());
				Location teleLoc = player.getPlayerIsland().getCenter();
				while (teleLoc.getBlock().getType()!= Material.AIR) {
					teleLoc.add(0,1,0);
				}
				player.teleport(teleLoc);*/

                break;
        }
    }

    void createProcess(SBPlayer player) {
        if (player.getServer().getServerName().equals("islands")) {
            try {
                Data.createIsland(player.getPlayer(), IslandId.randomIslandId());
                player.sendMessage("§7Island created!");
            } catch (Exception ex) {
                ex.printStackTrace();
                player.sendMessage("§7Failed to create your island, please report this!");
            }
        } else {
            BungeeUtil.sendPlayer(player, "islands");
        }
    }
}
