package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener extends SkyblockListener<BlockPlaceEvent> {
	@Override
	public void callEvent(BlockPlaceEvent event) {
		if (event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("islands")) {
			Log.info("Checking..");

			Block block = event.getBlock();
			Player player = event.getPlayer();
			SBPlayer sbPlayer = new SBPlayer(player);

			if (!sbPlayer.hasIsland())
				return;

			if (block.getLocation().distance(sbPlayer.getPlayerIsland().getCenter()) > 300) {
				event.setCancelled(true);
				player.sendMessage("§cYou've reached the edge of your island!");
			}
		}
	}
}
