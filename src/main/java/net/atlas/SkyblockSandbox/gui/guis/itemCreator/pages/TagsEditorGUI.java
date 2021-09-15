package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TagsEditorGUI extends NormalGUI {

    private Gui gui;

    public TagsEditorGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(13, event -> {
            ItemStack item = event.getWhoClicked().getItemInHand();
            if (event.getClick().isRightClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                item.setDurability((short) 0);
                itemMeta.spigot().setUnbreakable(false);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 2);
            }
            if (event.getClick().isLeftClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                item.setDurability((short) 0);
                itemMeta.spigot().setUnbreakable(true);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ITEM_PICKUP, 2, 1);
            }
        });
        setAction(49, event -> {
            new ItemCreatorGUIMain(getOwner()).open();
        });
        setAction(15, event -> {
            ItemStack item = event.getWhoClicked().getItemInHand();
            if (event.getClick().isRightClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 2);
            }
            if (event.getClick().isLeftClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ITEM_PICKUP, 2, 1);
            }
        });
        setAction(31, event -> {
            ItemStack item = event.getWhoClicked().getItemInHand();
            if (event.getClick().isRightClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.removeEnchant(Enchantment.LUCK);
                itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 2);
            }
            if (event.getClick().isLeftClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.addEnchant(Enchantment.LUCK, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ITEM_PICKUP, 2, 1);
            }
        });
        setAction(11, event -> {
            ItemStack item = event.getWhoClicked().getItemInHand();
            if (event.getClick().isRightClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 2);
            }
            if (event.getClick().isLeftClick()) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(itemMeta);
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ITEM_PICKUP, 2, 1);
            }
        });
        setAction(33, event -> {
            ItemStack item = event.getWhoClicked().getItemInHand();
            if (event.getWhoClicked().getItemInHand().getType().isSolid()) {
                if (event.getClick().isRightClick()) {
                    event.getWhoClicked().setItemInHand(NBTUtil.setString(event.getWhoClicked().getItemInHand(), "False", "wearable"));
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 2);
                }
                if (event.getClick().isLeftClick()) {
                    event.getWhoClicked().setItemInHand(NBTUtil.setString(event.getWhoClicked().getItemInHand(), "True", "wearable"));
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ITEM_PICKUP, 2, 1);
                }
            }
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Edit Item Tags";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(49, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create an Item"));

        setItem(13, makeColorfulItem(Material.IRON_BLOCK, "§aToggle Unbreakable", 1, 0, SUtil.colorize("&7Toggle the unbreakable tag", "", "&eLeft click to Enable", "&eRight click to Disable!")));
        setItem(15, makeColorfulItem(Material.ENCHANTED_BOOK, "§aToggle Enchant Tag", 1, 0, SUtil.colorize("&7Toggle the enchant tag", "", "&eLeft click to Enable", "&eRight click to Disable!")));
        setItem(31, makeColorfulItem(Material.GLOWSTONE_DUST, "§aToggle Glowing", 1, 0, SUtil.colorize("&7Toggle Item Glow", "", "&eLeft click to Enable", "&eRight click to Disable!")));
        setItem(11, makeColorfulItem(Material.GOLDEN_APPLE, "§aToggle Damage Tag", 1, 0, SUtil.colorize("&7Toggle the damage tag", "", "&eLeft click to Enable", "&eRight click to Disable!")));
        setItem(33, makeColorfulItem(Material.STAINED_GLASS, "&aToggle wearable", 1, 15, "&7Toggle being able to wear", "&7blocks on your head!", "", "&eLeft click to Enable", "&eRight click to Disable!"));
    }
}
