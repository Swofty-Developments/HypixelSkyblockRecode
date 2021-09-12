package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

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
        setAction(15, event -> {
            new AuctionCreatorGUI(getOwner(), false, 500, 6).open();
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
        setItem(15, makeColorfulItem(Material.GOLD_BARDING, "&6Your Auctions", 1, 0));
        setItem(13, makeColorfulItem(Material.GOLDEN_CARROT, "&6Your bids", 1, 0));
        setItem(11, makeColorfulItem(Material.GOLD_BLOCK, "&aBrowse AH", 1, 0));

    }
}
