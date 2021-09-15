package net.atlas.SkyblockSandbox.listener.sbEvents.storage;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoDB;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.storage.MongoStorage;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit extends SkyblockListener<PlayerQuitEvent>
{
	@EventHandler
	public void callEvent(PlayerQuitEvent event) {
		StorageCache cache = new StorageCache(new SBPlayer(event.getPlayer()));
		if (cache.isCached()) {
			MongoDB mongoDB = SBX.storage;
			for (int i = 0; i < 9; i++) {
				mongoDB.setData(event.getPlayer().getUniqueId(), "enderchest_page_" + (i + 1), cache.getEnderChestPage(i + 1));
			}
		}
	}
}
