package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler.Category;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AuctionBrowserGUI extends PaginatedGUI implements Backable {
    public static HashMap<UUID, Category> openCategory = new HashMap<>();

    public AuctionBrowserGUI(SBPlayer owner) {
        super(owner);
        openCategory.putIfAbsent(owner.getUniqueId(), Category.WEAPONS);
    }

    public int getPageSize() {
        return 24;
    }

    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getClickedInventory().equals(getOwner().getInventory())) return;
        for(Category cat : Category.values()) {
            if (event.getSlot() == cat.index * 9) {
                openCategory.put(getOwner().getUniqueId(), cat);
                (new AuctionBrowserGUI(getOwner())).open();
            }
        }

        if (!NBTUtil.getString(event.getCurrentItem(), "Auction-id").equals("")) {
            String auctionID = NBTUtil.getString(event.getCurrentItem(), "Auction-id");
            (new AuctionInspectorGUI(getOwner(), (AuctionItemHandler)AuctionItemHandler.ITEMS.get(UUID.fromString(auctionID)))).open();
        }

    }

    public boolean setClickActions() {
        setAction(46, (e) -> {
            if (getGui().getPrevPageNum() == 1 && getGui().getCurrentPageNum() == 1) {
                getOwner().sendMessage("&cYou are on the last page");
            } else {
                getGui().previous();
            }

        });
        setAction(53, (e) -> {
            if (getGui().getNextPageNum() == getGui().getPagesNum() && getGui().getCurrentPageNum() == getGui().getPagesNum()) {
                getOwner().sendMessage("&cYou are on the last page");
            } else {
                getGui().next();
            }

        });
        return false;
    }

    public String getTitle() {
        return "Auction browser";
    }

    public int getRows() {
        return 6;
    }

    public void setItems() {
        getGui().getFiller().fillBorder(((ItemBuilder)ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)((byte)((Category)openCategory.get(getOwner().getUniqueId())).glassColor))).name(Component.text(SUtil.colorize("&7 ")))).asGuiItem());
        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        Iterator var3 = getItems().iterator();

        while(var3.hasNext()) {
            ItemStack item = (ItemStack)var3.next();
            getGui().addItem(ItemBuilder.from(item).asGuiItem());
        }

        Category[] var7 = Category.values();
        int var8 = var7.length;

        for(int var5 = 0; var5 < var8; ++var5) {
            Category cat = var7[var5];
            if (cat.getSkull() == null) {
                setItem(cat.index * 9, makeColorfulItem(cat.getMat(), cat.color + cat.getName(), 1, 0, new String[0]));
            } else {
                setItem(cat.index * 9, makeColorfulCustomSkullItem(cat.getSkull(), cat.color + cat.getName(), 1));
            }

            setItem(cat.index * 9 + 1, ((ItemBuilder)ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)((byte)((Category)openCategory.get(getOwner().getUniqueId())).glassColor))).name(Component.text(SUtil.colorize("&7 ")))).build());
        }

        setItem(53, next);
        setItem(46, prev);
    }

    public void openBack() {
        new AuctionHouseGUI(getOwner()).open();
    }

    public String backTitle() {
        return "Auction House";
    }

    public int backItemSlot() {
        return 49;
    }

    public List<ItemStack> getItems() {
        List<ItemStack> list = new ArrayList();
        Iterator var2 = AuctionItemHandler.ITEMS.values().iterator();

        while(var2.hasNext()) {
            AuctionItemHandler item = (AuctionItemHandler)var2.next();
            if (openCategory.get(getOwner().getUniqueId()) == item.getCategory()) {
                list.add(item.createItem());
            }
        }

        return list;
    }
}