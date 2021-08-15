package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class ItemHeldEvent extends SkyblockListener<PlayerItemHeldEvent> {
    @EventHandler
    public void callEvent(PlayerItemHeldEvent event) {
        ItemStack swapto = event.getPlayer().getInventory().getItem(event.getNewSlot());
        ItemStack prev = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if(swapto!=null && swapto.hasItemMeta()) {
            SBItemStack i = new SBItemStack(swapto);
            ItemStack newItem = i.refreshLore();
            if (swapto.getItemMeta().getLore().equals(newItem.getItemMeta().getLore())) {
                event.getPlayer().getInventory().setItem(event.getNewSlot(),newItem);
            }

        }
    }
}
