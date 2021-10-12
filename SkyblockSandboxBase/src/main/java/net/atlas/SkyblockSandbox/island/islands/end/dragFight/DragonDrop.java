package net.atlas.SkyblockSandbox.island.islands.end.dragFight;


import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import lombok.Getter;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.pets.Pet;
import net.atlas.SkyblockSandbox.player.pets.PetBuilder;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.atlas.SkyblockSandbox.util.builders.SBItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public enum DragonDrop {
    HELMET("Helmet", SBItemBuilder.init().mat(Material.SKULL_ITEM).rarity(Rarity.LEGENDARY)),
    CHESTPLATE("Chestplate", SBItemBuilder.init().mat(Material.LEATHER_CHESTPLATE).rarity(Rarity.LEGENDARY)),
    LEGGINGS("Leggings", SBItemBuilder.init().mat(Material.LEATHER_LEGGINGS).rarity(Rarity.LEGENDARY)),
    BOOTS("Boots", SBItemBuilder.init().mat(Material.LEATHER_BOOTS).rarity(Rarity.LEGENDARY));

    private String prefix;
    private SBItemBuilder builder;

    DragonDrop(String prefix, SBItemBuilder builder) {
        this.prefix = prefix;
        this.builder = builder;
    }

    public ItemStack getDrop(DragonTypes type) {
        builder.name(type.getMobName() + " " + prefix).type(ItemType.ITEM);
        builder.hexColor(ArmorColors.getFromDragType(type).getHexColorFromPiece(this));
        return builder.build();
    }

    public enum ArmorColors {
        SUPERIOR(DragonTypes.SUPERIOR,"7558efbe66976099cfd62760d9e05170d2bb8f51e68829ab8a051c48cbc415cb", "F2DF11", "F2DF11", "F25D18"),
        STRONG(DragonTypes.STRONG,"efde09603b0225b9d24a73a0d3f3e3af29058d448ccd7ce5c67cd02fab0ff682", "D91E41", "E09419", "F0D124"),
        WISE(DragonTypes.WISE,"5a2984cf07c48da9724816a8ff0864bc68bce694ce8bd6db2112b6ba031070de", "29F0E9", "29F0E9", "29F0E9"),
        UNSTABLE(DragonTypes.UNSTABLE,"2922b5f8d554ca923f96832a5a4e9169bc2cdb360a2b06ebec09b6a6af4618e3", "B212E3", "B212E3", "B212E3"),
        YOUNG(DragonTypes.YOUNG,"5c486af3b882766e82a0bc1665ff02eb6e873b6e0d771f3adabc759b720226a", "DDE4F0", "DDE4F0", "DDE4F0"),
        PROTECTOR(DragonTypes.PROTECTOR,"f37a596cdc4b11a9948ffa38c2aa3c6942ef449eb0a3982281d3a5b5a14ef6ae", "99978B", "99978B", "99978B"),
        OLD(DragonTypes.OLD,"59e9e5600410c1d0254474a81fecfb3885c1cf6f504190d856f0ec7c9f055c42", "F0E6AA", "F0E6AA", "F0E6AA"),
        VOIDGLOOM(DragonTypes.VOIDGLOOM,"", "000000", "000000", "000000");

        @Getter
        private String headTexture, chestHex, legHex, bootHex;

        @Getter
        private DragonTypes type;

        ArmorColors(DragonTypes type,String headTexture, String chestHex, String legHex, String bootHex) {
            this.type = type;
            this.headTexture = "http://textures.minecraft.net/texture/" + headTexture;
            this.chestHex = chestHex;
            this.legHex = legHex;
            this.bootHex = bootHex;
        }

        public static ArmorColors getFromDragType(DragonTypes type) {
            for(ArmorColors c:ArmorColors.values()) {
                if(c.getType()==type) {
                    return c;
                }
            }
            return null;
        }

        public String getHexColorFromPiece(DragonDrop dragonDrop) {
            switch (dragonDrop) {
                case HELMET:
                    return headTexture;
                case CHESTPLATE:
                    return chestHex;
                case LEGGINGS:
                    return legHex;
                case BOOTS:
                    return bootHex;
            }
            return "";
        }

    }

    public enum Universal {
        SWORD("Aspect of the Dragons", SBItemBuilder.init().mat(Material.DIAMOND_SWORD)),
        PET("Ender Dragon", PetBuilder.init().petType(Pet.PetType.COMBAT.getPetModifier()).texture("http://textures.minecraft.net/texture/a1d08c0289d9efe519e87f7b814cb2349f4475bd3c37d44f9c4f0e508e77981e"));


        private String name;
        private SBItemBuilder builder;
        private PetBuilder petBuilder;

        Universal(String name, SBItemBuilder builder) {
            this.name = name;
            this.builder = builder;
            this.builder.name(name).stackable(false).id(name.toUpperCase().replace(' ', '_'));
        }

        Universal(String name, PetBuilder builder) {
            this.name = name;
            this.builder = null;
            this.petBuilder = builder;
            this.petBuilder.name(name).level(1).xp(0);
        }

        public ItemStack getDrop() {
            if (builder == null) {
                return petBuilder.build();
            } else {
                return builder.build();
            }
        }

        public ItemStack getDrop(Rarity rarity) {
            if (builder == null) {
                return petBuilder.rarity(rarity).build();
            } else {
                return builder.rarity(rarity).build();
            }
        }
    }

}
