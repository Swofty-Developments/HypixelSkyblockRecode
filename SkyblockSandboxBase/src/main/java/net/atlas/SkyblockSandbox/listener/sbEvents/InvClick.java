package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public class InvClick extends SkyblockListener<InventoryClickEvent> {
    @EventHandler
    public void callEvent(InventoryClickEvent event) {
        if(event.getClickedInventory() instanceof PlayerInventory) {
            if (event.getSlot() == 8) {
                event.setCancelled(true);
            }
        }
    }
}
