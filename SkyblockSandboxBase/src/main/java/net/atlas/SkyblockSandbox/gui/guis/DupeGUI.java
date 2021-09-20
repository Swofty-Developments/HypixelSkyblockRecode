package net.atlas.SkyblockSandbox.gui.guis;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DupeGUI extends NormalGUI {

    private BukkitTask prevRunnable;
    public DupeGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        getGui().setDefaultClickAction(event1 -> {
            String name = NBTUtil.getString(event.getCurrentItem(),"ID");
            event1.setCancelled(name.toLowerCase().contains("backpack"));
        });
        getGui().setCloseGuiAction(inventoryCloseEvent -> {
            prevRunnable.cancel();
            for(ItemStack i:inventoryCloseEvent.getInventory().getContents()) {
                if(i!=null&&i.getType()!=Material.AIR) {
                    getOwner().getInventory().addItem(i);
                }
            }
        });
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Dupe GUI";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void setItems() {
        prevRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(ItemStack i:getGui().getInventory().getContents()) {
                    if(i!=null&&i.getType()!=Material.AIR) {
                        if (i.getAmount() != 64) {
                            i.setAmount(i.getAmount() + 1);
                        }
                    }
                }
            }
        }.runTaskTimer(SBX.getInstance(),20L,40L);
    }
}
