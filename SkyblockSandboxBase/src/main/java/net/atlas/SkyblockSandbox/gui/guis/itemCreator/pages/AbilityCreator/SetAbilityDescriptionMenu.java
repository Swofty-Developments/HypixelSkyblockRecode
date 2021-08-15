package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreator;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreatorPage;
import net.maploop.items.Items;
import net.maploop.items.command.commands.itemCreator.Command_CreateItem;
import net.maploop.items.gui.AnvilGUI;
import net.maploop.items.gui.PaginatedGUI;
import net.maploop.items.gui.PlayerMenuUtility;
import net.maploop.items.util.IUtil;
import net.maploop.items.util.NBTUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SetAbilityDescriptionMenu extends PaginatedGUI {
    private final int indexx;

    public SetAbilityDescriptionMenu(PlayerMenuUtility playerMenuUtility,int indexx) {
        super(playerMenuUtility);
        this.indexx = indexx;
    }

    @Override
    public String getTitle() {
        return "Set item lore";
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public int getMaxItemsPerPage() {
        return 28;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 52:
                AnvilGUI anvilGUI = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                    @Override
                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                        if(event.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                            event.setWillClose(true);
                            event.setWillDestroy(true);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Command_CreateItem.storedItems.put(playerMenuUtility.getOwner().getUniqueId(),NBTUtil.addAbilityDescriptionLine(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()),event.getName(),indexx));
                                    Command_CreateItem.storedItems.put(playerMenuUtility.getOwner().getUniqueId(),NBTUtil.refreshLore(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId())));
                                    p.setItemInHand(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()));
                                    new SetAbilityDescriptionMenu(playerMenuUtility,indexx).open();
                                }
                            }.runTaskLater(Items.getInstance(), 1);
                        }
                    }
                });
                anvilGUI.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeItem(Material.PAPER, "Enter Lore Line", 1, 0, "§a^^^^^\n§7Enter a lore line in the\n§7text box!"));
                anvilGUI.open();
                break;
            case 45:
                if(page==0) {
                    new ItemCreator(owner, ItemCreatorPage.ABILITY_DESCRIPTION_PICKER).open();
                }
            default:
                if(event.getCurrentItem().getType().equals(Material.WOOL)) {
                    if(event.getClick()== ClickType.LEFT) {
                        ItemStack i = event.getCurrentItem();
                        AnvilGUI anvilGUI2 = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if(event.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                                    event.setWillClose(true);
                                    event.setWillDestroy(true);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            Command_CreateItem.storedItems.put(playerMenuUtility.getOwner().getUniqueId(),NBTUtil.setAbilityDescriptionLine(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()),event.getName(),indexx,NBTUtil.getInteger(i,"Line")));
                                            Command_CreateItem.storedItems.put(playerMenuUtility.getOwner().getUniqueId(),NBTUtil.refreshLore(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId())));
                                            p.setItemInHand(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()));
                                            new SetAbilityDescriptionMenu(playerMenuUtility,indexx).open();
                                        }
                                    }.runTaskLater(Items.getInstance(), 1);
                                }
                            }
                        });
                        anvilGUI2.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeItem(Material.PAPER, "Enter Lore Line", 1, 0, "§a^^^^^\n§7Enter a lore line in the\n§7text box!"));
                        anvilGUI2.open();
                        break;
                    } else {
                        if(event.getClick()==ClickType.MIDDLE) {
                            ItemStack i = event.getCurrentItem();

                            Command_CreateItem.storedItems.put(playerMenuUtility.getOwner().getUniqueId(),NBTUtil.removeAbilityDescriptionLine(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()),indexx,NBTUtil.getInteger(i,"Line")));
                            Command_CreateItem.storedItems.put(playerMenuUtility.getOwner().getUniqueId(),NBTUtil.refreshLore(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId())));
                            p.setItemInHand(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()));
                            new SetAbilityDescriptionMenu(playerMenuUtility,indexx).open();
                            break;
                        }
                    }
                }
        }
    }

    @Override
    public void setItems() {
        addMenuBorder();

        List<String> lore = NBTUtil.getAbilityDescription(Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()),indexx);

        ItemStack next = makeItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        inventory.setItem(53, next);
        inventory.setItem(52,makeItem(Material.BOOK_AND_QUILL,ChatColor.GREEN + "Add lore line",1,0,ChatColor.GRAY + "Click to add a lore line."));
        inventory.setItem(4,Command_CreateItem.storedItems.get(playerMenuUtility.getOwner().getUniqueId()));

        if(page > 0) {
            inventory.setItem(45, prev);
        } else {
            inventory.setItem(45, prev);
        }
        for(int i = 0;i<lore.size()/maxItemsPerPage;i++) {
            if(!((index + 1) >= lore.size())) {
                page = page + 1;
                super.open();
            } else {
                break;
            }
        }



        ItemStack close = makeItem(Material.BARRIER, "§cClose", 1, 0);
        inventory.setItem(49, close);


        if(!lore.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if(index >= lore.size()) break;
                ItemStack loreItem = new ItemStack(Material.WOOL,1,DyeColor.LIME.getData());
                ItemMeta meta = loreItem.getItemMeta();
                meta.setDisplayName(IUtil.colorize("&aLine #" + index));
                meta.setLore(Arrays.asList(lore.get(index), IUtil.colorize("\n&eLeft-Click to edit!\n\n&eRight-Click to edit in chat!\n&7or if you don't have enough space\n&7and want to add special &aSymbols&7.\n\n&bMiddle-Click to remove!")));
                loreItem.setItemMeta(meta);
                loreItem = NBTUtil.setString(loreItem, UUID.randomUUID().toString(),"UUID");
                loreItem = NBTUtil.setInteger(loreItem, index,"Line");
                inventory.addItem(loreItem);
            }
        }
    }
}
