package net.atlas.SkyblockSandbox.gui.guis;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
            if(event1.getClick().equals(ClickType.NUMBER_KEY)) {
                event1.setCancelled(true);
            }
            String name = NBTUtil.getString(event.getCurrentItem(),"ID");
            event1.setCancelled(name.toLowerCase().contains("backpack"));
            if(NBTUtil.getAllSignatures(event.getCurrentItem()).size()!=0) {
                boolean flag = false;
                for(String ss:NBTUtil.getAllSignatures(event.getCurrentItem())) {
                    String pString = NBTUtil.getSignature(event.getCurrentItem(),ss);
                    if(Bukkit.getPlayer(pString)!=null) {
                        Player p = Bukkit.getPlayer(pString);
                        if(p.isValid()&&p.getUniqueId()!=event.getWhoClicked().getUniqueId()) {
                            flag = true;
                        }
                    }
                }
                if(flag) {
                    event1.setCancelled(true);
                    event.getWhoClicked().sendMessage(SUtil.colorize("&cYou cannot dupe this item as it is signed by someone else!"));
                    ((Player)event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ENDERMAN_TELEPORT,1,0);
                }
            }
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
