package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import com.google.common.base.Enums;
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

            SBItemStack item = new SBItemStack(getOwner().getItemInHand());
            String rarityStr = item.getString(getOwner().getItemInHand(), "RARITY");

            Rarity rarity = Enums.getIfPresent(Rarity.class, rarityStr).or(Rarity.COMMON);
            recomb(rarity, item.asBukkitItem(), getOwner());
        });
        setAction(21, event -> {
            addStar(getOwner().getItemInHand(), event.getClick().isRightClick());
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
        setItem(22, makeColorfulSkullItem("&6Recombobulate", "http://textures.minecraft.net/texture/57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1, "&7Automatically recombobulate your item!", "", "&eClick to recombobulate!"));
        setItem(21, makeColorfulItem(Material.QUARTZ, "&c✪✪✪✪&6✪ Add dungeon stars", 1, 0, "&7Add a dungeon star to your item!", "", "&eRight click for master stars!"));

    }

    private void setRarity(Rarity r, ItemStack i, Player player) {
        ItemStack i1 = NBTUtil.setString(i, r.toString(), "RARITY");
        SBItemStack it = new SBItemStack(i1);
        player.setItemInHand(it.refreshLore());
    }

    public static String starKey = "dungeon_upgrades";
    public static String masterStarKey = "master_dungeon_upgrades";

    private void addStar(ItemStack i, boolean masterStar) {
        ItemStack finalI = new ItemStack(getOwner().getItemInHand());
        if(NBTUtil.getInteger(i,"dungeon_item")!=1) {
            finalI = NBTUtil.setInteger(finalI,1,"dungeon_item");
        }
        getOwner().playSound(getOwner().getLocation(), Sound.ZOMBIE_REMEDY, 2, 1);
        int starAmt = NBTUtil.getInteger(i, starKey);
        int masterStarAmt = NBTUtil.getInteger(i, masterStarKey);

        if (masterStar) {
            finalI = NBTUtil.setInteger(finalI, masterStarAmt + 1, masterStarKey);
        } else {
            finalI = NBTUtil.setInteger(finalI, starAmt + 1, starKey);
        }
        SBItemStack it = new SBItemStack(finalI);
        getOwner().setItemInHand(it.refreshLore());
    }

    private void recomb(Rarity r, ItemStack i, Player player) {
        ItemStack i1 = new ItemStack(i);
        getOwner().playSound(getOwner().getLocation(), Sound.ANVIL_USE, 2, 1);
        switch (r) {
            case COMMON:
                i1 = NBTUtil.setString(i, UNCOMMON.toString(), "RARITY");
                break;
            case UNCOMMON:
                i1 = NBTUtil.setString(i, RARE.toString(), "RARITY");
                break;
            case RARE:
                i1 = NBTUtil.setString(i, EPIC.toString(), "RARITY");
                break;
            case EPIC:
                i1 = NBTUtil.setString(i, LEGENDARY.toString(), "RARITY");
                break;
            case LEGENDARY:
                i1 = NBTUtil.setString(i, MYTHIC.toString(), "RARITY");
                break;
            case MYTHIC:
                i1 = NBTUtil.setString(i, SUPREME.toString(), "RARITY");
                break;
            case SUPREME:
                i1 = NBTUtil.setString(i, SPECIAL.toString(), "RARITY");
                break;
            case SPECIAL:
                i1 = NBTUtil.setString(i, SUPER_SPECIAL.toString(), "RARITY");
                break;
            case SUPER_SPECIAL:
                i1 = NBTUtil.setString(i, UNOBTAINABLE.toString(), "RARITY");
                break;
        }

        SBItemStack it = new SBItemStack(i1);
        it = new SBItemStack(it.setInteger(it.asBukkitItem(), it.getInteger(it.asBukkitItem(), "rarity_upgrades") + 1, "rarity_upgrades"));
        player.setItemInHand(it.refreshLore());
    }
}
