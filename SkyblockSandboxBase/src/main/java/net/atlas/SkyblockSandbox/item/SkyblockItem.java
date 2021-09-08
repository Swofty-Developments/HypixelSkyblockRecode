package net.atlas.SkyblockSandbox.item;

import net.atlas.SkyblockSandbox.item.ability.itemAbilities.HellShatter;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.SoulCry;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface SkyblockItem {

    SBItemStack item();

    boolean isPublic();

    static List<SBItemStack> getAllItems() {
        List<SBItemStack> items = new ArrayList<>();
        for(Default i: Default.values()) {
            items.add(i.item());
        }
        for(Sword i: Sword.values()) {
            items.add(i.item());
        }
        for(Axe i: Axe.values()) {
            items.add(i.item());
        }
        for(Dungeon i: Dungeon.values()) {
            items.add(i.item());
        }
        for(Unobtainable i: Unobtainable.values()) {
            items.add(i.item());
        }
        return items;
    }
    static List<SBItemStack> getAllPublicItems() {
        List<SBItemStack> items = new ArrayList<>();
        for(Default i: Default.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(Sword i: Sword.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(Axe i: Axe.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(Dungeon i: Dungeon.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(Unobtainable i: Unobtainable.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        return items;
    }

    enum Default implements SkyblockItem {
        BEDROCK(new SBItemStack("Bedrock","BEDROCK", Material.BEDROCK,0,true,false,null, type(), Rarity.LEGENDARY,5000,5000,144,60,100,8000,2300,100,50000)),
        SUMMONING_EYE(new SBItemStack("Summoning Eye","SUMMONING_EYE",Material.SKULL_ITEM,3,false,false,"http://textures.minecraft.net/texture/daa8fc8de6417b48d48c80b443cf5326e3d9da4dbe9b25fcd49549d96168fc0",Rarity.EPIC,0,0,0,0,0,0,0,0,0)),
        SLEEPING_EYE(new SBItemStack("Sleeping Eye","SLEEPING_EYE",Material.SKULL_ITEM,3,false,false,"http://textures.minecraft.net/texture/37c0d010dd0e512ffea108d7c5fe69d576c31ec266c884b51ec0b28cc457",Rarity.EPIC,0,0,0,0,0,0,0,0,0)),
        SMALL_BACKPACK(new SBItemStack("Small Backpack","SMALL_BACKPACK",Material.SKULL_ITEM,3,false,false,"http://textures.minecraft.net/texture/58bc8fa716cadd004b828cb27cc0f6f6ade3be41511688ca9eceffd1647fb9",Rarity.UNCOMMON,0,0,0,0,0,0,0,0,0),"0000","stored-items");

        private final SBItemStack item;

        Default(SBItemStack item) {
            this.item = item;
        }

        Default(SBItemStack item, String nbtMsg,String nbtKey) {
            this.item = item;
            this.item.setString(item.asBukkitItem(),nbtMsg,nbtKey);
        }

        @Override
        public SBItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return false;
        }


        public static ItemType type() {
            return ItemType.ITEM;
        }
    }

    enum Sword implements SkyblockItem {
        ASPECT_OF_DRAGONS(new SBItemStack("Aspect of the Dragons","ASPECT_OF_DRAGONS",Material.DIAMOND_SWORD,0,false,true, null,type(),Rarity.LEGENDARY,10000,8000,144,60,100,8000,2300,100,0)),
        ATOMSPLIT_KATANA(new SBItemStack("Atomsplit Katana","ATOMSPLIT_KATANA",Material.DIAMOND_SWORD,0,false,true,new SoulCry(),type(),Rarity.LEGENDARY,5000,5000,2000,75,100,600,2345,74,5000)),
        HELLS_COMPLEMENT(new SBItemStack("Hell's Complement","HELLS_COMPLEMENT",Material.BLAZE_ROD,0,false,true,new HellShatter(),type(),Rarity.MYTHIC,666,666,333,11,-66,66,0,0,66));

        private final SBItemStack item;

        Sword(SBItemStack item) {
            this.item = item;
        }

        @Override
        public SBItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.SWORD;
        }
    }

    enum Axe implements SkyblockItem {
        ;

        private final SBItemStack item;

        Axe(SBItemStack item) {
            this.item = item;
        }

        @Override
        public SBItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.AXE;
        }
    }


    enum Dungeon implements SkyblockItem {
        ;

        private final SBItemStack item;

        Dungeon(SBItemStack item) {
            this.item = item;
        }

        @Override
        public SBItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.DUNGEON_ITEM;
        }
    }

    ;enum Unobtainable implements SkyblockItem {
        ;

        private final SBItemStack item;

        Unobtainable(SBItemStack item) {
            this.item = item;
        }

        @Override
        public SBItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return false;
        }

        public static ItemType type() {
            return ItemType.ITEM;
        }
    }


}
