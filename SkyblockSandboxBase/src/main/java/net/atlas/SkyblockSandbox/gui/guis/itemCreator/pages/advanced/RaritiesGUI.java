package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.advanced;

import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.atlas.SkyblockSandbox.item.Rarity.*;
import static net.atlas.SkyblockSandbox.util.NBTUtil.getString;
import static net.atlas.SkyblockSandbox.util.NBTUtil.setString;

public class RaritiesGUI extends NormalGUI implements Backable {


    public RaritiesGUI(SBPlayer owner) {
        super(owner);


    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {

        setAction(10, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(Rarity.COMMON, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(11, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(UNCOMMON, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(12, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(Rarity.RARE, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(13, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(Rarity.EPIC, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(14, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(Rarity.LEGENDARY, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(15, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(Rarity.MYTHIC, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(16, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            ItemStack item = player.getItemInHand();
            setRarity(Rarity.SPECIAL, item, player);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        });
        setAction(22, event -> {
            SBItemBuilder builder = new SBItemBuilder(getOwner().getItemInHand());
            if(event.isRightClick()) {
                unRecomb(builder.rarity, getOwner().getItemInHand(), getOwner());
            } else {
                recomb(builder.rarity, getOwner().getItemInHand(), getOwner());
            }
        });

        return true;
    }

    @Override
    public String getTitle() {
        return "Set Item Rarity";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(10, makeColorfulItem(Material.STAINED_CLAY, "§fCommon", 1, 0, "§7Set your item's rarity", "§7to §fCommon§7!", "", "§eClick to set!"));
        setItem(11, makeColorfulItem(Material.STAINED_CLAY, "§aUncommon", 1, 5, "§7Set your item's rarity", "§7to §aUncommon§7!", "", "§eClick to set!"));
        setItem(12, makeColorfulItem(Material.STAINED_CLAY, "§9Rare", 1, 3, "§7Set your item's rarity", "§7to §9Rare§7!", "", "§eClick to set!"));
        setItem(13, makeColorfulItem(Material.STAINED_CLAY, "§5Epic", 1, 10, "§7Set your item's rarity", "§7to §5Epic§7!", "", "§eClick to set!"));
        setItem(14, makeColorfulItem(Material.STAINED_CLAY, "§6Legendary", 1, 4, "§7Set your item's rarity", "§7to §6Legendary§7!", "", "§eClick to set!"));
        setItem(15, makeColorfulItem(Material.STAINED_CLAY, "§dMythic", 1, 2, "§7Set your item's rarity", "§7to §dMythic§7!", "", "§eClick to set!"));
        setItem(16, makeColorfulItem(Material.STAINED_CLAY, "§cSpecial", 1, 14, "§7Set your item's rarity", "§7to §cSpecial§7!", "", "§eClick to set!"));
        setItem(22, makeColorfulSkullItem("&6Recombobulate", "http://textures.minecraft.net/texture/57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1, "&7Automatically recombobulate your item!", "", "&eLeft-Click to recombobulate!", "&bRight-Click to remove the recombobulator!"));
    }



    private void setRarity(Rarity r, ItemStack i, Player player) {
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(r.getColor() + ChatColor.stripColor(meta.getDisplayName()));
        List<String> lore = i.getItemMeta().getLore();
        if (getString(i, "recombobulated").equals("true")) {
            String recombsymbol = r.getColor() + "" + ChatColor.MAGIC + "L" + ChatColor.stripColor("") + r.getColor() + "" + ChatColor.BOLD;
            lore.set(lore.size()-1, recombsymbol + " " + r.name() + " " + recombsymbol);
        } else {
            lore.set(lore.size()-1, r.getColor() + "" + ChatColor.BOLD + r.name());
        }

        player.setItemInHand(setString(i, r.name(), "rarity"));
    }

    public static String starKey = "dungeon_upgrades";
    public static String masterStarKey = "master_dungeon_upgrades";

    private void recomb(Rarity r, ItemStack i, Player player) {
        getOwner().playSound(getOwner().getLocation(), Sound.ANVIL_USE, 2, 1);
        i = setString(i, "true", "recombobulated");
        switch (r) {
            case COMMON:
                setRarity(UNCOMMON, i, player);
                break;
            case UNCOMMON:
                setRarity(RARE, i, player);
                break;
            case RARE:
                setRarity(EPIC, i, player);
                break;
            case EPIC:
                setRarity(LEGENDARY, i, player);
                break;
            case LEGENDARY:
                setRarity(MYTHIC, i, player);
                break;
            case MYTHIC:
                setRarity(SUPREME, i, player);
                break;
            case SUPREME:
                setRarity(SPECIAL, i, player);
                break;
            case SPECIAL:
                setRarity(SUPER_SPECIAL, i, player);
                break;
            case SUPER_SPECIAL:
                setRarity(UNOBTAINABLE, i, player);
                break;
        }
    }
    private void unRecomb(Rarity r, ItemStack i, Player player) {
        getOwner().playSound(getOwner().getLocation(), Sound.ANVIL_USE, 2, 1);
        i = setString(i, "false", "recombobulated");
        switch (r) {
            case UNCOMMON:
                setRarity(COMMON, i, player);
                break;
            case RARE:
                setRarity(UNCOMMON, i, player);
                break;
            case EPIC:
                setRarity(RARE, i, player);
                break;
            case LEGENDARY:
                setRarity(EPIC, i, player);
                break;
            case MYTHIC:
                setRarity(LEGENDARY, i, player);
                break;
            case SUPREME:
                setRarity(MYTHIC, i, player);
                break;
            case SPECIAL:
                setRarity(SUPREME, i, player);
                break;
            case SUPER_SPECIAL:
                setRarity(SPECIAL, i, player);
                break;
            case UNOBTAINABLE:
                setRarity(SUPER_SPECIAL, i, player);
                break;
        }
    }

    @Override
    public void openBack() {
        new ItemCreatorGUIMain(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Normal Item Creator";
    }

    @Override
    public int backItemSlot() {
        return 30;
    }
}
