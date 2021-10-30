package net.atlas.SkyblockSandbox.gui.guis.itemCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.minecraft.server.v1_8_R3.ItemBanner;
import net.minecraft.server.v1_8_R3.ItemBucket;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemTypeGUI extends NormalGUI implements Backable {
    public ItemTypeGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        int[] slots = {11,12,13,14,15,20,21,22,23};
        for(int slot:slots) {
            setAction(slot,event -> {
                ItemStack i = NBTUtil.setString(getOwner().getItemInHand(),NBTUtil.getGenericString(event.getCurrentItem(),"type"),"item-type");
                getOwner().setItemInHand(i);
                getOwner().playSound(getOwner().getLocation(), Sound.ITEM_PICKUP,1,1);
            });
        }
        return true;
    }

    @Override
    public String getTitle() {
        return "Item type selector";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(11,ItemBuilder.from(makeColorfulItem(Material.DIAMOND_SWORD,"&aSword",1,0,"","&7Click to set the item type to sword")).setNbt("type",ItemType.SWORD.name()).asGuiItem().getItemStack());
        setItem(12,ItemBuilder.from(makeColorfulItem(Material.BOW,"&aBow",1,0,"","&7Click to set the item type to pickaxe")).setNbt("type",ItemType.BOW.name()).asGuiItem().getItemStack());
        setItem(13,ItemBuilder.from(makeColorfulItem(Material.DIAMOND_PICKAXE,"&aPickaxe",1,0,"","&7Click to set the item type to pickaxe")).setNbt("type",ItemType.PICKAXE.name()).asGuiItem().getItemStack());
        setItem(14,ItemBuilder.from(makeColorfulItem(Material.GOLD_AXE,"&aAxe",1,0,"","&7Click to set the item type to axe")).setNbt("type",ItemType.AXE.name()).asGuiItem().getItemStack());
        setItem(15,ItemBuilder.from(makeColorfulItem(Material.PRISMARINE_SHARD,"&aDrill",1,0,"","&7Click to set the item type to drill")).setNbt("type",ItemType.DRILL.name()).asGuiItem().getItemStack());
        setItem(20,ItemBuilder.from(makeColorfulItem(Material.IRON_HELMET,"&aHelmet",1,0,"","&7Click to set the item type to helmet")).setNbt("type",ItemType.HELMET.name()).asGuiItem().getItemStack());
        setItem(21,ItemBuilder.from(makeColorfulItem(Material.DIAMOND_CHESTPLATE,"&aChestplate",1,0,"","&7Click to set the item type to chestplate")).setNbt("type",ItemType.CHESTPLATE.name()).asGuiItem().getItemStack());
        setItem(22,ItemBuilder.from(makeColorfulItem(Material.GOLD_LEGGINGS,"&aLeggings",1,0,"","&7Click to set the item type to leggings")).setNbt("type",ItemType.LEGGINGS.name()).asGuiItem().getItemStack());
        setItem(23,ItemBuilder.from(makeColorfulItem(Material.LEATHER_BOOTS,"&aBoots",1,0,"","&7Click to set the item type to boots")).setNbt("type",ItemType.BOOTS.name()).asGuiItem().getItemStack());
    }

    @Override
    public void openBack() {
        new ItemCreatorGUIMain(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return new ItemCreatorGUIMain(getOwner()).getTitle();
    }

    @Override
    public int backItemSlot() {
        return 39;
    }
}
