package net.atlas.SkyblockSandbox.item.ability.itemAbilities;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityType;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class ShortBowTerm extends Ability {
    public static Integer cpsCap = 15;
    public static HashMap<Player, Boolean> canfire = new HashMap<>();

    @Override
    public String getAbilityName() {
        return "Shortbow: Instantly shoots!";
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.NONE;
    }

    @Override
    public double getManaCost() {
        return 0;
    }

    @Override
    public List<String> getAbilDescription() {
        return SUtil.colorize("&7Shoot &b3 &7arrows at once.","&7Can damage Enderman."," ","&cDivides your &9Crit Chance &cby 4!");
    }

    @Override
    public void leftClickAirAction(Player player, ItemStack item) {
        SBItemStack sbitem = new SBItemStack(item);
        for (int j = 1; j < sbitem.getAbilAmount()+1; j++) {
            SBItemStack sbItem = new SBItemStack(item);
            if (((String)sbItem.getAbilData(EnumAbilityData.NAME, j)).equalsIgnoreCase(getAbilityName())) {

                Location loc = player.getLocation();
                if (!canfire.containsKey(player)) {
                    canfire.put(player, true);
                }
                Arrow a1;
                Arrow a2;
                Arrow a3;
                if (canfire.get(player)) {
                    canfire.put(player, false);
                    a1 = player.launchProjectile(Arrow.class);
                    a1.setVelocity(a1.getVelocity().multiply(2.5));

                    a2 = player.launchProjectile(Arrow.class);

                    a2.setCustomName("terminator");
                    a2.setVelocity(rotateVector(a1.getVelocity(), 50.38));

                    a3 = player.launchProjectile(Arrow.class);

                    a3.setCustomName("terminator");
                    player.playSound(player.getLocation(), Sound.SHOOT_ARROW,1,1);
                    a3.setVelocity(rotateVector(a1.getVelocity(), -50.38));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (a1.isValid()) {
                                a1.remove();
                            }
                            if (a2.isValid()) {
                                a2.remove();
                            }
                            if (a3.isValid()) {
                                a3.remove();
                            }
                        }
                    }.runTaskLater(SBX.getInstance(), 300);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            canfire.put(player, true);
                        }
                    }.runTaskLater(SBX.getInstance(), 60 / 13);

                }
            }
        }
    }

    public Vector rotateVector(Vector vector, double whatAngle) {
        double cos = Math.cos(whatAngle);
        double sin = Math.sin(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }

    @Override
    public void leftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }

    @Override
    public void rightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }

    @Override
    public void rightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }

    @Override
    public void shiftLeftClickAirAction(Player p, ItemStack item) {
        leftClickAirAction(p, item);
    }

    @Override
    public void shiftLeftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }

    @Override
    public void shiftRightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }

    @Override
    public void shiftRightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }

    @Override
    public void hitEntityAction(Player p, EntityDamageByEntityEvent event, Entity paramEntity, ItemStack item) {
        leftClickAirAction(p, item);
    }

    @Override
    public void breakBlockAction(Player p, BlockBreakEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void clickedInInventoryAction(Player p, InventoryClickEvent event) {

    }

    @Override
    public void shiftEvent(Player p, PlayerToggleSneakEvent e) {

    }

    @Override
    public void rightClickEntityEvent(Player p, PlayerInteractAtEntityEvent e) {

    }

    @Override
    public void playerFishAction(Player p, PlayerFishEvent event, ItemStack item) {

    }

    @Override
    public void playerShootAction(Player p, EntityShootBowEvent event, ItemStack item) {
        leftClickAirAction(p, item);
        event.setCancelled(true);
    }
}
