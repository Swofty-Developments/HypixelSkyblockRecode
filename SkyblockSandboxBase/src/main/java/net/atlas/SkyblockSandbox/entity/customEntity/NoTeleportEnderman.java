package net.atlas.SkyblockSandbox.entity.customEntity;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.sound.Jingle;
import net.atlas.SkyblockSandbox.util.DamageUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class NoTeleportEnderman extends EntityEnderman {
    private static final UUID a = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier b;
    private static final Set<Block> c;
    private boolean bm;

    static {
        b = (new AttributeModifier(a, "Attacking speed boost", 0.15000000596046448D, 0)).a(false);
        c = Sets.newIdentityHashSet();
    }

    public NoTeleportEnderman(World world) {
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
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
        this.goalSelector.a(new PathfinderGoalPlayerWhoLookedAtTarget(this));
    }

    @Override
    public float getHeadHeight() {
        return 2.55F;
    }

    @Override
    public void m() {
        if (this.world.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                //this.world.addParticle(EnumParticle.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * (double)this.width, this.locY + this.random.nextDouble() * (double)this.length - 0.25D, this.locZ + (this.random.nextDouble() - 0.5D) * (double)this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D, new int[0]);
            }
        }

        this.aY = false;
        super.m();
    }

    @Override
    public void q(Entity entity) {

    }

    @Override
    protected void E() {
        if (this.U()) {
            this.damageEntity(DamageSource.DROWN, 1.0F);
        }

        if (this.co() && !this.bm && this.random.nextInt(100) == 0) {
            this.a(false);
        }

        if (this.world.w()) {
            float f = this.c(1.0F);
            if (f > 0.5F && this.world.i(new BlockPosition(this)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setGoalTarget((EntityLiving) null);
                this.a(false);
                this.bm = false;
                //this.n();
            }
        }

        //super.E();
    }

    @Override
    protected boolean n() {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;
        return this.k(d0, d1, d2);
    }

    @Override
    protected boolean b(Entity entity) {
        Vec3D vec3d = new Vec3D(this.locX - entity.locX, this.getBoundingBox().b + (double) (this.length / 2.0F) - entity.locY + (double) entity.getHeadHeight(), this.locZ - entity.locZ);
        vec3d = vec3d.a();
        double d1 = this.locX;
        double d2 = this.locY;
        double d3 = this.locZ;
        System.out.println("b");
        return this.k(d1, d2, d3);
    }

    @Override
    protected boolean k(double d0, double d1, double d2) {
        double d3 = this.locX;
        double d4 = this.locY;
        double d5 = this.locZ;
        boolean flag = false;
        BlockPosition blockposition = new BlockPosition(this.locX, this.locY, this.locZ);
        if (this.world.isLoaded(blockposition)) {
            boolean flag1 = false;

            while (!flag1 && blockposition.getY() > 0) {
                BlockPosition blockposition1 = blockposition.down();
                Block block = this.world.getType(blockposition1).getBlock();
                if (block.getMaterial().isSolid()) {
                    flag1 = true;
                } else {
                    --this.locY;
                    blockposition = blockposition1;
                }
            }

            if (flag1) {
                EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.world.getWorld(), d3, d4, d5), new Location(this.world.getWorld(), this.locX, this.locY, this.locZ));
                this.world.getServer().getPluginManager().callEvent(teleport);

                if (teleport.isCancelled()) {
                    return false;
                }

                Location to = teleport.getTo();
                this.enderTeleportTo(to.getX(), to.getY(), to.getZ());
                System.out.println("enderTp");
                if (this.world.getCubes(this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox())) {
                    flag = true;
                }
            }
        }

        //this.setPosition(d3, d4, d5);
        return false;
    }

    @Override
    public void enderTeleportTo(double d0, double d1, double d2) {
        System.out.println("test");
        return;
    }

    @Override
    protected String z() {
        return this.co() ? "mob.zombie.idle" : "mob.zombie.idle";
    }

    @Override
    protected String bo() {
        return "mob.silverfish.hit";
    }

    @Override
    protected String bp() {
        return "mob.zombie.idle";
    }

    @Override
    protected Item getLoot() {
        return Items.SNOWBALL;
    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {
        Item item = this.getLoot();
        if (item != null) {
            int j = this.random.nextInt(2 + i);

            for (int k = 0; k < j; ++k) {
                this.a(item, 1);
            }
        }

    }

    @Override
    public void setCarried(IBlockData iblockdata) {
        this.datawatcher.watch(16, (short) (Block.getCombinedId(iblockdata) & '\uffff'));
    }

    @Override
    public IBlockData getCarried() {
        return Block.getByCombinedId(this.datawatcher.getShort(16) & '\uffff');
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            if (damagesource.getEntity() == null || !(damagesource.getEntity() instanceof EntityEndermite)) {
                if (!this.world.isClientSide) {
                    this.a(true);
                }

                if (damagesource instanceof EntityDamageSource && damagesource.getEntity() instanceof EntityHuman) {
                    if (damagesource.getEntity() instanceof EntityPlayer && ((EntityPlayer) damagesource.getEntity()).playerInteractManager.isCreative()) {
                        this.a(false);
                    } else {
                        this.bm = true;
                    }
                }
            }
            boolean flag = damageEntity1(damagesource,f);
            if (damagesource.ignoresArmor() && this.random.nextInt(10) != 0) {
                //this.n();
                //System.out.println("e?");
            }

            return flag;
        }
    }

    public boolean damageEntity1(DamageSource damagesource, float f) {
        if (!damageEntity2(damagesource, f)) {
            return false;
        } else {
            Entity entity = damagesource.getEntity();
            return this.passenger != entity && this.vehicle != entity ? true : true;
        }
    }

    public boolean damageEntity2(DamageSource damagesource, float f) {
        if (this.world.isClientSide) {
            return false;
        } else {
            this.ticksFarFromPlayer = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else if (damagesource.o() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.aB = 1.5F;
                boolean flag = true;
                if ((float)this.noDamageTicks > (float)this.maxNoDamageTicks / 2.0F) {
                    if (f <= this.lastDamage) {
                        this.forceExplosionKnockback = true;
                        return false;
                    }

                    if (!this.d(damagesource, f - this.lastDamage)) {
                        return false;
                    }

                    this.lastDamage = f;
                    flag = false;
                } else {
                    this.getHealth();
                    if (!this.d(damagesource, f)) {
                        return false;
                    }

                    this.lastDamage = f;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.hurtTicks = this.av = 10;
                }

                this.aw = 0.0F;
                Entity entity = damagesource.getEntity();
                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.b((EntityLiving)entity);
                    }

                    if (entity instanceof EntityHuman) {
                        this.lastDamageByPlayerTime = 100;
                        this.killer = (EntityHuman)entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf)entity;
                        if (entitywolf.isTamed()) {
                            this.lastDamageByPlayerTime = 100;
                            this.killer = null;
                        }
                    }
                }

                if (flag) {
                    this.world.broadcastEntityEffect(this, (byte)2);
                    if (damagesource != DamageSource.DROWN) {
                        this.ac();
                    }

                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;
                        for(d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.aw = (float)(MathHelper.b(d1, d0) * 180.0D / 3.1415927410125732D - (double)this.yaw);
                        this.a(entity, f, d0, d1);
                    } else {
                        this.aw = (float)((int)(Math.random() * 2.0D) * 180);
                    }
                }

                String s;
                if (this.getHealth() <= 0.0F) {
                    s = this.bp();
                    if (flag && s != null) {
                        this.makeSound(s, this.bB(), this.bC());
                    }

                    this.die(damagesource);
                } else {
                    s = this.bo();
                    if (flag && s != null) {
                        this.makeSound(s, this.bB(), this.bC());
                    }
                }

                return true;
            }
        }
    }

    @Override
    protected boolean d(final DamageSource damagesource, float f) {
        if (!this.isInvulnerable(damagesource)) {
            float originalDamage = f;
            Function<Double, Double> hardHat = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return (damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && NoTeleportEnderman.this.getEquipment(4) != null ? -(f - f * 0.75D) : -0.0D;
                }
            };
            float hardHatModifier = ((Double)hardHat.apply((double)f)).floatValue();
            f += hardHatModifier;
            Function<Double, Double> blocking = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -0.0D;
                }
            };
            float blockingModifier = ((Double)blocking.apply((double)f)).floatValue();
            f += blockingModifier;
            Function<Double, Double> armor = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -(f - (double)NoTeleportEnderman.this.applyArmorModifier(damagesource, f.floatValue()));
                }
            };
            float armorModifier = ((Double)armor.apply((double)f)).floatValue();
            f += armorModifier;
            Function<Double, Double> resistance = new Function<Double, Double>() {
                public Double apply(Double f) {
                    if (!damagesource.isStarvation() && NoTeleportEnderman.this.hasEffect(MobEffectList.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                        int i = (NoTeleportEnderman.this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
                        int j = 25 - i;
                        float f1 = f.floatValue() * (float)j;
                        return -(f - (double)(f1 / 25.0F));
                    } else {
                        return -0.0D;
                    }
                }
            };
            float resistanceModifier = ((Double)resistance.apply((double)f)).floatValue();
            f += resistanceModifier;
            Function<Double, Double> magic = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -(f - (double)NoTeleportEnderman.this.applyMagicModifier(damagesource, f.floatValue()));
                }
            };
            float magicModifier = ((Double)magic.apply((double)f)).floatValue();
            f += magicModifier;
            Function<Double, Double> absorption = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -Math.max(f - Math.max(f - (double)NoTeleportEnderman.this.getAbsorptionHearts(), 0.0D), 0.0D);
                }
            };
            float absorptionModifier = ((Double)absorption.apply((double)f)).floatValue();
            EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, (double)originalDamage, (double)hardHatModifier, (double)blockingModifier, (double)armorModifier, (double)resistanceModifier, (double)magicModifier, (double)absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
            if (event.isCancelled()) {
                return false;
            } else {
                f = (float)event.getFinalDamage();
                if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getEquipment(4) != null) {
                    this.getEquipment(4).damage((int)(event.getDamage() * 4.0D + (double)this.random.nextFloat() * event.getDamage() * 2.0D), this);
                }

                float f2;
                if (!damagesource.ignoresArmor()) {
                    f2 = (float)(event.getDamage() + event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) + event.getDamage(EntityDamageEvent.DamageModifier.HARD_HAT));
                    this.damageArmor(f2);
                }

                absorptionModifier = (float)(-event.getDamage(EntityDamageEvent.DamageModifier.ABSORPTION));
                this.setAbsorptionHearts(Math.max(this.getAbsorptionHearts() - absorptionModifier, 0.0F));
                if (f != 0.0F) {

                    f2 = this.getHealth();
                    this.setHealth(f2 - f);
                    this.bs().a(damagesource, f2, f);
                    this.setAbsorptionHearts(this.getAbsorptionHearts() - f);
                }

                return true;
            }
        } else {
            return false;
        }
    }

    static class PathfinderGoalPlayerWhoLookedAtTarget extends PathfinderGoalNearestAttackableTarget {
        private EntityHuman g;
        private int h;
        private int i;
        private NoTeleportEnderman j;

        public PathfinderGoalPlayerWhoLookedAtTarget(NoTeleportEnderman entityenderman) {
            super(entityenderman, EntityHuman.class, true);
            this.j = entityenderman;
        }

        @Override
        public boolean a() {
            double d0 = this.f();
            List list = this.e.world.a(EntityHuman.class, this.e.getBoundingBox().grow(d0, 4.0D, d0), this.c);
            Collections.sort(list, this.b);
            if (list.isEmpty()) {
                return false;
            } else {
                this.g = (EntityHuman)list.get(0);
                return true;
            }
        }

        @Override
        public void c() {
            this.h = 5;
            this.i = 0;
        }

        @Override
        public void d() {
            this.g = null;
            this.j.a(false);
            AttributeInstance attributeinstance = this.j.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
            attributeinstance.c(NoTeleportEnderman.b);
            super.d();
        }

        @Override
        public boolean b() {
            if (this.g != null) {
                if (!this.j.c(this.g)) {
                    return false;
                } else {
                    this.j.bm = true;
                    this.j.a(this.g, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return super.b();
            }
        }

        @Override
        public void e() {
            if (this.g != null) {
                if (--this.h <= 0) {
                    this.d = this.g;
                    this.g = null;
                    super.c();
                    this.j.makeSound("mob.endermen.stare", 1.0F, 1.0F);
                    this.j.a(true);
                    AttributeInstance attributeinstance = this.j.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
                    attributeinstance.b(NoTeleportEnderman.b);
                }
            } else {
                if (this.d != null) {
                    if (this.d instanceof EntityHuman && this.j.c((EntityHuman)this.d)) {
                        if (this.d.h(this.j) < 16.0D) {
                            System.out.println("would-be teleport line 436 noTeleportEnderman");
                            //this.j.n();
                        }

                        this.i = 0;
                    } else if (this.d.h(this.j) > 256.0D && this.i++ >= 30 && this.j.b((Entity)this.d)) {
                        this.i = 0;
                    }
                }

                super.e();
            }

        }
    }


    public static Enderman spawn(Location loc) {

        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final NoTeleportEnderman customEnt = new NoTeleportEnderman(mcWorld);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Enderman) customEnt.getBukkitEntity();
    }

    public static Enderman spawnAsSlayer(Location loc, SBPlayer p, SlayerTier tier) {

        final UUID movementSpeedUID = UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c");
        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final NoTeleportEnderman customEnt = new NoTeleportEnderman(mcWorld);
        AttributeInstance attributes = customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "Move Speed", 0.00001, 1);
        attributes.b(modifier);
        attributes.a(modifier);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false); //Do we want to remove it when the NPC is far away? I won
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!(customEnt.getGoalTarget() instanceof EntityPlayer)) {
                    customEnt.setGoalTarget(((CraftPlayer) p.getPlayer()).getHandle());
                }
                if (customEnt.getBukkitEntity().isDead()) {
                    this.cancel();
                }
                double def = p.getMaxStat(SBPlayer.PlayerStat.DEFENSE);
                double dmgreduction = 1 - (def / (def + 100));
                double dmg = Slayers.ENDERMAN.getSlayerClass().getDPS().get(tier) / 2D * dmgreduction;

                for (org.bukkit.entity.Entity enb : customEnt.getBukkitEntity().getNearbyEntities(10, 10, 10)) {
                    if (enb instanceof Player) {
                        if (((Player) enb).getGameMode() != GameMode.CREATIVE) {
                            DamageUtil.spawnMarker(enb, customEnt.getBukkitEntity(), dmg, "&5");
                            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(customEnt.getBukkitEntity(), enb, EntityDamageEvent.DamageCause.ENTITY_ATTACK, dmg);
                            Bukkit.getServer().getPluginManager().callEvent(event);
                            ((Player) enb).damage(0);
                        }
                    }
                }
            }
        }.runTaskTimer(SBX.getInstance(), 20L, 20L);
        Enderman em = (Enderman) customEnt.getBukkitEntity();
        em.setMetadata("slayerowner",new FixedMetadataValue(SBX.getInstance(),p.getUniqueId()));
        return em;
    }

}
