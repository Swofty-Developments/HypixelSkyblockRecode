package net.atlas.SkyblockSandbox.gui.guis.backpacks;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class BackpackHandler implements Listener {
    private ItemStack backpackItem;
    private Backpack backpack;

    public BackpackHandler(Backpack backpack,ItemStack backpackItem) {
        this.backpackItem = backpackItem;
        this.backpack = backpack;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().toLowerCase().contains("backpack")) {
            backpack.closeInv(event);
        }
    }
}
