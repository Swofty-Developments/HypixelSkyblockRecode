package net.atlas.SkyblockSandbox.item.ability.itemAbilities;


import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityType;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.sound.Jingle;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static net.atlas.SkyblockSandbox.SBX.isSoulCryActive;
import static net.atlas.SkyblockSandbox.SBX.storedItem;

public class SoulCry extends Ability {

    @Override
    public String getAbilityName() {
        return "Soulcry";
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public double getManaCost() {
        return 200;
    }

    @Override
    public List<String> getAbilDescription() {
        return SUtil.colorize("&7Gain &c+400 Ferocity &7against", "&7Endermen for &a4s", "&8Soulflow Cost: &g1");
    }

    @Override
    public void leftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void leftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void rightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {
        SBPlayer pl = new SBPlayer(p);
        SBItemStack sbitem = new SBItemStack(item);
        if(item!=null && item.hasItemMeta()) {
            if (sbitem.hasAbility()) {
                for (int i = 0; i <= sbitem.getAbilAmount(); i++) {
                    if (((String) sbitem.getAbilData(EnumAbilityData.NAME, i)).equalsIgnoreCase(getAbilityName())) {
                        if (isSoulCryActive.containsKey(pl.getUniqueId())) {
                            if (!isSoulCryActive.get(pl.getUniqueId())) {
                                double oldfero = pl.getMaxStat(SBPlayer.PlayerStat.FEROCITY);
                                pl.playJingle(Jingle.SOULCRY,false);
                                pl.setMaxStat(SBPlayer.PlayerStat.FEROCITY, pl.getMaxStat(SBPlayer.PlayerStat.FEROCITY) + 400);
                                isSoulCryActive.put(pl.getUniqueId(), true);
                                if (sbitem.getString(item, "UUID").equals("")) {
                                    item = sbitem.setString(item, UUID.randomUUID().toString(), "UUID");
                                }
                                String uuid = sbitem.getString(item, "UUID");
                                storedItem.put(uuid, item);
                                item.setType(Material.GOLD_SWORD);
                                p.getItemInHand().setType(Material.GOLD_SWORD);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        pl.playJingle(Jingle.SOULCRY,false);
                                        pl.setMaxStat(SBPlayer.PlayerStat.FEROCITY, oldfero);
                                        isSoulCryActive.put(pl.getUniqueId(), false);
                                        for (int c = 0; c < p.getInventory().getContents().length; c++) {
                                            ItemStack i = p.getInventory().getItem(c);
                                            if (i != null) {
                                                SBItemStack it = new SBItemStack(i);
                                                if (it.getString(i, "UUID").equals(uuid)) {
                                                    p.getInventory().getItem(c).setType(Material.DIAMOND_SWORD);
                                                    System.out.println(c);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }.runTaskLater(SBX.getInstance(), 20 * 4);
                            }
                        } else {
                            isSoulCryActive.put(pl.getUniqueId(), false);
                            ItemStack finalItem = item;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    rightClickAirAction(p, event, finalItem);
                                }
                            }.runTaskLater(SBX.getInstance(), 1L);

                        }
                    }
                }
            }
        }
    }


    @Override
    public void rightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        rightClickAirAction(p, event, item);
    }

    @Override
    public void shiftLeftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void shiftLeftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void shiftRightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {
        rightClickAirAction(p, event, item);
    }

    @Override
    public void shiftRightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        rightClickAirAction(p, event, item);
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
        PlayerInteractEvent event = new PlayerInteractEvent(p, Action.RIGHT_CLICK_AIR, p.getItemInHand(), p.getTargetBlock((Set<Material>) null, 1), BlockFace.DOWN);
        rightClickAirAction(p, event, p.getItemInHand());
    }

    @Override
    public void playerFishAction(Player p, PlayerFishEvent event, ItemStack item) {

    }

    @Override
    public void playerShootAction(Player p, EntityShootBowEvent event, ItemStack item) {

    }
}
