package net.atlas.SkyblockSandbox.gui.guis;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TestPage extends NormalGUI {
    private final int rows;
    public TestPage(SBPlayer player, int size) {
        super(player);
        this.rows = size;
    }
    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Slots";
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public void setItems() {
        for (int i = 0; i < rows * 9; i++) {
            setItem(i, makeColorfulItem(Material.STAINED_GLASS_PANE, "", i, 15));
        }
    }
}
