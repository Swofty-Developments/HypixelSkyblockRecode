package net.atlas.SkyblockSandbox.AuctionHouse;

import lombok.Getter;
import lombok.Setter;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class AuctionItemHandler {
    public static HashMap<UUID, AuctionItemHandler> ITEMS = new HashMap<>();
    public static MongoAH mongo = new MongoAH();

    public final UUID auctionID;
    public final String itemStack;
    public final ZonedDateTime startTime;
    public ZonedDateTime endTime;
    public final boolean hasEnded;
    public final double startingPrice;
    public double currentPrice;
    public UUID highestBidder;
    public final boolean isBin;
    public final UUID owner;
    public AuctionItemHandler(UUID auctionID, UUID owner, String itemStack, ZonedDateTime startTime, ZonedDateTime endTime, boolean hasEnded, double startingPrice, double currentPrice, boolean isBin) {
        this.auctionID = auctionID;
        this.owner = owner;
        this.itemStack = itemStack;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasEnded = hasEnded;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.isBin = isBin;
    }
    public ItemStack createItem() {
        ItemStack s = BukkitSerilization.itemStackFromBase64(itemStack);
        ItemStack s1 = NBTUtil.setString(s, auctionID.toString(), "Auction-id");

        assert s1 != null;
        ItemMeta meta = s1.getItemMeta();
        List<String> lore = new ArrayList<>(s1.getItemMeta().getLore());
        lore.add("§8§m---------------");
        lore.add("§7Seller: " + getOwner());
        lore.add(isBin() ? "§7Buy it now: §6" + getStartingPrice() : "§7Starting bid: §6" + new DecimalFormat("#,###").format(getStartingPrice()));
        lore.add("");
        lore.add(isBin() ? "" : "§7Highest bid: §6" + new DecimalFormat("#,###").format(getCurrentPrice()));
        lore.add("§7Bidder: " + (getHighestBidder() != null ? getHighestBidder() : "§8None"));
        lore.add("");
        lore.add("§eClick to inspect!");
        meta.setLore(lore);
        s1.setItemMeta(meta);
        return s1;
    }
    public void bid(Player player) {
        setHighestBidder(player.getUniqueId());
        setCurrentPrice(getCurrentPrice()*1.15);
        mongo.setData(getAuctionID(), "currentPrice", getCurrentPrice());
        mongo.setData(getAuctionID(), "highestBidder", getHighestBidder());
        if (TimeUnit.MILLISECONDS.toMinutes(endTime.toInstant().toEpochMilli() - ZonedDateTime.now(ZoneId.of("-05:00")).toInstant().toEpochMilli()) <= 2) {
            setEndTime(endTime.plusMinutes(2));
            mongo.setData(getAuctionID(), "endTime", getEndTime().toInstant().toEpochMilli());
        }
    }
    public void updateToDB() {
        mongo.setData(this);
    }
}
