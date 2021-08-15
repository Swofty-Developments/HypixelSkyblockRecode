package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.event.EventHandler;

public class EntityExplodeEvent extends SkyblockListener<org.bukkit.event.entity.EntityExplodeEvent> {
    @EventHandler
    public void callEvent(org.bukkit.event.entity.EntityExplodeEvent event) {
        event.setCancelled(true);
    }
}
