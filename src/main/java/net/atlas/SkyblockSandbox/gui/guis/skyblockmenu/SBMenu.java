package net.atlas.SkyblockSandbox.gui.guis.skyblockmenu;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

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
        return true;
    }

    @Override
    public String getTitle() {
        return "Skyblock menu";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        DecimalFormat format = new DecimalFormat("###,###");
        setItem(22,makeColorfulSkullItem("&a" + owner.getName() + "'s Profile",owner.getName(),1,
                Arrays.asList("&cHealth &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.HEALTH)),
                        "&aDefense &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.DEFENSE)),
                        "&cStrength &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.STRENGTH)),
                        "&rSpeed &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.SPEED)),
                        "&9Crit Chance &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.CRIT_CHANCE)) + "%",
                        "&9Crit Damage &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.CRIT_DAMAGE)) + "%",
                        "&bIntelligence &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE)),
                        "&eBonus Attack Speed &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.ATTACK_SPEED)) + "%",
                        "&cFerocity &r" + format.format(owner.getMaxStat(SBPlayer.PlayerStat.FEROCITY)))));
        setItem(19,makeColorfulItem(Material.DIAMOND_SWORD,"&aYour SKills",1,0,"&7View your Skill progression and","&7rewards."));
        setItem(30,makeColorfulItem(Material.BONE,"&aPets",1,0,"","&eClick to open the pets menu!"));
    }

}
