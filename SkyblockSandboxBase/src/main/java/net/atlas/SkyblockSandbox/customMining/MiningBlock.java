package net.atlas.SkyblockSandbox.customMining;

import net.maploop.items.extras.Enchantment;
import net.maploop.items.item.CustomItem;
import net.maploop.items.item.ItemUtilities;
import net.maploop.items.util.EnchantUtil;
import net.maploop.items.util.IUtil;
import net.maploop.items.util.NBTUtil;
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

    public double getSoftCap() {
        return blockHP*6;
    }

    public void setBlockHP(double d) {
        blockHP = d;
    }

    public double getBlockHP() {
        return blockHP;
    }

    public void setDrops(CustomItem customItem, int max, int min) {
        Random random = new Random();
        int dropAmt = random.nextInt(max) + min;
        ItemStack i = new ItemStack(customItem.getMaterial(),dropAmt);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(customItem.getRarity().getColor() + customItem.getName());
        List<String> lore = customItem.getLore(i);
        meta.setLore(lore);
        i.setItemMeta(meta);
        drops = i;
    }

    public ItemStack getDrops() {
        return drops;
    }

    public void giveDrops(Block b,Player p) {
        ItemStack pick = p.getItemInHand();
        EnchantUtil eutil = new EnchantUtil(pick);
        if(eutil.hasEnchant(Enchantment.TELEKINESIS)) {
            if(hasAvaliableSlot(p)) {
                p.getInventory().addItem(getDrops());
            } else {
                b.getWorld().dropItem(b.getLocation(),getDrops());
            }
        } else {
            b.getWorld().dropItem(b.getLocation(),getDrops());
        }

    }

    private boolean hasAvaliableSlot(Player p){
        Inventory inv = p.getInventory();
        boolean check=false;
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                check = true;
                break;
            }
        }

        return check;
    }
}
