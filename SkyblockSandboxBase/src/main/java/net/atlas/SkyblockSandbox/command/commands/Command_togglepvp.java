package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import org.bukkit.plugin.Plugin;

public class Command_togglepvp extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_togglepvp(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "togglepvp", description = "Toggle PVP between true or false", permission = "sbx.admin.togglepvp")
    public void onCmd(SBCommandArgs arguments) {
        SBX.pvpEnabled = !SBX.pvpEnabled;
        arguments.getSender().sendMessage(SBX.pvpEnabled ? "§7PvP was turned §aon§7." : "§7PvP was turned §coff§7.");
    }
}
