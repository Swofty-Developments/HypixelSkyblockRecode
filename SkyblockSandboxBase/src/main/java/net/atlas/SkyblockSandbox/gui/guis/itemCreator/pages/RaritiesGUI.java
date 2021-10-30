package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;

import static net.atlas.SkyblockSandbox.item.Rarity.*;

public class RaritiesGUI extends NormalGUI {


    public RaritiesGUI(SBPlayer owner) {
        super(owner);


    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> {
            new ItemCreatorGUIMain(getOwner()).open();
        });

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
        setAction(21, event -> {
            addStar(getOwner().getItemInHand(), event.getClick().isRightClick());
        });
        setAction(23, event -> {
            removeStar(getOwner().getItemInHand(), event.getClick().isRightClick());
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
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create an item"));

        setItem(10, makeColorfulItem(Material.STAINED_CLAY, "§fCommon", 1, 0, "§7Set your item's rarity", "§7to §fCommon§7!", "", "§eClick to set!"));
        setItem(11, makeColorfulItem(Material.STAINED_CLAY, "§aUncommon", 1, 5, "§7Set your item's rarity", "§7to §aUncommon§7!", "", "§eClick to set!"));
        setItem(12, makeColorfulItem(Material.STAINED_CLAY, "§9Rare", 1, 3, "§7Set your item's rarity", "§7to §9Rare§7!", "", "§eClick to set!"));
        setItem(13, makeColorfulItem(Material.STAINED_CLAY, "§5Epic", 1, 10, "§7Set your item's rarity", "§7to §5Epic§7!", "", "§eClick to set!"));
        setItem(14, makeColorfulItem(Material.STAINED_CLAY, "§6Legendary", 1, 4, "§7Set your item's rarity", "§7to §6Legendary§7!", "", "§eClick to set!"));
        setItem(15, makeColorfulItem(Material.STAINED_CLAY, "§dMythic", 1, 2, "§7Set your item's rarity", "§7to §dMythic§7!", "", "§eClick to set!"));
        setItem(16, makeColorfulItem(Material.STAINED_CLAY, "§cSpecial", 1, 14, "§7Set your item's rarity", "§7to §cSpecial§7!", "", "§eClick to set!"));
        setItem(22, makeColorfulSkullItem("&6Recombobulate", "http://textures.minecraft.net/texture/57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1, "&7Automatically recombobulate your item!", "", "&eLeft-Click to recombobulate!", "&bRight-Click to remove the recombobulator!"));
        setItem(21, makeColorfulItem(Material.QUARTZ, "&c✪✪✪✪&6✪ Add dungeon stars", 1, 0, "&7Add a dungeon star to your item!", "", "&eRight click for master stars!"));
        setItem(23, makeColorfulItem(Material.QUARTZ, "&c✪✪✪✪&6✪ Remove dungeon stars", 1, 0, "&7Remove a dungeon star to your item!", "", "&eRight click for master stars!"));
    }



    private void setRarity(Rarity r, ItemStack i, Player player) {
        player.setItemInHand(new SBItemBuilder(i).rarity(r).build());
    }

    public static String starKey = "dungeon_upgrades";
    public static String masterStarKey = "master_dungeon_upgrades";

    private void addStar(ItemStack i, boolean masterStar) {
        SBItemBuilder builder = new SBItemBuilder(i);
        if(masterStar) {
            builder.masterStar(builder.masterstar + 1);
        }
        else {
            builder.normalStar(builder.normalstar + 1);
        }
        getOwner().setItemInHand(builder.build());
    }
    private void removeStar(ItemStack i, boolean masterStar) {
        SBItemBuilder builder = new SBItemBuilder(i);
        if(masterStar) {
            builder.masterStar(builder.masterstar - 1);
        }
        else {
            builder.normalStar(builder.normalstar - 1);
        }
        getOwner().setItemInHand(builder.build());
    }

    private void recomb(Rarity r, ItemStack i, Player player) {
        SBItemBuilder builder = new SBItemBuilder(i);
        getOwner().playSound(getOwner().getLocation(), Sound.ANVIL_USE, 2, 1);
        switch (r) {
            case COMMON:
                builder.rarity(UNCOMMON);
                break;
            case UNCOMMON:
                builder.rarity(RARE);
                break;
            case RARE:
                builder.rarity(EPIC);
                break;
            case EPIC:
                builder.rarity(LEGENDARY);
                break;
            case LEGENDARY:
                builder.rarity(MYTHIC);
                break;
            case MYTHIC:
                builder.rarity(SUPREME);
                break;
            case SUPREME:
                builder.rarity(SPECIAL);
                break;
            case SPECIAL:
                builder.rarity(SUPER_SPECIAL);
                break;
            case SUPER_SPECIAL:
                builder.rarity(UNOBTAINABLE);
                break;
        }
        builder.recomb(true);
        player.setItemInHand(builder.build());
    }
    private void unRecomb(Rarity r, ItemStack i, Player player) {
        SBItemBuilder builder = new SBItemBuilder(i);
        getOwner().playSound(getOwner().getLocation(), Sound.ANVIL_USE, 2, 1);
        switch (r) {
            case UNCOMMON:
                builder.rarity(COMMON);
                break;
            case RARE:
                builder.rarity(UNCOMMON);
                break;
            case EPIC:
                builder.rarity(RARE);
                break;
            case LEGENDARY:
                builder.rarity(EPIC);
                break;
            case MYTHIC:
                builder.rarity(LEGENDARY);
                break;
            case SUPREME:
                builder.rarity(MYTHIC);
                break;
            case SPECIAL:
                builder.rarity(SUPREME);
                break;
            case SUPER_SPECIAL:
                builder.rarity(SPECIAL);
                break;
            case UNOBTAINABLE:
                builder.rarity(SUPER_SPECIAL);
                break;
        }
        builder.recomb(false);
        player.setItemInHand(builder.build());
    }

}
