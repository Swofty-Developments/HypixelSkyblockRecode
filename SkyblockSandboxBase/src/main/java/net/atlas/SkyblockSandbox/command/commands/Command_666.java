package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.DupeGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_666 extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_666(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "666", description = "???", usage = "???")
    public void sixsixsixCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            ((Player) cmd.getSender()).playSound(((Player) cmd.getSender()).getLocation(), Sound.PORTAL,1,0);
            ((Player) cmd.getSender()).playSound(((Player) cmd.getSender()).getLocation(), Sound.PORTAL,1,0);
            ((Player) cmd.getSender()).playSound(((Player) cmd.getSender()).getLocation(), Sound.PORTAL,1,0);
        }

    }
}
