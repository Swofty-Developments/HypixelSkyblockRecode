package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.entity.pathfinderGoal.PathfinderGoalSummonOwnerHurtTarget;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.minecraft.server.v1_8_R3.PathfinderGoalOwnerHurtTarget;
import org.bukkit.event.EventHandler;

public class EntityDamageEvent extends SkyblockListener<org.bukkit.event.entity.EntityDamageEvent> {
    @EventHandler
    public void callEvent(org.bukkit.event.entity.EntityDamageEvent event) {
        if(event.getEntity().hasMetadata("entity-tag")) {
            event.setCancelled(true);
        }
    }
}
