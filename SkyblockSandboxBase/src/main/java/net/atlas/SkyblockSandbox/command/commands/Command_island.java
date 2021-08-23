package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.IslandId;
import org.bukkit.plugin.Plugin;

public class Command_island extends SkyblockCommandFramework {

	/**
	 * Initializes the command framework and sets up the command maps
	 *
	 * @param plugin
	 */
	public Command_island(Plugin plugin) {
		super(plugin);
	}

	@SBCommand(name = "island", aliases = {"is"}, description = "Command to island lol", usage = "/island <create/coop/delete>")
	public void islandCMD(SBCommandArgs cmd) {
		String[] args = cmd.getArgs();
		SBPlayer player = new SBPlayer(cmd.getPlayer());

		if (args.length == 0) {
			player.sendMessage("§7Missing args.");
			return;
		}

		switch (args[0].toLowerCase()) {
			case "create":
				if (player.hasIsland()) {
					player.sendMessage("§7Your island already exists!");
				} else {
					try {
						Data.createIsland(player.getPlayer(), IslandId.randomIslandId());
						player.sendMessage("§7Island created!");
					} catch (Exception ex) {
						player.sendMessage("§7Failed to create your island, please report this!");
					}
				}
				break;
			case "home":
			case "tp":
				if (!player.hasIsland()) {
					player.sendMessage("§7You need to create an island first!");
					return;
				}
				player.sendMessage("§7Teleporting home...");
				player.teleport(player.getPlayerIsland().getCenter());

				break;
		}
	}
}
