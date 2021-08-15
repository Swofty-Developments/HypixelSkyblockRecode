package net.atlas.SkyblockSandbox.island.islands.end.dragFight;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class DragHandler implements Listener {


    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if(e.getEntity() instanceof EnderDragon) {
            if(e.getTarget().getCustomName()!=null) {
                if (e.getTarget().getCustomName().equals("follower")) {
                    e.setTarget(e.getTarget());
                } else {
                    e.setCancelled(true);
                }
                e.setTarget(StartFight.as);
            } else {
                e.setTarget(StartFight.as);
                e.setCancelled(true);
            }
        }
    }
}
