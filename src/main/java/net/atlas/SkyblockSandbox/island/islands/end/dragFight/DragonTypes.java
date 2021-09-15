package net.atlas.SkyblockSandbox.island.islands.end.dragFight;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses.*;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

public enum DragonTypes {
    PROTECTOR("protector", "Protector Dragon", 63, ProtectorDragon.class),
    OLD("old", "Old Dragon", 63, OldDragon.class),
    WISE("wise", "Wise Dragon", 63, WiseDragon.class),
    UNSTABLE("unstable", "Unstable Dragon", 63, UnstableDragon.class),
    YOUNG("young", "Young Dragon", 63, YoungDragon.class),
    STRONG("strong", "Strong Dragon", 63, StrongDragon.class),
    SUPERIOR("superior", "Superior Dragon", 63, SuperiorDragon.class),
    VOIDGLOOM("voidgloom", "Voidgloom Dragon", 63, VoidgloomDragon.class);

    public String mobName;
    public String prefix;
    public Class<? extends Entity> entity;

    private DragonTypes(String prefix, String name, int id, Class<? extends Entity> custom) {
        addToMaps(custom, name, id);
        this.mobName = name;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }


    public static Entity spawnEntity(Location loc, Entity entity, DragonTypes type) {
        String mobName = type.getMobName();
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        entity.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + mobName);
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
        return entity;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addToMaps(Class clazz, String name, int id) {
        ((Map) getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map) getPrivateField("e", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(id, clazz);
        ((Map) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, id);
        ((Map) getPrivateField("g", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, id);
    }

    public static Object getPrivateField(String fieldName, @SuppressWarnings("rawtypes") Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    public String getMobName() {
        return mobName;
    }
}

