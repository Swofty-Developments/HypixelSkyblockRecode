package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener extends SkyblockListener<PlayerDropItemEvent> {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void callEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
		event.getPlayer().sendMessage("§cNo dropping items!");
	}

	@EventHandler
	public void someOtherEventIdK(ItemDespawnEvent event) {
		if (event.getEntity().getItemStack().hasItemMeta())
			event.setCancelled(true);
	}
}
