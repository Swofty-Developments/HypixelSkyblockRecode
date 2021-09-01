package net.atlas.SkyblockSandbox.gui.guis.storage;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DebugPageGUI extends NormalGUI {
	public DebugPageGUI(SBPlayer owner) {
		super(owner);
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		event.setCancelled(true);

		switch (event.getSlot()) {
			case 0:
				getOwner().performCommand("debugtest 1");
				getOwner().closeInventory();
				break;
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public String getTitle() {
		return "Debug Panel";
	}

	@Override
	public int getRows() {
		return 4;
	}

	@Override
	public void setItems() {
		setMenuGlass();

		setItem(0, makeColorfulItem(Material.EMPTY_MAP, "&eDebug 1", 1, 0, "&7Send current inventory in storage"));
	}
}
