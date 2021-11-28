package net.atlas.SkyblockSandbox.island.islands.bossRush.components;

import net.atlas.SkyblockSandbox.island.islands.bossRush.DungeonBoss;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;

import static net.atlas.SkyblockSandbox.command.commands.Command_spawnmob.setAI;

public class NecronBoss extends EntityWither {
    public NecronBoss(World world, boolean useAI) {
        super(world);

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(useAI) {
            this.goalSelector.a(0, new PathfinderGoalFloat(this));
            this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
            this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
            this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
            this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
            //this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
            this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
            this.b_ = 50;

            this.setHealth(50000f);
            this.setCustomName(SUtil.colorize("Necron"));
            this.setCustomNameVisible(true);

        } else {
            setAI(this,useAI);
        }

    }

    public void setAI(EntityLiving en, boolean ai) {
        en.getDataWatcher().watch(15, (byte) (ai ? 0 : 1));
    }

    public static Wither spawn(Location loc,boolean useAI) {
        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final NecronBoss customEnt = new NecronBoss(mcWorld,useAI);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false); //Do we want to remove it when the NPC is far away? I wont
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Wither) customEnt.getBukkitEntity();
    }


}
