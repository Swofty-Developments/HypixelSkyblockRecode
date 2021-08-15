package net.atlas.SkyblockSandbox.gui.guis.slayerGUI;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static net.atlas.SkyblockSandbox.SBX.activeSlayers;

public class MaddoxGUI extends SBGUI {

    public MaddoxGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> getOwner().closeInventory());
        setAction(10, event -> new ChooseTierGUI(getOwner(), Slayers.ZOMBIE).open());
        setAction(11, event -> new ChooseTierGUI(getOwner(), Slayers.SPIDER).open());
        setAction(12, event -> new ChooseTierGUI(getOwner(), Slayers.WOLF).open());
        if(!activeSlayers.containsKey(getOwner().getUniqueId())) {
            setAction(13, event -> new ChooseTierGUI(getOwner(), Slayers.ENDERMAN).open());
        } else {
            setAction(13, event -> {
                HashMap<Slayers, HashMap<SlayerTier, Double>> t = activeSlayers.get(getOwner().getUniqueId());
                Optional<Slayers> d = t.keySet().stream().findFirst();
                d.ifPresent(slayer -> new ConfirmGUI(getOwner(), slayer, SlayerTier.ONE, true).open());
            });
        }
        return true;
    }

    @Override
    public String getTitle() {
        return "Maddox The Slayer";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        if(!activeSlayers.containsKey(getOwner().getUniqueId())) {
            setItem(10, makeColorfulItem(Material.ROTTEN_FLESH, "&c☠ &eRevenant Horror", 1, 0, "&7Abhorrant Zombie stuck", "&7between life and death for", "&7an eternity.", "", "&7Zombie Slayer: &eN/A", "", "&eClick to view boss!"));
            setItem(11, makeColorfulItem(Material.WEB, "&c☠ &eTarantula Broodfather", 1, 0));
            setItem(12, makeColorfulItem(Material.MUTTON, "&c☠ &eSven Packmaster", 1, 0));
            setItem(13, makeColorfulItem(Material.ENDER_PEARL, "&c☠ &eVoidgloom Seraph", 1, 0));
            setItem(14, makeColorfulItem(Material.COAL_BLOCK, "&cComing Soon!", 1, 0));
            setItem(15, makeColorfulItem(Material.COAL_BLOCK, "&cComing Soon!", 1, 0));
            setItem(16, makeColorfulItem(Material.COAL_BLOCK, "&cComing Soon!", 1, 0));
            setItem(31, makeColorfulItem(Material.BARRIER,"&cClose",1,0));

        } else {
            setItem(13,makeColorfulItem(Material.STAINED_CLAY,"&aSlayer Quest Active!",1,14,"","&7Slay &c2,400 Combat XP &7worth","&7of " + activeSlayers.get(getOwner().getUniqueId()).keySet().stream().findFirst().orElse(null) + ".","","&eClick to cancel!"));
        }
    }

}
