package net.atlas.SkyblockSandbox.listener.sbEvents;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.guis.backpacks.Backpack;
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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEvent extends SkyblockListener<org.bukkit.event.player.PlayerInteractEvent> {
    @EventHandler
    public void callEvent(org.bukkit.event.player.PlayerInteractEvent event) {

        Player p = event.getPlayer();
        if (p.getItemInHand() != null && p.getItemInHand().hasItemMeta()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                runPetLogic(event);
                runBPLogic(event);
                SBItemStack item = new SBItemStack(event.getItem());
                if (item.getString(event.getItem(), "ID") != null) {
                    if (item.getString(event.getItem(), "ID").equals(SkyblockItem.Default.SUMMONING_EYE.item().getItemID())) {
                        for (SummoningAltaar value : SummoningAltaar.values()) {
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
            }
        }
    }

    void runPetLogic(org.bukkit.event.player.PlayerInteractEvent e) {
        if(Boolean.parseBoolean(NBTUtil.getString(e.getItem(),"is-pet"))) {
            Player p = e.getPlayer();
            ItemStack it = NBTUtil.setString(e.getItem(), "true", "is-equipped");
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
