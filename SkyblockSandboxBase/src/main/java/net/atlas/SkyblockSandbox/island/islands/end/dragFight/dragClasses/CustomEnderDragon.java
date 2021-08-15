package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.awt.*;
import java.lang.reflect.Field;

public class CustomEnderDragon extends EntityEnderDragon {

    public CustomEnderDragon(World world) {
        super(world);

        try {
            List goalB = (List)getPrivateField("b", goalSelector); goalB.clear();
            List goalC = (List)getPrivateField("c",  goalSelector); goalC.clear();
            List targetB = (List)getPrivateField("b", targetSelector); targetB.clear();
            List targetC = (List)getPrivateField("c", targetSelector); targetC.clear();
        } catch (Exception ignored) {

        }

    }

    public static CustomEnderDragon spawn(Location loc, String name) {
        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final CustomEnderDragon enderDragon = new CustomEnderDragon(mcWorld);

        enderDragon.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) enderDragon.getBukkitEntity()).setRemoveWhenFarAway(true);
        mcWorld.addEntity(enderDragon, CreatureSpawnEvent.SpawnReason.CUSTOM);
        enderDragon.setCustomName(name);
        enderDragon.setCustomNameVisible(false);
        return enderDragon;
    }

    private static Object getPrivateField(String fieldName, Object object) {
        Field field;
        Object o = null;

        try {
            field = PathfinderGoalSelector.class.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }





}
