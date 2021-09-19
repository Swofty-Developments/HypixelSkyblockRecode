package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import net.atlas.SkyblockSandbox.AuctionHouse.AuctionBidHandler;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.economy.CoinEvent;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AuctionInspectorGUI extends NormalGUI implements Backable {
    AuctionItemHandler item;

    public AuctionInspectorGUI(SBPlayer owner, AuctionItemHandler item) {
        super(owner);
        this.item = item;
    }

    public void handleMenu(InventoryClickEvent event) {
    }

    public boolean setClickActions() {
        if (this.item.getOwner().equals(this.getOwner().getUniqueId())) {
            this.setAction(29, (e) -> {
                this.getOwner().sendMessage("&aYou cannot bid on your own auction!");
            });
            if (this.item.getHighestBidder() == null) {
                this.setAction(31, (e) -> {
                    if (this.item.highestBidder == null) {
                        this.item.setHasEnded(true);
                        this.item.updateToDB();
                        AuctionItemHandler.ITEMS.remove(this.item.auctionID);
                        this.getOwner().closeInventory();
                    }

                });
            }
        } else if (SBX.getInstance().coins.getCoins(this.getOwner()) >= this.item.getCurrentPrice() * 1.15D) {
            this.getOwner().sendMessage(String.valueOf(SBX.getInstance().coins.getCoins(this.getOwner())));
            this.setAction(29, (e) -> {
                double lastPrice = 0.0D;

                for (AuctionBidHandler data : item.bids) {
                    if (data.uuid == this.getOwner().getUniqueId()) {
                        lastPrice = data.price;
                    }
                }

                this.item.bid(this.getOwner());
                this.getOwner().sendMessage("&fSuccessfully placed a bid for &6" + (new DecimalFormat("#,###")).format(this.item.getCurrentPrice()) + " coins&f!");
                SBX.getInstance().coins.removeCoins(this.getOwner(), this.item.getCurrentPrice() - lastPrice, CoinEvent.AH);
                this.updateItems();
            });
        }

        return true;
    }

    public String getTitle() {
        return "Auction View";
    }

    public int getRows() {
        return 6;
    }

    public void setItems() {
        this.setMenuGlass();
        this.setItem(13, this.item.createInspectorItem());
        if (this.item.getOwner().equals(this.getOwner().getUniqueId())) {
            this.setItem(29, makeColorfulItem(Material.POISONOUS_POTATO, "&6Submit bid", 1, 0, "\n&7New bid: &6" + (new DecimalFormat("#,###")).format(this.item.getCurrentPrice() * 1.15D) + " coins\n\n&aThis is your own auction!"));
            if (this.item.getBids().isEmpty()) {
                this.setItem(31, makeColorfulItem(Material.STAINED_CLAY, "&cCancel Auction", 1, 14, new String[0]));
            }
        } else if (SBX.getInstance().coins.getCoins(this.getOwner()) >= this.item.getCurrentPrice() * 1.15D) {
            this.setItem(29, makeColorfulItem(Material.GOLD_NUGGET, "&6Submit bid", 1, 0, new String[0]));
            this.setItem(31, makeColorfulItem(Material.GOLD_INGOT, "&fBid amound: &6" + this.item.getCurrentPrice() * 1.15D + " coins", 1, 0, new String[0]));
        } else {
            this.setItem(29, makeColorfulItem(Material.POISONOUS_POTATO, "&6Submit bid", 1, 0, new String[0]));
        }

        if (this.item.getBids().isEmpty()) {
            this.setItem(33, makeColorfulItem(Material.MAP, "&fBid History", 1, 0, "&7No bids have been placed on this\n&7item yet.\n\nBe the first to bid on it!"));
        } else {
            DecimalFormat format = new DecimalFormat("#,###");
            ArrayList<String> builder = new ArrayList();
            builder.add("&7Total bids: &a" + this.item.getBids().size() + " bids");
            builder.add("&8-----------------");
            int last = this.item.getBids().size() - 6 > 0 ? this.item.getBids().size() - 6 : 0;

            for(int i = 0; i < 6; ++i) {
                if (this.item.getBids().size() - i >= 0) {
                    AuctionBidHandler bid = this.item.getBids().get(this.item.getBids().size() - 1 - i);
                    if (bid != null) {
                        builder.add("&7Bid: &6" + format.format(bid.getPrice()) + " coins");
                        builder.add("&7By: " + bid.getName());
                        long time = ZonedDateTime.now(ZoneId.of("-05:00")).toInstant().toEpochMilli() - bid.getTime().toInstant().toEpochMilli();
                        if (TimeUnit.MILLISECONDS.toDays(time) >= 1L) {
                            builder.add("&b" + TimeUnit.MILLISECONDS.toDays(time) + " day(s) ago");
                        } else if (TimeUnit.MILLISECONDS.toHours(time) >= 1L) {
                            builder.add("&b" + TimeUnit.MILLISECONDS.toHours(time) + " hour(s) ago");
                        } else if (TimeUnit.MILLISECONDS.toMinutes(time) >= 1L) {
                            builder.add("&b" + TimeUnit.MILLISECONDS.toMinutes(time) + " minute(s) ago");
                        } else if (TimeUnit.MILLISECONDS.toSeconds(time) >= 1L) {
                            builder.add("&b" + TimeUnit.MILLISECONDS.toSeconds(time) + " second(s) ago");
                        }

                        if (this.item.getBids().size() - 1 - i != last) {
                            builder.add("&8-----------------");
                        }
                    }
                }
            }

            ItemStack item = makeColorfulItem(Material.MAP, "&fBid History", 1, 0, builder);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
            item.setItemMeta(meta);
            this.setItem(33, item);
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