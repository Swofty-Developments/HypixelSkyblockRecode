package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SetAbilityDescriptionMenu extends PaginatedGUI {
    private final int indexx;

    public SetAbilityDescriptionMenu(SBPlayer owner, int indexx) {
        super(owner);
        this.indexx = indexx;
    }

    @Override
    public String getTitle() {
        return "Set ability description line";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public int getPageSize() {
        return 28;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 52:
                AnvilGUI anvilGUI = new AnvilGUI(p, event1 -> {
                    if(event1.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                        event1.setWillClose(true);
                        event1.setWillDestroy(true);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SBItemStack p1 = new SBItemStack(getOwner().getItemInHand());
                                ItemStack i = p1.addAbilityDescriptionLine(getOwner().getItemInHand(), SUtil.colorize(event1.getName()),indexx);
                                p1 = new SBItemStack(i);
                                i = p1.refreshLore();
                                getOwner().setItemInHand(i);
                                new SetAbilityDescriptionMenu(getOwner(),indexx).open();
                            }
                        }.runTaskLater(SBX.getInstance(), 1);
                    }
                });
                anvilGUI.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter Lore Line", 1, 0, "§a^^^^^","§7Enter a lore line in the","§7text box!"));
                anvilGUI.open();
                break;
            case 45:
                if(getGui().getPrevPageNum()==1&&getGui().getCurrentPageNum()==1) {
                    new AbilityCreatorGUI(getOwner(),indexx).open();
                }  else {
                    getGui().previous();
                }
                break;
            case 53:
                getGui().next();
                break;
            default:
                if(event.getCurrentItem().getType().equals(Material.WOOL)) {
                    if(event.getClick()== ClickType.LEFT) {
                        ItemStack i = event.getCurrentItem();
                        AnvilGUI anvilGUI2 = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event2) {
                                if(event2.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                                    event2.setWillClose(true);
                                    event2.setWillDestroy(true);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SBItemStack p1 = new SBItemStack(getOwner().getItemInHand());
                                            SBItemStack p2 = new SBItemStack(event.getCurrentItem());
                                            ItemStack i = p1.setAbilDescriptLine(SUtil.colorize(event2.getName()),indexx, p2.getInteger(p1.asBukkitItem(),"Line"));
                                            p1 = new SBItemStack(i);
                                            i = p1.refreshLore();
                                            getOwner().setItemInHand(i);
                                            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
                                        }
                                    }.runTaskLater(SBX.getInstance(), 1);
                                }
                            }
                        });
                        anvilGUI2.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter Lore Line", 1, 0, "§a^^^^^","§7Enter a lore line in the","§7text box!"));
                        anvilGUI2.open();
                        break;
                    } else {
                        if(event.getClick()==ClickType.MIDDLE) {
                            ItemStack i = event.getCurrentItem();

                            SBItemStack p1 = new SBItemStack(getOwner().getItemInHand());
                            i = p1.removeAbilityDescriptionLine(p1.asBukkitItem(),indexx, p1.getInteger(i,"Line"));
                            p1 = new SBItemStack(i);
                            i = p1.refreshLore();
                            getOwner().setItemInHand(i);
                            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
                            p.playSound(p.getLocation(), Sound.CAT_MEOW,1,2);
                            break;
                        }
                    }
                }
        }
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(SUtil.colorize("&7 "))).asGuiItem());
        
        SBItemStack i = new SBItemStack(getOwner().getItemInHand());
        List<String> lore = i.getAbilityDescription(getOwner().getItemInHand(),indexx);

        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53, next);
        setItem(52,makeColorfulItem(Material.BOOK_AND_QUILL,ChatColor.GREEN + "Add lore line",1,0,ChatColor.GRAY + "Click to add a lore line."));

        if(getGui().getCurrentPageNum() > 1) {
            setItem(45, prev);
        } else {
            setItem(45, prev);
        }

        ItemStack close = makeColorfulItem(Material.BARRIER, "§cClose", 1, 0);
        setItem(49, close);


        if(!lore.isEmpty()) {
            for(String s:lore) {
                ItemStack loreItem = new ItemStack(Material.WOOL,1,DyeColor.LIME.getData());
                ItemMeta meta = loreItem.getItemMeta();
                meta.setDisplayName(SUtil.colorize("&aLine #" + lore.indexOf(s)));
                String newLore = lore.get(lore.indexOf(s));
                List<String> finalList = new ArrayList<>();
                finalList.add(newLore);

                for(String b:SUtil.colorize("","&eLeft-Click to edit!","","&eRight-Click to edit in chat!","&7or if you don't have enough space","&7and want to add special &aSymbols&7.","","&bMiddle-Click to remove!")) {
                    finalList.add(b);
                }
                meta.setLore(finalList);
                loreItem.setItemMeta(meta);
                loreItem = NBTUtil.setString(loreItem, UUID.randomUUID().toString(),"UUID");
                loreItem = NBTUtil.setInteger(loreItem, lore.indexOf(s),"Line");
                getGui().addItem(ItemBuilder.from(loreItem).asGuiItem());
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }
}
