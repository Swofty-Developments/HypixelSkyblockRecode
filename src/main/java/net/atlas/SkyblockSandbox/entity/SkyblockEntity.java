package net.atlas.SkyblockSandbox.entity;

import net.atlas.SkyblockSandbox.entity.customEntity.FissureZombie;
import net.atlas.SkyblockSandbox.entity.customEntity.NoTeleportEnderman;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum SkyblockEntity {

    //todo enderman is 58 change back after test
    NO_TP_ENDERMAN("Enderman", 58, EntityType.ENDERMAN, NoTeleportEnderman.class),
    ZOMBIE("Zombie", 54, EntityType.ZOMBIE, FissureZombie.class),
    WOLF("Wolf", 95, EntityType.WOLF, EntityWolf.class),
    OLD_WOLF("Old Wolf", 95, EntityType.WOLF, EntityWolf.class),
    WEAVER_SPIDER("Weaver Spider", 52, EntityType.SPIDER, EntitySpider.class),
    DASHER_SPIDER("Dasher Spider", 52, EntityType.SPIDER, EntitySpider.class),
    CRYPT_GHOUL("Crypt Ghoul", 54, EntityType.ZOMBIE, EntityZombie.class);


    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> customClass;

    private SkyblockEntity(String name, int id, EntityType entityType, Class<? extends EntityInsentient> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.customClass = customClass;
        //addToMaps(customClass, name, id);
        registerEntity(name,id,customClass,customClass);
    }

    /**
     * Register our entities.
     */
    public static void registerEntities() {
        for (SkyblockEntity entity : values())
            a(entity.getCustomClass(), entity.getName(), entity.getID());
    }

    /**
     * Unregister our entities to prevent memory leaks. Call on disable.
     */
    public static void unregisterEntities() {
        for (SkyblockEntity entity : values()) {
// Remove our class references.
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (SkyblockEntity entity : values())
            try {
// Unregister each entity by writing the NMS back in place of the custom class.
                a(entity.getCustomClass(), entity.getName(), entity.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    /**
     * A convenience method.
     *
     * @param clazz The class.
     * @param f     The string representation of the private static field.
     * @return The object found
     * @throws Exception if unable to get the object.
     */
    private static Object getPrivateStatic(Class<?> clazz, String f) throws Exception {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    /*
     * Since 1.7.2 added a check in their entity registration, simply bypass it and write to the maps ourself.
     */
    private static void a(Class<?> paramClass, String paramString, int paramInt) {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
            ((Map) getPrivateStatic(EntityTypes.class, "e")).put(paramInt, paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, paramInt);
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, paramInt);
        } catch (Exception exc) {
// Unable to register the new class.
        }
    }


    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Class<? extends EntityInsentient> getCustomClass() {
        return this.customClass;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addToMaps(Class clazz, String name, int id) {
        //getPrivateField is the method from above.
        //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
        ((Map) getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        //((Map)getPrivateField("e", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, id);
        //((Map)getPrivateField("g", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }

    public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
        try {

            List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getPrivateField(String fieldName, Class<? extends EntityTypes> clazz, Object object) {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }
}
