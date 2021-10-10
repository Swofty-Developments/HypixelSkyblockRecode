package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.DamageUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Head extends Function {
    public Head(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        Player player = getPlayer().getPlayer();
        int kickbackVal = 3;
        String texture = FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.TEXTURE);
        boolean kickback = Boolean.parseBoolean(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.DO_KICKBACK));
        Location loc = getPlayer().getLocation();
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName("HeadShooterStand");
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
                            player.setVelocity(player.getLocation().getDirection().setY(direction.getY() + kickbackVal).setX(-direction.getX() - kickbackVal).setZ(-direction.getZ() - kickbackVal));
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
                            entity.damage(DamageUtil.calculateSingleHit(e2,p));
                            this.cancel();
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeP);
                        }
                    }
                }

                if (loc.distance(location) >= 30) {
                    this.cancel();
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeP);
                }
            }
        }.runTaskTimer(SBX.getInstance(), 0L, 1L);

    }

    public boolean validEntity(Entity en) {
        return !en.hasMetadata("entity-tag") && !(en instanceof ArmorStand) && !(en instanceof Player);
    }

    @Override
    public AbilityValue.FunctionType getFunctionType() {
        return AbilityValue.FunctionType.HEAD;
    }

    public enum dataValues implements Function.dataValues {
        TEXTURE, DO_KICKBACK,
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return null;
    }

    @Override
    public void getGuiLayout() {

    }
}
