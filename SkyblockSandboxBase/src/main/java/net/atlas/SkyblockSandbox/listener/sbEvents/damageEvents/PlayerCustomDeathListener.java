package net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.PlayerCustomDeathEvent;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerCustomDeathListener extends SkyblockListener<PlayerCustomDeathEvent> {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void callEvent(PlayerCustomDeathEvent event) {
        event.setCancelled(true);
        SBPlayer p = event.getSBPlayer();
        p.sendMessage("§c☠ §7You died!");
        String deathMsg = "";
        switch (event.getDeathCause()) {
            case FALL:
                deathMsg = "fell to their death.";
                break;
            case FIRE:
            case LAVA:
                deathMsg = "burnt to death.";
                break;
            case VOID:
                deathMsg = "fell into the void.";
                break;
            case MAGIC:
                deathMsg = "magically died.";
                break;
            case SUICIDE:
                deathMsg = "commited sudoku.";
                break;
            case ENTITY_ATTACK:
                deathMsg = "was killed by a mob.";
                break;
            case ENTITY_EXPLOSION:
            case BLOCK_EXPLOSION:
                deathMsg = "blew up.";
                break;
            default:
                deathMsg = "died.";
                break;
        }
        for(Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(SUtil.colorize("&c☠ &7" + p.getName() + " " + deathMsg));
        }
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
