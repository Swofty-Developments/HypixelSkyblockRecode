package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionBidHandler;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class YourAuctionsGUI extends NormalGUI implements Backable {
    public static HashMap<UUID, ArrayList<AuctionItemHandler>> items = new HashMap<>();
    public YourAuctionsGUI(SBPlayer owner) {
        super(owner);
        ArrayList<AuctionItemHandler> list =  new ArrayList<>();
        for (AuctionItemHandler item : AuctionItemHandler.ITEMS.values()) {
            if (item.getOwner().toString().equals(owner.getUniqueId().toString())) {
                list.add(item);
            }
        }

        items.put(owner.getUniqueId(), list);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!NBTUtil.getString(event.getCurrentItem(), "Auction-id").equals("")) {
            String auctionID = NBTUtil.getString(event.getCurrentItem(), "Auction-id");
            (new AuctionInspectorGUI(getOwner(), AuctionItemHandler.ITEMS.get(UUID.fromString(auctionID)))).open();
        }
        if((Math.min(((int) ((items.get(getOwner().getUniqueId()).size() +1) / 9)) + 3, 6) * 9)-3 == event.getSlot()) {
            new AuctionCreatorGUI(getOwner(), false, 500, 6).open();
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Your auctions";
    }

    @Override
    public int getRows() {
        return (Math.min(((int) ((items.get(getOwner().getUniqueId()).size() +1) / 9)) + 3, 6));
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(FILLER_GLASS).asGuiItem());
        items.get(getOwner().getUniqueId()).forEach(item -> {
            getGui().addItem(ItemBuilder.from(item.createInspectorItem(getOwner())).asGuiItem());
        });
        setItem((Math.min(((int) ((items.get(getOwner().getUniqueId()).size() +1) / 9)) + 3, 6) * 9)-3, makeColorfulItem(Material.GOLD_BARDING, "&6Create auction", 1, 0));
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
        return (Math.min(((int) ((items.get(getOwner().getUniqueId()).size() +1) / 9)) + 3, 6) * 9)-5;
    }
}