package net.atlas.SkyblockSandbox.item.ability;

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
import java.util.function.Consumer;

public abstract class Ability {

    public abstract String getAbilityName();

    public abstract AbilityType getAbilityType();

    public abstract double getManaCost();

    public abstract List<String> getAbilDescription();

    public abstract void leftClickAirAction(Player p, ItemStack item);

    public abstract void leftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item);

    public abstract void rightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item);

    public abstract void rightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item);

    public abstract void shiftLeftClickAirAction(Player p, ItemStack item);

    public abstract void shiftLeftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item);

    public abstract void shiftRightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item);

    public abstract void shiftRightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item);

    public abstract void hitEntityAction(Player p, EntityDamageByEntityEvent event, Entity paramEntity, ItemStack item);

    public abstract void breakBlockAction(Player p, BlockBreakEvent event, Block paramBlock, ItemStack item);

    public abstract void clickedInInventoryAction(Player p, InventoryClickEvent event);

    public abstract void shiftEvent(Player p, PlayerToggleSneakEvent e);

    public abstract void rightClickEntityEvent(Player p,PlayerInteractAtEntityEvent e);

    public abstract void playerFishAction(Player p, PlayerFishEvent event, ItemStack item);

    public abstract void playerShootAction(Player p, EntityShootBowEvent event, ItemStack item);
}
