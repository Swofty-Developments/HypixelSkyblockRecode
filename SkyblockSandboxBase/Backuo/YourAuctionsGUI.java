package net.atlas.SkyblockSandbox.AuctionHouse.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItem;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.database.mongo.MongoDB;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class YourAuctionsGUI extends NormalGUI {
	private final static MongoDB DB = new MongoAH();
	private final static List<AuctionItem> ITEMS = new ArrayList<>();

	public YourAuctionsGUI(SBPlayer playerMenuUtility) {
		super(playerMenuUtility);
	}

	@Override
	public String getTitle() {
		return "Your Auctions";
	}

	@Override
	public int getRows() {
		return 4;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();

		if (new AuctionItemHandler.AuctionData().isAuctionedItem(event.getCurrentItem())) {
			String id = new AuctionItemHandler.AuctionData().getString(event.getCurrentItem(), "id");

			AuctionItem i = new AuctionItemHandler(UUID.fromString(id),
					Integer.parseInt(DB.getData( p.getUniqueId(), "price").toString()),
					Bukkit.getOfflinePlayer(UUID.fromString(DB.getData( p.getUniqueId(), "owner").toString())),
					BukkitSerilization.itemStackFromBase64(DB.getData(p.getUniqueId(), "item-stack").toString()),
					Boolean.parseBoolean(DB.getData( p.getUniqueId(), "bin").toString()),
					Long.parseLong(DB.getData( p.getUniqueId(), "end-time").toString())
			);

			new AuctionInspectGUI(getOwner(), i).open();
			return;
		}

		switch (event.getSlot()) {
			case 31: {
				new AuctionHouseGUI(getOwner()).open();
				break;
			}
			case 32: {
				new AuctionCreatorGUI(getOwner(), false, 500).open();
				break;
			}
			default:
				if (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) return;
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public void setItems() {
		getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(colorize("&7 "))).asGuiItem());

		ITEMS.clear();

		for (Document doc : DB.getAllDocuments()) {
			if (doc.get("owner").equals(getOwner().getUniqueId().toString())) {
				this.put(new AuctionItemHandler(UUID.fromString(doc.get("id").toString()), Integer.parseInt(doc.get("price").toString()),
						Bukkit.getOfflinePlayer(UUID.fromString(doc.get("owner").toString())),
						BukkitSerilization.itemStackFromBase64(doc.get("item-stack").toString()),
						Boolean.parseBoolean(doc.get("bin").toString()),
						Long.parseLong(doc.get("end-time").toString()))
				);
			}
		}

		for (AuctionItem item : ITEMS) {
			ItemStack s = item.getStack();
			ItemStack s1 = new AuctionItemHandler.AuctionData().put(s, "id", item.getId().toString());

			ItemMeta meta = s1.getItemMeta();
			List<String> lore = new ArrayList<>(s1.getItemMeta().getLore());
			lore.add("§8§m---------------");
			lore.add("§7Seller: " + item.getOwner().getPlayer().getDisplayName());
			lore.add(item.isBIN() ? "§7Buy it now: §6" + item.getPrice() : "§7Starting bid: §6" + new DecimalFormat("#,###").format(item.getPrice()));
			lore.add("");
			lore.add(item.isBIN() ? "" : "§7Highest bid: §6" + new DecimalFormat("#,###").format(Integer.parseInt(DB.getData(item.getId(), "top-bid-amount").toString())));
			lore.add("§7Bidder: " + (item.getHighestBidder() != null ? item.getHighestBidder().getPlayer().getDisplayName() : "§8None"));
			lore.add("");
			lore.add("§eClick to inspect!");
			meta.setLore(lore);
			s1.setItemMeta(meta);

			getGui().addItem(ItemBuilder.from(s).asGuiItem());
		}

		setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Auction House"));
		setItem(32, makeColorfulItem(Material.IRON_BARDING, "§aCreate Auction", 1, 0, "§eClick to become rich!"));
	}

	public void put(AuctionItem item) {
		ITEMS.add(item);
	}
}
