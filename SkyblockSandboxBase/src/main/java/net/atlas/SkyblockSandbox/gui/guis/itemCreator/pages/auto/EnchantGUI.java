package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.gui.guis.items.CreativeItemPage;
import net.atlas.SkyblockSandbox.gui.guis.items.ItemMainPage;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.atlas.SkyblockSandbox.util.signGUI.SignGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EnchantGUI extends PaginatedGUI implements Backable {
    public static final HashMap<UUID,String> search = new HashMap<>();
    public static final HashMap<UUID, ItemType> selectedType = new HashMap<>();
    List<ItemType> types = Arrays.asList(ItemType.CUSTOM, ItemType.SWORD, ItemType.ARMOR, ItemType.BOW, ItemType.ITEM, ItemType.ACCESSORY, ItemType.AXE, ItemType.PICKAXE, ItemType.HOE);

    public EnchantGUI(SBPlayer owner) {
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
            case 50: {
                if(event.getClick().equals(ClickType.RIGHT)) {
                    if (search.containsKey(p.getUniqueId())) {
                        search.remove(p.getUniqueId());
                        p.playSound(p.getLocation(), Sound.CAT_MEOW, 1f, 1.5f);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new EnchantGUI(p).open();
                            }
                        }.runTaskLater(SBX.getInstance(), 3);
                        return;
                    }
                    event.setCancelled(true);
                    return;
                }

                new SignGUI(SBX.getInstance().signManager, event1 -> {
                    String input = event1.getLines()[0];
                    if (!input.equals("")) {
                        search.put(p.getUniqueId(), input);
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {
                            new EnchantGUI(p).open();
                        }, 3);
                    } else {
                        p.sendMessage("&cThat is not a valid input");
                        new EnchantGUI(p).open();
                    }
                }).withLines("", "^^^^^^^^^^^^^^^", "Your search", "").open(getOwner().getPlayer());
                break;
            }
            case 53:
                if(getGui().getNextPageNum()==getGui().getPagesNum()&&getGui().getCurrentPageNum()==getGui().getPagesNum()) {
                    p.sendMessage("&cYou are on the last page");
                }  else {
                    getGui().next();
                }
                break;
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
                new EnchantGUI(getOwner()).open();
                break;
            }
            default:
                if (!(NBTUtil.getGenericString(event.getCurrentItem(), "type").equals("") || NBTUtil.getGenericString(event.getCurrentItem(), "type") == null)) {
                    Enchantment enchant = Enchantment.valueOf(NBTUtil.getGenericString(event.getCurrentItem(), "type"));
                    new AnvilGUI(p.getPlayer(), event1 -> {
                        if (event1.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                            String input = event1.getName();
                            SBItemBuilder item = new SBItemBuilder(p.getItemInHand());
                            if (NumUtils.isInt(input)) {
                                int lvl = Integer.parseInt(input);
                                if (lvl > enchant.getMaxLvl()) {
                                    p.sendMessage("&cThe max level for the enchant &9" + enchant.getName() + "&c is " + enchant.getMaxLvl() + "&c!");
                                } else {
                                    boolean isType = false;
                                    if (enchant.getItemType() == null) {
                                        for (ItemType type : enchant.getItemTypes()) {
                                            if (type.equals(item.type)) {
                                                isType = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        if (enchant.getItemType().getList().isEmpty()) {
                                            if (enchant.getItemType().equals(item.type)) isType = true;
                                        } else {
                                            for (String type : enchant.getItemType().getList()) {
                                                if (ItemType.valueOf(type).equals(item.type)) {
                                                    isType = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (isType) {
                                        item.putEnchantment(enchant, Integer.parseInt(input));
                                    } else {
                                        p.sendMessage("&cThe enchant &9" + enchant.getName() + "&c doesn't support your item type &e" + item.type.getValue() + "&c!");
                                    }
                                }
                                Bukkit.getScheduler().runTaskLaterAsynchronously(SBX.getInstance(), () -> {
                                    p.setItemInHand(item.applyEnchants().build());
                                    new EnchantGUI(p).open();
                                }, 7);
                            } else {
                                p.sendMessage("&cThat is not a valid input");
                                new EnchantGUI(p).open();
                            }
                        }
                    }).setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enchant level", 1, 0)).open();
                    break;
                }
                break;
        }
    }

    @Override
    public String getTitle() {
        return "Auto Enchants";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(SUtil.colorize("&7 "))).asGuiItem());

        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53, next);

        ItemStack searchItem = makeColorfulItem(Material.SIGN, "§aSearch Items", 1, 0,
                "§7Search through all Items.\n\n§eClick to search!");

        ItemStack searchItemsReset = makeColorfulItem(Material.SIGN, "§aSearch Items", 1, 0,
                SUtil.colorize("&7Search through all Items.\n&7Filtered: &6" + search.get(getOwner().getUniqueId()) + "\n\n&eClick to search!\n&bRight-Click to reset!"));

        if (search.containsKey(getOwner().getUniqueId())) {
            setItem(50, searchItemsReset);
        } else {
            setItem(50, searchItem);

        }
        setItem(45, prev);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("&7Filter through enchant categories");
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
        setItem(51, makeColorfulItem(Material.GOLD_BLOCK, "&bEnchant Categories", 1, 0, lore));
        for(ItemStack item : items(getOwner())) {
            getGui().addItem(ItemBuilder.from(item).asGuiItem());
        }
    }

    private ArrayList<ItemStack> items(SBPlayer player) {
        SBItemBuilder stack = new SBItemBuilder(player.getItemInHand());
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Enchantment enchant : Enchantment.values()) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add("&7Max Enchantment Level: &d" + enchant.getMaxLvl());
            lore.add("");
            lore.add("&6Enchant Description:");
            lore.addAll(StackUtils.stringToLore(enchant.getDescription(), 43, ChatColor.GRAY));
            lore.add("");
            if (stack.enchants.containsKey(enchant)) {
                lore.add("&aCurrently applied!");
            }
            lore.add("&eLeft-Click to apply!");
            lore.add("&bRight-Click to remove!");
            if (selectedType.get(player.getUniqueId()) == ItemType.CUSTOM) {
                items.add(ItemBuilder.from(makeColorfulItem(Material.ENCHANTED_BOOK, (enchant.isUltimate() ? "&d&l" : "&9") + enchant.getName(), 1, 0, lore)).setNbt("type", enchant.name()).build());
            }
            if (enchant.getItemTypes() != null) {
                for (ItemType type : enchant.getItemTypes()) {
                    if (selectedType.get(player.getUniqueId()) == type) {
                        items.add(ItemBuilder.from(makeColorfulItem(Material.ENCHANTED_BOOK, (enchant.isUltimate() ? "&d&l" : "&9") + enchant.getName(), 1, 0, lore)).setNbt("type", enchant.name()).build());
                    }
                }
            } else {
                if (selectedType.get(player.getUniqueId()) == ItemType.ARMOR) {
                    for (ItemType type : Arrays.asList(ItemType.BOOTS, ItemType.LEGGINGS, ItemType.CHESTPLATE, ItemType.HELMET)) {
                        if (enchant.getItemType() == type) {
                            items.add(ItemBuilder.from(makeColorfulItem(Material.ENCHANTED_BOOK, (enchant.isUltimate() ? "&d&l" : "&9") + enchant.getName(), 1, 0, lore)).setNbt("type", enchant.name()).build());
                        }
                    }
                }
                if (selectedType.get(player.getUniqueId()) == enchant.getItemType()) {
                    items.add(ItemBuilder.from(makeColorfulItem(Material.ENCHANTED_BOOK, (enchant.isUltimate() ? "&d&l" : "&9") + enchant.getName(), 1, 0, lore)).setNbt("type", enchant.name()).build());
                }
            }
        }
        if (search.containsKey(player.getUniqueId())) {
            ArrayList<ItemStack> searchItems = new ArrayList<>();
            for (ItemStack item : items) {
                if (NBTUtil.getGenericString(item, "type").contains(search.get(player.getUniqueId()).toUpperCase())) {
                    searchItems.add(item);
                }
            }
            return searchItems;
        }

        return items;
    }

    @Override
    public void openBack() {
        new ItemCreatorGUIMain(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Auto Item Creator";
    }

    @Override
    public int backItemSlot() {
        return 48;
    }
}
