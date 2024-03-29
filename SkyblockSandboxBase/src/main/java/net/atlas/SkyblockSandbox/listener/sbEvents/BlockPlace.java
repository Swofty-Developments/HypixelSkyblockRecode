package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.command.commands.Command_island;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import static net.atlas.SkyblockSandbox.playerIsland.PlayerIslandHandler.dist;

public class BlockPlace extends SkyblockListener<BlockPlaceEvent> {
    @EventHandler
    public void callEvent(BlockPlaceEvent event) {
        SBPlayer pl = new SBPlayer(event.getPlayer());

        if (Command_island.getVisiting(pl) != null) {
            event.setCancelled(true);

            return;
        }

        if(pl.hasIsland()) {
            if (pl.getWorld() == pl.getPlayerIsland().getCenter().getWorld()) {
                if(pl.getLocation().distance(pl.getPlayerIsland().getCenter()) > dist()) {
                    event.setCancelled(true);
                    pl.sendMessage(SUtil.colorize("&cYou cannot place blocks more than " + dist() + " blocks in that direction!"));
                } else {
                    event.setCancelled(event.getBlock().getType().equals(Material.BEDROCK));
                }
                return;
            }
        }
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
