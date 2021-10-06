package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;
import java.util.UUID;

public class AuctionHouseGUI extends NormalGUI {
    public AuctionHouseGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> {
            getOwner().closeInventory();
        });
        if (hasAuction(getOwner())) {
            setAction(15, event -> {
                new YourAuctionsGUI(getOwner()).open();
            });
        } else {
            setAction(15, event -> {
                new AuctionCreatorGUI(getOwner(), false, 500, 6).open();
            });
        }
        setAction(11, event -> {
            new AuctionBrowserGUI(getOwner()).open();
        });
        setAction(13, event -> {
            if(!getOwner().getItemsBided().isEmpty()) {
                new AuctionYourBidsGUI(getOwner()).open();
            }
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Auction House";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.BARRIER, "&cClose", 1, 0));
        if (hasAuction(getOwner())) {
            setItem(15, makeColorfulItem(Material.GOLD_BARDING, "&6Manage Auctions", 1, 0));
        } else {
            setItem(15, makeColorfulItem(Material.GOLD_BARDING, "&6Make auction", 1,0));
        }
        if (!getOwner().getItemsBided().isEmpty()) {
            setItem(13, makeColorfulItem(Material.GOLDEN_CARROT, "&6Your bids", 1, 0));
        }
        setItem(11, makeColorfulItem(Material.GOLD_BLOCK, "&aBrowse AH", 1, 0));

    }

    public static boolean hasAuction(SBPlayer player) {
        for (Map.Entry<UUID, AuctionItemHandler> entry : AuctionItemHandler.ITEMS.entrySet()) {
            AuctionItemHandler item = entry.getValue();
            if (item.getOwner().toString().equals(player.getUniqueId().toString())) {
               return true;
            }
        }
        return false;
    }
}
