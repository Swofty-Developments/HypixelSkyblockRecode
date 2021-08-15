package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.SummonListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.SummoningAltaar;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;

public class PlayerInteractEvent extends SkyblockListener<org.bukkit.event.player.PlayerInteractEvent> {
    @EventHandler
    public void callEvent(org.bukkit.event.player.PlayerInteractEvent event) {

        Player p = event.getPlayer();
        if (p.getItemInHand() != null && p.getItemInHand().hasItemMeta()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                SBItemStack item = new SBItemStack(event.getItem());
                if (item.getString(event.getItem(), "ID") != null) {
                    if (item.getString(event.getItem(), "ID").equals(SkyblockItem.Default.SUMMONING_EYE.item().getItemID())) {
                        for (SummoningAltaar value : SummoningAltaar.values()) {
                            if (value.getLoc().equals(event.getClickedBlock().getLocation())) {
                                SummonListener.placeEye(value, event.getPlayer());
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
