package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.TradeGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.playerIsland.SBLocations;
import net.atlas.SkyblockSandbox.util.BungeeUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Command_trade extends SkyblockCommandFramework {

    public static HashMap<Player, Player> requestTrade = new HashMap<>();

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_trade(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "trade", description = "Trade with players", usage = "/trade <Player>")
    public void tradeCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            String[] args = cmd.getArgs();
            if (args.length >= 1) {
                if (requestTrade.containsKey(p.getPlayer())) {
                    Player outGoingReq = requestTrade.get(p.getPlayer());
                    if (Bukkit.getOnlinePlayers().contains(outGoingReq)) {
                        new TradeGUI(p, new SBPlayer(outGoingReq),p).open();
                    } else {
                        p.sendMessage(ChatColor.RED + "Cannot find a player named " + ChatColor.WHITE + outGoingReq.getName() + ChatColor.RED + ", maybe they've gone offline?");
                    }
                    requestTrade.remove(p.getPlayer());
                } else {
                    Player incomingReq = Bukkit.getPlayer(args[0]);
                    if (!Bukkit.getOnlinePlayers().contains(incomingReq)) {
                        p.sendMessage(ChatColor.RED + "Cannot find a player named " + ChatColor.WHITE + args[0] + ChatColor.RED + ", maybe they've gone offline?");
                    }
                    //if (incomingReq.getName().equals(p.getName())) {
                    //p.sendMessage(ChatColor.RED + "You can't trade with yourself!");
                    //} else {
                    p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 2, 1);
                    p.sendMessage(ChatColor.GREEN + "You have sent a trade request to " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + ".");
                    requestTrade.put(incomingReq, p.getPlayer());
                    incomingReq.sendMessage(ChatColor.GREEN + "You have recieved a trade request from " + ChatColor.YELLOW + p.getName() + ChatColor.GREEN + ".");
                    //}
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid usage! /trade <Player>");
            }
        }

    }
}