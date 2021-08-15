package net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.PlayerCustomDeathEvent;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerCustomDeathListener extends SkyblockListener<PlayerCustomDeathEvent> {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void callEvent(PlayerCustomDeathEvent event) {
        event.setCancelled(true);
        SBPlayer p = event.getSBPlayer();
        p.sendMessage("§c☠ §7You died!");
        p.setHealth(p.getMaxHealth());
        p.setStat(SBPlayer.PlayerStat.HEALTH, p.getMaxStat(SBPlayer.PlayerStat.HEALTH));

        SBX plugin = SBX.getInstance();

        Location loc = p.getWorld().getSpawnLocation();
        p.teleport(loc);
        p.setHealth(p.getMaxHealth());
        p.setWalkSpeed(0.2f);

        p.teleport(loc);
        p.playSound(p.getLocation(), Sound.ZOMBIE_METAL,2,2);
    }
}
