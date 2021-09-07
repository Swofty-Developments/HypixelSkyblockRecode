package net.atlas.SkyblockSandbox.gui.guis.storage;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnderChestPage extends NormalGUI
{
	private final int page;

	public EnderChestPage(SBPlayer owner, int page)
	{
		super(owner);
		this.page = page;
	}

	@Override
	public void handleMenu(InventoryClickEvent event)
	{
		if (event.getSlot() < 9 && event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE)
			event.setCancelled(true);

		if (event.getSlot() == 0 && event.getCurrentItem().getType() == Material.ARROW)
		{
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(SBX.getInstance(), () -> new StorageGUI(getOwner()).open(), 3);
		}
	}

	@Override
	public boolean setClickActions()
	{
		return false;
	}

	@Override
	public String getTitle()
	{
		return "Ender Chest Page " + page;
	}

	@Override
	public int getRows()
	{
		return 6;
	}

	@Override
	public void setItems()
	{
		StorageCache cache = new StorageCache(getOwner());
		if (!cache.isCached())
		{
			cache.refresh(this.page);
		}

		List<ItemStack> contents = new ArrayList<>();

		try
		{
			contents.addAll(Arrays.asList(BukkitSerilization.itemStackArrayFromBase64(cache.getEnderChestPage(this.page))));
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			getOwner().sendMessage("§cFailed to fetch your inventory data. §7(If this isn't your first time opening this ender chest page §ereport this!§7)");
		}

		if (!contents.isEmpty())
			setContents(contents.toArray(new ItemStack[0]));

		for (int i = 0; i < 9; i++)
		{
			setItem(i, FILLER_GLASS);
		}

		setItem(0, makeColorfulItem(Material.ARROW, "&eBack", 1, 0, "&7Click to go back."));
	}
}
