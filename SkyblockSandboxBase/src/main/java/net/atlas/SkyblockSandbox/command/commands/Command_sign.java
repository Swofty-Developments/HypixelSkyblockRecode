package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_sign extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_sign(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "sign", aliases = {"signature"}, description = "Signs an item.", usage = "/sign <Line>")
    public void signCommand(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            String[] args = cmd.getArgs();
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            int line = -1;
            if (args.length >= 1) {
                if (NumUtils.isInt(args[0])) {
                    line = Integer.parseInt(args[0]);
                } else {
                    p.sendMessage(SUtil.colorize("&cInvalid line!"));
                    return;
                }
            }
            if (line < 0) {
                line = p.getItemInHand().getItemMeta().getLore().size();
            }
            p.setItemInHand(NBTUtil.setSignature(p.getItemInHand(), p.getName(), String.valueOf(line)));
            p.setItemInHand(new SBItemStack(p.getItemInHand()).refreshLore());
            p.sendMessage(SUtil.colorize("&aSuccessfully signed the item!"));
        }
    }
}
