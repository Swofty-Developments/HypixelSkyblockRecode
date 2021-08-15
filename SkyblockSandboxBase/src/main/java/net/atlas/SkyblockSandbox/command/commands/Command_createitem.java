package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
//import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreator;
//import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreatorPage;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_createitem extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_createitem(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "createitem", aliases = {"edit"}, description = "Opens the item creator GUI", usage = "/createitem")
    public void createitemCommand(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            String[] args = cmd.getArgs();
            new ItemCreatorGUIMain(p).open();
        }

    }
}
