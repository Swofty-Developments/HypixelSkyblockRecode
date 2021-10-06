package net.atlas.SkyblockSandbox.gui.guis.skyblockmenu;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SkillGUI extends NormalGUI {
    public SkillGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(49, event -> {
            getOwner().closeInventory();
        });
        setAction(48, event -> {
            new SBMenu(getOwner()).open();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Your Skills";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(4, makeColorfulItem(Material.DIAMOND_SWORD, "&aYour Skills", 1, 0, "&7View your Skill progression and", "&7rewards.", "", "&eClick to show rankings!"));
        setItem(19, makeSkillItem(SkillType.FARMING, Material.GOLD_HOE));
        setItem(20, makeSkillItem(SkillType.MINING, Material.STONE_PICKAXE));
        setItem(21, makeSkillItem(SkillType.COMBAT, Material.STONE_SWORD));
        setItem(22, makeSkillItem(SkillType.FORAGING, Material.SAPLING, 3));
        setItem(23, makeSkillItem(SkillType.FISHING, Material.FISHING_ROD));
        setItem(24, makeSkillItem(SkillType.ENCHANTING, Material.ENCHANTMENT_TABLE));
        setItem(25, makeSkillItem(SkillType.ALCHEMY, Material.BREWING_STAND_ITEM));
        setItem(29, makeSkillItem(SkillType.CARPENTRY, Material.WORKBENCH));
        setItem(30, makeSkillItem(SkillType.RUNECRAFTING, Material.MAGMA_CREAM));
        setItem(32, makeSkillItem(SkillType.TAMING, Material.MONSTER_EGG));

        setItem(49, makeColorfulItem(Material.BARRIER, "&cClose", 1, 0));
        setItem(48, makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To SkyBlock Menu"));

    }

    ItemStack makeSkillItem(SkillType type, Material mat) {
        double curxp = getOwner().getCurrentSkillExp(type);
        String curXpString = String.valueOf(curxp);
        int totalXp = SkillType.getLvlXP(getOwner().getSkillLvl(type));
        int percent = (int) (curxp * 100 / totalXp);
        double c = Math.round(percent * 10.0) / 10.0;
        String pcnt = "&e" + c + "%";
        if (curxp > 999999) {
            curXpString = NumberSuffix.format(curxp);
        }
        if (getOwner().getSkillLvl(type) >= type.getMaxLvl()) {
            return makeColorfulItem(mat, "&a" + type.getName() + " " + getOwner().getSkillLvl(type),
                    1, 0, "", "&8&oYou have reached the max", "&8&olevel for this Skill!", "", "&eClick to view!");
        }
        return makeColorfulItem(mat, "&a" + type.getName() + " " + getOwner().getSkillLvl(type),
                1, 0, "", "&7Progress to Level " + (getOwner().getSkillLvl(type) + 1) + ": " + pcnt,
                SUtil.getProgressBar(curxp, totalXp, 20, '-', ChatColor.DARK_GREEN, ChatColor.WHITE) + " &e" + curXpString + "&6/&e" + NumberSuffix.format(totalXp),
                "",
                "&7Level " + (getOwner().getSkillLvl(type) + 1) + " Rewards:",
                " &eComing Soon!", "", "&eClick to view!");
    }

    ItemStack makeSkillItem(SkillType type, Material mat, int damage) {
        double curxp = getOwner().getCurrentSkillExp(type);
        String curXpString = String.valueOf(curxp);
        int totalXp = SkillType.getLvlXP(getOwner().getSkillLvl(type));
        int percent = (int) (curxp * 100 / totalXp);
        double c = Math.round(percent * 10.0) / 10.0;
        String pcnt = "&e" + c + "%";
        if (curxp > 999999) {
            curXpString = NumberSuffix.format(curxp);
        }
        if (getOwner().getSkillLvl(type) >= type.getMaxLvl()) {
            return makeColorfulItem(mat, "&a" + type.getName() + " " + getOwner().getSkillLvl(type),
                    1, damage, "", "&8&oYou have reached the max", "&8&olevel for this Skill!", "", "&eClick to view!");
        }
        return makeColorfulItem(mat, "&a" + type.getName() + " " + getOwner().getSkillLvl(type),
                1, damage, "", "&7Progress to Level " + (getOwner().getSkillLvl(type) + 1) + ": " + pcnt,
                SUtil.getProgressBar(curxp, totalXp, 20, '-', ChatColor.DARK_GREEN, ChatColor.WHITE) + " &e" + curXpString + "&6/&e" + NumberSuffix.format(totalXp),
                "",
                "&7Level " + (getOwner().getSkillLvl(type) + 1) + " Rewards:",
                " &eComing Soon!", "", "&eClick to view!");
    }
}
