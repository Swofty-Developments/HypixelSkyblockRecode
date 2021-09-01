package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.storage.DebugPageGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_debugtest extends SkyblockCommandFramework {
	/**
	 * Initializes the command framework and sets up the command maps
	 *
	 * @param plugin
	 */
	public Command_debugtest(Plugin plugin) {
		super(plugin);
	}

	@SBCommand(name = "debugtest")
	public void debugTest(SBCommandArgs cmd) {
		String[] args = cmd.getArgs();
		Player player = cmd.getPlayer();

		if (args.length < 1)
			return;

		switch (args[0]) {
			case "help":
				player.sendMessage(SUtil.colorize("&fpanel &a- Open debug panel"));
				player.sendMessage(SUtil.colorize("&f1 &a- Send current inventory stored in storage"));
				player.sendMessage(SUtil.colorize("&f2 &a- Test for ERR-102"));
				break;

			case "panel":
				new DebugPageGUI(new SBPlayer(player)).open();
				break;

			case "1": {
				player.sendMessage(SUtil.colorize("&7Checking if your data is cached..."));

				StorageCache  cache = new StorageCache(new SBPlayer(player));
				if (!cache.isCached()) {
					player.sendMessage(SUtil.colorize("&cData not cached! &7Caching your data..."));

					cache.refresh(1);

					player.sendMessage(SUtil.colorize("&7Cache refreshed, fetching data from your cache...."));

					player.sendMessage(SUtil.colorize("&aFetched Data: &f" + cache.getEnderChestPage(1)));
					return;
				}

				player.sendMessage(SUtil.colorize("&7Data is cached, fetching data from your cache..."));
				player.sendMessage(SUtil.colorize("&aFetched Data: &f" + cache.getEnderChestPage(1)));

				break;
			}

			case "2": {
				player.sendMessage("§cNot now!");
				break;
			}
		}
	}
}
