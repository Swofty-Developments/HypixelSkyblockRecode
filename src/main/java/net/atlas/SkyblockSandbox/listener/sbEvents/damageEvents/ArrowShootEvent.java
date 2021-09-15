package net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ArrowShootEvent extends SkyblockListener<ProjectileLaunchEvent> {
    @EventHandler
    public void callEvent(ProjectileLaunchEvent event) {
        if(event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            SBPlayer p;
            if(event.getEntity().getShooter() instanceof Player) {
                p = new SBPlayer((Player) event.getEntity().getShooter());

            } else {
                p = new SBPlayer(Bukkit.getPlayer(UUID.randomUUID()));
            }
            startCheck(arrow,p);
        }
    }

    public void startCheck(Arrow arrow, SBPlayer p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(arrow.isOnGround()) {
                    arrow.remove();
                    this.cancel();
                }

            }
        }.runTaskTimer(SBX.getInstance(),0L,1L);
    }
}
