package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.AuctionHouse.guis.AuctionCreatorGUI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave extends SkyblockListener<PlayerQuitEvent> {

    @EventHandler
    public void callEvent(PlayerQuitEvent event) {
        if (AuctionCreatorGUI.PlayerItems.containsKey(event.getPlayer().getUniqueId())) {
            MongoCoins playerData = SBX.getMongoStats();
            playerData.setData(event.getPlayer().getUniqueId(), "AuctionItem", AuctionCreatorGUI.PlayerItems.get(event.getPlayer().getUniqueId()));
        }
    }
}
