package net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileHit extends SkyblockListener<ProjectileHitEvent> {
    @EventHandler
    public void callEvent(ProjectileHitEvent event) {
        for(Entity en:event.getEntity().getNearbyEntities(1,1,1)) {
            if(en instanceof Enderman) {
                Location pos = en.getLocation();
                new BukkitRunnable() {
                    int i=0;
                    @Override
                    public void run() {
                        if(i>=5) {
                            this.cancel();
                        }
                        ((CraftEntity)en).getHandle().setPosition(pos.getX(),pos.getY(),pos.getZ());
                        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(((CraftEntity)en).getHandle());
                        for(Player p: Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                        }
                        i++;
                    }
                }.runTaskTimer(SBX.getInstance(),0L,0L);
            }
        }
    }
}
