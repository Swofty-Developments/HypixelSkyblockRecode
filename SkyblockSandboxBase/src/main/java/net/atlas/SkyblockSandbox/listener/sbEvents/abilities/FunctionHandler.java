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
                                    headShooter(count, index, type);
                                    break;
                                }
                                case "Projectile_Shooter_Type": {
                                    projShooter(count, index, type);
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

    public void headShooter(int count, int index, String type) {
        boolean entityDamage = AbilityData.booleanFromFunction(player.getItemInHand(), EnumFunctionsData.HEAD_SHOOTER_DAMAGE_ENTITY, index, count, false);
        String texture = AbilityData.stringFromFunction(player.getItemInHand(), EnumFunctionsData.HEAD_SHOOTER_TYPE, index, count, null);
        int range = AbilityData.intFromFunction(player.getItemInHand(), EnumFunctionsData.HEAD_SHOOTER_RANGE, index, count, 1);
        int kickbackRange = AbilityData.intFromFunction(player.getItemInHand(), EnumFunctionsData.HEAD_SHOOTER_KICKBACK_RANGE, index, count, 1);
        boolean kickback = AbilityData.booleanFromFunction(player.getItemInHand(), EnumFunctionsData.HEAD_SHOOTER_KICKBACK, index, count, false);
        int baseDamage = AbilityData.intFromFunction(player.getItemInHand(), EnumFunctionsData.HEAD_SHOOTER_BASE_DAMAGE, index, count, 1000);
        Location loc = player.getLocation();
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName("HeadShooterFunctionArmorStand");
        stand.setCustomNameVisible(false);
        stand.setGravity(false);
        stand.setInvisible(true);


        PacketPlayOutSpawnEntityLiving spawnP = new PacketPlayOutSpawnEntityLiving(stand);
        PacketPlayOutEntityDestroy removeP = new PacketPlayOutEntityDestroy(stand.getId());

        PacketPlayOutEntityEquipment EquipP = new PacketPlayOutEntityEquipment(stand.getId(), 4, CraftItemStack.asNMSCopy(NormalGUI.makeColorfulSkullItem("HeadShooterFunction", texture, 1, "")));

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnP);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(EquipP);
        Location location = player.getLocation();
        Vector direction = player.getLocation().getDirection();
        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = stand.getBukkitEntity().getLocation();

                loc.add(direction);

                stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
                PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(stand);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(teleport);
                if (!loc.add(0, 2, 0).getBlock().isEmpty()) {
                    if (stand.getBukkitEntity().getNearbyEntities(3, 3, 3).contains(player)) {
                        if (kickback) {
                            player.setVelocity(player.getLocation().getDirection().setY(direction.getY() + kickbackRange).setX(-direction.getX() - kickbackRange).setZ(-direction.getZ() - kickbackRange));
                        }
                    }
                    this.cancel();
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeP);
                }
                for (Entity e2 : stand.getBukkitEntity().getNearbyEntities(5, 3, 5)) {
                    if (validEntity(e2)) {
                        if (!e2.isDead()) {
                            SBPlayer p = new SBPlayer(player);
                            LivingEntity entity = (LivingEntity) e2;
                            entity.damage(baseDamage * (1 + (p.getMaxStat(PlayerStat.INTELLIGENCE) / 100) * 0.3));
                            DamageUtil.spawnMarker(e2, player, baseDamage * (1 + (p.getMaxStat(PlayerStat.INTELLIGENCE) / 100) * 0.3), false);
                            this.cancel();
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeP);
                        }
                    }
                }

                if (loc.distance(location) >= range) {
                    this.cancel();
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeP);
                }
            }
        }.runTaskTimer(SBX.getInstance(), 0L, 1L);
    }

    public void projShooter(int count, int index, String type) {
        if (canfire.containsKey(player)) {
            if (canfire.get(player)) {
                if (thrownAxes.containsKey(player)) {
                    if (thrownAxes.get(player).size() > 25) {
                        player.sendMessage(ChatColor.RED + "Projectile limit reached.");
                    } else {
                        //throwProj(count, index);
                        throwProjOptimized(count, index);
                    }
                    canfire.put(player, false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            canfire.put(player, true);
                        }
                    }.runTaskLater(SBX.getInstance(), 6);

                } else {
                    //throwProj(count, index);
                    throwProjOptimized(count, index);
                }
            }
        } else {
            //throwProj(count, index);
            throwProjOptimized(count, index);
            canfire.put(player, true);
        }
    }

    void throwProj(int count, int index) {
        boolean entityDamage = AbilityData.booleanFromFunction(player.getItemInHand(), EnumFunctionsData.PROJECTILE_SHOOTER_DAMAGE_ENTITY, index, count, false);
        Material mat = Material.valueOf(AbilityData.stringFromFunction(player.getItemInHand(), EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, index, count, null));
        int baseDamage = AbilityData.intFromFunction(player.getItemInHand(), EnumFunctionsData.PROJECTILE_SHOOTER_BASE_DAMAGE, index, count, 1000);
        if (player.hasMetadata("AOTS")) {
            multiplier = player.getMetadata("AOTS").get(0).asInt();
            if (player.getMetadata("AOTS").get(0).asInt() != 16) {
                player.setMetadata("AOTS",
                        new FixedMetadataValue(SBX.getInstance(), player.getMetadata("AOTS").get(0).asInt() + 2));
            }
        } else {
            player.setMetadata("AOTS", new FixedMetadataValue(SBX.getInstance(), 2));
        }

        Location loc = player.getLocation().getDirection().normalize().multiply(35).toLocation(player.getWorld());

        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        stand.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

        stand.setArms(true);
        stand.setGravity(false);

        PacketPlayOutSpawnEntityLiving spawnP = new PacketPlayOutSpawnEntityLiving(stand);

        ItemStack i = new ItemStack(mat);
        PacketPlayOutEntityEquipment EquipP = new PacketPlayOutEntityEquipment(stand.getId(), 0, CraftItemStack.asNMSCopy(i));
        ((ArmorStand) stand.getBukkitEntity()).setRightArmPose(new EulerAngle(Math.toRadians(90), Math.toRadians(0), Math.toRadians(180)));
        ((ArmorStand) stand.getBukkitEntity()).setVisible(false);
        ((ArmorStand) stand.getBukkitEntity()).setMarker(true);
        stand.setBasePlate(false);
        for (Entity b : stand.getBukkitEntity().getNearbyEntities(50, 50, 50)) {
            if (b instanceof Player) {
                ((CraftPlayer) b).getHandle().playerConnection.sendPacket(spawnP);
                ((CraftPlayer) b).getHandle().playerConnection.sendPacket(EquipP);
            }
        }
        List<EntityArmorStand> list;
        if (thrownAxes.containsKey(player)) {
            list = thrownAxes.get(player);
        } else {
            list = new ArrayList<>();
        }
        list.add(stand);
        thrownAxes.put(player, list);
        new BukkitRunnable() {
            int angle = 0;
            Vector v = loc.toVector();
            int counter = 0;
            int cooldown = 11;

            @Override
            public void run() {
                for (Entity entity : loc.getWorld().getNearbyEntities(stand.getBukkitEntity().getLocation(), 2, 2, 2)) {
                    if (entity instanceof LivingEntity) {
                        if (!(entity instanceof Player) && !(entity instanceof ArmorStand)) {
                            if (!entity.isDead()) {
                                SBPlayer p = new SBPlayer(player);
                                LivingEntity e = (LivingEntity) entity;
                                double damage = baseDamage + (DamageUtil.calculateSingleHit(e, p));
                                if (cooldown > 10) {
                                    DamageUtil.spawnMarker(e, player, damage, false);
                                    cooldown = 0;
                                    if (e instanceof EnderDragon) {
                                        if (StartFight.fightActive) {
                                            DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());
                                            dragonScoreboard.updateDragonDMG(player, (damage / 10) * multiplier);
                                            LootListener.damage.put(player, StartFight.playerDMG.get(player));
                                            e.damage((damage / 10) * multiplier);
                                        }
                                    } else {
                                        e.damage((damage / 10) * multiplier);
                                    }
                                    player.playSound(e.getLocation(), Sound.ORB_PICKUP, 10f, 1.5f);
                                }
                            }

                        }
                    }
                }
                if (((ArmorStand) stand.getBukkitEntity()).getEyeLocation().distance(loc) <= 3
                        || (((ArmorStand) stand.getBukkitEntity()).getEyeLocation().add(0, 1, 0).getBlock().getType() != Material.AIR
                        && !((ArmorStand) stand.getBukkitEntity()).getEyeLocation().add(0, 1, 0).getBlock().isLiquid())
                        || counter >= 130) {
                    for (Player b : Bukkit.getOnlinePlayers()) {
                        if (b != null) {
                            PacketPlayOutEntityDestroy removeP = new PacketPlayOutEntityDestroy(stand.getId());
                            ((CraftPlayer) b).getHandle().playerConnection.sendPacket(removeP);
                            ((ArmorStand) stand.getBukkitEntity()).remove();
                        }
                    }
                    counter = 0;
                    multiplier = 0;
                    List<EntityArmorStand> list = thrownAxes.get(player);
                    list.remove(stand);
                    thrownAxes.put(player, list);
                    this.cancel();
                }
                angle -= 30;
                ((ArmorStand) stand.getBukkitEntity()).setRightArmPose(
                        new EulerAngle(Math.toRadians(angle), Math.toRadians(0), Math.toRadians(180)));
                stand.getBukkitEntity().teleport(new Location(stand.getWorld().getWorld(), stand.getBukkitEntity().getLocation().getX(),
                        stand.getBukkitEntity().getLocation().getY(), stand.getBukkitEntity().getLocation().getZ(), stand.getBukkitEntity().getLocation().getYaw(),
                        stand.getBukkitEntity().getLocation().getPitch()).add(v.normalize()));
                DataWatcher watcher = stand.getDataWatcher();
                PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(stand.getId(), watcher, true);
                PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(stand);

                for (Player b : Bukkit.getOnlinePlayers()) {
                    if (b != null) {
                        ((CraftPlayer) b).getHandle().playerConnection.sendPacket(metadata);
                        ((CraftPlayer) b).getHandle().playerConnection.sendPacket(teleport);
                    }
                }
                counter++;
                cooldown++;
            }
        }.runTaskTimer(SBX.getInstance(), 1L, 1L);
    }


    void throwProjOptimized(int count, int index) {
        boolean entityDamage = AbilityData.booleanFromFunction(player.getItemInHand(), EnumFunctionsData.PROJECTILE_SHOOTER_DAMAGE_ENTITY, index, count, false);
        Material mat = Material.valueOf(AbilityData.stringFromFunction(player.getItemInHand(), EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, index, count, null));
        int baseDamage = AbilityData.intFromFunction(player.getItemInHand(), EnumFunctionsData.PROJECTILE_SHOOTER_BASE_DAMAGE, index, count, 1000);
        if (player.hasMetadata("AOTS")) {
            multiplier = player.getMetadata("AOTS").get(0).asInt();
            if (player.getMetadata("AOTS").get(0).asInt() != 16) {
                player.setMetadata("AOTS",
                        new FixedMetadataValue(SBX.getInstance(), player.getMetadata("AOTS").get(0).asInt() + 2));
            }
        } else {
            player.setMetadata("AOTS", new FixedMetadataValue(SBX.getInstance(), 2));
        }

        Location loc = player.getLocation().getDirection().normalize().multiply(35).toLocation(player.getWorld());

        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        stand.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

        stand.setArms(true);
        stand.setGravity(false);

        PacketPlayOutSpawnEntityLiving spawnP = new PacketPlayOutSpawnEntityLiving(stand);

        ItemStack i = new ItemStack(mat);
        PacketPlayOutEntityEquipment EquipP = new PacketPlayOutEntityEquipment(stand.getId(), 0, CraftItemStack.asNMSCopy(i));
        ((ArmorStand) stand.getBukkitEntity()).setRightArmPose(new EulerAngle(Math.toRadians(90), Math.toRadians(0), Math.toRadians(180)));
        ((ArmorStand) stand.getBukkitEntity()).setVisible(false);
        ((ArmorStand) stand.getBukkitEntity()).setMarker(true);
        stand.setBasePlate(false);
        for (Entity b : stand.getBukkitEntity().getNearbyEntities(50, 50, 50)) {
            if (b instanceof Player) {
                ((CraftPlayer) b).getHandle().playerConnection.sendPacket(spawnP);
                ((CraftPlayer) b).getHandle().playerConnection.sendPacket(EquipP);
            }
        }
        List<EntityArmorStand> list;
        if (thrownAxes.containsKey(player)) {
            list = thrownAxes.get(player);
        } else {
            list = new ArrayList<>();
        }
        list.add(stand);
        thrownAxes.put(player, list);
        cooldownMap.put(stand, 11);
        counterMap.put(stand, 0);
        angleMap.put(stand, 0);
        vMap.put(stand, loc.toVector());
        final HashMap<Player, List<EntityArmorStand>> copyThrown = new HashMap<>(thrownAxes);
        if (prevRunnable == null) {
            prevRunnable = new BukkitRunnable() {

                @Override
                public void run() {
                    List<EntityArmorStand> toremove = new ArrayList<>();
                    for (EntityArmorStand entityArmorStand : copyThrown.get(player)) {
                        for (Entity entity : loc.getWorld().getNearbyEntities(entityArmorStand.getBukkitEntity().getLocation(), 2, 2, 2)) {
                            if (entity instanceof LivingEntity) {
                                if (!(entity instanceof Player) && !(entity instanceof ArmorStand)) {
                                    if (!entity.isDead()) {
                                        LivingEntity e = (LivingEntity) entity;
                                        double damage = baseDamage + (DamageUtil.calculateSingleHit(e, new SBPlayer(player)));
                                        if (cooldownMap.get(entityArmorStand) > 10) {
                                            DamageUtil.spawnMarker(e, player, damage, false);
                                            cooldownMap.put(entityArmorStand, 0);
                                            if (e instanceof EnderDragon) {
                                                if (StartFight.fightActive) {
                                                    DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());
                                                    dragonScoreboard.updateDragonDMG(player, (damage / 10) * multiplier);
                                                    LootListener.damage.put(player, StartFight.playerDMG.get(player));
                                                    e.damage((damage / 10) * multiplier);
                                                }
                                            } else {
                                                e.damage((damage / 10) * multiplier);
                                            }
                                            player.playSound(e.getLocation(), Sound.ORB_PICKUP, 10f, 1.5f);
                                        }
                                    }

                                }
                            }
                        }
                        if (((ArmorStand) entityArmorStand.getBukkitEntity()).getEyeLocation().distance(loc) <= 3
                                || (((ArmorStand) entityArmorStand.getBukkitEntity()).getEyeLocation().add(0, 1, 0).getBlock().getType() != Material.AIR
                                && !((ArmorStand) entityArmorStand.getBukkitEntity()).getEyeLocation().add(0, 1, 0).getBlock().isLiquid())
                                || counterMap.get(entityArmorStand) >= 130) {
                            for (Player b : Bukkit.getOnlinePlayers()) {
                                if (b.isValid()) {
                                    PacketPlayOutEntityDestroy removeP = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
                                    ((CraftPlayer) b).getHandle().playerConnection.sendPacket(removeP);
                                    ((ArmorStand) entityArmorStand.getBukkitEntity()).remove();
                                }
                            }
                            counterMap.put(entityArmorStand, 0);
                            multiplier = 0;
                            List<EntityArmorStand> list = thrownAxes.get(player);
                            list.remove(entityArmorStand);
                            thrownAxes.put(player, list);
                            if (thrownAxes.get(player).isEmpty()) {
                                prevRunnable = null;
                                this.cancel();
                            }

                        }
                        angleMap.put(entityArmorStand, angleMap.get(entityArmorStand) - 30);
                        ((ArmorStand) entityArmorStand.getBukkitEntity()).setRightArmPose(
                                new EulerAngle(Math.toRadians(angleMap.get(entityArmorStand)), Math.toRadians(0), Math.toRadians(180)));
                        entityArmorStand.getBukkitEntity().teleport(new Location(entityArmorStand.getWorld().getWorld(), entityArmorStand.getBukkitEntity().getLocation().getX(),
                                entityArmorStand.getBukkitEntity().getLocation().getY(), entityArmorStand.getBukkitEntity().getLocation().getZ(), entityArmorStand.getBukkitEntity().getLocation().getYaw(),
                                entityArmorStand.getBukkitEntity().getLocation().getPitch()).add(vMap.get(entityArmorStand).normalize()));
                        DataWatcher watcher = entityArmorStand.getDataWatcher();
                        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityArmorStand.getId(), watcher, true);
                        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(entityArmorStand);
                        for (Player b : Bukkit.getOnlinePlayers()) {
                            if (b != null) {
                                ((CraftPlayer) b).getHandle().playerConnection.sendPacket(metadata);
                                ((CraftPlayer) b).getHandle().playerConnection.sendPacket(teleport);
                            }
                        }
                        counterMap.put(entityArmorStand, counterMap.get(entityArmorStand) + 1);
                        cooldownMap.put(entityArmorStand, cooldownMap.get(entityArmorStand) + 1);
                    }
                }
            }.runTaskTimer(SBX.getInstance(), 1L, 1L);
        }
    }

    private boolean validEntity(Entity entity) {
        return entity instanceof LivingEntity && entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.VILLAGER && entity.getType() != EntityType.SLIME;
    }

}
