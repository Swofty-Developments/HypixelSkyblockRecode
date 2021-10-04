package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Random;

import static net.atlas.SkyblockSandbox.command.commands.Command_spawnmob.setAI;

public class EntitySpawnEvent extends SkyblockListener<org.bukkit.event.entity.EntitySpawnEvent> {

    public static HashMap<Entity, ArmorStand> holoMap = new HashMap<>();
    public static HashMap<Integer, HashMap<Entity,EntityArmorStand>> holoMap2 = new HashMap<>();
    public static HashMap<Integer,Enderman> worldEnderman = new HashMap<>();

    @EventHandler
    public void callEvent(org.bukkit.event.entity.EntitySpawnEvent event) {
        if(!(event.getEntity() instanceof EnderDragon)) {
            if (!(event.getEntity() instanceof Bat) && !(event.getEntity() instanceof Slime)) {
                //if(event.getEntity().getMetadata("summon").isEmpty()) {
                    if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof ArmorStand)) {
                        /*EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld)event.getEntity().getWorld()).getHandle(),event.getEntity().getLocation().getX(),event.getEntity().getLocation().getY()+1,event.getEntity().getLocation().getZ());
                        entityArmorStand.setCustomName("test");
                        entityArmorStand.setCustomNameVisible(true);
                        entityArmorStand.setGravity(false);
                        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
                        for(Player p: Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                        }*/
                        ArmorStand as = (ArmorStand) event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation().add(0D, 1D, 0D), EntityType.ARMOR_STAND);
                        Slime empty = (Slime) event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation().add(0D, 0D, 0D), EntityType.SLIME);
                        empty.setMetadata("entity-tag", new FixedMetadataValue(SBX.getInstance(), event.getEntity().getUniqueId()));

                        empty.setCustomName(SUtil.colorize("&70"));
                        empty.setSize(1);
                        empty.setMaxHealth(Integer.MAX_VALUE);

                        setAI(empty, false);
                        empty.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10, false, false));
                        empty.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 10, false, false));
                        as.setGravity(false);
                        as.setVisible(false);
                        as.setCustomNameVisible(true);
                        as.setRemoveWhenFarAway(false);
                        as.setMetadata("entity-tag", new FixedMetadataValue(SBX.getInstance(), event.getEntity().getUniqueId()));
                        as.setMetadata("Invulnerable", new FixedMetadataValue(SBX.getInstance(), 1));
                        as.setMarker(true);
                        as.setSmall(true);
                        StringBuilder builder = new StringBuilder();
                        Random random = new Random();
                        int rand = random.nextInt(98) + 1;
                        int lvl = rand;
                        LivingEntity en = (LivingEntity) event.getEntity();
                        builder.append(SUtil.colorize("&8[&7Lvl" + lvl + "&8]"));
                        builder.append(SUtil.colorize(" &c" + event.getEntity().getName()));
                        builder.append(SUtil.colorize(" &a" + en.getHealth() + "&7/&c" + en.getMaxHealth()));
                        as.setCustomName(builder.toString());
                        holoMap.put(event.getEntity(), as);
                        HashMap<Entity,EntityArmorStand> placeHolder = new HashMap<>();
                        //placeHolder.put(event.getEntity(),entityArmorStand);
                        //holoMap2.put(event.getEntity().getEntityId(),placeHolder);
                        empty.setPassenger(as);
                        en.setPassenger(empty);
                        if(event.getEntity() instanceof Enderman) {
                            worldEnderman.put(event.getEntity().getEntityId(),(Enderman) event.getEntity());
                        }
                    }
                //}
            }
        }
    }
}
