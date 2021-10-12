package net.atlas.SkyblockSandbox.island.islands.end.dragFight;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses.*;
import net.minecraft.server.v1_8_R3.Entity;
import net.royawesome.jlibnoise.module.modifier.Abs;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

public enum DragonTypes {
    PROTECTOR("protector", "Protector Dragon",9000000,0.75),
    OLD("old", "Old Dragon",15000000,0.6),
    WISE("wise", "Wise Dragon",9000000,0.75),
    UNSTABLE("unstable", "Unstable Dragon",9000000,0.75),
    YOUNG("young", "Young Dragon",7500000,1.3),
    STRONG("strong", "Strong Dragon",9000000,0.75),
    SUPERIOR("superior", "Superior Dragon", 12000000,1),
    VOIDGLOOM("voidgloom", "Voidgloom Dragon", 75000000,0.6);

    public String mobName;
    public String prefix;
    private DragonBuilder builder;

    private DragonTypes(String prefix, String name,double health,double moveSpeed) {
        addToMaps(CustomEnderDragon.class, name, 63);
        builder = DragonBuilder.init().health(health).name(name).moveSpeed(moveSpeed).dragonType(this);
        this.mobName = name;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }


    public Entity spawnEntity(Location loc) {
        return builder.build(loc);
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

