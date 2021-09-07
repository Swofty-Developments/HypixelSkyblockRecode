package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.entity.customEntity.NoTeleportEnderman;
import net.atlas.SkyblockSandbox.gui.guis.skyblockmenu.SBMenu;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;

public class Command_sbmenu extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_sbmenu(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "sbmenu", aliases = {"menu"}, description = "opens the skyblock menu", usage = "/sbmenu")
    public void spawnMobCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            new SBMenu(p).open();
        }
    }
}
