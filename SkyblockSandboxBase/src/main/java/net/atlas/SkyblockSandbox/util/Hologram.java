package net.atlas.SkyblockSandbox.util;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private Location holoLoc;
    private List<EntityArmorStand> stands = new ArrayList();
    private boolean isDisplayed;

    public Hologram(Location holoLoc) {
        this.holoLoc = holoLoc;
    }

    public void addLine(String line) {
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) holoLoc.getWorld()).getHandle(), holoLoc.getX(), holoLoc.getY() + (0.25 * stands.size()), holoLoc.getZ());
        stand.setCustomNameVisible(true);
        stand.setCustomName(SUtil.colorize(line));
        stand.setGravity(false);
        ((ArmorStand) stand.getBukkitEntity()).setVisible(false);
        stands.add(stand);
    }

    public void displayHolo(Player p) {
        if (!isDisplayed) {
            for (EntityArmorStand stand : stands) {
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
            isDisplayed = true;
        }
    }

    public void updateHolo(Player p) {
        if(isDisplayed) {
            removeHolo(p);
            for (EntityArmorStand stand : stands) {
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }

        }
    }


    public void removeHolo(Player p) {
        for (EntityArmorStand stand : stands) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
        isDisplayed = false;
    }
}
