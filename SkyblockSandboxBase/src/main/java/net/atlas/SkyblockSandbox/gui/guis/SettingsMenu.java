package net.atlas.SkyblockSandbox.gui.guis;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SettingsMenu extends NormalGUI {
    public SettingsMenu(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        return false;
    }
}
