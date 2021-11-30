package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.items.HypixelItemsHelper;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Command_submititem extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_submititem(Plugin plugin) {
        super(plugin);
    }
    @SBCommand(name = "submititem", permission = "admin.command.submititem", usage = "/submititem <hypixel/atlas> <id>", noPerm = "&cComing soon!", description = "Submit items to the item catalogues!")
    public void submititemCMD(SBCommandArgs cmd) {
        SBPlayer p = new SBPlayer(cmd.getPlayer());
        String[] args = cmd.getArgs();
        if (args[0].equals("hypixel")) {
            SBItemBuilder item = new SBItemBuilder(p.getItemInHand()).id(args[1]).sign(p.getUniqueId().toString());
            HypixelItemsHelper.hypixelItems.add(item);
            p.sendMessage("&aSuccessfully added item to the Hypixel Item Catalogue!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    HypixelItemsHelper.cacheToMongo();
                    System.out.println("Successfully synced cache and mongo!");
                }
            }.runTaskAsynchronously(SBX.getInstance());
        } else {
            p.sendMessage("&cProbably wont work. make sure you do /submititem hypixel <id>");
        }
    }
}
