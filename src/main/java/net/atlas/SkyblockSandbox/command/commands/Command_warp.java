package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.playerIsland.SBLocations;
import net.atlas.SkyblockSandbox.util.BungeeUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Command_warp extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_warp(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "warp", description = "Fast travel", usage = "/warp <Location>")
    public void warpCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            String[] args = cmd.getArgs();
            if(args.length>=1) {
                String loc = args[0].toUpperCase();
                SBLocations sbLoc = Enums.getIfPresent(SBLocations.class,loc).orNull();
                if(sbLoc!=null) {
                    sbLoc.teleport(p);
                    p.playSound(p.getLocation(),Sound.ENDERMAN_TELEPORT,2,1);
                    p.sendMessage(SUtil.colorize("&7Warping..."));
                    if(p.getServer().getServerName().equalsIgnoreCase("islands")) {
                        BungeeUtil.sendPlayer(p,"hub");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid warp location!");
                }
            }
        }

    }
}
