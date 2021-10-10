package net.atlas.SkyblockSandbox.listener.sbEvents.abilities;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.util.DamageUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.Particles;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.NPC;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

import static net.atlas.SkyblockSandbox.SBX.*;


public class FunctionHandler implements Runnable {
    private final int index;
    private final String function;
    private final Player player;

    Integer multiplier = 1;
    public boolean activatedcrithit = false;


    FunctionHandler(Player player, int index, String function) {
        this.index = index;
        this.function = function;
        this.player = player;
    }

    @Override
    public void run() {
        switch (function) {
            case "RIGHT_CLICK":
            case "SHIFT_RIGHT_CLICK":
            case "SHIFT_LEFT_CLICK":
            case "LEFT_CLICK":
            case "RIGHT CLICK":
            case "SHIFT RIGHT CLICK":
            case "SHIFT LEFT CLICK":
            case "LEFT CLICK": {
                for (int count = 1; count <= 3; count++) {
                    for (String type : AbilityData.AFromB(2)) {
                        if (!AbilityData.retrieveFunctionData(AbilityData.AFromString(type), player.getItemInHand(), index, count).equals("")) {
                            switch (type) {
                                case "Particle_Type": {
                                    particles(count, index, type);
                                    break;
                                }
                                case "Implosion_Range": {
                                    implosion(count, index, type);
                                    break;
                                }
                                case "Teleport_Range": {
                                    teleport(count, index, type);
                                    break;
                                }
                                case "Sound_Type": {
                                    sound(count, index, type);
                                    break;
                                }
                                case "Head_Shooter_Type": {
                                    break;
                                }
                                case "Projectile_Shooter_Type": {
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            }
            default: {
                if (!AbilityData.hasAbility(player.getItemInHand(), index)) return;
                player.sendMessage(SUtil.colorize("&cThe click method you are trying to use is either not in the game or its not set!"));
                break;
            }
        }
    }

    public void drawCircle(Location loc, float radius, EnumParticle particle, int amount, Player player) {
        for (double t = 0; t < 50; t += 0.5) {
            float x = radius * (float) Math.sin(t);
            float z = radius * (float) Math.cos(t);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX() + x, (float) loc.getY(), (float) loc.getZ() + z, 0, 0, 0, 0, amount);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public void particles(int count, int index, String type) {
        try {
            Particles value = AbilityData.ValueFromName(String.valueOf(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_TYPE, player.getItemInHand(), index, count)));
            if (value.equals(Particles.ITEM_CRACK) || value.equals(Particles.BLOCK_CRACK) || value.equals(Particles.BLOCK_DUST)) {
                return;
            } else {
                int amount = 1;
                if (AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_SHOOTING, player.getItemInHand(), index, count).equals("True")) {
                    int range = (NumUtils.isInt(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_SHOOT_RANGE, player.getItemInHand(), index, count).toString()) ? Integer.parseInt(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_SHOOT_RANGE, player.getItemInHand(), index, count).toString()) : 0);
                    float damageMulti = (NumUtils.isFloat(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_DAMAGE_MULTIPLIER, player.getItemInHand(), index, count).toString()) ? Float.parseFloat(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_DAMAGE_MULTIPLIER, player.getItemInHand(), index, count).toString()) : 0.3F);
                    Location location = player.getLocation().clone();
                    Vector direction = location.getDirection().normalize();
                    direction.multiply(range);
                    Location endLoc = location.clone().add(direction);
                    direction.normalize();
                    SBPlayer p = new SBPlayer(player);
                    double d = 10000 * (1 + (p.getMaxStat(PlayerStat.INTELLIGENCE) / 100) * damageMulti);
                    int j = 0;
                    for (double i = 0; i < range / 5f; i += 0.1) {
                        Location currentLoc = location.add(direction);
                        if (AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, player.getItemInHand(), index, count).equals("True")) {
                            for (Entity entity : currentLoc.getWorld().getNearbyEntities(currentLoc, 0.5, 0.5, 0.5)) {
                                if (!(entity instanceof Player || entity instanceof ArmorStand || entity instanceof NPC || entity instanceof Item)) {
                                    if (entity.isDead()) {
                                        ((LivingEntity) entity).damage(0);
                                    } else {
                                        ++j;
                                        DamageUtil.spawnMarker(entity, player, d, false);
                                        ((LivingEntity) entity).damage(d);
                                    }
                                }
                            }

                        }
                        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(value.getA(), true,
                                (float) currentLoc.getX(),
                                (float) currentLoc.getY() + 1,
                                (float) currentLoc.getZ(), 0, 0, 0, 0, 1);
                        if (player.hasPermission("items.admin")) {
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(particles);
                            }
                        } else {
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
                        }
                    }
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_MESSAGE)) {
                        if (AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_MESSAGE, player.getItemInHand(), index, count).equals("True")) {
                            if (j >= 1) {
                                DecimalFormat format = new DecimalFormat("#,###");
                                int damage = (int) (d * j);
                                player.sendMessage(SUtil.colorize("&7Your Particle function hit &c" + j + "&7 enemies dealing &c" + format.format(damage) + " damage&7."));
                            }
                        }
                    } else {
                        if (j >= 1) {
                            DecimalFormat format = new DecimalFormat("#,###");
                            int damage = (int) (d * j);
                            player.sendMessage(SUtil.colorize("&7Your Particle function hit &c" + j + "&7 enemies dealing &c" + format.format(damage) + " damage&7."));
                        }
                    }
                }
                switch (String.valueOf(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_SHAPE, player.getItemInHand(), index, count))) {
                    case "Circle": {
                        drawCircle(player.getLocation(), 5, value.getA(), amount, player);
                        break;
                    }
                    case "On Player": {
                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(value.getA(), true, (float) player.getLocation().getX(), (float) player.getLocation().getY(), (float) player.getLocation().getZ(), 0, 0, 0, 0, amount);
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                            break;
                        }
                    }
                    default: {
                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(value.getA(), true, (float) player.getLocation().getX(), (float) player.getLocation().getY(), (float) player.getLocation().getZ(), 0, 0, 0, 0, amount);
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                            break;
                        }
                    }
                }
            }

        } catch (NullPointerException e) {
            player.sendMessage(SUtil.colorize("&cThe function #" + count + "&c is either missing something or something is wrong!"));
        }
    }

    public void sound(int count, int index, String type) {
        try {
            Sound sound = Sound.valueOf(String.valueOf(AbilityData.retrieveFunctionData(EnumFunctionsData.SOUND_TYPE, player.getItemInHand(), index, count)));
            float pitch = AbilityData.floatFromFunction(player.getItemInHand(), EnumFunctionsData.SOUND_PITCH, index, count, 0.5F);
            float volume = AbilityData.floatFromFunction(player.getItemInHand(), EnumFunctionsData.SOUND_VOLUME, index, count, 1.0F);
            int amount = AbilityData.intFromFunction(player.getItemInHand(), EnumFunctionsData.SOUND_AMOUNT, index, count, 10);
            float delay = AbilityData.floatFromFunction(player.getItemInHand(), EnumFunctionsData.SOUND_DELAY, index, count, 0F);
            new BukkitRunnable() {
                int counted = 0;

                @Override
                public void run() {
                    if (counted < amount) {
                        player.playSound(player.getLocation(), sound, pitch, volume);
                    } else {
                        this.cancel();
                    }
                    counted++;
                }
            }.runTaskTimer(SBX.getInstance(), 0, (long) (delay * 20));
        } catch (NullPointerException e) {
            player.sendMessage(SUtil.colorize("&cThe function #" + count + "&c is either missing something or something is wrong!"));
        }
    }

    public void implosion(int count, int index, String type) {
        try {
            SBPlayer p = new SBPlayer(player);
            double d = 10000 * (1 + (p.getMaxStat(PlayerStat.INTELLIGENCE) / 100) * 0.3);
            int i = 0;
            int range = (NumUtils.isInt(AbilityData.retrieveFunctionData(EnumFunctionsData.IMPLOSION_RANGE, player.getItemInHand(), index, count).toString()) ? Integer.parseInt(AbilityData.retrieveFunctionData(EnumFunctionsData.IMPLOSION_RANGE, player.getItemInHand(), index, count).toString()) : 0);
            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), range, range, range)) {
                if (e instanceof LivingEntity) {
                    if (!(e instanceof Player || e instanceof ArmorStand || e instanceof NPC || e.hasMetadata("entity-tag"))) {
                        if (e.isDead()) {
                            ((LivingEntity) e).damage(0);
                        } else {
                            ++i;
                            ((LivingEntity) e).damage(d);
                        }
                    }
                }
            }
            if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.IMPLOSION_MESSAGE)) {
                if (AbilityData.retrieveFunctionData(EnumFunctionsData.IMPLOSION_MESSAGE, player.getItemInHand(), index, count).equals("True")) {
                    if (i >= 1) {
                        DecimalFormat format = new DecimalFormat("#,###");
                        int damage = (int) (d * i);
                        player.sendMessage(SUtil.colorize("&7Your Implosion Function hit &c" + i + "&7 enemies dealing &c" + format.format(damage) + " damage&7."));
                    }
                }
            } else {
                if (i >= 1) {
                    DecimalFormat format = new DecimalFormat("#,###");
                    int damage = (int) (d * i);
                    player.sendMessage(SUtil.colorize("&7Your Implosion Function hit &c" + i + "&7 enemies dealing &c" + format.format(damage) + " damage&7."));
                }
            }
        } catch (NullPointerException e) {
            player.sendMessage(SUtil.colorize("&cThe function #" + count + "&c is either missing something or something is wrong!"));
        }
    }

    public void teleport(int count, int index, String type) {
        try {
            int range = Integer.parseInt(String.valueOf(AbilityData.retrieveFunctionData(AbilityData.AFromString(type), player.getItemInHand(), index, count)));
            boolean message = AbilityData.booleanFromFunction(player.getItemInHand(), EnumFunctionsData.TELEPORT_MESSAGE, index, count, true);
            ArrayList<Block> blocks = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, range);
            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                if (block.getType() != Material.AIR || block.isLiquid()) {
                    if (i == 0 || i == 1) {
                        if (message) {
                            player.sendMessage(SUtil.colorize("&cThere is a block in the way!"));
                        }
                        range = 0;
                        return;
                    }
                    range = i - 1;
                    break;
                }
            }
            Block b = blocks.get(range);
            Location loc = new Location(b.getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5, player.getLocation().getYaw(),
                    player.getLocation().getPitch());
            player.teleport(loc);
        } catch (NullPointerException e) {
            player.sendMessage(SUtil.colorize("&cThe function #" + count + "&c is either missing something or something is wrong!"));
        }
    }

    private boolean validEntity(Entity entity) {
        return entity instanceof LivingEntity && entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.VILLAGER && entity.getType() != EntityType.SLIME;
    }

}
