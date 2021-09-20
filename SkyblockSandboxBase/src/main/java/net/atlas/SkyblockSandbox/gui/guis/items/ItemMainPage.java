package net.atlas.SkyblockSandbox.gui.guis.items;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemMainPage extends NormalGUI {
    public ItemMainPage(SBPlayer player) {
        super(player);
    }
    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        SBPlayer player = getOwner();
        setAction(31, event -> {
            player.closeInventory();
        });
        setAction(11, event -> {
            new CreativeItemPage(player).open();
        });
        setAction(13, event -> {
            new HypixelItemPage(player).open();
        });
        setAction(15, event -> {
            new AbilityItemsPage(player).open();
        });

        return true;
    }

    @Override
    public String getTitle() {
        return "Items Main Page";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(11, makeColorfulItem(Material.EMERALD_BLOCK, "&aCreative menu", 1, 0, "&7Get all vanilla items\n&7from this menu\n\n&eClick to open!"));
        setItem(13, makeColorfulItem(Material.GOLD_INGOT, "&aHypixel items menu", 1, 0, "&7Get all items from Hypixel\n&7from this menu\n\n&eClick to open!"));
        setItem(15, makeColorfulItem(Material.BLAZE_POWDER, "&aAbility items menu", 1, 0, "&7Get all items with working\n&7abilities from this menu\n\n&eClick to open!"));

        setItem(31, makeColorfulItem(Material.BARRIER,"&cClose",1,0));

    }
}
