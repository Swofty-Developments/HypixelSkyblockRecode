package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.PortalType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCreatePortalEvent;

public class EntityPortalEvent extends SkyblockListener<EntityCreatePortalEvent> {
    @EventHandler
    public void callEvent(EntityCreatePortalEvent event) {
        if(event.getPortalType().equals(PortalType.ENDER)) {
            event.setCancelled(true);
        }
    }
}
