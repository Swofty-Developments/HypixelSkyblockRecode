package net.atlas.SkyblockSandbox.listener.sbEvents.storage;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose extends SkyblockListener<InventoryCloseEvent>
{
	@EventHandler
	public void callEvent(InventoryCloseEvent event)
	{
		if (event.getInventory().getTitle().contains("Ender Chest Page"))
		{
			int page = Integer.parseInt(event.getInventory().getTitle().split(" ")[3]);
			String base64Contents = BukkitSerilization.itemStackArrayToBase64(event.getInventory().getContents());

			Player player = (Player) event.getPlayer();

			StorageCache cache = new StorageCache(new SBPlayer(player));
			cache.setEnderChestPage(page, base64Contents);

			event.getPlayer().sendMessage("§aYour ender chest data was saved!");
		}
	}
}
