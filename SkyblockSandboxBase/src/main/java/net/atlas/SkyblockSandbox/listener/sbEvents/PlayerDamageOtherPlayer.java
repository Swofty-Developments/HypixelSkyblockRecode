package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageOtherPlayer extends SkyblockListener<EntityDamageByEntityEvent> {
    @Override
    public void callEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (!SBX.pvpEnabled)
                event.setCancelled(true);
        }
    }
}
