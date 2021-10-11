package net.atlas.SkyblockSandbox.abilityCreator.functions;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionsEditorGUI;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.util.DamageUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static net.atlas.SkyblockSandbox.SBX.*;
import static net.atlas.SkyblockSandbox.SBX.cooldownMap;

public class Projectile extends Function {
    int multiplier = 0;

    public Projectile(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        Player player = getPlayer().getPlayer();
        if (canfire.containsKey(player)) {
            if (canfire.get(player)) {
                if (thrownAxes.containsKey(player)) {
                    if (thrownAxes.get(player).size() > 25) {
                        player.sendMessage(ChatColor.RED + "Projectile limit reached.");
                    } else {
                        throwProjOptimized();
                    }
                    canfire.put(player, false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            canfire.put(player, true);
                        }
                    }.runTaskLater(SBX.getInstance(), 6);

                } else {
                    throwProjOptimized();
                }
            }
        } else {
            throwProjOptimized();
            canfire.put(player, true);
        }
    }


    void throwProjOptimized() {
        Player player = getPlayer().getPlayer();
        Material mat = Enums.getIfPresent(Material.class, FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PROJECTILE_MATERIAL)).or(Material.DIAMOND_AXE);
        if (player.hasMetadata("AOTS")) {
            multiplier = player.getMetadata("AOTS").get(0).asInt();
            if (player.getMetadata("AOTS").get(0).asInt() != 16) {
                player.setMetadata("AOTS",
                        new FixedMetadataValue(SBX.getInstance(), player.getMetadata("AOTS").get(0).asInt() + 2));
            }
        } else {
            player.setMetadata("AOTS", new FixedMetadataValue(SBX.getInstance(), 2));
        }
        if (multiplier > 16) {
            multiplier = 16;
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
        if (prevRunnable == null) {
            prevRunnable = new BukkitRunnable() {

                @Override
                public void run() {
                    ArrayList<EntityArmorStand> stands = new ArrayList<>(list);
                    for (EntityArmorStand entityArmorStand : stands) {
                        for (Entity entity : loc.getWorld().getNearbyEntities(entityArmorStand.getBukkitEntity().getLocation(), 2, 2, 2)) {
                            if (entity instanceof LivingEntity) {
                                if (!entity.isDead()) {
                                    if (validEntity(entity)) {
                                        LivingEntity e = (LivingEntity) entity;
                                        double damage = 10000 + ((DamageUtil.calculateSingleHit(e, new SBPlayer(player), false) * 0.10));
                                        //if (cooldownMap.get(entityArmorStand) > 10) {
                                        DamageUtil.spawnMarker(e, new SBPlayer(player), damage, false);
                                        //cooldownMap.put(entityArmorStand, 0);
                                        if (e instanceof EnderDragon) {
                                            if (StartFight.fightActive) {
                                                DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());
                                                dragonScoreboard.updateDragonDMG(player, damage);
                                                LootListener.damage.put(player, StartFight.playerDMG.get(player));
                                            }
                                        }
                                        e.damage((damage));
                                        player.playSound(e.getLocation(), Sound.ORB_PICKUP, 10f, 1.5f);
                                        //}
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

    public enum dataValues implements Function.dataValues {
        PROJECTILE_MATERIAL;
    }

    @Override
    public AbilityValue.FunctionType getFunctionType() {
        return AbilityValue.FunctionType.PROJECTILE;
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return Arrays.asList(Head.class);
    }

    @Override
    public void getGuiLayout() {
        Material mat = Enums.getIfPresent(Material.class, FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PROJECTILE_MATERIAL)).or(Material.DIAMOND_AXE);
        ItemStack matItem = StackUtils.makeColorfulItem(mat, "&aSelected Material: &e" + SUtil.firstLetterUpper(mat.name()), 1, 0, "&7Click an item in your", "&7inventory to set the projectile type!", "", "&eClick any item to set!");
        setItem(CENTER_SLOT, matItem);
        setDefaultClickAction(event -> {
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.SKULL_ITEM || event.getCurrentItem().getType() == Material.SKULL) {
                return;
            }
            if (event.getRawSlot() > getPlayer().getInventory().getSize()) {
                return;
            }
            getPlayer().setItemInHand(FunctionUtil.setFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PROJECTILE_MATERIAL, event.getCurrentItem().getType().name()));
            updateItems();

        });
    }

    public boolean validEntity(Entity en) {
        return !en.hasMetadata("entity-tag") && !(en instanceof ArmorStand) && !(en instanceof Player);
    }
}
