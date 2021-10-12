package net.atlas.SkyblockSandbox.island.islands.end.dragFight;


import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.pets.Pet;
import net.atlas.SkyblockSandbox.player.pets.PetBuilder;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.atlas.SkyblockSandbox.util.builders.SBItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum DragonDrop {
    HELMET("Helmet", SBItemBuilder.init().mat(Material.SKULL_ITEM).rarity(Rarity.LEGENDARY)),
    CHESTPLATE("Chestplate",SBItemBuilder.init().mat(Material.LEATHER_CHESTPLATE).rarity(Rarity.LEGENDARY)),
    LEGGINGS("Leggings",SBItemBuilder.init().mat(Material.LEATHER_LEGGINGS).rarity(Rarity.LEGENDARY)),
    BOOTS("Boots",SBItemBuilder.init().mat(Material.LEATHER_BOOTS).rarity(Rarity.LEGENDARY));

    private String prefix;
    private SBItemBuilder builder;

    DragonDrop(String prefix, SBItemBuilder builder) {
        this.prefix = prefix;
        this.builder = builder;
    }

    public ItemStack getDrop(DragonTypes type) {
        builder.name(type.getMobName() + " " + prefix).type(ItemType.ITEM);
        return builder.build();
    }
    public ItemStack getUniversalDrop() {
        return builder.build();
    }

    public enum Universal {
        SWORD("Aspect of the Dragons",SBItemBuilder.init().mat(Material.DIAMOND_SWORD)),
        PET("Ender Dragon", PetBuilder.init().petType(Pet.PetType.COMBAT.getPetModifier()).texture("http://textures.minecraft.net/texture/a1d08c0289d9efe519e87f7b814cb2349f4475bd3c37d44f9c4f0e508e77981e"));


        private String name;
        private SBItemBuilder builder;
        private PetBuilder petBuilder;

        Universal(String name, SBItemBuilder builder) {
            this.name = name;
            this.builder = builder;
            this.builder.name(name).stackable(false).id(name.toUpperCase().replace(' ','_'));
        }

        Universal(String name, PetBuilder builder) {
            this.name = name;
            this.builder = null;
            this.petBuilder = builder;
            this.petBuilder.name(name).level(1).xp(0);
        }

        public ItemStack getDrop() {
            if(builder==null) {
                return petBuilder.build();
            } else {
                return builder.build();
            }
        }

        public ItemStack getDrop(Rarity rarity) {
            if(builder==null) {
                return petBuilder.rarity(rarity).build();
            } else {
                return builder.rarity(rarity).build();
            }
        }
    }

}
