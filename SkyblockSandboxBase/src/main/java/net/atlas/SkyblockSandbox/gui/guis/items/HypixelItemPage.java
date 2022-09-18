package net.atlas.SkyblockSandbox.gui.guis.items;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.EnchantGUI;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class HypixelItemPage extends PaginatedGUI {
    public static final HashMap<UUID,String> search = new HashMap<>();
    List<ItemType> types = Arrays.asList(ItemType.CUSTOM, ItemType.SWORD, ItemType.ARMOR, ItemType.BOW, ItemType.ITEM, ItemType.ACCESSORY, ItemType.AXE, ItemType.PICKAXE, ItemType.HOE);
    public static final HashMap<UUID, ItemType> selectedType = new HashMap<>();

    public HypixelItemPage(SBPlayer owner) {
        super(owner);
        selectedType.putIfAbsent(getOwner().getUniqueId(), ItemType.CUSTOM);
    }

    @Override
    public int getPageSize() {
        return 28;
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        SBPlayer p = new SBPlayer((Player) event.getWhoClicked());
        if(event.getClickedInventory().equals(p.getInventory())) return;
        switch (event.getSlot()) {
            case 45:
                if(getGui().getPrevPageNum()==1&&getGui().getCurrentPageNum()==1) {
                    p.sendMessage("&cYou are on the last page");
                }  else {
                    getGui().previous();
                }
                break;
            case 49: {
                new ItemMainPage(p).open();
                break;
            }
            case 50: {
                if(event.getClick().equals(ClickType.RIGHT)) {
                    if (search.containsKey(p.getUniqueId())) {
                        search.remove(p.getUniqueId());
                        p.playSound(p.getLocation(), Sound.CAT_MEOW, 1f, 1.5f);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new HypixelItemPage(p).open();
                            }
                        }.runTaskLater(SBX.getInstance(), 3);
                        return;
                    }
                    event.setCancelled(true);
                    return;
                }

                new AnvilGUI(p.getPlayer(), e -> {
                    if(e.getName() == null || e.getName() == "") {
                        p.sendMessage("&cInvalid search!");
                        return;
                    }

                    search.put(p.getUniqueId(), e.getName());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new HypixelItemPage(p).open();
                        }
                    }.runTaskLater(SBX.getInstance(), 6);
                }).setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter search", 1, 0, colorize("&6^^^^^^^\n&3Your Search"))).open();


                break;
            }
            case 51: {
                if (event.isRightClick()) {
                    if (types.indexOf(selectedType.get(getOwner().getUniqueId())) == 0) {
                        selectedType.put(getOwner().getUniqueId(), ItemType.HOE);
                    } else {
                        selectedType.put(getOwner().getUniqueId(), types.get(types.indexOf(selectedType.get(getOwner().getUniqueId())) - 1));
                    }
                } else {
                    if (types.indexOf(selectedType.get(getOwner().getUniqueId())) == types.size() - 1) {
                        selectedType.put(getOwner().getUniqueId(), ItemType.CUSTOM);
                    } else {
                        selectedType.put(getOwner().getUniqueId(), types.get(types.indexOf(selectedType.get(getOwner().getUniqueId())) + 1));
                    }
                }
                new HypixelItemPage(getOwner()).open();
                break;
            }
            case 53:
                if(getGui().getNextPageNum()==getGui().getPagesNum()&&getGui().getCurrentPageNum()==getGui().getPagesNum()) {
                    p.sendMessage("&cYou are on the last page");
                }  else {
                    getGui().next();
                }
                break;
            default:
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(colorize("&7 "))) return;
                if (p.hasSpace()) {
                    ItemStack item1 = event.getCurrentItem();
                    SBItemBuilder item = new SBItemBuilder(item1.clone());
                    item.sign(null);
                    p.getInventory().addItem(item.build());
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                }
                break;
        }
    }

    @Override
    public String getTitle() {
        return "Hypixel items";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(colorize("&7 "))).asGuiItem());

        setItem(49, makeColorfulItem(Material.ARROW, "&aGo back", 1, 0, "&7Go back to the main page."));

        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53, next);

        ItemStack searchItem = makeColorfulItem(Material.ANVIL, "§aSearch Items", 1, 0,
                "§7Search through all Items.\n\n§eClick to search!");

        ItemStack searchItemsReset = makeColorfulItem(Material.ANVIL, "§aSearch Items", 1, 0,
                colorize("&7Search through all Items.\n&7Filtered: &6" + search.get(getOwner().getUniqueId()) + "\n\n&eClick to search!\n&bRight-Click to reset!"));

        if (search.containsKey(getOwner().getUniqueId())) {
            setItem(50, searchItemsReset);
        } else {
            setItem(50, searchItem);

        }
        setItem(45, prev);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("&7Filter through item categories");
        lore.add("");
        for (ItemType type : types) {
            if (selectedType.get(getOwner().getUniqueId()) == type) {
                lore.add("&6► " + type.getValue());
            } else {
                lore.add("&7" + type.getValue());
            }
        }
        lore.add("");
        lore.add("&bRight-Click to go backwards!");
        lore.add("&eClick to switch categories!");
        setItem(51, makeColorfulItem(Material.GOLD_BLOCK, "&bItem Categories", 1, 0, lore));
        for(SBItemBuilder item : items(getOwner())) {
            getGui().addItem(ItemBuilder.from(item.build()).asGuiItem());
        }
    }

    private ArrayList<SBItemBuilder> items(SBPlayer player) {
        ArrayList<SBItemBuilder> items = new ArrayList<>();
        for (SBItemBuilder item : HypixelItemsHelper.hypixelItems) {
            if (selectedType.get(player.getUniqueId()) == ItemType.CUSTOM) {
                items.add(item);
            }
            if (selectedType.get(player.getUniqueId()) == ItemType.ARMOR) {
                for (ItemType type : Arrays.asList(ItemType.BOOTS, ItemType.LEGGINGS, ItemType.CHESTPLATE, ItemType.HELMET)) {
                    if (item.type == type) {
                        items.add(item);
                    }
                }
            }
            if (selectedType.get(player.getUniqueId()) == item.type) {
                items.add(item);
            }
        }
        if (search.containsKey(player.getUniqueId())) {
            ArrayList<SBItemBuilder> searchItems = new ArrayList<>();
            for (SBItemBuilder item : items) {
                if (item.name.contains(search.get(player.getUniqueId()).toUpperCase())) {
                    searchItems.add(item);
                }
            }
            return searchItems;
        }
        return items;
    }
}
