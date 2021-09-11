package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.AuctionHouse.guis.AuctionCreatorGUI;
import net.atlas.SkyblockSandbox.AuctionHouse.guis.AuctionHouseGUI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.TestPage;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import signgui.SignGUI;

public class Command_dev extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_dev(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "dev", aliases = {"test"}, description = "Developer only command", usage = "/dev", permission = "sbx.developer", noPerm = "&cThis command is only for developers!")
    public void devCmd(SBCommandArgs cmd) {
        if (!(cmd.getSender() instanceof Player)) return;
        SBPlayer p = new SBPlayer((Player) cmd.getSender());
        switch (cmd.getArgs()[0].toLowerCase()) {
            case "guislot": {
                new TestPage(p, Integer.parseInt(cmd.getArgs()[1])).open();
                break;
            }
            case "ah": {
                new AuctionHouseGUI(p).open();
                break;
            }
            case "sign": {
                new SignGUI(SBX.getInstance().signManager, (e -> {
                    p.sendMessage("&eYour output was" + e.getLines()[0]);
                }))
                .withLines("","^^^^^^^^^^^^^^^","Auction starting", "bid price")
                .open(p.getPlayer());
            }
        }
    }
}
