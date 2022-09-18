package net.atlas.SkyblockSandbox.gui.guis.storage;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.skyblockmenu.SBMenu;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StorageGUI extends NormalGUI
{
	public StorageGUI(SBPlayer owner) {
		super(owner);
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		Player bp = (Player) event.getWhoClicked();
		SBPlayer player = new SBPlayer(bp);

		event.setCancelled(true);

		/*switch (event.getSlot()) {
			case 1:
				player.sendMessage("WASSSUPPPPP");
				break;
			case 2:
				player.sendMessage("WASSSUPPPPPPPP 2");
				break;

		}*/

		if (event.getCurrentItem().getType() == Material.PAPER) {
			new EnderChestPage(getOwner(), event.getSlot() - 8).open();
			getOwner().playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 0);
		}

		if (event.getCurrentItem().getType() == Material.ARROW && event.getSlot() == 45) {
			new Thread(() -> {
				try {
					Thread.sleep(100);
				} catch (Exception ignored) { }

				new SBMenu(player).open();
			}).start();
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public String getTitle() {
		return "Storage";
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setItems() {
		SBPlayer player = getOwner();

		setMenuGlass();
		setItem(4, makeColorfulItem(Material.ENDER_CHEST, "&aEnder Chest", 1, 0, "&7Store global items you can\n&7access anywhere in your ender\n&7chest."));

		setItem(9, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 1", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(10, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 2", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(11, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 3", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(12, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 4", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(13, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 5", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(14, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 6", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(15, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 7", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(16, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 8", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(17, makeColorfulItem(Material.PAPER, "&aEnder Chest Page 9", 1, 0, "&7\n&eLeft-Click to open!\n&bRight-Click to do nothing!"));
		setItem(45, makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To Skyblock Menu"));
	}
}
