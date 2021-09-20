package net.atlas.SkyblockSandbox.gui.guis.skyblockmenu;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SBMenu extends NormalGUI {
    public SBPlayer owner;
    public SBMenu(SBPlayer owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {}

    @Override
    public boolean setClickActions() {
        setAction(19,event -> {
            new SkillGUI(getOwner()).open();
        });
        setAction(30,event -> {
            new PetsMenu(getOwner()).open();
        });
        setAction(20,event -> {
            Bukkit.getServer().dispatchCommand(getOwner().getPlayer(),"items");
        });
        setAction(24,event -> {
            Bukkit.getServer().dispatchCommand(getOwner().getPlayer(),"gui open store");
        });
        setAction(25,event -> {
            Bukkit.getServer().dispatchCommand(getOwner().getPlayer(),"storage");
        });
        setAction(49,event -> getGui().close(getOwner().getPlayer()));
        setAction(50,event -> {
            Bukkit.getServer().dispatchCommand(getOwner().getPlayer(),"gui open settings");
        });
        setAction(31,event -> {
            Gui gui = Gui.gui(GuiType.WORKBENCH).title(Component.text("Crafting Table"))
                    .create();
            gui.open(getOwner().getPlayer());
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Skyblock Menu";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        DecimalFormat format = new DecimalFormat("###,###");
        setItem(13,makeColorfulSkullItem("&a" + owner.getName() + "'s Profile",owner.getName(),1,
                Arrays.asList("&cHealth &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.HEALTH)),
                        "&aDefense &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.DEFENSE)),
                        "&cStrength &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.STRENGTH)),
                        "&rSpeed &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.SPEED)),
                        "&9Crit Chance &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.CRIT_CHANCE)) + "%",
                        "&9Crit Damage &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.CRIT_DAMAGE)) + "%",
                        "&bIntelligence &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE)),
                        "&eBonus Attack Speed &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.ATTACK_SPEED)) + "%",
                        "&cFerocity &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.FEROCITY)))));
        setItem(20,makeColorfulItem(Material.BLAZE_POWDER,"&aItem Catalogue",1,0,"&7View a list of every Skyblock item and &emore!","","&eClick to open!"));
        setItem(22,makeColorfulItem(Material.EMERALD,"&aUtilities panel",1,0,"&8Personal Troubleshooting","&7Troubleshoot various minor bugs/glitches","&7that may occur to you.","","&eClick to open!"));
        setItem(23,makeColorfulItem(Material.BOOK_AND_QUILL,"&aQuest Log",1,0,"&7View your active quests,","&7progress, and rewards.","","&eClick to view!"));
        setItem(24,makeColorfulItem(Material.GOLD_INGOT,"&aStore",1,0,"&7Support the server and","&7purchase ranks, cosmetics","&7and more!"));
        setItem(25,makeColorfulItem(Material.CHEST,"&aStorage",1,0,"&7Access your items anytime","&7from anywhere.","","&eClick to open!"));
        setItem(19,makeColorfulItem(Material.DIAMOND_SWORD,"&aYour Skills",1,0,"&7View your Skill progression and","&7rewards."));
        setItem(30,makeColorfulItem(Material.BONE,"&aPets",1,0,"","&eClick to open the pets menu!"));
        setItem(31,makeColorfulItem(Material.WORKBENCH,"&aCrafting Table",1,0,"","&7Click to open the crafting grid!"));
        setItem(49,makeColorfulItem(Material.BARRIER,"&cClose",1,0));
        setItem(50,makeColorfulItem(Material.REDSTONE_TORCH_ON,"&aSettings",1,0,"&7Configure your SkyBlock","&7settings.","","&eClick to configure!"));
    }

}
