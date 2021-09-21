package net.atlas.SkyblockSandbox.AuctionHouse;

import lombok.Getter;
import lombok.Setter;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
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
    public boolean hasEnded;
    public final double startingPrice;
    public double currentPrice;
    public UUID highestBidder;
    public final ArrayList<AuctionBidHandler> bids;
    public final boolean isBin;
    public final UUID owner;
    public final Category category;
    public AuctionItemHandler(UUID auctionID, UUID owner, String itemStack, ZonedDateTime startTime, ZonedDateTime endTime, boolean hasEnded, double startingPrice, double currentPrice, ArrayList<AuctionBidHandler> bids, boolean isBin, Category category) {
        this.auctionID = auctionID;
        this.owner = owner;
        this.itemStack = itemStack;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasEnded = hasEnded;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.bids = bids;
        this.isBin = isBin;
        this.category = category;
    }

    public ItemStack createItem() {
        ItemStack s = BukkitSerilization.itemStackFromBase64(itemStack);
        ItemStack s1 = NBTUtil.setString(s, auctionID.toString(), "Auction-id");

        assert s1 != null;
        ItemMeta meta = s1.getItemMeta();
        List<String> lore = s1.getItemMeta().getLore() != null ? s1.getItemMeta().getLore() : new ArrayList<>();
        lore.add("§8§m---------------");
        lore.add("§7Seller: " + this.getOwner());
        if (this.getBids().isEmpty()) {
            lore.add("§7Starting bid: §6" + (new DecimalFormat("#,###")).format(this.getStartingPrice()) + " coins");
        } else {
            lore.add("&7Bids: &a" + this.getBids().size());
            lore.add("");
            lore.add("&7Top bid: &6" + (new DecimalFormat("#,###")).format((this.getBids().get(this.getBids().size() - 1)).getPrice()) + " coins");
            lore.add("&7Bidder: " + (this.getBids().get(this.getBids().size() - 1)).getName());
        }

        lore.add("");
        lore.add("§7Ends in: §e" + time(this.endTime, ZonedDateTime.now(ZoneId.of("-05:00"))));
        lore.add("§eClick to inspect!");
        meta.setLore(SUtil.colorize(lore));
        s1.setItemMeta(meta);
        return s1;
    }

    public ItemStack createInspectorItem() {
        ItemStack s = BukkitSerilization.itemStackFromBase64(this.itemStack);
        ItemStack s1 = NBTUtil.setString(s, this.auctionID.toString(), "Auction-id");

        assert s1 != null;

        ItemMeta meta = s1.getItemMeta();
        List<String> lore = s1.getItemMeta().getLore() != null ? s1.getItemMeta().getLore() : new ArrayList<>();
        lore.add("§8§m---------------");
        lore.add("§7Seller: " + this.getOwner());
        if (this.getBids().isEmpty()) {
            lore.add("§7Starting bid: §6" + (new DecimalFormat("#,###")).format(this.getStartingPrice()) + " coins");
        } else {
            lore.add("&7Bids: &a" + this.getBids().size());
            lore.add("");
            lore.add("&7Top bid: &6" + (new DecimalFormat("#,###")).format((this.getBids().get(this.getBids().size() - 1)).getPrice()) + " coins");
            lore.add("&7Bidder: " + (this.getBids().get(this.getBids().size() - 1)).getName());
        }

        lore.add("");
        lore.add("§7Ends in: §e" + time(this.endTime, ZonedDateTime.now(ZoneId.of("-05:00"))));
        meta.setLore(SUtil.colorize(lore));
        s1.setItemMeta(meta);
        return s1;
    }
    
    public void bid(SBPlayer player) {
        setCurrentPrice(getCurrentPrice() * 1.15D);
        mongo.setData(getAuctionID(), "currentPrice", getCurrentPrice());
        new AuctionBidHandler(player.getUniqueId(), player.getDisplayName(), ZonedDateTime.now(ZoneId.of("-05:00")), getCurrentPrice()).addBid(auctionID);
        AuctionBidHandler.cacheToMongo(auctionID);
        if (endTime.toInstant().toEpochMilli() - TimeUnit.MILLISECONDS.toMinutes(ZonedDateTime.now(ZoneId.of("-05:00")).toInstant().toEpochMilli()) <= 2L) {
            setEndTime(endTime.plusMinutes(2L));
            mongo.setData(getAuctionID(), "endTime", getEndTime().toInstant().toEpochMilli());
        }
    }

    public static String time(ZonedDateTime endDate, ZonedDateTime startDate) {
        long different = endDate.toInstant().toEpochMilli() - startDate.toInstant().toEpochMilli();
        long secondsInMilli = 1000L;
        long minutesInMilli = secondsInMilli * 60L;
        long hoursInMilli = minutesInMilli * 60L;
        long daysInMilli = hoursInMilli * 24L;
        long elapsedDays = different / daysInMilli;
        different %= daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different %= hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different %= minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        StringBuilder builder = new StringBuilder();
        if (elapsedDays > 0L) {
            builder.append(elapsedDays).append("d ");
        }

        if (elapsedHours > 0L) {
            builder.append(elapsedHours).append("h ");
            if (elapsedHours < 6L) {
                if (elapsedMinutes > 0L) {
                    builder.append(elapsedMinutes).append("m ");
                }

                if (elapsedSeconds > 0L) {
                    builder.append(elapsedSeconds).append("s");
                }
            }
        }

        return builder.toString();
    }
    
    public void updateToDB() {
        mongo.setData(this);
    }

    public static void mongoToCache() {
        for (Document doc : mongo.getAllDocuments()) {
            UUID id = UUID.fromString(doc.getString("auctionID"));
            ArrayList<AuctionBidHandler> bids = new ArrayList<>();
            try {
                for (Object o : (ArrayList<Object>) mongo.getDoc(id).get("bids")) {
                    Document doc2 = (Document) o;
                    doc2.forEach((uuid, nameDoc) -> {
                        ((Document) nameDoc).forEach((name, timePriceDoc) -> {
                            ((Document) timePriceDoc).forEach((time, price) -> {
                                bids.add(new AuctionBidHandler(UUID.fromString(uuid), name, ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(time)), ZoneId.of("-05:00")), (Double) price));
                            });
                        });
                    });
                }
            } catch (Exception ignored) {
            }
            AuctionBidHandler.bids.put(id, bids);
            ITEMS.put(id, new AuctionItemHandler(id, UUID.fromString(doc.getString("owner")), doc.getString("item"), ZonedDateTime.ofInstant(Instant.ofEpochMilli(doc.getLong("startTime")), ZoneId.of("-05:00")), ZonedDateTime.ofInstant(Instant.ofEpochMilli(doc.getLong("endTime")), ZoneId.of("-05:00")), doc.getBoolean("hasEnded"), doc.getDouble("startingPrice"), doc.getDouble("currentPrice"), bids, false, Category.valueOf(doc.getString("category"))));
        }
    }

    public enum Category {
        WEAPONS(0, "Weapons", Material.GOLD_SWORD, ChatColor.GOLD, 1, new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.GOLD_SWORD, Material.BOW}),
        ARMOR(1, "Armor", Material.DIAMOND_CHESTPLATE, ChatColor.AQUA, 11, new Material[]{Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_HELMET, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS}),
        ACCESSORIES(2, "Accessories", Material.SKULL_ITEM, "http://textures.minecraft.net/texture/f36b821c1afdd5a5d14e3b3bd0a32263c8df5df5db6e1e88bf65e97b27a8530", ChatColor.DARK_GREEN, 13, (Material[])null),
        CONSUMABLES(3, "Consumables", Material.APPLE, ChatColor.RED, 14, new Material[]{Material.APPLE, Material.GOLDEN_APPLE, Material.GOLDEN_CARROT, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_FISH, Material.COOKED_MUTTON, Material.COOKED_RABBIT, Material.POTION, Material.POTATO_ITEM, Material.BAKED_POTATO, Material.POISONOUS_POTATO, Material.RAW_FISH}),
        BLOCKS(4, "Blocks", Material.COBBLESTONE, ChatColor.YELLOW, 12, (Material[])null),
        TOOLSMISC(5, "Tools & Misc", Material.STICK, ChatColor.LIGHT_PURPLE, 10, new Material[]{Material.GOLD_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.STONE_AXE, Material.WOOD_AXE, Material.DIAMOND_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOOD_PICKAXE, Material.DIAMOND_SPADE, Material.GOLD_SPADE, Material.IRON_SPADE, Material.STONE_SPADE, Material.WOOD_SPADE});

        public final String name;
        public final Material[] types;
        public final ChatColor color;
        public final int index;
        public final Material mat;
        public final int glassColor;
        public String skull = null;

        Category(int index, String name, Material mat, ChatColor color, int glassColor, Material[] itemTypes) {
            this.name = name;
            this.types = itemTypes;
            this.color = color;
            this.index = index;
            this.mat = mat;
            this.glassColor = glassColor;
        }

        Category(int index, String name, Material mat, String skull, ChatColor color, int glassColor, Material[] itemTypes) {
            this.name = name;
            this.types = itemTypes;
            this.color = color;
            this.index = index;
            this.mat = mat;
            this.glassColor = glassColor;
            this.skull = skull;
        }

        public String getName() {
            return this.name;
        }

        public Material[] getTypes() {
            return this.types;
        }

        public ChatColor getColor() {
            return this.color;
        }

        public int getIndex() {
            return this.index;
        }

        public Material getMat() {
            return this.mat;
        }

        public int getGlassColor() {
            return this.glassColor;
        }

        public String getSkull() {
            return this.skull;
        }
    }
}
