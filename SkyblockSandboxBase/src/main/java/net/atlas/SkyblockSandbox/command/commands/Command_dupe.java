package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.DupeGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_dupe extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_dupe(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "dupe", description = "Allows you to dupe", usage = "/dupe")
    public void dupeCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            new DupeGUI(new SBPlayer((Player) cmd.getSender())).open();
        }

    }
}
