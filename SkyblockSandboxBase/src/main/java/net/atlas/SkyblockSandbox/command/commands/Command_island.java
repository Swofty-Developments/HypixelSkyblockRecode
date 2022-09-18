package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.files.CfgFile;
import net.atlas.SkyblockSandbox.listener.sbEvents.MessageListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.IslandId;
import net.atlas.SkyblockSandbox.playerIsland.PlayerIsland;
import net.atlas.SkyblockSandbox.playerIsland.SBLocations;
import net.atlas.SkyblockSandbox.util.BungeeUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Command_island extends SkyblockCommandFramework {

    private final static Map<String, String> playerToken = new HashMap<>();
    public static final Map<String, String> visits = new HashMap<>();

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_island(Plugin plugin) {
        super(plugin);
    }

    private final static Map<UUID, UUID> invited = new HashMap<>();

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
                if (new CfgFile().getConfiguration().getBoolean("island-server")) {
                    player.sendMessage("▼7Teleporting Home...");

                    visits.remove(player.getName());

                    Location teleLoc = player.getPlayerIsland().getCenter();
                    while (teleLoc.getBlock().getType()!= Material.AIR) {
                        teleLoc.add(0,1,0);
                    }
                    player.teleport(teleLoc);

                    return;
                }

                player.sendMessage("§7Teleporting home...");
                BungeeUtil.sendPlayer(player, new CfgFile().getConfiguration().getString("island-server-name"));
            }
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

                break;
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "coop": {
                    if (!new CfgFile().getConfiguration().getBoolean("island-server")) {
                        player.sendMessage("§eError occurred while performing this action: §cYou must be on your island to invite others!");

                        return;
                    }

                    if (!player.hasIsland()) {
                        player.sendMessage("§cYou must have an island in order to add people to it!");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage("§cUndefined target!");
                        return;
                    }

                    invited.put(target.getUniqueId(), player.getUniqueId());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forwardcmdtobungeecord " + player.getName() + " inviteplayertoisland " + player.getName() + " " + target.getName());
                    Log.info("Forwarded.");
                    player.sendMessage("§7" + target.getName() + " §awas successfully added to your island.");

                    break;
                }
                case "accept": {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                    if (invited.containsKey(player.getUniqueId())) {
                        if (!invited.get(player.getUniqueId()).equals(target.getUniqueId())) {
                            player.sendMessage("§cInvalid destination (er=312)");
                            Log.info("Invalid destination error occurred with " + player.getName() + "!\nstart=" + player.getUniqueId() + "\n" +
                                    "destination=" + target.getUniqueId() + "\ner=312");
                            return;
                        }

                        invited.remove(player.getUniqueId());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                "forwardcmdtobungeecord " + player.getName() + " joinplayerisland " + player.getName() + " " + target.getName());

                        playerToken.put(target.getName(), UUID.randomUUID().toString());

                        new Thread(() -> {
                           try {
                               Thread.sleep(30000);
                           } catch (Exception ignored) {}

                           playerToken.remove(target.getName());
                        }).start();

                        player.sendMessage("§7Waiting for owner confirmation...");
                        break;
                    }
                    break;
                }
                case "visit": {
                    player.sendMessage("§7Processing request...");

                    String target = args[1];

                    ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                    dataOutput.writeUTF("doesIslandExist:" + target);

                    player.sendPluginMessage(SBX.getInstance(), Command_forward.VISIT_MESSAGE_CHANNEL, dataOutput.toByteArray());

                    if (!MessageListener.exists.containsKey(target)) {
                        player.sendMessage("§cProcess failed: ER-32 §7(ISLAND_NOT_FOUND)");

                        return;
                    }

                    ByteArrayDataOutput output = ByteStreams.newDataOutput();
                    output.writeUTF("visitRequest:" + player.getName() + ":" + target);

                    player.sendPluginMessage(SBX.getInstance(), Command_forward.VISIT_MESSAGE_CHANNEL, output.toByteArray());

                    player.sendMessage("§aRequested processed! Sending you to the island...");
                    BungeeUtil.sendPlayer(player, new CfgFile().getConfiguration().getString("island-server-name"));
                }
            }
        }
    }

    @SBCommand(name = "hub", aliases = "spawn", description = "Go to hub")
    public void hubCommand(SBCommandArgs arguments) {
        if (new CfgFile().getConfiguration().getBoolean("island-server")) {
            arguments.getPlayer().sendMessage("§7Sending you to " +
                    BungeeUtil.sendRandom(new SBPlayer(arguments.getPlayer()), new CfgFile().getConfiguration().getStringList("hub-server-names")) +
                    "...");

        } else {
            SBLocations.HUB.teleport(arguments.getPlayer());
        }
        return;

    }

    @SBCommand(name = "cmdislandcmdaaa_nooneisgonnafindthiscommand_iswear_pleasedontfindit_tyvm")
    public void addMemberCmd(SBCommandArgs arguments) {
        SBPlayer player = new SBPlayer(arguments.getPlayer());
        String[] args = arguments.getArgs();

        if (!playerToken.containsKey(player.getName())) {
            player.sendMessage("§eInvalid destination error: Your timer has expired!");

            return;
        }

        if (arguments.getArgs().length == 1) {
            if (new CfgFile().getConfiguration().getBoolean("island-server")) {
                OfflinePlayer target = Bukkit.getPlayer(args[0]);

                PlayerIsland island = player.getPlayerIsland();
                island.addMember(target);

                for (OfflinePlayer p : island.getMembers()) {
                    player.sendMessage(p.getUniqueId().toString());
                }

                player.sendMessage(island.getMembers().toString());

                player.sendMessage("§aSuccessfully added + " + target.getName() + "to your island! ☻");

                BungeeUtil.sendPlayer(new SBPlayer(target.getPlayer()), new CfgFile().getConfiguration().getString("island-server-name"));
            } else {
                player.sendMessage("§can undefined error occurred");
            }
        } else {
            arguments.getPlayer().sendMessage("§cundefined");
        }
    }

    void createProcess(SBPlayer player) {
        if (new CfgFile().getConfiguration().getBoolean("island-server")) {
            try {
                Data.createIsland(player.getPlayer(), IslandId.randomIslandId(), new ArrayList<>());
                player.sendMessage("§7Island created!");
            } catch (Exception ex) {
                ex.printStackTrace();
                player.sendMessage("§7Failed to create your island, please report this!");
            }
        } else {
            BungeeUtil.sendPlayer(player, "islands");
        }
    }

    public static String getVisiting(Player who) {
        return visits.get(who.getName());
    }
}
