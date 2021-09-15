package net.atlas.SkyblockSandbox.item;

import net.atlas.SkyblockSandbox.item.ability.itemAbilities.HellShatter;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.SoulCry;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.builders.SBItemBuilder;
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

        BEDROCK(SBItemBuilder.init().name("Bedrock").id("BEDROCK").mat(Material.BEDROCK).rarity(Rarity.LEGENDARY).type(type()).stat(PlayerStat.DAMAGE,50000D).build()),
        SUMMONING_EYE(SBItemBuilder.init().name("Summoning Eye").id("SUMMONING_EYE").mat(Material.SKULL_ITEM).rarity(Rarity.EPIC).type(type()).url("http://textures.minecraft.net/texture/daa8fc8de6417b48d48c80b443cf5326e3d9da4dbe9b25fcd49549d96168fc0").stackable(false).build()),
        SLEEPING_EYE(SBItemBuilder.init().name("Sleeping Eye").id("SLEEPING_EYE").mat(Material.SKULL_ITEM).rarity(Rarity.EPIC).type(type()).url("http://textures.minecraft.net/texture/37c0d010dd0e512ffea108d7c5fe69d576c31ec266c884b51ec0b28cc457").build()),
        SMALL_BACKPACK(SBItemBuilder.init().name("Small Backpack").id("SMALL_BACKPACK").mat(Material.SKULL_ITEM).rarity(Rarity.UNCOMMON).type(type()).url("http://textures.minecraft.net/texture/58bc8fa716cadd004b828cb27cc0f6f6ade3be41511688ca9eceffd1647fb9").build(),"0000","stored-items");

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
        ASPECT_OF_DRAGONS(SBItemBuilder.init().name("Aspect of the Dragons").id("ASPECT_OF_DRAGONS").mat(Material.DIAMOND_SWORD).rarity(Rarity.LEGENDARY).type(type()).build()),
        ATOMSPLIT_KATANA(SBItemBuilder.init().name("Atomsplit Katana").id("ATOMSPLIT_KATANA").mat(Material.DIAMOND_SWORD).ability(new SoulCry(),1).rarity(Rarity.LEGENDARY).type(type()).stat(PlayerStat.DAMAGE,5000D).build()),
        HELLS_COMPLEMENT(SBItemBuilder.init().name("Hell's Complement").id("HELLS_COMPLEMENT").mat(Material.BLAZE_ROD).ability(new HellShatter(),1).rarity(Rarity.MYTHIC).type(type()).stat(PlayerStat.DAMAGE,666D).stat(PlayerStat.STRENGTH,666D).build());

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

    enum Pickaxe implements SkyblockItem {
        STONK(SBItemBuilder.init().mat(Material.GOLD_PICKAXE).id("STONK").name("Stonk").rarity(Rarity.EPIC).stat(PlayerStat.MINING_SPEED,60D).type(ItemType.ITEM).build());

        private final SBItemStack item;

        Pickaxe(SBItemStack item) {
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

    enum Drill implements SkyblockItem {
        ;

        private final SBItemStack item;

        Drill(SBItemStack item) {
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
