package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
            setRarity(Rarity.UNCOMMON, item, player);
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

    }

    private void setRarity(Rarity r, ItemStack i, Player player) {
        ItemStack i1 = NBTUtil.setString(i, r.toString(), "RARITY");
        SBItemStack it = new SBItemStack(i1);
        player.setItemInHand(it.refreshLore());
    }
}
