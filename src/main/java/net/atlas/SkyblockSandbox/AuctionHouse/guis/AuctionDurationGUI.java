package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AuctionDurationGUI extends NormalGUI {
    public AuctionDurationGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public void setItems() {

    }
}
