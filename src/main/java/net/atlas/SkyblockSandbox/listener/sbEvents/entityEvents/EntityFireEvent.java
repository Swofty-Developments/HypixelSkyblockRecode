package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityFireEvent extends SkyblockListener<EntityCombustEvent> {
    @EventHandler
    public void callEvent(EntityCombustEvent event) {
        if(event.getEntity() instanceof Player) {
            return;
        }
        event.setCancelled(true);
    }
}
