package net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.PlayerCustomDeathEvent;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.*;

public class PlayerDamageListener extends SkyblockListener<EntityDamageEvent> {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void callEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            SBPlayer player = new SBPlayer((Player) event.getEntity());
            switch (event.getCause()) {
                case PROJECTILE:
                    event.setCancelled(true);
                    player.damage(0.1);
                    player.setCause(event.getCause());
                    event.setCancelled(true);
                case FALL:
                    player.setStat(HEALTH, player.getStat(HEALTH) - event.getDamage());
                    player.damage(0.1);
                    player.setCause(event.getCause());
                    event.setCancelled(true);
                case FIRE_TICK:
                case LAVA:
                case SUFFOCATION:
                case DROWNING:
                case FIRE:
                    player.setStat(HEALTH, player.getStat(HEALTH) - 5);
                    player.damage(0.1);
                    player.setCause(event.getCause());
                    event.setCancelled(true);
            }

        }
    }
}
