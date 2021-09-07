package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.storage.StorageGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.plugin.Plugin;

public class Command_storage extends SkyblockCommandFramework {
	/**
	 * Initializes the command framework and sets up the command maps
	 *
	 * @param plugin
	 */
	public Command_storage(Plugin plugin) {
		super(plugin);
	}

	@SBCommand(name = "storage", description = "open storage", usage = "/storage", aliases = {"storage"})
	public void storageCMD(SBCommandArgs cmdArgs) {
		new StorageGUI(new SBPlayer(cmdArgs.getPlayer())).open();
	}
}
