package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmGUI extends NormalGUI{
    public ConfirmGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> {
            getOwner().closeInventory();
        });
        setAction(11, event -> {
            new ItemCreatorGUIMain(getOwner()).open();
        });
        setAction(15, event -> {
            new net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.advanced.ItemCreatorGUIMain(getOwner()).open();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Are you sure?";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.BARRIER, "&cClose", 1, 0));
        setItem(11, makeColorfulItem(Material.STAINED_CLAY, "&aConfirm", 1, 13, ""));
        setItem(15, makeColorfulItem(Material.STAINED_CLAY, "&cDeny", 1, 14, ""));

    }
}
