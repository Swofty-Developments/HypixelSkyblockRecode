package net.atlas.SkyblockSandbox.entity.customEntity;

import com.google.common.base.Predicate;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.entity.pathfinderGoal.PathfinderGoalFastMeleeAttack;
import net.atlas.SkyblockSandbox.entity.pathfinderGoal.PathfinderGoalSummonOwnerHurtTarget;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class FissureZombie extends EntityZombie {

    public FissureZombie(World world,EntityLiving owner) {
        super(world);
        List goalB = (List)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
        ((Navigation)this.getNavigation()).a(true);
        this.goalSelector.a(1,new PathfinderGoalFloat(this));
        this.targetSelector.a(2,new PathfinderGoalSummonOwnerHurtTarget(this,owner));
        //this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new PathfinderGoalFastMeleeAttack(this, 1.0D, true));
        //this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
        //this.targetSelector.a(1, new PathfinderGoalSummonOwnerHurtTarget(this,owner));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
        //this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget(this, EntitySkeleton.class, false));

    }

    @Override
    public void m() {
        if (this.world.w() && !this.world.isClientSide && !this.isBaby()) {
            float f = this.c(1.0F);
            BlockPosition blockposition = new BlockPosition(this.locX, (double) Math.round(this.locY), this.locZ);
            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.i(blockposition)) {
                boolean flag = true;
            }
        }

        if (this.au() && this.getGoalTarget() != null && this.vehicle instanceof EntityChicken) {
            ((EntityInsentient) this.vehicle).getNavigation().a(this.getNavigation().j(), 1.5D);
        }

        super.m();
    }

    public static Zombie spawn(Location loc, double health,SBPlayer owner) {
        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final FissureZombie customEnt = new FissureZombie(mcWorld, ((CraftPlayer)owner.getPlayer()).getHandle());
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        Zombie zombie = (Zombie) customEnt.getBukkitEntity();
        zombie.setMaxHealth(health);
        zombie.setHealth(health);
        zombie.setCustomName(SUtil.colorize("&rFissure Zombie"));
        zombie.setCustomNameVisible(false);
        return zombie;
    }

    public static Zombie spawnSummon(Location loc, double health, SBPlayer p) {

        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final FissureZombie customEnt = new FissureZombie(mcWorld, ((CraftPlayer)p.getPlayer()).getHandle());
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        Zombie zombie = (Zombie) customEnt.getBukkitEntity();
        zombie.setMaxHealth(health);
        zombie.setHealth(health);
        zombie.setCustomName(SUtil.colorize("&4Fissure Zombie"));
        zombie.setCustomNameVisible(false);
        zombie.setMetadata("summon", new FixedMetadataValue(SBX.getInstance(), p.getUniqueId()));
        zombie.setMetadata("defense",new FixedMetadataValue(SBX.getInstance(),p.getMaxStat(SBPlayer.PlayerStat.DEFENSE)*0.75));
        new BukkitRunnable() {
            Location rand = null;
            @Override
            public void run() {
                if(zombie.getLocation().distance(p.getLocation())>=10) {
                    boolean flag = false;
                    if(zombie.getLocation().distance(p.getLocation())>=20) {
                        flag = true;
                    }
                    Random random = new Random();
                    double dx = random.nextInt(20-10);
                    double dz = random.nextInt(20-10);
                    rand = p.getLocation().add(dx,0,dz);
                    if(flag) {
                        zombie.teleport(rand);
                    } else {
                        moveTo(rand, zombie);
                    }
                }

            }
        }.runTaskTimer(SBX.getInstance(), 0L, 20L);
        return zombie;
    }

    public static void moveTo(Location loc, LivingEntity entity) {
        ((EntityInsentient) ((CraftEntity) entity).getHandle()).getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.0f);
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }
}
