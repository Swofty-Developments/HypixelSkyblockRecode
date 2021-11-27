package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.command.commands.Command_island;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import static net.atlas.SkyblockSandbox.playerIsland.PlayerIslandHandler.dist;

public class PlaceBucket extends SkyblockListener<PlayerBucketEmptyEvent> {
    @Override
    public void callEvent(PlayerBucketEmptyEvent event) {
        SBPlayer pl = new SBPlayer(event.getPlayer());
        pl.sendMessage("Placed!");

        if (Command_island.getVisiting(pl) != null) {
            event.setCancelled(true);

            return;
        }

        if(pl.hasIsland()) {
            if (pl.getWorld() == pl.getPlayerIsland().getCenter().getWorld()) {
                if(pl.getLocation().distance(pl.getPlayerIsland().getCenter()) > dist()) {
                    event.setCancelled(true);
                    pl.sendMessage(SUtil.colorize("&cYou cannot place blocks more than " + dist() + " blocks in that direction!"));
                }
                return;
            }
        }

        event.setCancelled(true);
    }

    public static class FillBucket extends SkyblockListener<PlayerBucketFillEvent> {
        @Override
        public void callEvent(PlayerBucketFillEvent event) {
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
                    }
                    return;
                }
            }

            event.setCancelled(true);
        }
    }
}
