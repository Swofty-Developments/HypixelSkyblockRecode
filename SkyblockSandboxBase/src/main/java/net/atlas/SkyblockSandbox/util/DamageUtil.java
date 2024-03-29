package net.atlas.SkyblockSandbox.util;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.*;

public class DamageUtil {
    public static final String damageColorSequence = "ffe6cc";
    static HashMap<LivingEntity, Integer> entityPhase = new HashMap<>();

    public static void spawnMarker(Entity damagee, Entity damager, double damage, boolean crit) {
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) damagee.getWorld()).getHandle(), damagee.getLocation().getX(), damagee.getLocation().getY(), damagee.getLocation().getZ());
        stand.setCustomNameVisible(true);
        double randomX = Math.random();
        double randomY = Math.random();
        randomX += -0.2D;
        randomX += -0.2D;
        stand.getBukkitEntity().teleport(stand.getBukkitEntity().getLocation().add(randomX, 1, randomY));
        damage = Math.floor(damage);
        DecimalFormat format = new DecimalFormat("#");
        if (crit) {
            stand.setCustomName(formatDamage(damage));
        } else {
            stand.setCustomName(SUtil.colorize("&7" + format.format(damage)));
        }
        stand.setInvisible(true);
        stand.setGravity(false);
        ((ArmorStand) stand.getBukkitEntity()).setMarker(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        if (damager instanceof Player) {
            if (damager instanceof SBPlayer) {
                ((CraftPlayer) ((SBPlayer) damager).getPlayer()).getHandle().playerConnection.sendPacket(packet);
            } else {
                ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(packet);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(stand.getId());
                    if (damager instanceof SBPlayer) {
                        ((CraftPlayer) ((SBPlayer) damager).getPlayer()).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                    } else {
                        ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                    }
                    stand.getBukkitEntity().remove();

                }
            }.runTaskLater(SBX.getInstance(), 20L);
        }

    }

    public static void spawnMarker(Entity damagee, Entity damager, double damage, String color) {
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) damagee.getWorld()).getHandle(), damagee.getLocation().getX(), damagee.getLocation().getY(), damagee.getLocation().getZ());
        stand.setCustomNameVisible(true);
        double randomX = Math.random();
        double randomY = Math.random();
        randomX += -0.2D;
        randomX += -0.2D;
        stand.getBukkitEntity().teleport(stand.getBukkitEntity().getLocation().add(randomX, 1, randomY));
        damage = Math.floor(damage);
        DecimalFormat format = new DecimalFormat("#");

        stand.setCustomName(SUtil.colorize(color + format.format(damage)));

        stand.setInvisible(true);
        stand.setGravity(false);
        ((ArmorStand) stand.getBukkitEntity()).setMarker(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        if (damager instanceof Player) {
            ((CraftPlayer) (damager)).getHandle().playerConnection.sendPacket(packet);
        }
        if (damagee instanceof Player) {
            ((CraftPlayer) damagee).getHandle().playerConnection.sendPacket(packet);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(stand.getId());
                stand.getBukkitEntity().remove();
                if (damager instanceof Player) {
                    ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                }

                if (damagee instanceof Player) {
                    ((CraftPlayer) damagee).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                }


            }
        }.runTaskLater(SBX.getInstance(), 20L);

    }

    public static boolean doDragonDamage(LivingEntity damagee,SBPlayer p,double dmg) {
        if (StartFight.fightActive) {
            if (damagee instanceof CraftEnderDragon) {
                DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());
                CraftEnderDragon dragon = (CraftEnderDragon) damagee;
                double hlth = StartFight.maxDragHealth;

                if (dmg > 120000) {
                    dmg = 120000 + (dmg * 0.00003);
                }
                dragonScoreboard.updateDragonDMG(p.getPlayer(), dmg);
                LootListener.damage.put(p.getPlayer(), StartFight.playerDMG.get(p.getPlayer()));
                StartFight.dragonHealth -= dmg;
                if (StartFight.dragonHealth < 0) {
                    StartFight.dragonHealth = 0;
                }
                double pcntHealth = (StartFight.dragonHealth / StartFight.maxDragHealth) * 200;
                if (pcntHealth > 200) {
                    pcntHealth = 200;
                }
                if (pcntHealth == 0) {
                    damagee.setHealth(0);
                    EntityDeathEvent event = new EntityDeathEvent(damagee,null,0);
                    Bukkit.getPluginManager().callEvent(event);
                } else {
                    damagee.setHealth(pcntHealth);
                    damagee.damage(0);
                }
                return true;
            }
        }
        return false;
    }

    public static double calculateSingleHit(Entity en, SBPlayer p) {
        boolean isCrit = false;
        double cc = p.getMaxStat(CRIT_CHANCE);
        Random random = new Random();
        double crit = random.nextDouble();
        if (cc / 100 >= crit) {
            isCrit = true;
        }
        double wpDmg = p.getMaxStat(DAMAGE);
        double str = p.getMaxStat(STRENGTH);
        double cd = p.getMaxStat(CRIT_DAMAGE);
        double init = (wpDmg + 5D) * (1D + (str / 100D));
        double mult = 1 + (/*combat lvl*/0 * 0.04);
        double dmg = 0;
        if (isCrit) {
            dmg = init * mult * (1 + (cd / 100D));
            dmg = calculateEnchants(p, (LivingEntity) en, dmg);
            dmg = calculateDefense((LivingEntity) en, dmg);
            if (checkHitShield(en, p, dmg)) {
                dmg = 0;
            } else {
                DamageUtil.spawnMarker(en, p, dmg, true);
                ((LivingEntity) en).damage(0);
            }
        } else {
            dmg = init * mult;
            dmg = calculateEnchants(p, (LivingEntity) en, dmg);
            dmg = calculateDefense((LivingEntity) en, dmg);
            if (checkHitShield(en, p, dmg)) {
                dmg = 0;
            } else {
                DamageUtil.spawnMarker(en, p, dmg, false);
                ((LivingEntity) en).damage(0);
            }
        }

        return dmg;
    }

    public static double calculateSingleHit(Entity en, SBPlayer p,boolean spawnMarker) {
        boolean isCrit = false;
        double cc = p.getMaxStat(CRIT_CHANCE);
        Random random = new Random();
        double crit = random.nextDouble();
        if (cc / 100 >= crit) {
            isCrit = true;
        }
        double wpDmg = p.getMaxStat(DAMAGE);
        double str = p.getMaxStat(STRENGTH);
        double cd = p.getMaxStat(CRIT_DAMAGE);
        double init = (wpDmg + 5D) * (1D + (str / 100D));
        double mult = 1 + (/*combat lvl*/0 * 0.04);
        double dmg = 0;
        if (isCrit) {
            dmg = init * mult * (1 + (cd / 100D));
            dmg = calculateEnchants(p, (LivingEntity) en, dmg);
            dmg = calculateDefense((LivingEntity) en, dmg);
            if (checkHitShield(en, p, dmg)) {
                dmg = 0;
            } else {
                if (spawnMarker) {
                    DamageUtil.spawnMarker(en, p, dmg, true);
                }
                ((LivingEntity) en).damage(0);
            }
        } else {
            dmg = init * mult;
            dmg = calculateEnchants(p, (LivingEntity) en, dmg);
            dmg = calculateDefense((LivingEntity) en, dmg);
            if (checkHitShield(en, p, dmg)) {
                dmg = 0;
            } else {
                if (spawnMarker) {
                    DamageUtil.spawnMarker(en, p, dmg, false);
                }
                ((LivingEntity) en).damage(0);
            }
        }

        return dmg;
    }

    public static double calculateSingleHit(Entity damagee) {
        boolean isCrit = false;
        HashMap<SBPlayer.PlayerStat, Double> playerStats = new HashMap<>();
        for (SBPlayer.PlayerStat stat : SBPlayer.PlayerStat.values()) {
            playerStats.put(stat, stat.getBase());
        }
        double cc = playerStats.get(CRIT_CHANCE);
        Random random = new Random();
        double crit = random.nextDouble();
        if (cc / 100 >= crit) {
            isCrit = true;
        }
        double wpDmg = playerStats.get(SBPlayer.PlayerStat.DAMAGE);
        double str = playerStats.get(SBPlayer.PlayerStat.STRENGTH);
        double cd = playerStats.get(CRIT_DAMAGE);
        double init = (wpDmg + 5) * (1 + (str / 100));
        double mult = 1 + (/*combat lvl*/0 * 0.04); /*+enchants + weaponbonus*/
        double dmg = 0;
        if (isCrit) {
            dmg = init * mult * (1 + (cd / 100));
        } else {
            dmg = init * mult;
        }
        dmg = calculateDefense((LivingEntity) damagee, dmg);
        if (checkHitShield(damagee, dmg)) {
            dmg = 0;
        } else {
            ((LivingEntity) damagee).damage(0);
        }

        return dmg;
    }

    public static Entity getEntityByUniqueID(UUID uniqueID) {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getUniqueId().equals(uniqueID))
                        return entity;
                }
            }
        }

        return null;
    }

    public static double calculateDefense(LivingEntity en, double beforeDmg) {
        if (en instanceof Player) {
            SBPlayer p = new SBPlayer(((Player) en));
            double def = p.getMaxStat(SBPlayer.PlayerStat.DEFENSE);
            double dmgreduction = 1 - (def / (def + 100));
            return beforeDmg * dmgreduction;
        }
        if (!en.getMetadata("defense").isEmpty()) {
            double def = en.getMetadata("defense").get(0).asInt();
            double dmgreduction = 1 - (def / (def + 100));
            //dmgreduction is just a percentage that i multiply total damage by.
            return beforeDmg * dmgreduction;
        }
        return beforeDmg;
    }

    public static boolean checkHitShield(Entity en, SBPlayer p, double dmg) {
        if (en.hasMetadata("hitshield")) {
            if (!entityPhase.containsKey((LivingEntity) en)) {
                entityPhase.put((LivingEntity) en, 0);
            }
            int prevPhase = entityPhase.get((LivingEntity) en);
            int hitShield = en.getMetadata("hitshield").get(0).asInt();
            LivingEntity livingen = (LivingEntity) en;
            double pcnt = (livingen.getHealth() / livingen.getMaxHealth()) * 100;
            if (hitShield != 0) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, hitShield / 100f);
                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), hitShield - 1));
                en.setMetadata("canDamage", new FixedMetadataValue(SBX.getInstance(), true));
                if (hitShield - 1 <= 0) {
                    p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 0.5f, 1);
                }
                return true;
            } else {
                if (pcnt <= 75) {
                    entityPhase.put((LivingEntity) en, 1);
                    if (pcnt <= 50) {
                        entityPhase.put((LivingEntity) en, 2);
                        if (pcnt <= 25) {
                            entityPhase.put((LivingEntity) en, 3);
                        }
                    }
                }
                if (prevPhase != entityPhase.get((LivingEntity) en)) {
                    SlayerTier tier = Enums.getIfPresent(SlayerTier.class, en.getMetadata(Slayers.ENDERMAN.toString()).get(0).asString()).orNull();
                    if (tier != null) {
                        switch (tier) {
                            case ONE:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 10));
                                break;
                            case TWO:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 35));
                                break;
                            case THREE:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 70));
                                break;
                            case FOUR:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 100));
                                break;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static boolean checkHitShield(Entity en, double dmg) {
        if (en.hasMetadata("hitshield")) {
            SBPlayer p = new SBPlayer(Bukkit.getPlayer(UUID.fromString(en.getMetadata("slayerowner").get(0).asString())));
            if (!entityPhase.containsKey((LivingEntity) en)) {
                entityPhase.put((LivingEntity) en, 0);
            }
            int prevPhase = entityPhase.get((LivingEntity) en);
            int hitShield = en.getMetadata("hitshield").get(0).asInt();
            LivingEntity livingen = (LivingEntity) en;
            double pcnt = (livingen.getHealth() / livingen.getMaxHealth()) * 100;
            if (hitShield != 0) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, hitShield / 100f);
                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), hitShield - 1));
                en.setMetadata("canDamage", new FixedMetadataValue(SBX.getInstance(), true));
                if (hitShield - 1 <= 0) {
                    p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 0.5f, 1);
                }
                return true;
            } else {
                if (pcnt <= 75) {
                    entityPhase.put((LivingEntity) en, 1);
                    if (pcnt <= 50) {
                        entityPhase.put((LivingEntity) en, 2);
                        if (pcnt <= 25) {
                            entityPhase.put((LivingEntity) en, 3);
                        }
                    }
                }
                if (prevPhase != entityPhase.get((LivingEntity) en)) {
                    SlayerTier tier = Enums.getIfPresent(SlayerTier.class, en.getMetadata(Slayers.ENDERMAN.toString()).get(0).asString()).orNull();
                    if (tier != null) {
                        switch (tier) {
                            case ONE:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 10));
                                break;
                            case TWO:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 35));
                                break;
                            case THREE:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 70));
                                break;
                            case FOUR:
                                en.setMetadata("hitshield", new FixedMetadataValue(SBX.getInstance(), 100));
                                break;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static String formatDamage(double colordamage) {
        DecimalFormat format = new DecimalFormat("0");
        String formatted = format.format(colordamage);
        String init = "✧" + formatted + "✧";
        StringBuilder result = new StringBuilder();
        int iterator = 0;
        for (char c : init.toCharArray()) {
            if (iterator >= damageColorSequence.length()) iterator = 0;

            result.append("§").append(damageColorSequence.charAt(iterator)).append(c);
            iterator++;
        }

        return result.toString();
    }

    public static double calculateEnchants(SBPlayer p, LivingEntity en, double curDmg) {
        if (!(p.getItemInHand().getType().equals(Material.AIR) || p.getItemInHand() == null)) {
            for (Enchantment ench : Enchantment.values()) {
                if (ench.getItemType() != null) {
                    if (ench.getItemType().equals(ItemType.SWORD) || ench.getItemType().equals(ItemType.ITEM)) {
                        int enchLvl = new SBItemStack(p.getItemInHand()).getEnchantment(ench);
                        if (enchLvl != 0) {
                            EntityDamageByEntityEvent event1 = new EntityDamageByEntityEvent(p.getPlayer(), en, EntityDamageEvent.DamageCause.CUSTOM, curDmg);
                            if (ench.getDamageAction() != null) {
                                curDmg = ench.getDamageAction().apply(event1, (int) curDmg, enchLvl);
                            }
                        }
                    }
                }
            }
        }
        return curDmg;
    }
}
