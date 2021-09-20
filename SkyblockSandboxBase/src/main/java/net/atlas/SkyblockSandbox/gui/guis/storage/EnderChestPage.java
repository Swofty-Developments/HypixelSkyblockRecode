package net.atlas.SkyblockSandbox.gui.guis.storage;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnderChestPage extends NormalGUI
{
	private final int page;

	public EnderChestPage(SBPlayer owner, int page) {
		super(owner);
		this.page = page;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		if (event.getSlot() < 9 && event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE)
			event.setCancelled(true);

		if (event.getRawSlot() == 0 && event.getCurrentItem().getType() == Material.ARROW) {
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(SBX.getInstance(), () -> new StorageGUI(getOwner()).open(), 3);
		}

		if (event.getRawSlot() == 7 && event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(SBX.getInstance(), () -> new EnderChestPage(getOwner(), page - 1).open(), 3);
			getOwner().playSound(getOwner().getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
		}

		if (event.getRawSlot() == 8 && event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(SBX.getInstance(), () -> new EnderChestPage(getOwner(), page + 1).open(), 3);
			getOwner().playSound(getOwner().getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public String getTitle() {
		return "Ender Chest Page " + page;
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setItems() {
		StorageCache cache = new StorageCache(getOwner());
		if (!cache.isCached()) {
			cache.refresh(this.page);
		}

		List<ItemStack> contents = new ArrayList<>();

		try {
			contents.addAll(Arrays.asList(BukkitSerilization.itemStackArrayFromBase64(cache.getEnderChestPage(this.page))));
		} catch (Exception exception) {
			exception.printStackTrace();
			getOwner().sendMessage("§cFailed to fetch your inventory data. §7(If this isn't your first time opening this ender chest page §ereport this!§7)");
		}

		if (!contents.isEmpty()) {
			for(ItemStack i:new ArrayList<>(contents)) {
				contents.set(contents.indexOf(i), NBTUtil.removeTag(i,"mf-gui"));
			}
			setContents(contents.toArray(new ItemStack[0]));
		}

		for (int i = 0; i < 9; i++) {
			setItem(i, FILLER_GLASS);
		}

		setItem(0, makeColorfulItem(Material.ARROW, "&eBack", 1, 0, "&7Click to go back."));
		if (this.page != 1)
			setItem(7, makeColorfulSkullItem("&aPrevious", "http://textures.minecraft.net/texture/118a2dd5bef0b073b13271a7eeb9cfea7afe8593c57a93821e43175572461812",
					1, "&7Go to the previous page."));
		if (this.page != 9)
			setItem(8, makeColorfulSkullItem("&aNext", "http://textures.minecraft.net/texture/d99f28332bcc349f42023c29e6e641f4b10a6b1e48718cae557466d51eb922",
					1, "&7Go to the next page."));
	}
}
