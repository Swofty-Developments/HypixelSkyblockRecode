package net.atlas.SkyblockSandbox.item.ability.itemAbilities;

import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityType;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AOTSThrow extends Ability {
    @Override
    public String getAbilityName() {
        return "Throw";
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public double getManaCost() {
        return 20;
    }

    @Override
    public List<String> getAbilDescription() {
        return SUtil.colorize("&7Throw your axe damaging all","&7enemies in its path dealing","&c10% &7melee damage.","&7Consecutive throws stack &c2x","&7damage but cost &92x &7mana up","&7to 16x");
    }

    @Override
    public void leftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void leftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void rightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {

    }

    @Override
    public void rightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        rightClickAirAction(p,event,item);
    }

    @Override
    public void shiftLeftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void shiftLeftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void shiftRightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {
        rightClickAirAction(p,event,item);
    }

    @Override
    public void shiftRightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        rightClickAirAction(p,event,item);
    }

    @Override
    public void hitEntityAction(Player p, EntityDamageByEntityEvent event, Entity paramEntity, ItemStack item) {

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

    }
}
