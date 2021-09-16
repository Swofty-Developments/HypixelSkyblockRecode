package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Command_enchant extends SkyblockCommandFramework
{
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_enchant(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "enchant",aliases = {"addench","addenchant"}, description = "adds a custom enchant to an item")
    public void enchantCmd(SBCommandArgs arguments) {
        String[] args = arguments.getArgs();
        CommandSender sender = arguments.getSender();
        if(sender instanceof Player) {
            if (args.length >= 2) {
                Enchantment enchant = Enums.getIfPresent(Enchantment.class,args[0].toUpperCase()).orNull();
                if(enchant!=null) {
                    if(NumUtils.isInt(args[1])) {
                        SBPlayer player = new SBPlayer(((Player) sender));
                        SBItemStack stack = new SBItemStack(player.getItemInHand());
                        ItemStack newItem = stack.addEnchantment(enchant,Integer.parseInt(args[1]));
                        player.setItemInHand(newItem);
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP,1,1);
                        player.sendMessage(SUtil.colorize("&aEnchanting succeeded!"));
                    } else {
                        sender.sendMessage(SUtil.colorize("&cInvalid enchantment level!"));
                    }
                } else {
                    sender.sendMessage(SUtil.colorize("&cInvalid enchantment!"));
                }


            } else {
                sender.sendMessage(SUtil.colorize("&cPlease provide a valid enchantment and level!"));
            }
        }
    }
}
