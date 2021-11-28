package net.atlas.SkyblockSandbox.island.islands.bossRush;

import dev.triumphteam.gui.components.GuiAction;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DungeonBoss {

    //Actual Entity params
    private EntityType bossEn;
    private double health;
    private String name;

    private Entity spawnedEn;

    private final BossHandler handler = new BossHandler(this);

    //boss phase
    //set to -1 for starters because phase 1 is represented as 0
    private int phaseAmt;
    private int currentPhase = -1;

    //need instance of entity to add AI goals
    private boolean isSpawned;

    public Entity spawn(Location bossLoc, boolean useAI) {
        //spawning entity
        spawnedEn = bossLoc.getWorld().spawnEntity(bossLoc, bossEn);
        spawnedEn.setCustomName(SUtil.colorize(name));

        //if no AI, disabling it completely (Boss Pedestals);
        setAI(spawnedEn, false);

        //you can add AI now
        isSpawned = true;
        if (useAI) {
            addAIGoal(() -> {

            });
        }
        return spawnedEn;
    }

    public int getPhase() {
        return currentPhase;
    }

    public boolean setPhase(int _phase) {
        currentPhase = _phase;
        return true;
    }

    public boolean incrementPhase() {
        if (currentPhase >= phaseAmt) {
            return false;
        }
        currentPhase++;
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setEntityType(EntityType bossEn) {
        this.bossEn = bossEn;
    }

    public Entity getSpawnedEn() {
        if (!isSpawned) {
            throw new NullPointerException("You cannot add AI to a non-spawned boss!");
        }
        return spawnedEn;
    }

    public void addAIGoal(Runnable rb) {
        if (!isSpawned) {
            throw new NullPointerException("You cannot add AI to a non-spawned boss!");
        }
        Consumer<SBPlayer> follow = handler.addAIAction((player) -> {
            Location playerLoc = player.getLocation();
            EntityCreature cr = ((EntityCreature) ((CraftEntity) spawnedEn).getHandle());
            cr.getNavigation().a(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());
            spawnedEn.teleport(playerLoc);
            System.out.println("Following");
        });
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Entity en : spawnedEn.getWorld().getNearbyEntities(spawnedEn.getLocation(), 40, 40, 40)) {
                    if (en instanceof Player) {
                        SBPlayer player = new SBPlayer((Player) en);
                        follow.accept(player);
                    }
                }

            }
        }.runTaskTimer(SBX.getInstance(), 0L, 1L);
    }

    public void setAI(Entity en, boolean ai) {
        EntityLiving handle = ((CraftLivingEntity) en).getHandle();
        handle.getDataWatcher().watch(15, (byte) (ai ? 0 : 1));
    }

    public String getName() {
        return name;
    }

    public static void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
        try {

            List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()){
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)){
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
}
