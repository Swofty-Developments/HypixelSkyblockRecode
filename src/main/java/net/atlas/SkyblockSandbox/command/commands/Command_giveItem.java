package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Command_giveItem extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_giveItem(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "giveItem", aliases = { "getItem","getCustomItem","getCustom" }, description = "Gives you any custom item.", usage = "/getItem <Item ID>")
    public void giveItemCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            String[] args = cmd.getArgs();
            if(args.length>=1) {
                String itemString = args[0].toUpperCase();
                for(SBItemStack i:SkyblockItem.getAllItems()) {
                    if(i.getItemID().equalsIgnoreCase(itemString)) {
                        SBItemStack ii = new SBItemStack(i.asBukkitItem());
                        ItemStack b = ii.refreshLore();
                        p.getInventory().addItem(b);
                        p.playSound(p.getLocation(), Sound.ITEM_PICKUP,2,1);
                        break;
                    }
                }
            }
        }

    }
}
