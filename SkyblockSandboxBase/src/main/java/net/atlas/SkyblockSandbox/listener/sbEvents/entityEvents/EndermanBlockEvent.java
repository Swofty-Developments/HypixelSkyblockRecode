package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EndermanBlockEvent extends SkyblockListener<EntityChangeBlockEvent> {
    @EventHandler
    public void callEvent(EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof Enderman) {
            event.setCancelled(true);
        }
        if(event.getEntity() instanceof Wither) {
            event.setCancelled(true);
        }
        if(event.getEntity() instanceof WitherSkull) {
            event.setCancelled(true);
        }
    }
}
