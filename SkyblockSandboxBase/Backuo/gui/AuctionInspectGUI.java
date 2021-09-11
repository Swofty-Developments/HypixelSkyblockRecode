package net.atlas.SkyblockSandbox.AuctionHouse.gui;

import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItem;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.database.mongo.MongoDB;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuctionInspectGUI extends NormalGUI{
	private final static MongoDB DB = new MongoAH();
	private final static DecimalFormat DF = new DecimalFormat("#,###");

	private final AuctionItem item;

	public AuctionInspectGUI(SBPlayer playerMenuUtility, AuctionItem item) {
		super(playerMenuUtility);
		this.item = item;
	}

	@Override
	public String getTitle() {
		return item.isBIN() ? "BIN Auction View" : "Auction View";
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();

		switch (event.getCurrentItem().getType()) {
			case ARROW:
				new AuctionBrowserGUI(getOwner(), AuctionBrowserGUI.AuctionCategory.WEAPONS).open();
				break;
			case GOLD_NUGGET: {
				if (!item.isBIN()) {
					player.closeInventory();
					try {
						AuctionItem currItem = item;
						currItem.setHighestBid((int) (currItem.getHighestBid() + (currItem.getHighestBid() * 0.2)));
						currItem.setHighestBidder(player);
					} catch (Exception ex) {
						player.sendMessage("§cFailed to place your bid! §7(" + ex.getMessage().toUpperCase() + ")");
						SBX.getInstance().getLogger().severe("Failed to place bid for " + item.getId().toString() + "!");
						SBX.getInstance().getLogger().severe("Exception message: " + ex.getMessage().toUpperCase());
						return;
					}
					player.sendMessage("§aBid of §6" + DF.format(item.getHighestBid()) + " coins §aplaced for " + item.getStack().getItemMeta().getDisplayName() + "§a!");
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 2);
				} else {
					try {
						player.getInventory().addItem(new AuctionItemHandler.AuctionData().remove(item.getStack()));
						item.setEnded(true);
						item.setBought(true);
						item.setBuyer(player.getUniqueId().toString());
						player.closeInventory();
					} catch (Exception ex) {
						SBX.getInstance().getLogger().severe("Failed to save purchase item " + item.getId().toString());
						player.sendMessage("§cFailed to purchase item! Please report this!");
						return;
					}
					player.sendMessage("§aSuccessfully bought " + item.getStack().getItemMeta().getDisplayName() + " §a for §6" + DF.format(item.getPrice()) + " coins§a!");
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 2);
				}
				break;
			}

			case GOLD_BLOCK: {
				if (item.isBought()) {
					if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
						player.sendMessage("§aClaimed §6" + new DecimalFormat("#,###").format(item.getHighestBid()) + " coins§a!");
						player.closeInventory();
						item.remove();
					}
				} else {
					if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
						player.getInventory().addItem(item.getStack());
						item.remove();
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
						break;
					}
				}

				break;
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
		SBPlayer player = getOwner();

		ItemStack s = item.getStack();
		ItemStack s1 = new AuctionItemHandler.AuctionData().put(s, "id", item.getId().toString());

		ItemMeta meta = s1.getItemMeta();
		List<String> lore = new ArrayList<>(s1.getItemMeta().getLore());
		lore.add("§8§m---------------");
		lore.add("§7Seller: " + item.getOwner().getPlayer().getDisplayName());
		lore.add(item.isBIN() ? "§7Buy it now: §6" + item.getPrice() : "§7Starting bid: §6" + DF.format(item.getPrice()));
		lore.add("");
		lore.add(item.isBIN() ? "" : "§7Highest bid: §6" + new DecimalFormat("#,###").format(Integer.parseInt(DB.getData(item.getId(), "top-bid-amount").toString())));
		lore.add("§7Bidder: " + (item.getHighestBidder() != null ? item.getHighestBidder().getPlayer().getDisplayName() : "§8None"));
		lore.add("");
		lore.add(item.isEnded() ? "§7Status: §cEnded!" : "§7Status: §8Pending...");
		lore.add("");
		lore.add(item.getOwner().getUniqueId().equals(player.getUniqueId()) ? "§aYou own this auction!" : "§aCurrently inspecting!");
		meta.setLore(lore);
		s1.setItemMeta(meta);

		setItem(13, s1);

		if (item.isEnded()) {
			if (item.isBought()) {
				if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
					setItem(31, makeColorfulItem(Material.GOLD_BLOCK, "§6Claim Coins", 1, 0, "§7Amount: §6" + item.getHighestBid() + " coins\n\n§eClick to claim!"));
				} else if (Bukkit.getOfflinePlayer(UUID.fromString(item.getBuyer())) != null && Bukkit.getOfflinePlayer(UUID.fromString(item.getBuyer())).getUniqueId().equals(player.getUniqueId())) {
					setItem(31, makeColorfulItem(Material.GOLD_BLOCK, "§6Claim Items", 1, 0, "§eClick to claim!"));
				}
			} else {
				if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
					setItem(31, makeColorfulItem(Material.GOLD_BLOCK, "§6Claim Items", 1, 0, "§eClick to claim!"));
				}
			}
		} else {
			if (Boolean.parseBoolean(DB.getData(item.getId(), "bin").toString())) {
				if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
					setItem(31, makeColorfulItem(Material.POISONOUS_POTATO, "§6Buy item", 1, 0, "§7item price: §6" + DF.format(item.getPrice()) + " coins\n\n§cYou cannot buy your own auction!"));
					return;
				}
				setItem(31, makeColorfulItem(Material.GOLD_NUGGET, "§6Buy item", 1, 0, "§7item price: §6" + DF.format(item.getPrice()) + " coins\n\n§eClick to buy it now!"));
			} else {
				if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
					setItem(29, makeColorfulItem(Material.POISONOUS_POTATO, "§6Submit bid", 1, 0, "§7New bid: §6" + (item.getHighestBid() + (item.getPrice() * 0.2)) + " coins\n\n§cYou cannot bid on your own auction!"));
					return;
				}
				setItem(29, makeColorfulItem(Material.GOLD_NUGGET, "§6Submit bid", 1, 0, "§7New bid: §6" + (item.getHighestBid() + (item.getPrice() * 0.2)) + " coins\n\n§eClick to submit bid!"));
			}
		}

		if (item.getOwner().getUniqueId().equals(player.getUniqueId())) {
			setItem(33, makeColorfulItem(Material.STAINED_CLAY, "§cCancel Auction", 1, 14, "§eClick to cancel!"));
		}

		setItem(49, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Auction Browser"));
	}
}
