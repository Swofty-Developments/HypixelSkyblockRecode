package net.atlas.SkyblockSandbox.island.islands.end.dragFight;


import net.atlas.SkyblockSandbox.item.SBItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum DragonDrop {
    HELMET("helmet"),
    CHESTPLATE("chestplate"),
    LEGGINGS("leggings"),
    BOOTS("boots"),
    SWORD("aspect_of_the_dragons"),
    PET("");

    private String prefix;

    DragonDrop(String prefix) {
        this.prefix = prefix;
    }

    public ItemStack setArmorType(DragonTypes type) {
        return new SBItemStack(type.getMobName() + " " + prefix, Material.LEATHER_CHESTPLATE,1).asBukkitItem();
        // type.getPrefix()+"_"+prefix;
    }

    public ItemStack getItem() {
        return new SBItemStack(prefix, Material.LEATHER_CHESTPLATE,1).asBukkitItem();
    }
}
