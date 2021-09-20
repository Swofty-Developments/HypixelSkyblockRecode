package net.atlas.SkyblockSandbox.command.commands;

import com.sk89q.util.StringUtil;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.DupeGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Command_rename extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     */
    public Command_rename(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "rename", description = "Allows you to rename your items", usage = "/rename")
    public void renameCmd(SBCommandArgs cmd) {
        String[] args = cmd.getArgs();
        if (cmd.getSender() instanceof Player) {
            if(args.length>=1) {
                StringBuilder s = new StringBuilder();
                for(String ss:args) {
                    s.append(SUtil.colorize(ss));
                }
                SBPlayer p = new SBPlayer((Player) cmd.getSender());
                ItemStack item = p.getItemInHand();
                item = NBTUtil.setString(item, s.toString(),"item-name");
                item = new SBItemStack(item).refreshName();
                item = new SBItemStack(item).refreshLore();
                p.setItemInHand(item);
                p.sendMessage(SUtil.colorize("&aSuccess! Renamed to: " + item.getItemMeta().getDisplayName()));
            }
        }

    }
}
