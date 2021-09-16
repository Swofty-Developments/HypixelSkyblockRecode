package net.atlas.SkyblockSandbox.customMining;


import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class MiningBlock {

    private final Block block;


    public MiningBlock(Block block) {
        this.block = block;
    }

    private ItemStack drops;
    private static double blockHP;
    private static double softCap;
    private SkillType type = SkillType.MINING;
    private double xpAmt = 10;

    public double getSoftCap() {
        return blockHP * 6;
    }

    public void setBlockHP(double d) {
        blockHP = d;
    }

    public void setXPType(SkillType type) {
        this.type = type;
    }

    public void setXPAmt(double amt) {
        this.xpAmt = amt;
    }

    public double getXpAmt() {
        return xpAmt;
    }

    public SkillType getSkillType() {
        return type;
    }

    public double getBlockHP() {
        return blockHP;
    }

    public void setDrops(SBItemStack customItem, int max, int min) {
        Random random = new Random();
        int dropAmt = random.nextInt(max) + min;
        ItemStack i = customItem.asBukkitItem();
        i.setAmount(dropAmt);
        drops = i;
    }

    public ItemStack getDrops() {
        return drops;
    }

    public void giveDrops(Block b, Player p) {
        ItemStack pick = p.getItemInHand();
        SBItemStack item = new SBItemStack(pick);
        if (item.getEnchantment(Enchantment.TELEKINESIS) != 0) {
            if (hasAvaliableSlot(p)) {
                p.getInventory().addItem(getDrops());
            } else {
                b.getWorld().dropItem(b.getLocation(), getDrops());
            }
        } else {
            b.getWorld().dropItem(b.getLocation(), getDrops());
        }

    }

    private boolean hasAvaliableSlot(Player p) {
        Inventory inv = p.getInventory();
        boolean check = false;
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                check = true;
                break;
            }
        }

        return check;
    }
}
