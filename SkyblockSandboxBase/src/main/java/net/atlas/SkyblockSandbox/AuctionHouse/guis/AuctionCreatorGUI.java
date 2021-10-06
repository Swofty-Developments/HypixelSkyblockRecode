package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionBidHandler;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler.Category;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.signGUI.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AuctionCreatorGUI extends NormalGUI implements Backable {
    public static HashMap<UUID, String> PlayerItems = new HashMap<>();
    public static HashMap<UUID, Double> playerPrice = new HashMap<>();
    public static HashMap<UUID, Float> playerTime = new HashMap<>();

    public AuctionCreatorGUI(SBPlayer owner, boolean bin, double price, float timeInHours) {
        super(owner);
        playerPrice.putIfAbsent(owner.getUniqueId(), price);
        playerTime.putIfAbsent(owner.getUniqueId(), timeInHours);
    }

    public AuctionCreatorGUI(SBPlayer owner, boolean bin) {
        super(owner);
    }

    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        SBPlayer player = this.getOwner();
        if (event.getClickedInventory().equals(this.getOwner().getInventory())) {
            if (!PlayerItems.containsKey(this.getOwner().getUniqueId())) {
                if (!event.getCurrentItem().hasItemMeta()) {
                    player.sendMessage("&cThis item must have a Display name or Lore!");
                } else {
                    PlayerItems.put(this.getOwner().getUniqueId(), BukkitSerilization.itemStackToBase64(event.getCurrentItem()));
                    player.getInventory().setItem(event.getSlot(), null);
                    this.updateItems();
                }
            }
        } else {
            switch(event.getSlot()) {
                case 13:
                    if (PlayerItems.containsKey(this.getOwner().getUniqueId())) {
                        player.getInventory().addItem(BukkitSerilization.itemStackFromBase64(PlayerItems.get(player.getUniqueId())));
                        PlayerItems.remove(this.getOwner().getUniqueId());
                        this.updateItems();
                    }
                    break;
                case 29:
                    if (PlayerItems.containsKey(this.getOwner().getUniqueId())) {
                        final UUID auctionID = UUID.randomUUID();
                        long time = 0L;
                        Category category = Category.TOOLSMISC;

                        for(Category cat : Category.values()) {
                            if (cat.getTypes() != null) {
                                for(Material mat : cat.getTypes()) {
                                    if (BukkitSerilization.itemStackFromBase64(PlayerItems.get(this.getOwner().getUniqueId())).getType().equals(mat)) {
                                        category = cat;
                                    }
                                }
                            }
                        }

                        if (playerTime.get(player.getUniqueId()) < 1.0F) {
                            AuctionItemHandler.ITEMS.put(auctionID, new AuctionItemHandler(auctionID, getOwner().getUniqueId(), PlayerItems.get(getOwner().getUniqueId()), ZonedDateTime.now(ZoneId.of("-05:00")), ZonedDateTime.now(ZoneId.of("-05:00")).plusMinutes(Math.round(playerTime.get(player.getUniqueId()) * 60.0F)), false, playerPrice.get(getOwner().getUniqueId()), playerPrice.get(getOwner().getUniqueId()), new ArrayList<>(), false, category));
                            AuctionBidHandler.bids.put(auctionID, new ArrayList<>());
                        } else {
                            AuctionItemHandler.ITEMS.put(auctionID, new AuctionItemHandler(auctionID, getOwner().getUniqueId(), PlayerItems.get(getOwner().getUniqueId()), ZonedDateTime.now(ZoneId.of("-05:00")), ZonedDateTime.now(ZoneId.of("-05:00")).plusHours(Math.round(playerTime.get(player.getUniqueId()))), false, playerPrice.get(getOwner().getUniqueId()), playerPrice.get(getOwner().getUniqueId()), new ArrayList<>(), false, category));
                            AuctionBidHandler.bids.put(auctionID, new ArrayList<>());
                        }

                        PlayerItems.remove(player.getUniqueId());
                        playerTime.remove(player.getUniqueId());
                        playerPrice.remove(player.getUniqueId());
                        (new AuctionHouseGUI(this.getOwner())).open();
                        (new BukkitRunnable() {
                            public void run() {
                                AuctionItemHandler.ITEMS.get(auctionID).updateToDB();
                            }
                        }).runTaskLaterAsynchronously(SBX.getInstance(), 10L);
                        System.out.println(AuctionItemHandler.ITEMS.get(auctionID));
                    }
                    break;
                case 31:
                    (new SignGUI(SBX.getInstance().signManager, (e) -> {
                        if (NumUtils.isDouble(e.getLines()[0])) {
                            Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {
                                playerPrice.put(this.getOwner().getUniqueId(), Double.valueOf(e.getLines()[0]));
                                (new AuctionCreatorGUI(this.getOwner(), false)).open();
                            }, 3L);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.0F);
                            player.sendMessage("§cThat's not a valid number!");
                        }

                    })).withLines("", "^^^^^^^^^^^^^^^", "Auction starting", "bid price").open(player.getPlayer());
                    break;
                case 33:
                    (new AuctionDurationGUI(player)).open();
            }

        }
    }

    public boolean setClickActions() {
        return false;
    }

    public String getTitle() {
        return "Auction Creator";
    }

    public int getRows() {
        return 6;
    }

    public void setItems() {
        this.setMenuGlass();

        try {
            if (PlayerItems.get(this.getOwner().getUniqueId()) == null) {
                throw new IllegalArgumentException();
            }

            this.setItem(13, BukkitSerilization.itemStackFromBase64(PlayerItems.get(this.getOwner().getUniqueId())));
        } catch (IllegalArgumentException var2) {
            this.setItem(13, makeColorfulItem(Material.STONE_BUTTON, "&7No item", 1, 0));
        }

        this.setItem(31, makeColorfulItem(Material.POWERED_RAIL, "&fStarting bid &6" + playerPrice.get(this.getOwner().getUniqueId()) + " coins", 1, 0));
        if (PlayerItems.containsKey(this.getOwner().getUniqueId())) {
            this.setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§aCreate", 1, 5, "§eClick to create auction!"));
        } else {
            this.setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§cCreate", 1, 14, "§cCan't create auction!"));
        }

        if (playerTime.get(this.getOwner().getUniqueId()) >= 24.0F) {
            this.setItem(33, makeColorfulItem(Material.WATCH, "&fDuration: &e" + Math.round(playerTime.get(this.getOwner().getUniqueId())) / 24 + " days", 1, 0));
        } else if (playerTime.get(this.getOwner().getUniqueId()) >= 1.0F) {
            this.setItem(33, makeColorfulItem(Material.WATCH, "&fDuration: &e" + Math.round(playerTime.get(this.getOwner().getUniqueId())) + " hours", 1, 0));
        } else {
            this.setItem(33, makeColorfulItem(Material.WATCH, "&fDuration: &e" + Math.round(playerTime.get(this.getOwner().getUniqueId()) * 60.0F) + " minutes", 1, 0));
        }

    }

    public void openBack() {
        (new AuctionHouseGUI(this.getOwner())).open();
    }

    public String backTitle() {
        return "Auction House";
    }

    public int backItemSlot() {
        return 49;
    }
}