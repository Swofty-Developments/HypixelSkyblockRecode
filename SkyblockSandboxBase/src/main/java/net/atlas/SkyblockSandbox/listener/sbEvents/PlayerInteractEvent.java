package net.atlas.SkyblockSandbox.listener.sbEvents;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityHandler;
import net.atlas.SkyblockSandbox.gui.guis.backpacks.Backpack;
import net.atlas.SkyblockSandbox.gui.guis.skyblockmenu.SBMenu;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.SummonListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.SummoningAltaar;
import net.atlas.SkyblockSandbox.item.BackpackSize;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.Serialization;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import static net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight.spawnLoc;

public class PlayerInteractEvent extends SkyblockListener<org.bukkit.event.player.PlayerInteractEvent> {
    AbilityHandler handler = new AbilityHandler();
    @EventHandler
    public void callEvent(org.bukkit.event.player.PlayerInteractEvent event) {
        handleAbils(event);
        Player p = event.getPlayer();
        if (p.getItemInHand() != null && p.getItemInHand().hasItemMeta()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                runPetLogic(event);
                runBPLogic(event);
                sbmenuLogic(event);
                SBItemStack item = new SBItemStack(event.getItem());
                if (item.getString(event.getItem(), "ID") != null) {
                    if (item.getString(event.getItem(), "ID").equals(SkyblockItem.Default.SUMMONING_EYE.item().getItemID())) {
                        for (SummoningAltaar value : SummoningAltaar.values()) {
                            value.setWorld(event.getPlayer().getWorld());
                            spawnLoc = new Location(event.getPlayer().getWorld(),spawnLoc.getX(),spawnLoc.getY(),spawnLoc.getZ());
                            if (value.getLoc().equals(event.getClickedBlock().getLocation())) {
                                SummonListener.placeEye(value, event.getPlayer());
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                runPetLogic(event);
                runBPLogic(event);
                sbmenuLogic(event);
            }
        }
    }

    void sbmenuLogic(org.bukkit.event.player.PlayerInteractEvent e) {
        if(NBTUtil.getString(e.getItem(),"ID").equals("SKYBLOCK_MENU")) {
            new SBMenu(new SBPlayer(e.getPlayer())).open();
        }
    }

    void handleAbils(org.bukkit.event.player.PlayerInteractEvent event) {
        handler.callEvent(event);
    }

    void runPetLogic(org.bukkit.event.player.PlayerInteractEvent e) {
        if(Boolean.parseBoolean(NBTUtil.getString(e.getItem(),"is-pet"))) {
            Player p = e.getPlayer();
            ItemStack it = NBTUtil.setString(e.getItem(), "true", "is-equipped");
            it = NBTUtil.removeTag(it,"mf-gui");
            SBItemStack itnew = new SBItemStack(it);
            it = itnew.refreshLore();
            String serialized = Serialization.itemStackToBase64(it);
            SBX.getMongoStats().setData(e.getPlayer().getUniqueId(), "pet_" + serialized, serialized);

            p.sendMessage(SUtil.colorize("&aAdded " + e.getItem().getItemMeta().getDisplayName() + "&a to your pets menu!"));
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5, 1);
            e.setCancelled(true);
            p.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }
    }

    void runBPLogic(org.bukkit.event.player.PlayerInteractEvent e) {
        if(ChatColor.stripColor(NBTUtil.getString(e.getItem(),"ID")).toLowerCase().contains("backpack")) {
            String name = ChatColor.stripColor(NBTUtil.getString(e.getItem(),"ID"));
            String size = name.split("_")[0].toUpperCase();
            BackpackSize backpackSize = Enums.getIfPresent(BackpackSize.class,size).orNull();
            if(backpackSize!=null) {
                new Backpack(new SBPlayer(e.getPlayer()),backpackSize,e.getItem()).open();
            }
        }
    }
}
