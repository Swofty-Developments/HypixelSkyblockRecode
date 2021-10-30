package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmGUI extends NormalGUI {
    public ConfirmGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        return false;
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
        setItem(11, makeColorfulItem(Material.STAINED_CLAY, "&aConfirm", 1, 13, ""));
    }
}
