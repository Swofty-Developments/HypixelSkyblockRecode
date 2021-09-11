package net.atlas.SkyblockSandbox.AuctionHouse.gui;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AuctionHouseGUI extends NormalGUI {
	public AuctionHouseGUI(SBPlayer playerMenuUtility) {
		super(playerMenuUtility);
	}

	@Override
	public String getTitle() {
		return "Auction House";
	}

	@Override
	public int getRows() {
		return 4;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();

		switch (event.getCurrentItem().getType()) {
			case BARRIER:
				player.closeInventory();
				break;
			case GOLD_BLOCK:
				new AuctionBrowserGUI(getOwner(), AuctionBrowserGUI.AuctionCategory.WEAPONS).open();
				break;
			case EMPTY_MAP:
				player.closeInventory();
				player.sendMessage("§cRequest failed please try again later.");
				break;
			case GOLD_BARDING:
				if (getOwner().hasAuctions()) {
					new YourAuctionsGUI(getOwner()).open();
					return;
				}
				new AuctionCreatorGUI(getOwner(), false, 500).open();
				break;
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public void setItems() {
		setMenuGlass();

		setItem(11, makeColorfulItem(Material.GOLD_BLOCK, "§6Auction Browser", 1, 0, SUtil.colorize("&7Find items for sale by players\n&7across Skyblock Sandbox!\n\n&7Items offered here are for\n&6auction&7, " +
				"&7meaning you have to\n&7place the top bid to acquire\n&7them!\n\n&eClick to browse!")));

		setItem(15, makeColorfulItem(Material.GOLD_BARDING, "§aCreate Auction", 1, 0));

		setItem(31, makeColorfulItem(Material.BARRIER, "§cClose", 1, 0));
	}
}
