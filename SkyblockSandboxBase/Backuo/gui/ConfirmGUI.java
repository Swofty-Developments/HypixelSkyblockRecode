package net.atlas.SkyblockSandbox.AuctionHouse.gui;

import com.mongodb.lang.Nullable;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItem;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class ConfirmGUI extends NormalGUI {
	private final AuctionItem item;
	private final ConfirmReason reason;

	private int price = 0;
	private boolean bin = false;
	private long endTime = 0L;

	public ConfirmGUI(SBPlayer playerMenuUtility, ConfirmReason reason, @Nullable AuctionItem item) {
		super(playerMenuUtility);
		this.reason = reason;
		this.item = item;
	}

	public ConfirmGUI(SBPlayer playerMenuUtility, ConfirmReason reason) {
		this(playerMenuUtility, reason, null);
	}

	public ConfirmGUI(SBPlayer playerMenuUtility, ConfirmReason reason, boolean bin, int price, long endTime) {
		this(playerMenuUtility, reason, null);
		this.bin = bin;
		this.price = price;
		this.endTime = endTime;
	}

	@Override
	public String getTitle() {
		return "Confirm";
	}

	@Override
	public int getRows() {
		return 3;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();

		switch (event.getCurrentItem().getDurability()) {
			case 14:
				player.closeInventory();
				player.sendMessage("§cCancelled!");
				break;
			case 5:

				switch (reason) {
					case CREATE_AUCTION: {
						if (this.price != 0) {
							AuctionBrowserGUI.put(new AuctionItemHandler(UUID.randomUUID(), this.price, player, AuctionCreatorGUI.SELECTED_ITEM.get(player.getUniqueId()), this.bin, this.endTime).save());
							player.closeInventory();
							player.sendMessage("§aCreated!");
							player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 2);
							AuctionCreatorGUI.SELECTED_ITEM.remove(player.getUniqueId());

							Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () ->
							{
								new AuctionInspectGUI(getOwner(), new AuctionItemHandler(UUID.randomUUID(), this.price, player, AuctionCreatorGUI.SELECTED_ITEM.get(player.getUniqueId()), this.bin, this.endTime)).open();
							}, 2);

							break;
						}

						break;
					}
				}
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public void setItems() {
		setMenuGlass();

		setItem(15, makeColorfulItem(Material.STAINED_CLAY, "§cCancel", 1, 14, "§8Cancel Action"));

		switch (reason) {
			case CREATE_AUCTION: {
				if (this.price != 0) {
					setItem(11, makeColorfulItem(Material.STAINED_CLAY, "§aConfirm", 1, 5,
							colorize("&8Create Auction\n\n&7Item: " + AuctionCreatorGUI.SELECTED_ITEM.get(getOwner().getUniqueId()).getItemMeta().getDisplayName() + "\n&7Price: &6" + this.price + " coins\n&7BIN: " + (this.bin ? "&atrue" : "&cfalse") + "\n\n&eClick to create!")));
					return;
				}

				if (item == null) {
					setItem(11, makeColorfulItem(Material.STAINED_CLAY, "§aConfirm", 1, 5, "§8Confirm Action"));
					return;
				}

				break;
			}
		}
	}

	public enum ConfirmReason {
		CREATE_AUCTION;
	}
}
