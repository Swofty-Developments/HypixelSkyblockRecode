package net.atlas.SkyblockSandbox.AuctionHouse.gui;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SignGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuctionCreatorGUI extends NormalGUI
{
	public final static Map<UUID, ItemStack> SELECTED_ITEM = new HashMap<>();

	private final boolean bin;
	private final int price;

	public AuctionCreatorGUI(SBPlayer playerMenuUtility, boolean bin, int price) {
		super(playerMenuUtility);
		this.bin = bin;
		this.price = price;
	}

	@Override
	public String getTitle() {
		return bin ? "Create BIN Auction" : "Create Auction";
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
			case STAINED_CLAY: {
				if (event.getCurrentItem().getDurability() == (byte) 14)
					return;
				if (event.getCurrentItem().getDurability() == 5) {
					new ConfirmGUI(getOwner(), ConfirmGUI.ConfirmReason.CREATE_AUCTION, this.bin, this.price, System.currentTimeMillis() + 5000).open();
					break;
				}
				return;
			}
			case STONE_BUTTON:
			case STAINED_GLASS_PANE: return;

			case GOLD_INGOT:
				if (event.getSlot() == 48 || event.getSlot() == 31)
				{
					if (event.getCurrentItem().hasItemMeta())
					{
						if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aSwitch to BIN"))
						{
							new AuctionCreatorGUI(getOwner(), true, (this.price)).open();
						}
						if (event.getCurrentItem().getItemMeta().getDisplayName().contains("price"))
						{
							new SignGUI(player, new String[] {"^^^^^^", "Enter your", "query."}, (player1, input) ->
							{
								if (NumUtils.isInt(input))
								{
									Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () ->
									{
										new AuctionCreatorGUI(getOwner(), bin, Integer.parseInt(input)).open();
									}, 3);
								}
								else
								{
									player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
									player.sendMessage("§cThat's not a valid number!");
								}
							});
						}
					}
					return;
				}
				break;
			case POWERED_RAIL:
				if (event.getSlot() == 48 || event.getSlot() == 31) {
					if (event.getCurrentItem().hasItemMeta()) {
						if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aSwitch to Auction")) {
							new AuctionCreatorGUI(getOwner(), false, this.price).open();
						}
						if (event.getCurrentItem().getItemMeta().getDisplayName().contains("bid")) {
							new SignGUI(player, new String[] {"^^^^^", "Enter your", "query."}, (player1, input) -> {
								if (NumUtils.isInt(input)) {
									Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {
										new AuctionCreatorGUI(getOwner(), bin, Integer.parseInt(input)).open();
									}, 2);
								}
							});

							AnvilGUI gui = new AnvilGUI(player, e -> {
								if (NumUtils.isInt(e.getName())) {
									Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {
										new AuctionCreatorGUI(getOwner(), bin, Integer.parseInt(e.getName())).open();
									}, 3);
								} else {
									player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
									player.sendMessage("§cThat's not a valid number!");
								}
							});
							gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Item price", 1, 0, "§f^^^^^\n§fYour item's price"));
							gui.open();
						}
					}
					return;
				}
				break;
			case ARROW:
			{
				break;
			}
		}

		if (event.getCurrentItem() == null) return;

		if (SELECTED_ITEM.containsKey(player.getUniqueId())) {
			if (event.getSlot() == 13) {
				event.getInventory().setItem(13, makeColorfulItem(Material.STONE_BUTTON, "§ePut Item", 1, 0));
				player.getInventory().addItem(SELECTED_ITEM.get(player.getUniqueId()));
				player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 0);
				SELECTED_ITEM.remove(player.getUniqueId());
				setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§cCreate", 1, 14, "§cCan't create auction!"));
			}
			return;
		}

		if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().hasLore()) {
			if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aGo Back"))
			{
				return;
			}

			setItem(13, event.getCurrentItem());
			SELECTED_ITEM.put(player.getUniqueId(), event.getCurrentItem());
			player.getInventory().setItem(event.getSlot(), null);
			player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 2);
			setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§aCreate", 1, 5, "§eClick to create auction!"));
		} else {
			player.sendMessage("§cYour item must have a displayname or lore in order to be auctioned!");
		}
	}

	@Override
	public boolean setClickActions() {
		return false;
	}

	@Override
	public void setItems() {
		DecimalFormat df = new DecimalFormat("#,###");
		SBPlayer player = getOwner();

		setMenuGlass();
		if (SELECTED_ITEM.containsKey(player.getUniqueId()))
			setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§aCreate", 1, 5, "§eClick to create auction!"));
		else
			setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§cCreate", 1, 14, "§cCan't create auction!"));

		if (SELECTED_ITEM.containsKey(player.getUniqueId()))
			setItem(13, SELECTED_ITEM.get(player.getUniqueId()));
		else
			setItem(13, makeColorfulItem(Material.STONE_BUTTON, "§ePut Item", 1, 0));

		if (this.bin)
			setItem(48, makeColorfulItem(Material.POWERED_RAIL, "§aSwitch to Auction", 1, 0));
		else
			setItem(48, makeColorfulItem(Material.GOLD_INGOT, "§aSwitch to BIN", 1, 0));

		if (this.bin) {
			setItem(31, makeColorfulItem(Material.GOLD_INGOT, "§fItem price: §6" + df.format(this.price) + " coins", 1, 0, SUtil.colorize("&7The price at which you want to\n&7sell this item.\n\n&7Extra fee: &6" + (50 + (this.price * 0.01)) + " coins\n\n&eClick to edit!")));
		} else {
			setItem(31, makeColorfulItem(Material.POWERED_RAIL, "§fStarting bid: §6" + df.format(this.price) + " coins", 1, 0, "§eClick to edit!"));
		}
	}
}
