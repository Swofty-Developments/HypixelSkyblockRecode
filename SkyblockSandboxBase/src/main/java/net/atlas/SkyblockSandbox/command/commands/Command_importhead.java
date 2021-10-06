package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.builders.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.net.MalformedURLException;
import java.net.URL;

public class Command_importhead extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_importhead(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "importhead", aliases = { "importcustomhead","gethead","customhead" }, description = "Gives you any custom head", usage = "/importhead <minecraft.net URL>",permission = "sbx.admin.importhead")
    public void importHeadCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            String[] args = cmd.getArgs();
            if(args.length>=1) {
                String url = args[0];

                StringBuilder builder = new StringBuilder();
                builder.append("http://textures.minecraft.net/texture/");
                builder.append(url);
                try {
                    URL url1 = new URL(builder.toString());
                } catch (MalformedURLException ignored) {
                    p.sendMessage(ChatColor.RED + "Invalid texture!");
                }
                SBItemStack i = SBItemBuilder.init().name("&eCustom Imported Head").id("CUSTOM_HEAD").mat(Material.SKULL_ITEM).url(builder.toString()).rarity(Rarity.COMMON).stackable(false).type(ItemType.ITEM).build();
                p.getWorld().dropItem(p.getLocation().add(5,0,0),i.asBukkitItem());
                p.sendMessage(SUtil.colorize("&aSuccess! imported a custom skull!\n &cNOTE: Inappropriate skulls will result in a mute/ban."));
            }
        }

    }
}
