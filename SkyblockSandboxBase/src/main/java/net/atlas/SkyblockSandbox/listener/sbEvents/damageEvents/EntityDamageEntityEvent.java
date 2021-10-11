package net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.PlayerCustomDeathEvent;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.sound.Jingle;
import net.atlas.SkyblockSandbox.util.DamageUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bson.BsonSymbol;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.UUID;

public class EntityDamageEntityEvent extends SkyblockListener<EntityDamageByEntityEvent> {

    DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());


    @EventHandler
    public void callEvent(EntityDamageByEntityEvent event) {
        Entity damagee = event.getEntity();
        Entity damager = event.getDamager();
        if (damagee.hasMetadata("entity-tag")) {
            event.setCancelled(true);
            if (DamageUtil.getEntityByUniqueID(UUID.fromString(damagee.getMetadata("entity-tag").get(0).asString())) != null) {
                damagee = DamageUtil.getEntityByUniqueID(UUID.fromString(damagee.getMetadata("entity-tag").get(0).asString()));
                event = new EntityDamageByEntityEvent(damager, damagee, event.getCause(), event.getDamage());
                callEvent(event);
                return;
            }

        }
        if (damager instanceof Player) {
            event.setCancelled(true);
            SBPlayer p = new SBPlayer(((Player) damager));
            if (damagee instanceof LivingEntity) {
                if (damagee instanceof Player) {
                    if (!SBX.pvpEnabled) {
                        event.setCancelled(true);
                        return;
                    }
                }
                LivingEntity en = (LivingEntity) damagee;
                if (en.getMetadata("summon").isEmpty()) {
                    calculateHit(p, en, event, false);
                    int timeshit = 0;
                    if (en.getMetadata("times-hit").size() >= 1) {
                        timeshit = en.getMetadata("times-hit").get(0).asInt();
                    }
                    en.setMetadata("times-hit", new FixedMetadataValue(SBX.getInstance(), timeshit + 1));
                    return;
                }
            }
            return;
        }
        if (damagee instanceof Player) {
            SBPlayer p = new SBPlayer((Player) damagee);
            double def = p.getMaxStat(SBPlayer.PlayerStat.DEFENSE);
            double dmgreduction = 1 - (def / (def + 100));
            double dmg = 0;
            if (damager.hasMetadata(Slayers.ENDERMAN.toString())) {
                SlayerTier tier = Enums.getIfPresent(SlayerTier.class, damager.getMetadata(Slayers.ENDERMAN.toString()).get(0).asString()).orNull();
                if (tier != null) {
                    dmg = Slayers.ENDERMAN.getSlayerClass().getDPS().get(tier) / 2D * dmgreduction;
                }
            } else {
                dmg = DamageUtil.calculateSingleHit(damagee);
            }
            p.setStat(SBPlayer.PlayerStat.HEALTH, p.getStat(SBPlayer.PlayerStat.HEALTH) - dmg);
            double newHealth;
            double oldrng = (p.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0);
            if (oldrng == 0)
                newHealth = 0;
            else {
                double newRng = 20;
                if (p.getMaxStat(SBPlayer.PlayerStat.HEALTH) > 100) {
                    newRng = (40 - 0);
                }
                newHealth = Math.floor(((p.getStat(SBPlayer.PlayerStat.HEALTH) - 0) * newRng) / oldrng);
            }
            if (newHealth <= 0) {
                p.setHealth(p.getMaxHealth());
                Bukkit.getPluginManager().callEvent(new PlayerCustomDeathEvent((Player) damagee, p, event.getCause()));
            } else {
                p.setHealth(newHealth);
            }
            event.setDamage(0);
            return;
        }
        if (damager instanceof LivingEntity) {
            calculateEntityHit((LivingEntity) damagee, (LivingEntity) damager, event);
            return;
        }
        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            if (arrow.getShooter() instanceof Player) {
                SBPlayer p = new SBPlayer((Player) arrow.getShooter());
                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
                if (damagee instanceof LivingEntity) {
                    LivingEntity en = (LivingEntity) damagee;
                    event.setCancelled(true);
                    en.setMaximumNoDamageTicks(0);
                    //en.setNoDamageTicks(0);
                    calculateHit(p, en, event, true);
                    arrow.remove();
                }
            }
        }

    }


    public double calculateHit(SBPlayer p, LivingEntity damagee, EntityDamageByEntityEvent e, boolean arrow) {
        return calculateAttackSpeed(p, damagee, e, arrow);
    }

    public double calculateAttackSpeed(SBPlayer p, LivingEntity damagee, EntityDamageByEntityEvent event, boolean arrow) {
        if (damagee.hasMetadata("canDamage")) {
            //setting I frames for atck speed
            if (damagee.getMetadata("canDamage").get(0).asBoolean()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        damagee.setNoDamageTicks(0);
                        damagee.setMaximumNoDamageTicks(0);
                    }
                }.runTaskLater(SBX.getInstance(), 1L);
                //calculating and doing ferocity hits, checks if its greater than 0
                if (p.getMaxStat(SBPlayer.PlayerStat.FEROCITY) > 0) {
                    double fero = p.getMaxStat(SBPlayer.PlayerStat.FEROCITY);
                    calculateFeroHit(p, damagee, fero);
                }
                double atckSpeed = p.getMaxStat(SBPlayer.PlayerStat.ATTACK_SPEED);
                int iframes = (int) (10 / (1 + (atckSpeed / 100)));
                if (!arrow) {
                    damagee.setMetadata("canDamage", new FixedMetadataValue(SBX.getInstance(), false));
                }
                double dmg = DamageUtil.calculateSingleHit(damagee, p);
                event.setCancelled(false);
                if (event.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)) {
                    //Actually dealing damage
                    damagee.damage(dmg);
                } else {
                    if (damagee.hasMetadata(Slayers.ENDERMAN.toString())) {
                        if (damagee.getMetadata("hitshield").get(0).asInt() == 0) {
                            damagee.damage(dmg);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                damagee.setVelocity(damagee.getVelocity().normalize().multiply(0.1));
                            }
                        }.runTaskLater(SBX.getInstance(), 1L);
                    } else {
                        if(!DamageUtil.doDragonDamage(damagee,p,dmg)) {
                            damagee.damage(dmg);
                        }
                    }
                }

                if (!arrow) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            damagee.setMetadata("canDamage", new FixedMetadataValue(SBX.getInstance(), true));
                        }
                    }.runTaskLater(SBX.getInstance(), iframes);
                }
                return dmg;
            }
        } else {
            damagee.setMetadata("canDamage", new FixedMetadataValue(SBX.getInstance(), true));
            calculateHit(p, damagee, event, arrow);
        }
        return 0;
    }

    public void calculateFeroHit(SBPlayer p, LivingEntity damagee, double fero) {

        Random random = new Random();
        double chance = random.nextDouble();
        if ((fero / 100) >= chance) {
            if (fero > 100) {
                if (fero > 5000) {
                    fero = 5000;
                }
                fero -= 100;
                double finalFero = fero;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!damagee.isDead()) {
                            calculateFeroDoubleStrike(p, damagee, finalFero);
                        }
                    }
                }.runTaskLater(SBX.getInstance(), 10L);
            }
            p.playJingle(Jingle.FEROCITY_START, false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!damagee.isDead()) {
                        double dmg = DamageUtil.calculateSingleHit(damagee, p);
                        Location loc = damagee.getLocation();
                        if (dmg != 0) {
                            //damagee.damage(dmg);
                            if (damagee.getHealth() - dmg < 0) {
                                damagee.damage(dmg);
                            } else {
                                damagee.setHealth(damagee.getHealth() - dmg);
                            }
                            DamageUtil.doDragonDamage(damagee, p, dmg);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    damagee.setNoDamageTicks(0);
                                    damagee.setMaximumNoDamageTicks(0);
                                    if (!damagee.hasMetadata(Slayers.ENDERMAN.toString())) {
                                        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(((CraftEntity) damagee).getHandle(), (byte) 2);
                                        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                                    }
                                }
                            }.runTaskLater(SBX.getInstance(), 1L);
                        }
                        p.playJingle(Jingle.FEROCITY, false);
                    }
                }
            }.runTaskLater(SBX.getInstance(), 10L);
        }
    }

    public void calculateFeroDoubleStrike(SBPlayer p, LivingEntity damagee, double fero) {

        Random random = new Random();
        double chance = random.nextDouble();
        if ((fero / 100) >= chance) {
            if (fero > 100) {
                if (fero > 5000) {
                    fero = 5000;
                }
                fero -= 100;
                double finalFero = fero;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!damagee.isDead()) {
                            calculateFeroDoubleStrike(p, damagee, finalFero);
                        }
                    }
                }.runTaskLater(SBX.getInstance(), 5L);
            }
            if (!damagee.isDead()) {
                double dmg = DamageUtil.calculateSingleHit(damagee, p);
                Location loc = damagee.getLocation();
                if (dmg != 0) {
                    //damagee.damage(dmg);
                    if (damagee.getHealth() - dmg < 0) {
                        damagee.damage(dmg);
                    } else {
                        damagee.setHealth(damagee.getHealth() - dmg);
                    }
                    DamageUtil.doDragonDamage(damagee,p,dmg);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            damagee.setNoDamageTicks(0);
                            damagee.setMaximumNoDamageTicks(0);
                            if (!damagee.hasMetadata(Slayers.ENDERMAN.toString())) {
                                PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(((CraftEntity) damagee).getHandle(), (byte) 2);
                                ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                            }
                        }
                    }.runTaskLater(SBX.getInstance(), 1L);
                } else {
                    //damagee.damage(0);
                }
                p.playJingle(Jingle.FEROCITY, false);
            }
        }
    }

    public double calculateEntityHit(LivingEntity damagee, LivingEntity damager, EntityDamageByEntityEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                damagee.setNoDamageTicks(0);
                damagee.setMaximumNoDamageTicks(0);
            }
        }.runTaskLater(SBX.getInstance(), 1L);
        double dmg = DamageUtil.calculateSingleHit(damagee);
        if (damagee.hasMetadata(Slayers.ENDERMAN.toString())) {
            //
            if (damagee.getMetadata("hitshield").get(0).asInt() != 0) {
            } else {
                damagee.damage(dmg);
            }
        }
        return dmg;
    }

}
