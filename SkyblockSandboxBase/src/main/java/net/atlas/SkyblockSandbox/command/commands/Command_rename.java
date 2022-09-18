package net.atlas.SkyblockSandbox.command.commands;

import com.sk89q.util.StringUtil;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.DupeGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Command_rename extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     */
    public Command_rename(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "rename", description = "Allows you to rename your items", usage = "/rename <string>")
    public void renameCmd(SBCommandArgs cmd) {
        String[] args = cmd.getArgs();
        if (cmd.getSender() instanceof Player) {
            if(args.length>=1) {
                StringBuilder s = new StringBuilder();
                int i = 0;
                for(String ss:args) {
                    if(i!=0) {
                        s.append(" ").append(SUtil.colorize(ss));
                    } else {
                        s.append(SUtil.colorize(ss));
                    }
                    i++;
                }
                SBPlayer p = new SBPlayer((Player) cmd.getSender());
                ItemStack item = p.getItemInHand();
                if (p.getStringFromItemInHand("non-legacy").equals("true")) {
                    SBItemBuilder builder = new SBItemBuilder(item).name(s.toString());
                    p.setItemInHand(builder.build());
                    p.sendMessage(SUtil.colorize("&aSuccess! Renamed to: " + p.getItemInHand().getItemMeta().getDisplayName()));
                } else {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(s.toString());
                    item.setItemMeta(meta);
                    p.setItemInHand(item);
                    p.sendMessage(SUtil.colorize("&aSuccess! Renamed to: " + item.getItemMeta().getDisplayName()));
                }
            }
        }

    }

    @SBCommand(name = "sll", aliases = {"setloreline"}, description = "Allows you to set the lore of your items", usage = "/sll <line #> <string>")
    public void sllCmd(SBCommandArgs cmd) {
        SBPlayer p = new SBPlayer((Player) cmd.getSender());
        String[] args = cmd.getArgs();
        if (cmd.getSender() instanceof Player) {
            if(args.length>=2) {
                if (!NumUtils.isInt(args[0])) {
                    p.sendMessage("&cThe lore line must be a number!");
                    return;
                }
                if (Integer.parseInt(args[0]) <= 0) {
                    p.sendMessage("&cThe lore line must be greater than 0!");
                    return;
                }
                if (Integer.parseInt(args[0]) >= 50) {
                    p.sendMessage("&cThe lore line cannot be more than 50!");
                    return;
                }
                if (p.getSetting(SBPlayer.Settings.LORE_GEN) && !p.getStringFromItemInHand("non-legacy").equals("true")) {
                    p.sendMessage("&cMust set an item name before doing this!");
                    return;
                }
                StackUtils.setLoreLine(Integer.parseInt(args[0]), p, args);
            } else {
                p.sendMessage("&cInvalid usage, /sll <line #> <string>");
            }
        }

    }

    @SBCommand(name = "rll", aliases = {"removeloreline"}, description = "Allows you to set the lore of your items", usage = "/rll <line #>")
    public void rllCmd(SBCommandArgs cmd) {
        SBPlayer p = new SBPlayer((Player) cmd.getSender());
        String[] args = cmd.getArgs();
        if (cmd.getSender() instanceof Player) {
            if(args.length>=1) {
                if (!NumUtils.isInt(args[0])) {
                    p.sendMessage("&cThe lore line must be a number!");
                    return;
                }
                if (Integer.parseInt(args[0]) <= 0) {
                    p.sendMessage("&cThe lore line must be greater than 0!");
                    return;
                }
                if (Integer.parseInt(args[0]) >= 50) {
                    p.sendMessage("&cThe lore line cannot be more than 50!");
                    return;
                }
                if (p.getSetting(SBPlayer.Settings.LORE_GEN) && !p.getStringFromItemInHand("non-legacy").equals("true")) {
                    p.sendMessage("&cMust set an item name before doing this!");
                    return;
                }
                int lineNumber = Integer.parseInt(args[0]);
                if (p.getSetting(SBPlayer.Settings.LORE_GEN)) {
                    SBItemBuilder item = new SBItemBuilder(p.getItemInHand());
                    ArrayList<String> itemLore = item.description;

                    if (lineNumber < (itemLore.size() + 1)) {
                        itemLore.remove((lineNumber - 1));
                        p.setItemInHand(item.setDescription(itemLore).build());
                    } else {
                        p.sendMessage("&cLine &b" + lineNumber + "&c is out of range.");
                        return;
                    }
                } else {
                    List<String> itemLore = p.getItemInHand().getItemMeta().getLore();

                    if (lineNumber < (itemLore.size() + 1)) {
                        itemLore.remove((lineNumber - 1));
                        ItemStack item = p.getItemInHand();
                        ItemMeta im = item.getItemMeta();
                        im.setLore(itemLore);
                        item.setItemMeta(im);
                        p.setItemInHand(item);
                    } else {
                        p.sendMessage("&cLine &b" + lineNumber + "&c is out of range.");
                        return;
                    }
                    p.sendMessage("&aSuccessfully removed lore line!");
                }
            } else {
                p.sendMessage("&cInvalid usage, /rll <line #>");
            }
        }

    }
}
