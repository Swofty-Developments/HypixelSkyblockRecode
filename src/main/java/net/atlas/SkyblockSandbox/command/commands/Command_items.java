package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.TestPage;
import net.atlas.SkyblockSandbox.gui.guis.items.ItemMainPage;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_items extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_items(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "items", aliases = {"i", "e"}, description = "Gets items from Hypixel, Vanilla and our own system.", usage = "/items")
    public void itemsCmd(SBCommandArgs cmd) {
        if (!(cmd.getSender() instanceof Player)) return;
        SBPlayer p = new SBPlayer((Player) cmd.getSender());
        new ItemMainPage(p).open();
    }
}
