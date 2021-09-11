package net.atlas.SkyblockSandbox.AuctionHouse.gui;

import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItem;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.database.mongo.MongoDB;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.gui.SignGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class AuctionBrowserGUI extends PaginatedGUI
{
	public static boolean DISABLED = false;

	private final static Map<UUID, String> SEARCH_MAP = new HashMap<>();

	private final static MongoDB DB = new MongoAH();
	public final static List<AuctionItem> ITEMS = new ArrayList<>();

	private final AuctionCategory category;

	public AuctionBrowserGUI(SBPlayer playerMenuUtility, AuctionCategory category)
	{
		super(playerMenuUtility);
		this.category = category;
	}

	@Override
	public String getTitle() {
		return "Auction Browser";
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public int getPageSize() {
		return 24;
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();

		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getType().equals(Material.AIR)) return;

		if (new AuctionItemHandler.AuctionData().isAuctionedItem(event.getCurrentItem()))
		{
			String id = new AuctionItemHandler.AuctionData().getString(event.getCurrentItem(), "id");

			AuctionItem i = null;
			i = new AuctionItemHandler(UUID.fromString(id),
					Integer.parseInt(DB.getData(player.getUniqueId(), "price").toString()),
					Bukkit.getOfflinePlayer(UUID.fromString(DB.getData(player.getUniqueId(), "owner").toString())),
					BukkitSerilization.itemStackFromBase64(DB.getData(player.getUniqueId(), "item-stack").toString()),
					Boolean.parseBoolean(DB.getData(player.getUniqueId(), "bin").toString()),
					Long.parseLong(DB.getData(player.getUniqueId(), "end-time").toString())
			);

			new AuctionInspectGUI(getOwner(), i).open();
			return;
		}

		switch (event.getCurrentItem().getType())
		{
			case DIAMOND_SWORD:
				if (event.getCurrentItem().getItemMeta().getLore().contains("§8Category"))
				{
					new AuctionBrowserGUI(getOwner(), AuctionCategory.WEAPONS).open();
					return;
				}
				break;
			case DIAMOND_CHESTPLATE:
				if (event.getCurrentItem().getItemMeta().getLore().contains("§8Category"))
				{
					new AuctionBrowserGUI(getOwner(), AuctionCategory.ARMOR).open();
					return;
				}
				break;
			case SKULL_ITEM:
				if (event.getCurrentItem().getItemMeta().getLore().contains("§8Category"))
				{
					new AuctionBrowserGUI(getOwner(), AuctionCategory.ACCESSORIES).open();
					return;
				}
				break;
			case APPLE:
				if (event.getCurrentItem().getItemMeta().getLore().contains("§8Category"))
				{
					new AuctionBrowserGUI(getOwner(), AuctionCategory.CONSUMABLES).open();
					return;
				}
				break;
			case COBBLESTONE:
				if (event.getCurrentItem().getItemMeta().getLore().contains("§8Category"))
				{
					new AuctionBrowserGUI(getOwner(), AuctionCategory.BLOCKS).open();
					return;
				}
				break;
			case STICK:
				if (event.getCurrentItem().getItemMeta().getLore().contains("§8Category"))
				{
					new AuctionBrowserGUI(getOwner(), AuctionCategory.TOOLS_MISC).open();
					return;
				}
				break;
			case ARROW: {
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aNext"))
				{
					if (!((getGui().getCurrentPageNum() == getGui().getPagesNum())))
					{
						getGui().next();
					}
					else
					{
						player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
						player.sendMessage(ChatColor.RED + "You are on the last page.");
					}
					return;
				}
				else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aPrevious"))
				{
					if (!((getGui().getCurrentPageNum() == getGui().getPrevPageNum())))
					{
						getGui().previous();
					}
					else
					{
						player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
						player.sendMessage(ChatColor.RED + "You already on the last page.");
					}

					return;
				}
				break;
			}
			case SIGN:
			{
				if (event.getClick().isRightClick())
				{
					SEARCH_MAP.remove(player.getUniqueId());

					player.playSound(player.getLocation(), Sound.CAT_MEOW, 1, 2);

					new BukkitRunnable() {
						@Override
						public void run() {
							new AuctionBrowserGUI(getOwner(), category).open();
						}
					}.runTaskLater(SBX.getInstance(), 4);

					return;
				}

				new SignGUI(player, new String[] {"^^^^^", "Enter your", "search!"}, (player1, input) ->
				{
					if (input == null || input == "")
					{
						player.sendMessage("§cInvalid query!");
						return;
					}

					SEARCH_MAP.put(player.getUniqueId(), input);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							new AuctionBrowserGUI(getOwner(), category).open();
						}
					}.runTaskLater(SBX.getInstance(), 4);
				});

				/*
				new AnvilGUI(player, e ->
				{
					if (e.getName() == null || e.getName() == "")
					{
						player.sendMessage("§cInvalid Search!");
						return;
					}

					SEARCH_MAP.put(player.getUniqueId(), e.getName());

					new BukkitRunnable() {
						@Override
						public void run() {
							new AuctionBrowserGUI(getOwner(), category).open();
						}
					}.runTaskLater(SBX.getInstance(), 4);


				}).setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.SIGN, "&aEnter Search", 1, 0, "&7^^^^^^\n&7Your Query")).open();
				 */

				break;
			}
			case STAINED_GLASS_PANE:
				return;
		}
	}

	@Override
	public void setItems() {
		if (DISABLED) {
			getOwner().closeInventory();
			getOwner().sendMessage("§cThe auction house is temporarily disabled!");
			return;
		}


		final ItemStack A = makeColorfulItem(Material.STAINED_GLASS_PANE, " ", 1, this.category.getIndex());


		if (SEARCH_MAP.containsKey(getOwner().getUniqueId())) {
			setItem(48, makeColorfulItem(Material.SIGN, "&aSearch", 1, 0, "&7Search through all\n&7items in the Auction House!\n\n&7Query: &e"
					+ SEARCH_MAP.get(getOwner().getUniqueId()) + "\n\n&eClick to search!\n&bRight-click to reset search!"));
		} else {
			setItem(48, makeColorfulItem(Material.SIGN, "&aSearch", 1, 0, "&7Search through all\n&7items in the Auction House!\n\n&eClick to search!"));
		}

		setItem(1, A);
		setItem(2, A);
		setItem(3, A);
		setItem(4, A);
		setItem(5, A);
		setItem(6, A);
		setItem(7, A);
		setItem(8, A);
		setItem(10, A);
		setItem(17, A);
		setItem(19, A);
		setItem(26, A);
		setItem(28, A);
		setItem(35, A);
		setItem(37, A);
		setItem(44, A);
		setItem(46, A);
		setItem(47, A);
		setItem(49, A);
		setItem(50, A);
		setItem(51, A);
		setItem(52, A);
		setItem(53, A);

		setItem(0, makeColorfulItem(Material.DIAMOND_SWORD, "§6Weapons", 1, 0, "&8Category"));
		setItem(9, makeColorfulItem(Material.DIAMOND_CHESTPLATE, "§9Armor", 1, 0, "&8Category"));
		setItem(18, makeColorfulCustomSkullItem("http://textures.minecraft.net/texture/4fcee1d580881a4384828f1b94a0decacec84384c4da64473710466246a4ece6", "§2Accessories", 1,  "&8Category"));
		setItem(27, makeColorfulItem(Material.APPLE, "§cConsumables", 1, 0, "§8Category"));
		setItem(36, makeColorfulItem(Material.COBBLESTONE, "§eBlocks", 1, 0, "§8Category"));
		setItem(45, makeColorfulItem(Material.STICK, "§dTools & Misc", 1, 0, "§8Category"));

		setItem(46, makeColorfulItem(Material.ARROW, "§aPrevious Page", 1, 0, "§eClick to view!"));
		setItem(53, makeColorfulItem(Material.ARROW, "§aNext Page", 1, 0, "§eClick to view!"));

		if (ITEMS != null && !ITEMS.isEmpty()) {
			for (AuctionItem item : ITEMS) {
				if (item.getCategory() != this.category) continue;
				ItemStack s = item.getStack();
				ItemStack s1 = new AuctionItemHandler.AuctionData().put(s, "id", item.getId().toString());

				ItemMeta meta = s1.getItemMeta();
				List<String> lore = new ArrayList<>(s1.getItemMeta().getLore());
				lore.add("§8§m---------------");
				lore.add("§7Seller: " + item.getOwner().getPlayer().getDisplayName());
				lore.add(item.isBIN() ? "§7Buy it now: §6" + item.getPrice() : "§7Starting bid: §6" + new DecimalFormat("#,###").format(item.getPrice()));
				lore.add("");
				lore.add(item.isBIN() ? "" : "§7Highest bid: §6" + new DecimalFormat("#,###").format(Integer.parseInt(DB.getData( item.getId(), "top-bid-amount").toString())));
				lore.add("§7Bidder: " + (item.getHighestBidder() != null ? item.getHighestBidder().getPlayer().getDisplayName() : "§8None"));
				lore.add("");
				lore.add("§eClick to inspect!");
				meta.setLore(lore);
				s1.setItemMeta(meta);
			}
		}
	}

	public static void put(AuctionItem item) {

		ITEMS.add(item);
	}

	public enum AuctionCategory {
		WEAPONS("Weapons", 1),
		ARMOR("Armor", 11),
		ACCESSORIES("Accessories", 13),
		CONSUMABLES("Comsumables ", 14),
		BLOCKS("Blocks", 12),
		TOOLS_MISC("Tools & Misc", 10),
		A("A", 0);

		public int getIndex() {
			return index;
		}

		public String getName() {
			return name;
		}

		private final int index;
		private final String name;

		AuctionCategory(String name, int i) {
			this.name = name;
			this.index = i;
		}
	}

	private List<AuctionItem> searchFor(String search) {
		List<AuctionItem> matches = new ArrayList<>();
		List<AuctionItem> list = new ArrayList<>(ITEMS);

		for (AuctionItem item : list) {
			if (item == null) continue;

			if (ChatColor.stripColor(item.getStack().getItemMeta().getDisplayName()).contains(ChatColor.stripColor(search))) {
				matches.add(item);
			}
		}

		return matches;
	}
}
