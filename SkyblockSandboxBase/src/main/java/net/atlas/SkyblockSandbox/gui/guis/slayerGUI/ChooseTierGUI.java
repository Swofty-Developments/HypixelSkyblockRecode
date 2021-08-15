package net.atlas.SkyblockSandbox.gui.guis.slayerGUI;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.util.NumberTruncation.RomanNumber;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChooseTierGUI extends SBGUI {

    public Slayers slayer;

    public ChooseTierGUI(SBPlayer owner, Slayers slayer) {
        super(owner);
        this.slayer = slayer;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {}

    @Override
    public boolean setClickActions() {
        Material slayerMat = Material.BARRIER;
        switch (slayer) {
            case ZOMBIE:
                slayerMat=Material.ROTTEN_FLESH;
                break;
            case SPIDER:
                slayerMat=Material.WEB;
                break;
            case WOLF:
                slayerMat=Material.MUTTON;
                break;
            case ENDERMAN:
                slayerMat=Material.ENDER_PEARL;
                break;
        }
        for(int i = 0;i<slayer.getSlayerClass().getMaxTier().toInteger();i++) {
            setItem(i+11,makeColorfulItem(slayerMat,"&c" + ChatColor.stripColor(slayer.getSlayerClass().getSlayerName()) + " " + RomanNumber.toRoman(i+1),1,0,
                    "&8Placeholder","","&7Health: &c" + slayer.getSlayerClass().getSlayerHealth().get(SlayerTier.fromInt(i+1)),
                    "&7Damage: &c" + slayer.getSlayerClass().getDPS().get(SlayerTier.fromInt(i+1)) + " &7per second","","&eClick to slay!"));
            int finalI = i;
            setAction(i+11, event -> new ConfirmGUI(getOwner(),slayer,SlayerTier.fromInt(finalI + 1),false).open());
        }
        setAction(49,event -> new MaddoxGUI(getOwner()).open());
        return true;
    }

    @Override
    public String getTitle() {
        return ChatColor.stripColor(slayer.getSlayerClass().getSlayerName());
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();

        setItem(28,makeColorfulItem(Material.GOLD_BLOCK,"&eSlayer Lvl: &cN/A",1,0));
        setItem(31,makeColorfulItem(Material.GOLD_NUGGET,"&dClick to view leveling rewards!",1,0));
        setItem(34,makeColorfulItem(Material.BOOK,"&eIdk",1,0));
        setItem(49,makeColorfulItem(Material.ARROW,"&cGo back",1,0));
    }
}
