package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import static net.atlas.SkyblockSandbox.playerIsland.PlayerIslandHandler.dist;

public class BlockBreak extends SkyblockListener<BlockBreakEvent> {
    @EventHandler
    public void callEvent(BlockBreakEvent event) {
        SBPlayer pl = new SBPlayer(event.getPlayer());
        if(pl.getItemInHand().getType().name().contains("SWORD")) {
            event.setCancelled(true);
            return;
        }
        if(pl.hasIsland()) {
            if (pl.getWorld() == pl.getPlayerIsland().getCenter().getWorld()) {
                if(pl.getLocation().distance(pl.getPlayerIsland().getCenter()) > dist()) {
                    event.setCancelled(true);
                } else {
                    event.setCancelled(event.getBlock().getType().equals(Material.BEDROCK));
                }
                return;
            }
        }
        event.setCancelled(true);
    }
}
