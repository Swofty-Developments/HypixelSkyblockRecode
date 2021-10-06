package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionBidHandler;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class AuctionYourBidsGUI extends NormalGUI implements Backable {
    public static HashMap<UUID, ArrayList<AuctionItemHandler>> items = new HashMap<>();
    public AuctionYourBidsGUI(SBPlayer owner) {
        super(owner);
        items.put(owner.getUniqueId(), owner.getItemsBided());
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!NBTUtil.getString(event.getCurrentItem(), "Auction-id").equals("")) {
            String auctionID = NBTUtil.getString(event.getCurrentItem(), "Auction-id");
            (new AuctionInspectorGUI(getOwner(), AuctionItemHandler.ITEMS.get(UUID.fromString(auctionID)))).open();
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Your bids";
    }

    @Override
    public int getRows() {
        return Math.min(((int) (items.get(getOwner().getUniqueId()).size() / 9)) + 3, 6);
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(FILLER_GLASS).asGuiItem());
        items.get(getOwner().getUniqueId()).forEach(item -> {
            getGui().addItem(ItemBuilder.from(item.createInspectorItem(getOwner())).asGuiItem());
        });
    }

    @Override
    public void openBack() {
        new AuctionHouseGUI(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Auction house";
    }

    @Override
    public int backItemSlot() {
        return (Math.min(((int) (items.get(getOwner().getUniqueId()).size() / 9)) + 3, 6) * 9)-5;
    }
}
