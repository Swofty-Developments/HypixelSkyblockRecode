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
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
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
                if (item.hasEnded && !item.claimed) {
                    if (item.getBids().isEmpty()) {
                        if (getOwner().hasSpace()) {
                            this.getOwner().getInventory().addItem(BukkitSerilization.itemStackFromBase64(this.item.getItemStack()));
                            item.setClaimed(true);
                            item.updateToDB();
                            AuctionItemHandler.ITEMS.remove(this.item.auctionID);
                            this.getOwner().closeInventory();
                        } else {
                            getOwner().sendMessage("&cYou don't have enough space in your inventory!");
                        }
                    } else {
                        SBX.getInstance().coins.addCoins(getOwner(), item.getLastBid().getPrice(), CoinEvent.AH);
                        item.setClaimed(true);
                        item.updateToDB();
                        AuctionItemHandler.ITEMS.remove(this.item.auctionID);
                    }
                } else {
                    this.getOwner().sendMessage("&aYou cannot bid on your own auction!");
                }
            });
            if (this.item.getBids().isEmpty()) {
                this.setAction(31, (e) -> {
                    if (this.item.getBids().isEmpty()) {
                        if (getOwner().hasSpace()) {
                            this.item.setHasEnded(true);
                            this.item.setClaimed(true);
                            this.item.updateToDB();
                            AuctionItemHandler.ITEMS.remove(this.item.auctionID);
                            this.getOwner().getInventory().addItem(BukkitSerilization.itemStackFromBase64(this.item.getItemStack()));
                            this.getOwner().closeInventory();
                        } else {
                            getOwner().sendMessage("&cYou don't have enough space in your inventory!");
                        }
                    }

                });
            }
        } else{
            this.setAction(29, (e) -> {
                if (item.hasEnded && !item.claimed) {
                    if(item.getLastBid().getUuid() == getOwner().getUniqueId()) {
                        if (getOwner().hasSpace()) {
                            this.getOwner().getInventory().addItem(BukkitSerilization.itemStackFromBase64(this.item.getItemStack()));
                            this.getOwner().closeInventory();
                        } else {
                            getOwner().sendMessage("&cYou don't have enough space in your inventory!");
                        }
                    } else {
                        double amount = 0D;
                        for (AuctionBidHandler bid : item.getBids()) {
                            if (bid.getUuid() == getOwner().getUniqueId()) {
                                amount = bid.getPrice();
                            }
                        }
                        SBX.getInstance().coins.addCoins(getOwner(), amount, CoinEvent.AH);
                    }
                } else {
                    if (SBX.getInstance().coins.getCoins(this.getOwner()) >= this.item.getCurrentPrice() * 1.15D) {
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
                    }
                }
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
        this.setItem(13, this.item.createInspectorItem(getOwner()));

        if (this.item.getOwner().equals(this.getOwner().getUniqueId())) {
            if (!item.hasEnded) {
                this.setItem(29, makeColorfulItem(Material.POISONOUS_POTATO, "&6Submit bid", 1, 0, "\n&7New bid: &6" + (new DecimalFormat("#,###")).format(this.item.getCurrentPrice() * 1.15D) + " coins\n\n&aThis is your own auction!"));
                if (this.item.getBids().isEmpty()) {
                    this.setItem(31, makeColorfulItem(Material.STAINED_CLAY, "&cCancel Auction", 1, 14));
                }
            } else {
                if (item.getBids().isEmpty()) {
                    setItem(29, makeColorfulItem(Material.GOLD_BLOCK, "&7Collect Auction", 1, 0, "\n&7No one has bid on this item.\n&bYou may pick it back up.\n\n&eClick to pick up item!"));
                } else {
                    setItem(29, makeColorfulItem(Material.GOLD_BLOCK, "&7Collect Auction", 1, 0, "\n&7Item sold to " + item.getBids().get(item.getBids().size() - 1).getName() + "\n&7for &6" + item.getLastBid().getPrice() + " coins"));
                }
            }
        } else {
            if(item.hasEnded) {
                setItem(29, makeColorfulItem(Material.GOLD_BLOCK, "&7Collect Auction", 1, 0, "\n&7You had the top bid for &6" + new DecimalFormat("0,000").format(item.getLastBid().getPrice()) + " coins\nYou may collect the item.\n\n&eClick to pick up item!"));
            } else {
                if (SBX.getInstance().coins.getCoins(this.getOwner()) >= this.item.getCurrentPrice() * 1.15D) {
                    this.setItem(29, makeColorfulItem(Material.GOLD_NUGGET, "&6Submit bid", 1, 0));
                    this.setItem(31, makeColorfulItem(Material.GOLD_INGOT, "&fBid amound: &6" + this.item.getCurrentPrice() * 1.15D + " coins", 1, 0));
                } else {
                    this.setItem(29, makeColorfulItem(Material.POISONOUS_POTATO, "&6Submit bid", 1, 0));
                }
            }
        }

        if (this.item.getBids().isEmpty()) {
            ItemStack item = makeColorfulItem(Material.MAP, "&fBid History", 1, 0, "&7No bids have been placed on this\n&7item yet.\n\n&7Be the first to bid on it!");
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            item.setItemMeta(meta);
            this.setItem(33, item);
        } else {
            DecimalFormat format = new DecimalFormat("#,###");
            ArrayList<String> builder = new ArrayList();
            builder.add("&7Total bids: &a" + this.item.getBids().size() + " bids");
            builder.add("&8-----------------");
            int last = Math.max(this.item.getBids().size() - 6, 0);

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
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
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