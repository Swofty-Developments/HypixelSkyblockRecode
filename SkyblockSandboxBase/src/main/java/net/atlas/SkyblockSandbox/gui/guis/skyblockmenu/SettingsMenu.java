package net.atlas.SkyblockSandbox.gui.guis.skyblockmenu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.gui.guis.skyblockmenu.SBMenu;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.player.SBPlayer.Settings.*;

public class SettingsMenu extends NormalGUI implements Backable {
    public static HashMap<UUID, Boolean> loreGen = LORE_GEN.map;
    public static HashMap<UUID, Boolean> visibility = VISIBILITY.map;
    public static HashMap<UUID, Boolean> scoreboard = SCOREBOARD.map;

    public SettingsMenu(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();

        setItem(SBPlayer.Settings.LORE_GEN.getSlot(), ItemBuilder.from(SBPlayer.Settings.LORE_GEN.getItem()).glow(loreGen.get(getOwner().getUniqueId())).build());
        setItem(SBPlayer.Settings.VISIBILITY.getSlot(), ItemBuilder.from(SBPlayer.Settings.VISIBILITY.getItem()).glow(visibility.get(getOwner().getUniqueId())).build());
        setItem(SCOREBOARD.getSlot(), ItemBuilder.from(SCOREBOARD.getItem()).glow(scoreboard.get(getOwner().getUniqueId())).build());
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(11, event -> {
            if (!loreGen.containsKey(getOwner().getUniqueId()) || !loreGen.get(getOwner().getUniqueId())) {
                loreGen.put(getOwner().getUniqueId(), true);
            } else {
                loreGen.put(getOwner().getUniqueId(), false);
            }
            updateItems();
        });
        setAction(13, event -> {
            if (!visibility.containsKey(getOwner().getUniqueId()) || !visibility.get(getOwner().getUniqueId())) {
                visibility.put(getOwner().getUniqueId(), true);
            } else {
                visibility.put(getOwner().getUniqueId(), false);
            }
            updateItems();
        });
        setAction(15, event -> {
            if (!scoreboard.containsKey(getOwner().getUniqueId()) || !scoreboard.get(getOwner().getUniqueId())) {
                scoreboard.put(getOwner().getUniqueId(), true);
            } else {
                scoreboard.put(getOwner().getUniqueId(), false);
            }
            updateItems();
        });
        return true;
    }

    @Override
    public void openBack() {
        new SBMenu(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Skyblock Menu";
    }

    @Override
    public int backItemSlot() {
        return 30;
    }
}
